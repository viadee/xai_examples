package de.viadee.xai_examples.cervical_cancer;

import de.viadee.xai.anchor.adapter.classifiers.AbstractTabularSmileClassifier;
import de.viadee.xai.anchor.adapter.classifiers.GradientBoostedTreeTabularClassifier;
import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.TabularInstance;
import de.viadee.xai.anchor.algorithm.AnchorConstructionBuilder;
import de.viadee.xai.anchor.algorithm.AnchorResult;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Main entry comprising all steps to create both local and global explanations
 */
public class CancerExplanation {

    public static void main(String[] args) throws IOException {
        // Load dataset and its description
        final AnchorTabular anchorTabular = CancerDataset.createTabular();

        // Obtain a second suitable model. Train it ourselves this time.
        final AbstractTabularSmileClassifier randomForestModel = new GradientBoostedTreeTabularClassifier(100, false);
        randomForestModel.fit(anchorTabular.getTabularInstances());

        // Print the model's accuracy
        outputTrainAccuracy(randomForestModel, anchorTabular);

        // Pick (random because balanced) instance to be explained
        final TabularInstance explainedInstance = anchorTabular.getTabularInstances()[0];

        // Create builder that can be used to create an AnchorConstruction instance
        final AnchorConstructionBuilder<TabularInstance> builder = anchorTabular
                .createDefaultBuilder(randomForestModel, explainedInstance)
                .setTau(0.8);
        final AnchorResult<TabularInstance> anchorResult = builder.build().constructAnchor();

        System.out.println("====Explained instance====" + System.lineSeparator() +
                anchorTabular.getVisualizer().visualizeInstance(explainedInstance));
        System.out.println("====Result====" + System.lineSeparator() +
                anchorTabular.getVisualizer().visualizeResult(anchorResult));
    }


    private static void outputTrainAccuracy(Function<TabularInstance, Integer> predictFunction,
                                            AnchorTabular anchorTabular) {
        final TabularInstance[] testData = anchorTabular.getTabularInstances();
        final int[] actualLabels = Stream.of(testData).mapToInt(t -> t.getDiscretizedLabel().intValue()).toArray();
        int matches = 0;
        for (int i = 0; i < testData.length; i++) {
            if (predictFunction.apply(testData[i]).equals(actualLabels[i]))
                matches++;
        }

        System.out.println("==== Accuracy====" + System.lineSeparator() +
                matches / (double) testData.length);
    }
}
