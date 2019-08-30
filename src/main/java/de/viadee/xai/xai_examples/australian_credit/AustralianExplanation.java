package de.viadee.xai.xai_examples.australian_credit;

import de.viadee.xai.anchor.adapter.classifiers.GradientBoostedTreeTabularClassifier;
import de.viadee.xai.anchor.adapter.classifiers.TabularRandomForestClassifier;
import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.TabularInstance;
import de.viadee.xai.anchor.adapter.tabular.TabularPerturbationFunction;
import de.viadee.xai.anchor.adapter.tabular.dmn.DMN_Explainer;
import de.viadee.xai.anchor.algorithm.AnchorConstructionBuilder;
import de.viadee.xai.anchor.algorithm.AnchorResult;
import de.viadee.xai.anchor.algorithm.exploration.KL_LUCB;
import de.viadee.xai.anchor.algorithm.global.CoveragePick;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main entry comprising all steps to create both local and global explanations
 */
public class AustralianExplanation {
    private static final Logger LOGGER = LoggerFactory.getLogger(AustralianExplanation.class);


    public static void main(String[] args) throws IOException {
        // Load dataset and its description
        final AnchorTabular anchorTabular = AustralianDataset.createTabularTrainingDefinition();

        // Our first model to be explained (GBM). Imported from an H2O model pre-built and trained in R
        final Australian_H2OModelWrapper h2oModel = new Australian_H2OModelWrapper();
        // Obtain a second suitable model (RandomForest). Train it ourselves this time.
        final TabularRandomForestClassifier randomForestModel = new TabularRandomForestClassifier(100, false);
        randomForestModel.fit(anchorTabular.getTabularInstances());
        final GradientBoostedTreeTabularClassifier gbm = new GradientBoostedTreeTabularClassifier(100, false);
        gbm.fit(anchorTabular.getTabularInstances());

        // Print the model's test data accuracy
        outputTestsetAccuracy("H2OModel", h2oModel::predict);
        outputTestsetAccuracy("RandomForest", randomForestModel);


        final double tau = 0.8;
        // Create builder that can be used to create an AnchorConstruction instance
        // H2O default builder
        List<AnchorResult<TabularInstance>> explanations = Stream.of(anchorTabular.getTabularInstances())
                .limit(10)
                .map(instance ->
                        new AnchorConstructionBuilder<>(
                                randomForestModel::apply,
                                new TabularPerturbationFunction(instance, anchorTabular.getTabularInstances()),
                                instance, randomForestModel.apply(instance))
                                .setTau(tau)
                                .setInitSampleCount(20)
                                .setBestAnchorIdentification(new KL_LUCB(400))
                                .build().constructAnchor())
                //
                // .filter(i -> i.getPrecision() > tau)
                .collect(Collectors.toList());

        try {
            Writer writer = new PrintWriter("AustralianUnique.csv", "UTF-8");
            CSVPrinter csvWriter = CSVFormat.RFC4180.withQuoteMode(QuoteMode.ALL)
                    .withHeader("coverage", "precision").print(writer);

            explanations.stream()
                    .map(result -> new Double[]{result.getCoverage(), result.getPrecision()})
                    .forEach(values -> {
                        try {
                            csvWriter.printRecord(values);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            csvWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Create local explanations
        // System.out.println("====H2O Local Explanation====");
        // printLocalExplanationResult(explainedInstance, anchorTabular, h2oBuilder);
//        System.out.println("====Random Forest Local Explanation====");
//        printLocalExplanationResult(explainedInstance, anchorTabular, randomForestBuilder);

//         Create global explanations
        System.out.println("====H2O Global Explanation====");
//        printGlobalExplanationResult(anchorTabular, h2oBuilder, false);
//        System.out.println("====Random Forest Global Explanation====");
//        printGlobalExplanationResult(anchorTabular, randomForestBuilder, false);
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
                                                     AnchorConstructionBuilder<TabularInstance> builder, boolean dmnOutput) {
        // Use the CoveragePick algorithm to create global explanations
        final List<AnchorResult<TabularInstance>> globalExplanations = new CoveragePick<>(builder, 10,
                Executors.newCachedThreadPool(), null)
                .run(tabular.getTabularInstances(), 20);

        System.out.println(tabular.getVisualizer().visualizeGlobalResults(globalExplanations));


        // DMN Decision Table Builder
        if (dmnOutput == true) {
            OutputStream outputStream = new OutputStream() {
                private StringBuilder string = new StringBuilder();

                @Override
                public void write(int b) throws IOException {
                    this.string.append((char) b);
                }

                public String toString() {
                    return this.string.toString();
                }
            };
            LOGGER.debug(outputStream.toString());
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DMN_Explainer.createDMNglobalExplanation(globalExplanations, System.out);
        }
    }


    private static void outputTestsetAccuracy(String name, Function<TabularInstance, Integer> predictFunction) {
        final TabularInstance[] testData = AustralianDataset.createTabularTestDefinition().getTabularInstances();
        final int[] actualTestLabels = AustralianDataset.readTestLabels();
        int matches = 0;
        for (int i = 0; i < testData.length; i++) {
            if (predictFunction.apply(testData[i]).equals(actualTestLabels[i]))
                matches++;
        }

        System.out.println("====" + name + " Testset Accuracy====" + System.lineSeparator() +
                matches / (double) testData.length);
    }

}
