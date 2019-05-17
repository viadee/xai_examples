package de.viadee.xai.xai_examples.churnKaggle;

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
class churnKaggle_H2OModelWrapper {
    private final EasyPredictModelWrapper model;

    /**
     * Imports the .zip file (MOJO) containing all exported materials
     *
     * @throws IOException if an error occurs
     */
    churnKaggle_H2OModelWrapper() throws IOException {
        final URL zipURL = ClassLoader.getSystemResource("./churnKaggle/churnKaggle_GBM.zip");
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
        row.put("CreditScore", ((Integer) instance.getTransformedValue("CreditScore")).doubleValue());
        row.put("Geography", instance.getTransformedValue("Geography"));
        row.put("Gender", instance.getTransformedValue("Gender"));
        row.put("Age", ((Integer) instance.getTransformedValue("Age")).doubleValue());
        row.put("Tenure", ((Integer) instance.getTransformedValue("Tenure")).doubleValue());
        row.put("Balance", instance.getTransformedValue("Balance"));
        row.put("NumOfProducts", ((Integer) instance.getTransformedValue("NumOfProducts")).doubleValue());
        row.put("HasCrCard", ((Integer) instance.getTransformedValue("HasCrCard")).doubleValue());
        row.put("IsActiveMember", ((Integer) instance.getTransformedValue("IsActiveMember")).doubleValue());
        row.put("EstimatedSalary", instance.getTransformedValue("EstimatedSalary"));
        try {
            return ((BinomialModelPrediction) model.predict(row)).labelIndex;
        } catch (PredictException e) {
            throw new RuntimeException(e);
        }
    }
}
