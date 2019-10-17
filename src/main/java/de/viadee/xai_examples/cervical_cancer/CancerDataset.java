package de.viadee.xai_examples.cervical_cancer;

import de.viadee.discretizers4j.impl.ManualDiscretizer;
import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.builder.AnchorTabularBuilderByName;
import de.viadee.xai.anchor.adapter.tabular.column.BooleanColumn;
import de.viadee.xai.anchor.adapter.tabular.column.DoubleColumn;
import de.viadee.xai.anchor.adapter.tabular.column.IntegerColumn;
import de.viadee.xai.anchor.adapter.tabular.column.StringColumn;

import java.io.IOException;

/**
 * Used to prepare and read-in the cervical cancer dataset
 * <p>
 * The preprocessed dataset contains the following attributes we are interested in:
 * <p>
 * Age
 * Number.of.sexual.partners
 * First.sexual.intercourse
 * Num.of.pregnancies
 * Smokes
 * Smokes..years.
 * Hormonal.Contraceptives
 * Hormonal.Contraceptives..years.
 * IUD
 * IUD..years.
 * STDs
 * STDs..number.
 * STDs..Number.of.diagnosis
 * STDs..Time.since.first.diagnosis
 * STDs..Time.since.last.diagnosis
 * Biopsy
 */
class CancerDataset {
    /**
     * Creates the tabular object
     *
     * @return the {@link AnchorTabular} instance that can be reused by other services
     * @throws IOException if an error occurs while loading the file
     */
    static AnchorTabular createTabular() throws IOException {
        return new AnchorTabularBuilderByName()
                .addColumn(IntegerColumn.fromStringInput("Age", new ManualDiscretizer(15, 25, 35, 50, 60)))
                .addColumn(IntegerColumn.fromStringInput("Number.of.sexual.partners", 3))
                .addColumn(IntegerColumn.fromStringInput("First.sexual.intercourse", 5))
                .addColumn(IntegerColumn.fromStringInput("Num.of.pregnancies", new ManualDiscretizer(0, 1, 2, 4)))
                .addColumn(BooleanColumn.fromStringInput("Smokes"))
                .addColumn(DoubleColumn.fromStringInput("Smokes..years.", 4))
                .addColumn(BooleanColumn.fromStringInput("Hormonal.Contraceptives"))
                .addColumn(DoubleColumn.fromStringInput("Hormonal.Contraceptives..years.", 5))
                .addColumn(BooleanColumn.fromStringInput("IUD"))
                .addColumn(DoubleColumn.fromStringInput("IUD..years.", 5))
                .addColumn(BooleanColumn.fromStringInput("STDs"))
                .addColumn(IntegerColumn.fromStringInput("STDs..number.", new ManualDiscretizer(0, 1)))
                .addColumn(IntegerColumn.fromStringInput("STDs..Number.of.diagnosis", new ManualDiscretizer(0)))
                .addColumn(IntegerColumn.fromStringInput("STDs..Time.since.first.diagnosis", new ManualDiscretizer(0, 3)))
                .addColumn(IntegerColumn.fromStringInput("STDs..Time.since.last.diagnosis", new ManualDiscretizer(0, 3)))
                .addTargetColumn(new StringColumn("Biopsy"))
                .setDoBalance(true)
                .build(ClassLoader.getSystemResourceAsStream("cervical_cancer/cervical_cancer_preprocessed.csv"));
    }
}
