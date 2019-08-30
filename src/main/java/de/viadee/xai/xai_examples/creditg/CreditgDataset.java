package de.viadee.xai.xai_examples.creditg;

import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.builder.AnchorTabularBuilderSequential;
import de.viadee.xai.anchor.adapter.tabular.column.DoubleColumn;
import de.viadee.xai.anchor.adapter.tabular.column.IntegerColumn;
import de.viadee.xai.anchor.adapter.tabular.column.StringColumn;
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
class CreditgDataset {

    /**
     * @return the {@link AnchorTabular} object that contains the training data and its definitions
     */
    static AnchorTabular createTabularTrainingDefinition() {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./creditg/train_creditg.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");

        try {
            return new AnchorTabularBuilderSequential()
                    .setDoBalance(false)
                    .addIgnoredColumn("Id")
                    .addColumn(new StringColumn("checking_status"))
                    .addColumn(IntegerColumn.fromStringInput("duration", -1, 4))
                    .addColumn(new StringColumn("credit_history"))
                    .addColumn(new StringColumn("purpose"))
                    .addColumn(IntegerColumn.fromStringInput("credit_amount", -1, 4))
                    .addColumn(new StringColumn("savings_status"))
                    .addColumn(new StringColumn("employment"))
                    .addColumn(IntegerColumn.fromStringInput("installment_commitment"))
                    .addColumn(new StringColumn("personal_status"))
                    .addColumn(new StringColumn("other_parties"))
                    .addColumn(IntegerColumn.fromStringInput("residence_since"))
                    .addColumn(new StringColumn("poperty_magnitude"))
                    .addColumn(IntegerColumn.fromStringInput("age", -1, 5))
                    .addColumn(new StringColumn("other_payment_plans"))
                    .addColumn(new StringColumn("housing"))
                    .addColumn(IntegerColumn.fromStringInput("existing_credits"))
                    .addColumn(new StringColumn("job"))
                    .addColumn(IntegerColumn.fromStringInput("num_dependents"))
                    .addColumn(new StringColumn("own_telephone"))
                    .addColumn(new StringColumn("foreign_worker"))
                    .addTargetColumn(new StringColumn("class"))
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
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./creditg/test_creditg.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");

        try {
            return new AnchorTabularBuilderSequential()
                    .setDoBalance(false)
                    .addIgnoredColumn("Id")
                    .addColumn(new StringColumn("checking_status"))
                    .addColumn(IntegerColumn.fromStringInput("duration", -1, 4))
                    .addColumn(new StringColumn("credit_history"))
                    .addColumn(new StringColumn("purpose"))
                    .addColumn(IntegerColumn.fromStringInput("credit_amount", -1, 4))
                    .addColumn(new StringColumn("savings_status"))
                    .addColumn(new StringColumn("employment"))
                    .addColumn(IntegerColumn.fromStringInput("installment_commitment"))
                    .addColumn(new StringColumn("personal_status"))
                    .addColumn(new StringColumn("other_parties"))
                    .addColumn(IntegerColumn.fromStringInput("residence_since"))
                    .addColumn(new StringColumn("poperty_magnitude"))
                    .addColumn(IntegerColumn.fromStringInput("age", -1, 5))
                    .addColumn(new StringColumn("other_payment_plans"))
                    .addColumn(new StringColumn("housing"))
                    .addColumn(IntegerColumn.fromStringInput("existing_credits"))
                    .addColumn(new StringColumn("job"))
                    .addColumn(IntegerColumn.fromStringInput("num_dependents"))
                    .addColumn(new StringColumn("own_telephone"))
                    .addColumn(new StringColumn("foreign_worker"))
                    .addTargetColumn(new StringColumn("class"))
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
