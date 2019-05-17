package de.viadee.xai.xai_examples.australian_credit;

import de.viadee.xai.anchor.adapter.tabular.TabularInstance;
import hex.genmodel.ModelMojoReader;
import hex.genmodel.MojoModel;
import hex.genmodel.MojoReaderBackend;
import hex.genmodel.MojoReaderBackendFactory;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.exception.PredictException;
import hex.genmodel.easy.prediction.BinomialModelPrediction;

import java.io.IOException;
import java.net.URL;

/**
 * Imports the H2O model exported from R.
 * <p>
 * Contains definitions and necessary configuration
 */
class Australian_H2OModelWrapper {
    private final EasyPredictModelWrapper model;

    /**
     * Imports the .zip file (MOJO) containing all exported materials
     *
     * @throws IOException if an error occurs
     */
    Australian_H2OModelWrapper() throws IOException {
        final URL zipURL = ClassLoader.getSystemResource("./australian_credit/Australian_Credit_GBM.zip");
        final MojoReaderBackend reader = MojoReaderBackendFactory.createReaderBackend(zipURL,
                MojoReaderBackendFactory.CachingStrategy.MEMORY);
        final MojoModel mojoModel = ModelMojoReader.readFrom(reader);
        model = new EasyPredictModelWrapper(mojoModel);
    }

    /**
     * Calls the model for a specific instance.
     * <p>
     * Maps the datatypes to match the required definition
     *
     * @param instance the instance to obtain a prediction for
     * @return the prediction
     */
    int predict(TabularInstance instance) {
        final RowData row = new RowData();
        row.put("A1", ((Integer) instance.getTransformedValue("A1")).doubleValue());
        row.put("A2", ((Integer) instance.getTransformedValue("A2")).doubleValue());
        row.put("A3", ((Integer) instance.getTransformedValue("A3")).doubleValue());
        row.put("A4", ((Integer) instance.getTransformedValue("A4")).doubleValue());
        row.put("A5", ((Integer) instance.getTransformedValue("A5")).doubleValue());
        row.put("A6", ((Integer) instance.getTransformedValue("A6")).doubleValue());
        row.put("A7", ((Integer) instance.getTransformedValue("A7")).doubleValue());
        row.put("A8", ((Integer) instance.getTransformedValue("A8")).doubleValue());
        row.put("A9", ((Integer) instance.getTransformedValue("A9")).doubleValue());
        row.put("A10", ((Integer) instance.getTransformedValue("A10")).doubleValue());
        row.put("A11", ((Integer) instance.getTransformedValue("A11")).doubleValue());
        row.put("A12", ((Integer) instance.getTransformedValue("A12")).doubleValue());
        row.put("A13", ((Integer) instance.getTransformedValue("A13")).doubleValue());
        row.put("A14", ((Integer) instance.getTransformedValue("A14")).doubleValue());

        try {
            return ((BinomialModelPrediction) model.predict(row)).labelIndex;
        } catch (PredictException e) {
            throw new RuntimeException(e);
        }
    }
}
