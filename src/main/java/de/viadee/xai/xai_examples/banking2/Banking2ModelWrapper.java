package de.viadee.xai.xai_examples.banking2;

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
class Banking2ModelWrapper {
    private final EasyPredictModelWrapper model;

    /**
     * Imports the .zip file (MOJO) containing all exported materials
     *
     * @throws IOException if an error occurs
     */
    Banking2ModelWrapper() throws IOException {
        final URL zipURL = ClassLoader.getSystemResource("./banking2/banking2_GBM.zip");
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
        row.put("V1", ((Integer) instance.getTransformedValue("V1")).doubleValue());
        row.put("V2", instance.getTransformedValue("V2"));
        row.put("V3", instance.getTransformedValue("V3"));
        row.put("V4", instance.getTransformedValue("V4"));
        row.put("V5", instance.getTransformedValue("V5"));
        row.put("V6", ((Integer) instance.getTransformedValue("V6")).doubleValue());
        row.put("V7", instance.getTransformedValue("V7"));
        row.put("V8", instance.getTransformedValue("V8"));
        row.put("V9", instance.getTransformedValue("V9"));
        row.put("V10", ((Integer) instance.getTransformedValue("V10")).doubleValue());
        row.put("V11", instance.getTransformedValue("V11"));
        row.put("V12", ((Integer) instance.getTransformedValue("V12")).doubleValue());
        row.put("V13", ((Integer) instance.getTransformedValue("V13")).doubleValue());
        row.put("V14", ((Integer) instance.getTransformedValue("V15")).doubleValue());
        row.put("V15", ((Integer) instance.getTransformedValue("V15")).doubleValue());
        row.put("V16", instance.getTransformedValue("V16"));

        try {
            return ((BinomialModelPrediction) model.predict(row)).labelIndex;
        } catch (PredictException e) {
            throw new RuntimeException(e);
        }
    }
}
