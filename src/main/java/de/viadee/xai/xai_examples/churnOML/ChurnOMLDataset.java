package de.viadee.xai.xai_examples.churnOML;


import de.viadee.discretizers4j.Discretizer;
import de.viadee.discretizers4j.impl.*;

import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.builder.AnchorTabularBuilderSequential;
import de.viadee.xai.anchor.adapter.tabular.column.DoubleColumn;
import de.viadee.xai.anchor.adapter.tabular.column.IntegerColumn;
import de.viadee.xai.anchor.adapter.tabular.transformations.Transformer;
import de.viadee.xai.anchor.adapter.tabular.util.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * Loads the dataset and its definitions and prepares the {@link AnchorTabular} object
 */
class ChurnOMLDataset {

    static final Supplier<Discretizer> discretizerSupplier = () -> new PercentileMedianDiscretizer(5);

    /**
     * @return the {@link AnchorTabular} object that contains the training data and its definitions
     */
    static AnchorTabular createTabularTrainingDefinition() {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./churnOML/train_churnOML.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");

        try {
            return new AnchorTabularBuilderSequential()
                    .setDoBalance(false)
                    .addIgnoredColumn("Id")
                    .addIgnoredColumn("state")
                    .addIgnoredColumn("account_length")
                    .addColumn(IntegerColumn.fromStringInput("area_code"))
                    .addIgnoredColumn("phone_number")
                    .addColumn(IntegerColumn.fromStringInput("international_plan"))
                    .addColumn(IntegerColumn.fromStringInput("voice_mail_plan"))
                    .addColumn(IntegerColumn.fromStringInput("number_vmail_messages", -1, null, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_day_minutes", -1, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("total_day_calls", -1, null, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_day_charge", -1, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_eve_minutes", -1, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("total_eve_calls", -1, null, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_eve_charge", -1, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_night_minutes", -1, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("total_night_calls", -1, null, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_night_charge", -1, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_intl_minutes", -1, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("total_intl_calls", -1, null, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_intl_charge", -1, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("number_customer_service_calls"))
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
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./churnOML/test_churnOML.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");

        try {
            return new AnchorTabularBuilderSequential()
                    .setDoBalance(false)
                    .addIgnoredColumn("Id")
                    .addIgnoredColumn("state")
                    .addIgnoredColumn("account_length")
                    .addColumn(IntegerColumn.fromStringInput("area_code"))
                    .addIgnoredColumn("phone_number")
                    .addColumn(IntegerColumn.fromStringInput("international_plan"))
                    .addColumn(IntegerColumn.fromStringInput("voice_mail_plan"))
                    .addColumn(IntegerColumn.fromStringInput("number_vmail_messages", -1, null, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_day_minutes", -1,  discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("total_day_calls", -1, null, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_day_charge", -1,  discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_eve_minutes", -1,  discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("total_eve_calls", -1, null, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_eve_charge", -1,  discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_night_minutes", -1,  discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("total_night_calls", -1, null, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_night_charge", -1,  discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_intl_minutes", -1,  discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("total_intl_calls", -1, null, discretizerSupplier.get()))
                    .addColumn(DoubleColumn.fromStringInput("total_intl_charge", -1,  discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("number_customer_service_calls"))
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
