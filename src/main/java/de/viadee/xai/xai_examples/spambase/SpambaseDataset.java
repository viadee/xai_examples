package de.viadee.xai.xai_examples.spambase;

import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.column.DoubleColumn;
import de.viadee.xai.anchor.adapter.tabular.column.IntegerColumn;
import de.viadee.xai.anchor.adapter.tabular.transformations.Transformer;
import de.viadee.xai.anchor.adapter.tabular.util.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Loads the dataset and its definitions and prepares the {@link AnchorTabular} object
 */
class SpambaseDataset {

    /**
     * @return the {@link AnchorTabular} object that contains the training data and its definitions
     */
    static AnchorTabular createTabularTrainingDefinition() {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./spambase/train_spambase.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");

        try {
            return new AnchorTabular.Builder()
                    .setDoBalance(false)
                    .addIgnoredColumn("Id")
                    //.addColumn(DoubleColumn.fromStringInput("word_freq_make", -1, 7))
                    .addIgnoredColumn("word_freq_make")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_address", -1, 7))
                    //.addColumn(DoubleColumn.fromStringInput("word_freq_all", -1, 7))
                    .addIgnoredColumn("word_freq_all")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_3d", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_our", -1, 7))
                    //.addColumn(DoubleColumn.fromStringInput("word_freq_over", -1, 7))
                    .addIgnoredColumn("word_freq_over")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_remove", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_internet", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_order", -1, 7))
                    .addIgnoredColumn("word_freq_order")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_mail", -1, 7))
                    .addIgnoredColumn("word_freq_mail")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_receive", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_will", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_people", -1, 7))
                    .addIgnoredColumn("word_freq_people")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_report", -1, 7))
                    .addIgnoredColumn("word_freq_report")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_addresses", -1, 7))
                    .addIgnoredColumn("word_freq_addresses")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_free", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_business", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_email", -1, 7))
                    .addIgnoredColumn("word_freq_email")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_you", -1, 7))
                    .addIgnoredColumn("word_freq_you")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_credit", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_your", -1, 7))
                    .addIgnoredColumn("word_freq_your")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_font", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_000", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_money", -1, 7))
                    //.addColumn(DoubleColumn.fromStringInput("word_freq_hp", -1, 7))
                    .addIgnoredColumn("word_freq_hp")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_hpl", -1, 7))
                    .addIgnoredColumn("word_freq_hpl")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_george", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_650", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_lab", -1, 7))
                    .addIgnoredColumn("word_freq_lab")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_labs", -1, 7))
                    .addIgnoredColumn("word_freq_labs")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_telnet", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_857", -1, 7))
                    .addIgnoredColumn("word_freq_857")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_data", -1, 7))
                    .addIgnoredColumn("word_freq_data")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_415", -1, 7))
                    .addIgnoredColumn("word_freq_415")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_85", -1, 7))
                    .addIgnoredColumn("word_freq_85")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_technology", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_1999", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_parts", -1, 7))
                    .addIgnoredColumn("word_freq_parts")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_pm", -1, 7))
                    .addIgnoredColumn("word_freq_pm")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_direct", -1, 7))
                    .addIgnoredColumn("word_freq_direct")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_cs", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_meeting", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_original", -1, 7))
                    .addIgnoredColumn("word_freq_original")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_project", -1, 7))
                    .addIgnoredColumn("word_freq_project")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_re", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_edu", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_table", -1, 7))
                    .addIgnoredColumn("word_freq_table")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_conference", -1, 7))
                    .addIgnoredColumn("word_freq_conference")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%3B", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%28", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%5B", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%21", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%24", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%23", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("capital_run_length_average", -1, 7))
                    .addColumn(IntegerColumn.fromStringInput("capital_run_length_longest", -1, 7))
                    .addColumn(IntegerColumn.fromStringInput("capital_run_length_total", -1, 7))
                    .addTargetColumn(IntegerColumn.fromStringInput("class"))
                    .build(trainingDataStream, true, false);
        } catch (IOException e) {
            throw new RuntimeException("Could not read data");
        }
    }

    /**
     * @return the {@link AnchorTabular} object that contains the test data and its definitions
     */
    static AnchorTabular createTabularTestDefinition() {
        // The following implementation is very much similar to the above method.
        // It is contained in an own block to increase the tutorial's readability
        // Main difference: no target label is included in test set data
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./spambase/test_spambase.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");

        try {
            return new AnchorTabular.Builder()
                    .setDoBalance(false)
                    .addIgnoredColumn("Id")
                    //.addColumn(DoubleColumn.fromStringInput("word_freq_make", -1, 7))
                    .addIgnoredColumn("word_freq_make")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_address", -1, 7))
                    //.addColumn(DoubleColumn.fromStringInput("word_freq_all", -1, 7))
                    .addIgnoredColumn("word_freq_all")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_3d", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_our", -1, 7))
                    //.addColumn(DoubleColumn.fromStringInput("word_freq_over", -1, 7))
                    .addIgnoredColumn("word_freq_over")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_remove", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_internet", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_order", -1, 7))
                    .addIgnoredColumn("word_freq_order")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_mail", -1, 7))
                    .addIgnoredColumn("word_freq_mail")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_receive", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_will", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_people", -1, 7))
                    .addIgnoredColumn("word_freq_people")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_report", -1, 7))
                    .addIgnoredColumn("word_freq_report")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_addresses", -1, 7))
                    .addIgnoredColumn("word_freq_addresses")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_free", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_business", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_email", -1, 7))
                    .addIgnoredColumn("word_freq_email")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_you", -1, 7))
                    .addIgnoredColumn("word_freq_you")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_credit", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_your", -1, 7))
                    .addIgnoredColumn("word_freq_your")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_font", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_000", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_money", -1, 7))
                    //.addColumn(DoubleColumn.fromStringInput("word_freq_hp", -1, 7))
                    .addIgnoredColumn("word_freq_hp")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_hpl", -1, 7))
                    .addIgnoredColumn("word_freq_hpl")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_george", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_650", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_lab", -1, 7))
                    .addIgnoredColumn("word_freq_lab")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_labs", -1, 7))
                    .addIgnoredColumn("word_freq_labs")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_telnet", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_857", -1, 7))
                    .addIgnoredColumn("word_freq_857")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_data", -1, 7))
                    .addIgnoredColumn("word_freq_data")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_415", -1, 7))
                    .addIgnoredColumn("word_freq_415")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_85", -1, 7))
                    .addIgnoredColumn("word_freq_85")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_technology", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_1999", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_parts", -1, 7))
                    .addIgnoredColumn("word_freq_parts")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_pm", -1, 7))
                    .addIgnoredColumn("word_freq_pm")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_direct", -1, 7))
                    .addIgnoredColumn("word_freq_direct")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_cs", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_meeting", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_original", -1, 7))
                    .addIgnoredColumn("word_freq_original")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_project", -1, 7))
                    .addIgnoredColumn("word_freq_project")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_re", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_edu", -1, 7))
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_table", -1, 7))
                    .addIgnoredColumn("word_freq_table")
                    // .addColumn(DoubleColumn.fromStringInput("word_freq_conference", -1, 7))
                    .addIgnoredColumn("word_freq_conference")
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%3B", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%28", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%5B", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%21", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%24", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("word_freq_%23", -1, 7))
                    .addColumn(DoubleColumn.fromStringInput("capital_run_length_average", -1, 7))
                    .addColumn(IntegerColumn.fromStringInput("capital_run_length_longest", -1, 7))
                    .addColumn(IntegerColumn.fromStringInput("capital_run_length_total", -1, 7))
                    .addTargetColumn(IntegerColumn.fromStringInput("class"))
                    .build(trainingDataStream, true, false);
        } catch (IOException e) {
            throw new RuntimeException("Could not read data");
        }
    }

    private static final class TicketNumberTransformer implements Transformer {

        @Override
        public Serializable apply(Serializable serializable) {
            // Transforms the ticket column to contain only the ticket number without ticket prefixes
            String replaced = ((String) serializable).replaceAll("[^\\d]+", "");

            return ("".equals(replaced)) ? -1 : replaced;
        }
    }

    /**
     * @return the labels of the test set as specified in gender_submission.
     */
    static int[] readTestLabels() {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./churnOML/test_churnOML.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");

        try {
            final Collection<String[]> csv = CSVReader.readCSV(trainingDataStream, true);
            final int[] result = new int[csv.size() - 1];
            final Iterator<String[]> iter = csv.iterator();
            iter.next(); // Skip header entry
            int i = 0;
            while (iter.hasNext()) {
                result[i++] = Integer.parseInt(iter.next()[1]);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
