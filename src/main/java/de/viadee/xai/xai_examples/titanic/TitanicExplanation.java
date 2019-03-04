package de.viadee.xai.xai_examples.titanic;

import de.viadee.xai.anchor.adapter.classifiers.TabularRandomForestClassifier;
import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.TabularInstance;
import de.viadee.xai.anchor.algorithm.AnchorConstructionBuilder;
import de.viadee.xai.anchor.algorithm.AnchorResult;
import de.viadee.xai.anchor.algorithm.global.CoveragePick;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Main entry comprising all steps to create both local and global explanations
 */
public class TitanicExplanation {

    public static void main(String[] args) {
        // Load dataset and its description
        final AnchorTabular anchorTabular = TitanicDataset.createTabularDefinition();

        // Train a random forest. Required so that there is a model that can be explained by anchors
        final TabularRandomForestClassifier classifier = TabularRandomForestClassifier
                .createAndFit(50, anchorTabular.getTabularInstances());

        // Create default builder which can be used to create anchors
        final AnchorConstructionBuilder<TabularInstance> defaultBuilder = anchorTabular
                .createDefaultBuilder(classifier, anchorTabular.getTabularInstances()[0]);

        // Create a local explanation
        final AnchorResult<TabularInstance> anchor = defaultBuilder
                .enableThreading(10, Executors.newFixedThreadPool(10), null)
                .build()
                .constructAnchor();

        System.out.println("====Explained instance====" + System.lineSeparator() +
                anchorTabular.getVisualizer().visualizeInstance(anchor.getInstance()));

        System.out.println("====Result====" + System.lineSeparator() +
                anchorTabular.getVisualizer().visualizeResult(anchor));

        // Use the CoveragePick algorithm to create global explanations
        final List<AnchorResult<TabularInstance>> globalExplanations = new CoveragePick<>(defaultBuilder, 10,
                Executors.newCachedThreadPool(), null)
                .run(anchorTabular.shuffleSplitInstances(1, 0)[0], 20);

        System.out.println(anchorTabular.getVisualizer().visualizeGlobalResults(globalExplanations));
    }

}
