package de.viadee.xai.xai_examples.banking2;

import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.builder.AnchorTabularBuilderSequential;
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
class Banking2Dataset {

    /**
     * @return the {@link AnchorTabular} object that contains the training data and its definitions
     */
    static AnchorTabular createTabularTrainingDefinition() {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("banking2/train_bankmarketing2.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");

        try {
            return new AnchorTabularBuilderSequential()
                    .setDoBalance(false)
                    .addIgnoredColumn("Id")
                    .addColumn(IntegerColumn.fromStringInput("V1", -1, 5))
                    .addColumn(new StringColumn("V2"))
                    .addColumn(new StringColumn("V3"))
                    .addColumn(new StringColumn("V4"))
                    .addColumn(new StringColumn("V5"))
                    .addColumn(IntegerColumn.fromStringInput("V6", -1, 6))
                    .addColumn(new StringColumn("V7"))
                    .addColumn(new StringColumn("V8"))
                    .addColumn(new StringColumn("V9"))
                    .addColumn(IntegerColumn.fromStringInput("V10", -1, 3))
                    .addColumn(new StringColumn("V11"))
                    .addColumn(IntegerColumn.fromStringInput("V12", -1, 5))
                    .addColumn(IntegerColumn.fromStringInput("V13", -1, 5))
                    .addColumn(IntegerColumn.fromStringInput("V14"))
                    .addColumn(IntegerColumn.fromStringInput("V15", -1, 3))
                    .addColumn(new StringColumn("V16"))
                    .addTargetColumn(IntegerColumn.fromStringInput("Class"))
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
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./banking2/test_bankmarketing2.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");

        try {
            return new AnchorTabularBuilderSequential()
                    .setDoBalance(false)
                    .addIgnoredColumn("Id")
                    .addColumn(IntegerColumn.fromStringInput("V1", -1, 5))
                    .addColumn(new StringColumn("V2"))
                    .addColumn(new StringColumn("V3"))
                    .addColumn(new StringColumn("V4"))
                    .addColumn(new StringColumn("V5"))
                    .addColumn(IntegerColumn.fromStringInput("V6", -1, 6))
                    .addColumn(new StringColumn("V7"))
                    .addColumn(new StringColumn("V8"))
                    .addColumn(new StringColumn("V9"))
                    .addColumn(IntegerColumn.fromStringInput("V10", -1, 3))
                    .addColumn(new StringColumn("V11"))
                    .addColumn(IntegerColumn.fromStringInput("V12", -1, 5))
                    .addColumn(IntegerColumn.fromStringInput("V13", -1, 5))
                    .addColumn(IntegerColumn.fromStringInput("V14"))
                    .addColumn(IntegerColumn.fromStringInput("V15", -1, 3))
                    .addColumn(new StringColumn("V16"))
                    .addTargetColumn(IntegerColumn.fromStringInput("Class"))
                    .build(trainingDataStream, true, false);
        } catch (IOException e) {
            throw new RuntimeException("Could not read data");
        }
    }

    /**
     * @return the labels of the test set as specified in gender_submission.
     */
    static int[] readTestLabels() {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./banking2/test_bankmarketing2.csv");
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

    private static final class TicketNumberTransformer implements Transformer {

        @Override
        public Serializable apply(Serializable serializable) {
            // Transforms the ticket column to contain only the ticket number without ticket prefixes
            String replaced = ((String) serializable).replaceAll("[^\\d]+", "");

            return ("".equals(replaced)) ? -1 : replaced;
        }
    }
}
