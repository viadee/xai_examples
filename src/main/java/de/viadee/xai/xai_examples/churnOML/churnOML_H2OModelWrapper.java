package de.viadee.xai.xai_examples.churnOML;

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
class churnOML_H2OModelWrapper {
    private final EasyPredictModelWrapper model;

    /**
     * Imports the .zip file (MOJO) containing all exported materials
     *
     * @throws IOException if an error occurs
     */
    churnOML_H2OModelWrapper() throws IOException {
        final URL zipURL = ClassLoader.getSystemResource("./churnOML/GBM_churnOML.zip");
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
        row.put("area_code", ((Integer) instance.getTransformedValue("area_code")).doubleValue());
        row.put("international_plan", ((Integer) instance.getTransformedValue("international_plan")).doubleValue());
        row.put("voice_mail_plan", ((Integer) instance.getTransformedValue("voice_mail_plan")).doubleValue());
        row.put("number_vmail_messages", ((Integer) instance.getTransformedValue("number_vmail_messages")).doubleValue());
        row.put("total_day_minutes", instance.getTransformedValue("total_day_minutes"));
        row.put("total_day_calls", ((Integer) instance.getTransformedValue("total_day_calls")).doubleValue());
        row.put("total_day_charge", instance.getTransformedValue("total_day_charge"));
        row.put("total_eve_minutes", instance.getTransformedValue("total_eve_minutes"));
        row.put("total_eve_calls", ((Integer) instance.getTransformedValue("total_eve_calls")).doubleValue());
        row.put("total_eve_charge", instance.getTransformedValue("total_eve_charge"));
        row.put("total_night_minutes", instance.getTransformedValue("total_night_minutes"));
        row.put("total_night_calls", ((Integer) instance.getTransformedValue("total_night_calls")).doubleValue());
        row.put("total_night_charge", instance.getTransformedValue("total_night_charge"));
        row.put("total_intl_minutes", instance.getTransformedValue("total_intl_minutes"));
        row.put("total_intl_calls", ((Integer) instance.getTransformedValue("total_intl_calls")).doubleValue());
        row.put("total_intl_charge", instance.getTransformedValue("total_intl_charge"));
        row.put("number_customer_service_calls", ((Integer) instance.getTransformedValue("number_customer_service_calls")).doubleValue());
        try {
            return ((BinomialModelPrediction) model.predict(row)).labelIndex;
        } catch (PredictException e) {
            throw new RuntimeException(e);
        }
    }
}
