package de.viadee.xai.xai_examples.titanic;

import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.column.DoubleColumn;
import de.viadee.xai.anchor.adapter.tabular.column.IntegerColumn;
import de.viadee.xai.anchor.adapter.tabular.column.StringColumn;

import java.io.IOException;
import java.io.InputStream;

/**
 * Loads the dataset and prepares the {@link AnchorTabular} object
 */
class TitanicDataset {

    /**
     * @return the {@link AnchorTabular} object
     */
    static AnchorTabular createTabularDefinition() {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream("train.csv");
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load training data");

        try {
            return new AnchorTabular.Builder()
                    .setDoBalance(true)
                    .addIgnoredColumn("PassengerId")
                    .addTargetColumn(IntegerColumn.fromStringInput("Survived"))
                    .addColumn(IntegerColumn.fromStringInput("Pclass"))
                    .addColumn(new StringColumn("Name"))
                    .addColumn(new StringColumn("Sex"))
                    .addColumn(DoubleColumn.fromStringInput("Age", -1, 5))
                    .addColumn(IntegerColumn.fromStringInput("SibSp"))
                    .addColumn(IntegerColumn.fromStringInput("Parch"))
                    .addColumn(new StringColumn("Ticket"))
                    .addColumn(DoubleColumn.fromStringInput("Fare", 6))
                    .addIgnoredColumn(new StringColumn("Cabin"))
                    .addColumn(new StringColumn("Embarked"))
                    .build(trainingDataStream, true, false);
        } catch (IOException e) {
            throw new RuntimeException("Could not read training data");
        }
    }
}
