package de.viadee.xai.xai_examples.titanic;

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
class H2OModelWrapper {
    private final EasyPredictModelWrapper model;

    /**
     * Imports the .zip file (MOJO) containing all exported materials
     *
     * @throws IOException if an error occurs
     */
    H2OModelWrapper() throws IOException {
        final URL zipURL = ClassLoader.getSystemResource("titanic/h2oModel.zip");
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
        row.put("Sex", instance.getTransformedValue("Sex"));
        row.put("CabinGiven", String.valueOf(instance.getTransformedValue("Cabin")).toUpperCase());
        row.put("Pclass", ((Integer) instance.getTransformedValue("Pclass")).doubleValue());
        row.put("Age", instance.getTransformedValue("Age"));
        row.put("SibSp", ((Integer) instance.getTransformedValue("SibSp")).doubleValue());
        row.put("Fare", instance.getTransformedValue("Fare"));
        row.put("TicketNum", ((Integer) 0).doubleValue());

        try {
            return ((BinomialModelPrediction) model.predict(row)).labelIndex;
        } catch (PredictException e) {
            throw new RuntimeException(e);
        }
    }
}
