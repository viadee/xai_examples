package de.viadee.xai.xai_examples.australian_credit;

import de.viadee.discretizers4j.Discretizer;
import de.viadee.discretizers4j.impl.*;
import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.builder.AnchorTabularBuilderSequential;
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
class AustralianDataset {

    static final Supplier<Discretizer> discretizerSupplier = () -> new UniqueValueDiscretizer();

    /**
     * @return the {@link AnchorTabular} object that contains the training data and its definitions
     */
    static AnchorTabular createTabularTrainingDefinition() {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./australian_credit/train_australianCredit.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");


        try {
            return new AnchorTabularBuilderSequential()
                    .setDoBalance(false)
                    .addIgnoredColumn("Id")
                    .addColumn(IntegerColumn.fromStringInput("A1"))
                    .addColumn(IntegerColumn.fromStringInput("A2", -1, null, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("A3", -1, null, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("A4"))
                    .addColumn(IntegerColumn.fromStringInput("A5"))
                    .addColumn(IntegerColumn.fromStringInput("A6"))
                    .addColumn(IntegerColumn.fromStringInput("A7", -1, null, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("A8"))
                    .addColumn(IntegerColumn.fromStringInput("A9"))
                    .addColumn(IntegerColumn.fromStringInput("A10", -1, null, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("A11"))
                    .addColumn(IntegerColumn.fromStringInput("A12"))
                    .addColumn(IntegerColumn.fromStringInput("A13", -1, null, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("A14", -1, null, discretizerSupplier.get()))
                    .addTargetColumn(IntegerColumn.fromStringInput("A15"))
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
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./australian_credit/test_australianCredit.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");

        try {
            return new AnchorTabularBuilderSequential()
                    .setDoBalance(false)
                    .addIgnoredColumn("Id")
                    .addColumn(IntegerColumn.fromStringInput("A1"))
                    .addColumn(IntegerColumn.fromStringInput("A2", -1, null, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("A3", -1, null, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("A4"))
                    .addColumn(IntegerColumn.fromStringInput("A5"))
                    .addColumn(IntegerColumn.fromStringInput("A6"))
                    .addColumn(IntegerColumn.fromStringInput("A7", -1, null, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("A8"))
                    .addColumn(IntegerColumn.fromStringInput("A9"))
                    .addColumn(IntegerColumn.fromStringInput("A10", -1, null, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("A11"))
                    .addColumn(IntegerColumn.fromStringInput("A12"))
                    .addColumn(IntegerColumn.fromStringInput("A13", -1, null, discretizerSupplier.get()))
                    .addColumn(IntegerColumn.fromStringInput("A14", -1, null, discretizerSupplier.get()))
                    .addTargetColumn(IntegerColumn.fromStringInput("A15"))
                    .build(trainingDataStream, true, false);
        } catch (IOException e) {
            throw new RuntimeException("Could not read data");
        }
    }

    /**
     * @return the labels of the test set as specified in gender_submission.
     */
    static int[] readTestLabels() {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("./australian_credit/test_australianCredit.csv");
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
