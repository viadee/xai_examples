package de.viadee.xai.xai_examples.titanic;

import de.viadee.xai.anchor.adapter.classifiers.TabularRandomForestClassifier;
import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.TabularInstance;
import de.viadee.xai.anchor.algorithm.AnchorConstructionBuilder;
import de.viadee.xai.anchor.algorithm.AnchorResult;
import de.viadee.xai.anchor.algorithm.global.CoveragePick;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * Main entry comprising all steps to create both local and global explanations
 */
public class TitanicExplanation {

    public static void main(String[] args) throws IOException {
        // Load dataset and its description
        final AnchorTabular anchorTabular = TitanicDataset.createTabularTrainingDefinition();

        // Our first model to be explained (GBM). Imported from an H2O model pre-built and trained in R
        final H2OModelWrapper h2oModel = new H2OModelWrapper();
        // Obtain a second suitable model (RandomForest). Train it ourselves this time.
        final TabularRandomForestClassifier randomForestModel = new TabularRandomForestClassifier(100, true);
        randomForestModel.fit(anchorTabular.getTabularInstances());

        // Print the model's test data accuracy
        outputTestsetAccuracy("H2OModel", h2oModel::predict);
        outputTestsetAccuracy("RandomForest", randomForestModel);

        // Pick instance to be explained
        // Next pick specific instance (countess or patrick dooley)
        final TabularInstance explainedInstance = anchorTabular.getTabularInstances()[759];
        //final TabularInstance explainedInstance = anchorTabular.getTabularInstances()[890];


        // Create builder that can be used to create an AnchorConstruction instance
        // H2O default builder
        final AnchorConstructionBuilder<TabularInstance> h2oBuilder = anchorTabular
                .createDefaultBuilder(h2oModel::predict, explainedInstance);
        // TODO enable random forest again - there seems to be an issue with it
        // RandomForest default builder
        //final AnchorConstructionBuilder<TabularInstance> randomForestBuilder = anchorTabular
        //        .createDefaultBuilder(randomForestModel, explainedInstance);


        // Create local explanations
        System.out.println("====H2O Local Explanation====");
        printLocalExplanationResult(explainedInstance, anchorTabular, h2oBuilder);
        //System.out.println("====Random Forest Local Explanation====");
        //printLocalExplanationResult(explainedInstance, anchorTabular, randomForestBuilder);

        // Create global explanations
        System.out.println("====H2O Global Explanation====");
        printGlobalExplanationResult(anchorTabular, h2oBuilder);
        //System.out.println("====Random Forest Global Explanation====");
        //printGlobalExplanationResult(anchorTabular, randomForestBuilder);
    }

    private static void printLocalExplanationResult(TabularInstance instance, AnchorTabular tabular,
                                                    AnchorConstructionBuilder<TabularInstance> builder) {
        final AnchorResult<TabularInstance> anchor = builder
                .build()
                .constructAnchor();

        System.out.println("====Explained instance====" + System.lineSeparator() +
                tabular.getVisualizer().visualizeInstance(anchor.getInstance()));

        System.out.println("====Result====" + System.lineSeparator() +
                tabular.getVisualizer().visualizeResult(anchor));
    }

    private static void printGlobalExplanationResult(AnchorTabular tabular,
                                                     AnchorConstructionBuilder<TabularInstance> builder) {
        // Use the CoveragePick algorithm to create global explanations
        final List<AnchorResult<TabularInstance>> globalExplanations = new CoveragePick<>(builder, 10,
                Executors.newCachedThreadPool(), null)
                .run(tabular.getTabularInstances(), 20);

        System.out.println(tabular.getVisualizer().visualizeGlobalResults(globalExplanations));
    }


    private static void outputTestsetAccuracy(String name, Function<TabularInstance, Integer> predictFunction) {
        final TabularInstance[] testData = TitanicDataset.createTabularTestDefinition().getTabularInstances();
        final int[] actualTestLabels = TitanicDataset.readTestLabels();
        int matches = 0;
        for (int i = 0; i < testData.length; i++) {
            if (predictFunction.apply(testData[i]).equals(actualTestLabels[i]))
                matches++;
        }

        System.out.println("====" + name + " Testset Accuracy====" + System.lineSeparator() +
                matches / (double) testData.length);
    }


}
