package de.viadee.xai.xai_examples.spambase;

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
class Spambase_H2OModelWrapper {
    private final EasyPredictModelWrapper model;

    /**
     * Imports the .zip file (MOJO) containing all exported materials
     *
     * @throws IOException if an error occurs
     */
    Spambase_H2OModelWrapper() throws IOException {
        final URL zipURL = ClassLoader.getSystemResource("./spambase/SE_spambase.zip");
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
//        row.put("word_freq_make", instance.getTransformedValue("word_freq_make"));
        row.put("word_freq_address", instance.getTransformedValue("word_freq_address"));
//        row.put("word_freq_all", instance.getTransformedValue("word_freq_all"));
        row.put("word_freq_3d", instance.getTransformedValue("word_freq_3d"));
        row.put("word_freq_our", instance.getTransformedValue("word_freq_our"));
//        row.put("word_freq_over", instance.getTransformedValue("word_freq_over"));
        row.put("word_freq_remove", instance.getTransformedValue("word_freq_remove"));
        row.put("word_freq_internet", instance.getTransformedValue("word_freq_internet"));
//        row.put("word_freq_order", instance.getTransformedValue("word_freq_order"));
//        row.put("word_freq_mail", instance.getTransformedValue("word_freq_mail"));
        row.put("word_freq_receive", instance.getTransformedValue("word_freq_receive"));
        row.put("word_freq_will", instance.getTransformedValue("word_freq_will"));
//        row.put("word_freq_people", instance.getTransformedValue("word_freq_people"));
//        row.put("word_freq_report", instance.getTransformedValue("word_freq_report"));
//        row.put("word_freq_addresses", instance.getTransformedValue("word_freq_addresses"));
        row.put("word_freq_free", instance.getTransformedValue("word_freq_free"));
        row.put("word_freq_business", instance.getTransformedValue("word_freq_business"));
//        row.put("word_freq_email", instance.getTransformedValue("word_freq_email"));
//        row.put("word_freq_you", instance.getTransformedValue("word_freq_you"));
        row.put("word_freq_credit", instance.getTransformedValue("word_freq_credit"));
//        row.put("word_freq_your", instance.getTransformedValue("word_freq_your"));
        row.put("word_freq_font", instance.getTransformedValue("word_freq_font"));
        row.put("word_freq_000", instance.getTransformedValue("word_freq_000"));
        row.put("word_freq_money", instance.getTransformedValue("word_freq_money"));
//        row.put("word_freq_hp", instance.getTransformedValue("word_freq_hp"));
//        row.put("word_freq_hpl", instance.getTransformedValue("word_freq_hpl"));
        row.put("word_freq_george", instance.getTransformedValue("word_freq_george"));
        row.put("word_freq_650", instance.getTransformedValue("word_freq_650"));
//        row.put("word_freq_lab", instance.getTransformedValue("word_freq_lab"));
//        row.put("word_freq_labs", instance.getTransformedValue("word_freq_labs"));
        row.put("word_freq_telnet", instance.getTransformedValue("word_freq_telnet"));
//        row.put("word_freq_857", instance.getTransformedValue("word_freq_857"));
//        row.put("word_freq_data", instance.getTransformedValue("word_freq_data"));
//        row.put("word_freq_415", instance.getTransformedValue("word_freq_415"));
//        row.put("word_freq_85", instance.getTransformedValue("word_freq_85"));
        row.put("word_freq_technology", instance.getTransformedValue("word_freq_technology"));
        row.put("word_freq_1999", instance.getTransformedValue("word_freq_1999"));
//        row.put("word_freq_parts", instance.getTransformedValue("word_freq_parts"));
//        row.put("word_freq_pm", instance.getTransformedValue("word_freq_pm"));
//        row.put("word_freq_direct", instance.getTransformedValue("word_freq_direct"));
        row.put("word_freq_cs", instance.getTransformedValue("word_freq_cs"));
        row.put("word_freq_meeting", instance.getTransformedValue("word_freq_meeting"));
//        row.put("word_freq_original", instance.getTransformedValue("word_freq_original"));
//        row.put("word_freq_project", instance.getTransformedValue("word_freq_project"));
        row.put("word_freq_re", instance.getTransformedValue("word_freq_re"));
        row.put("word_freq_edu", instance.getTransformedValue("word_freq_edu"));
//        row.put("word_freq_table", instance.getTransformedValue("word_freq_table"));
//        row.put("word_freq_conference", instance.getTransformedValue("word_freq_conference"));
        row.put("word_freq_%3B", instance.getTransformedValue("word_freq_%3B"));
        row.put("word_freq_%28", instance.getTransformedValue("word_freq_%28"));
        row.put("word_freq_%5B", instance.getTransformedValue("word_freq_%5B"));
        row.put("word_freq_%21", instance.getTransformedValue("word_freq_%21"));
        row.put("word_freq_%24", instance.getTransformedValue("word_freq_%24"));
        row.put("word_freq_%23", instance.getTransformedValue("word_freq_%23"));
        row.put("capital_run_length_average", instance.getTransformedValue("capital_run_length_average"));
        row.put("capital_run_length_longest", ((Integer) instance.getTransformedValue("capital_run_length_longest")).doubleValue());
        row.put("capital_run_length_total", ((Integer) instance.getTransformedValue("capital_run_length_total")).doubleValue());
        try {
            return ((BinomialModelPrediction) model.predict(row)).labelIndex;
        } catch (PredictException e) {
            throw new RuntimeException(e);
        }
    }
}