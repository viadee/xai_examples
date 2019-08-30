package de.viadee.xai.xai_examples.titanic;

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
public class TitanicLocalEval {
    private static final Logger LOGGER = LoggerFactory.getLogger(TitanicLocalEval.class);


    public static void main(String[] args) throws IOException {
        // Load dataset and its description
        final AnchorTabular anchorTabular = TitanicDataset.createTabularTrainingDefinition();

        // Our first model to be explained (GBM). Imported from an H2O model pre-built and trained in R
        final H2OModelWrapper h2oModel = new H2OModelWrapper();

        final double tau = 0.9;
        // Create builder that can be used to create an AnchorConstruction instance
        // H2O default builder
        List<AnchorResult<TabularInstance>> explanations = Stream.of(anchorTabular.getTabularInstances())
                .limit(250)
                .map(instance ->
                        new AnchorConstructionBuilder<>(
                                h2oModel::predict,
                                new TabularPerturbationFunction(instance, anchorTabular.getTabularInstances()),
                                instance, h2oModel.predict(instance))
                                .setTau(tau)
                                .setInitSampleCount(20)
                                .setBestAnchorIdentification(new KL_LUCB(400))
                                .build().constructAnchor())
                .collect(Collectors.toList());

        try {
            Writer writer = new PrintWriter("TitanicEqualF3.csv", "UTF-8");
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
    }

}
