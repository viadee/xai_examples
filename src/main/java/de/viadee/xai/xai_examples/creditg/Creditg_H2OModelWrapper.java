package de.viadee.xai.xai_examples.creditg;

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
class Creditg_H2OModelWrapper {
    private final EasyPredictModelWrapper model;

    /**
     * Imports the .zip file (MOJO) containing all exported materials
     *
     * @throws IOException if an error occurs
     */
    Creditg_H2OModelWrapper() throws IOException {
        final URL zipURL = ClassLoader.getSystemResource("./creditg/SE_creditg.zip");
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
        row.put("checking_status", instance.getTransformedValue("checking_status"));
        row.put("duration", ((Integer) instance.getTransformedValue("duration")).doubleValue());
        row.put("credit_history", instance.getTransformedValue("credit_history"));
        row.put("purpose", instance.getTransformedValue("purpose"));
        row.put("credit_amount", ((Integer) instance.getTransformedValue("credit_amount")).doubleValue());
        row.put("savings_status", instance.getTransformedValue("savings_status"));
        row.put("employment", instance.getTransformedValue("employment"));
        row.put("installment_commitment", ((Integer) instance.getTransformedValue("installment_commitment")).doubleValue());
        row.put("personal_status", instance.getTransformedValue("personal_status"));
        row.put("other_parties", instance.getTransformedValue("other_parties"));
        row.put("residence_since", ((Integer) instance.getTransformedValue("residence_since")).doubleValue());
        row.put("poperty_magnitude", instance.getTransformedValue("poperty_magnitude"));
        row.put("age", ((Integer) instance.getTransformedValue("age")).doubleValue());
        row.put("other_payment_plans", instance.getTransformedValue("other_payment_plans"));
        row.put("housing", instance.getTransformedValue("housing"));
        row.put("existing_credits", ((Integer) instance.getTransformedValue("existing_credits")).doubleValue());
        row.put("job", instance.getTransformedValue("job"));
        row.put("num_dependents", ((Integer) instance.getTransformedValue("num_dependents")).doubleValue());
        row.put("own_telephone", instance.getTransformedValue("own_telephone"));
        row.put("foreign_worker", instance.getTransformedValue("foreign_worker"));
        try {
            return ((BinomialModelPrediction) model.predict(row)).labelIndex;
        } catch (PredictException e) {
            throw new RuntimeException(e);
        }
    }
}
