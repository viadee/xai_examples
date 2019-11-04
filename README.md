[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)

# XAI Examples

This repository contains tutorials and readily compilable projects/source code concerning Explainable Artificial Intelligence (XAI).

The following algorithms and specific implementations are being used:
+ Anchors. [Implementation](https://github.com/viadee/javaAnchorExplainer) and its optional [Adapters](https://github.com/viadee/javaAnchorAdapters)

## Anchors Titanic Examples

One of the Anchors implementation's main feature consists in facilitating the usage of tabular explanations by providing default 
solutions to common scenarios in conjunction with the anchorj library.
The following use-case exemplifies its usage by creating both local and global explanations of the 
[Titanic tabular dataset](https://www.kaggle.com/c/titanic/data).

### 1. Referencing Dependencies
Using Apache Maven, the required anchorj dependencies are easily referenced and added as follows:

    <!-- AnchorJ -->
    <dependency>
         <groupId>de.viadee.xai.anchor</groupId>
         <artifactId>algorithm</artifactId>
         <version>1.0.2</version>
    </dependency>
    
    <!-- AnchorJ Default Solutions Extension containing AnchorTabular -->
    <dependency>
        <groupId>de.viadee.xai.anchor</groupId>
        <artifactId>DefaultConfigsAdapter</artifactId>
        <version>1.0.3</version>
    </dependency>
    
    <!-- AnchorJ Default Machine Learning Models -->
    <dependency>
        <groupId>de.viadee.xai.anchor</groupId>
        <artifactId>DefaultMLMethods</artifactId>
        <version>1.0.3</version>
    </dependency>
    
    <!-- Loading data from CSV -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-csv</artifactId>
        <version>1.6</version>
    </dependency>

    <!-- Logging --> 
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-slf4j-impl</artifactId>
        <version>2.8.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.8.1</version>
    </dependency>

### 1.5 Optional: Logging settings

If you want to see the logging output create a file named 'log4j2.xml' in the resources folder
and add the following lines:

    <?xml version="1.0" encoding="UTF-8"?>
    <Configuration monitorinterval="30" status="info" strict="true">
        <Properties>
            <property name="pattern">%-5p [%d{yyyy-MM-dd HH:mm:ss.SSS}][%t][%c] %m%n</property>
        </Properties>
        <Appenders>
            <Console name="stdout" target="SYSTEM_OUT">
                <Layout type="PatternLayout" pattern="${pattern}"/>
            </Console>
        </Appenders>
    
        <Loggers>
            <Root level="debug">
                <AppenderRef ref="stdout" level="info"/>
            </Root>
        </Loggers>
    </Configuration>

### 2. Loading and Describing the Dataset

<code>AnchorTabular</code> is henceforth used to easily set up the Anchors algorithm to handle tabular data. 
Therefore, it possesses a builder that enables registering arbitrary columns that describe the dataset. 
A column contains a name for identification, a number of transformations and a discretization.
Whereas transformations are meant to clean data, discretization may be used to achieve better results with Anchors by 
grouping various feature values.

The Titanic dataset gets loaded and configured as follows. 

    AnchorTabular anchorTabular = new AnchorTabularBuilderSequential()
     .setDoBalance(false)
     .addIgnoredColumn("PassengerId")
     .addTargetColumn(IntegerColumn.fromStringInput("Survived"))
     .addColumn(IntegerColumn.fromStringInput("Pclass"))
     .addColumn(new StringColumn("Name"))
     .addColumn(new StringColumn("Sex"))
     .addColumn(DoubleColumn.fromStringInput("Age", -1, 5))
     .addColumn(IntegerColumn.fromStringInput("SibSp"))
     .addColumn(IntegerColumn.fromStringInput("Parch"))
     .addColumn(IntegerColumn.fromStringInput("Ticket", -1,
             Collections.singletonList(new TicketNumberTransformer()), null))
     .addColumn(DoubleColumn.fromStringInput("Fare", -1, 6))
     .addColumn(new StringColumn("Cabin", Arrays.asList(
             new ReplaceNonEmptyTransformer(true),
             new ReplaceEmptyTransformer(false)),
             null))
     .addColumn(new StringColumn("Embarked"))
     .build(ClassLoader.getSystemResourceAsStream("titanic/train.csv", true, false);

Using the sequential tabular builder, definitions for columns are stated in ascending order - just as they appear in the data.
Please note that attributes are described in greater depth in the code. 

All of the configured attributes, such as columns, transformations and discretizations can be implemented and 
extended as required.

It even is possibly to refrain from using this extension altogether and implement a custom solution based directly on 
the base library.

The anchorAdapters will conclude this phase by logging all discretization changes it made to the dataset. This helps monitoring the discretization, finding mistakes and looks like follows in this example's case:

    DEBUG [2019-07-26 15:48:28.112][main][de.viadee.xai.anchor.adapter.tabular.builder.TabularPreprocessor] Discretization for column [Pclass] is configured as follows:
        [1] --> 2
        [2] --> 1
        [3] --> 0
    DEBUG [2019-07-26 15:48:28.113][main][de.viadee.xai.anchor.adapter.tabular.builder.TabularPreprocessor] Discretization for column [Name] is configured as follows:
        [Ware, Mrs. John James (Florence Louise Long)] --> 362
        [Cotterill, Mr. Henry Harry""] --> 377
        [Olsson, Mr. Oscar Wilhelm] --> 300
        [Buckley, Mr. Daniel] --> 106
        [Buckley, Miss. Katherine] --> 113
        [Riordan, Miss. Johanna Hannah""] --> 408
        [Pallas y Castello, Mr. Emilio] --> 369
        [Karnes, Mrs. J Frank (Claire Bennett)] --> 246
        [Payne, Mr. Vivian Ponsonby] --> 390
        [Palsson, Master. Paul Folke] --> 389
        [Denbury, Mr. Herbert] --> 338
        [Makinen, Mr. Kalle Edvard] --> 97
        [Davies, Mr. John Samuel] --> 9
        [Chronopoulos, Mr. Demetrios] --> 115
        [Dodge, Mrs. Washington (Ruth Vidaver)] --> 374
        ... (and 403 more elements)
    DEBUG [2019-07-26 15:48:28.114][main][de.viadee.xai.anchor.adapter.tabular.builder.TabularPreprocessor] Discretization for column [Sex] is configured as follows:
        [female] --> 1
        [male] --> 0
    DEBUG [2019-07-26 15:48:28.116][main][de.viadee.xai.anchor.adapter.tabular.builder.TabularPreprocessor] Discretization for column [Age] is configured as follows:
        ]-1, -1) --> -1
        [0.17, 21) --> 17
        [22, 27) --> 24
        [28, 39) --> 32
        [40, 76[ --> 48
    DEBUG [2019-07-26 15:48:28.116][main][de.viadee.xai.anchor.adapter.tabular.builder.TabularPreprocessor] Discretization for column [SibSp] is configured as follows:
        [0] --> 0
        [1] --> 1
        [2] --> 2
        [3] --> 3
        [4] --> 4
        [5] --> 5
        [8] --> 6
    DEBUG [2019-07-26 15:48:28.116][main][de.viadee.xai.anchor.adapter.tabular.builder.TabularPreprocessor] Discretization for column [Parch] is configured as follows:
        [0] --> 0
        [1] --> 1
        [2] --> 3
        [3] --> 2
        [4] --> 4
        [5] --> 6
        [6] --> 5
        [9] --> 7
    DEBUG [2019-07-26 15:48:28.118][main][de.viadee.xai.anchor.adapter.tabular.builder.TabularPreprocessor] Discretization for column [Ticket] is configured as follows:
        [24065] --> 13
        [11778] --> 96
        [2] --> 173
        [23101284] --> 230
        [233478] --> 168
        [23101291] --> 45
        [3085] --> 30
        [9232] --> 193
        [349202] --> 267
        [349211] --> 68
        [14879] --> 198
        [2079] --> 320
        [349220] --> 10
        [11813] --> 48
        [4133] --> 126
        ... (and 347 more elements)
    DEBUG [2019-07-26 15:48:28.120][main][de.viadee.xai.anchor.adapter.tabular.builder.TabularPreprocessor] Discretization for column [Fare] is configured as follows:
        ]-1, 7.75) --> 7.27
        [7.78, 8.67) --> 7.9
        [8.72, 14.46) --> 12.55
        [14.46, 26) --> 21
        [26.55, 56.5) --> 31.5
        [57.75, 512.33[ --> 83.16
    DEBUG [2019-07-26 15:48:28.120][main][de.viadee.xai.anchor.adapter.tabular.builder.TabularPreprocessor] Discretization for column [Cabin] is configured as follows:
        [false] --> 0
        [true] --> 1
    DEBUG [2019-07-26 15:48:28.121][main][de.viadee.xai.anchor.adapter.tabular.builder.TabularPreprocessor] Discretization for column [Embarked] is configured as follows:
        [Q] --> 0
        [S] --> 1
        [C] --> 2

### 3. Obtaining the Model
Anchors is a <span style="background-color: #FFFF00">Model-Agnostic</span> explanation algorithm and can describe 
<b>any</b> classification model. Hence, its presence is implicitly assumed when creating explanations. 

However, for this example a default solution, i.e. a random forest model is used to remove the need for requirements.

    TabularRandomForestClassifier randomForestModel = new TabularRandomForestClassifier(100, true);
    randomForestModel.fit(anchorTabular.getTabularInstances());
                    
Nonetheless, an arbitrary and custom model can easily be included by implementing the 
<code>ClassificationFunction</code> interface and its predict method.
More options are further provided by the <code>ModelImportExtension</code> project. This enables, among others, exported 
H2O models to be effortlessly explained.
                    
### 4. Obtaining the Explanation
Since both a classifier and perturbation function are now provided, an <code>AnchorConstructionBuilder</code> can be 
obtained. 
(The perturbation function is created by AnchorTabular. Implementing a custom solution is - of course - possible)

The <code>AnchorConstructionBuilder</code> offers configuring various parameters of the algorithm and
can be received by the previously configured <code>AnchorTabular</code> as follows:

    AnchorConstructionBuilder<TabularInstance> defaultBuilder = anchorTabular
                    .createDefaultBuilder(classifier, anchorTabular.getTabularInstances()[0]);
                    
This builder instance can henceforth be used to create explanations (in this case for the first instance contained 
by the <code>anchorTabular</code> instance):

    AnchorResult<TabularInstance> anchor = defaultBuilder.build().constructAnchor();        
    
<code>anchor</code> now provides information about why the model predicted the instance the way it did. 
In order to make the explanation human readable, the <code>TabularInstanceVisualizer</code> provided by the 
<code>anchorTabular</code> can be used as follows:

    System.out.println("====Explained instance====" + System.lineSeparator() +
            anchorTabular.getVisualizer().visualizeInstance(anchor.getInstance()));
            
    System.out.println("====Result====" + System.lineSeparator() +
            anchorTabular.getVisualizer().visualizeResult(anchor));
            
### 5. Global Explanations
The main project contains various algorithms that are able to aggregate multiple single explanations. 
Thereof, <code>CoveragePick</code> is expected to work best. It can be used as follows:

    List<AnchorResult<TabularInstance>> globalExplanations = new CoveragePick<>(defaultBuilder, 10,
                                                                                Executors.newCachedThreadPool(),
                                                                                null)
                    .run(anchorTabular.shuffleSplitInstances(1, 0)[0], 20);          

Similarly, its results may be visualized
    
    System.out.println(anchorTabular.getVisualizer().visualizeGlobalResults(globalExplanations));
       
    
### 6. Exemplary Outputs
The above stated examples produce output similar to the following samples.

    ====Explained instance====
        Pclass='3'
        Name='Gilnagh, Miss. Katherine 'Katie''
        Sex='female'
        Age='16'
        SibSp='0'
        Parch='0'
        Ticket='35851'
        Fare='7'
        Embarked='Q'
        WITH LABEL Survived='1'
    ====Result====
        IF Sex='female' {0.85,-0.58} AND 
        Embarked='Q' {0.11,-0.37} AND 
        SibSp='0' {0.04,-0} AND 
        Parch='0' {0.02,-0}
        THEN PREDICT 1
        WITH PRECISION 1.0 AND COVERAGE 0.033

    
    ===Global Result #1===
        IF Fare IN RANGE [0,8] {0.63,-0.64} AND 
        Sex='male' {0.37,-0.07} AND 
        Parch='0' {0.01,-0}
        THEN PREDICT 0
        WITH PRECISION 1.0 AND COVERAGE 0.283
    ===Global Result #2===
        IF Fare IN RANGE [53,512] {0.68,-0.8} AND 
        Sex='female' {0.29,-0.07} AND 
        Pclass='1' {0.04,-0}
        THEN PREDICT 1
        WITH PRECISION 1.0 AND COVERAGE 0.117
    ===Global Result #3===
        IF SibSp='8' {0.64,-0.99} AND 
        Pclass='3' {0.06,0} AND 
        Ticket='CA. 2343' {0.19,0} AND 
        Embarked='S' {0.09,0} AND 
        Age IN RANGE [-1,0] {0.04,0}
        THEN PREDICT 0
        WITH PRECISION 1.0 AND COVERAGE 0.007
    ===Global Result #4===
        IF Fare IN RANGE [27,52] {0.61,-0.87} AND 
        Name='Barkworth, Mr. Algernon Henry Wilson' {0.24,-0.12} AND 
        Ticket='27042' {0.16,0}
        THEN PREDICT 1
        WITH PRECISION 1.0 AND COVERAGE 0.002
    ===Global Result #5===
        IF Fare IN RANGE [15,26] {0.55,-0.81} AND 
        Parch='2' {0.19,-0.16} AND 
        SibSp='1' {0.12,-0.02} AND 
        Name='Dean, Master. Bertram Vere' {0.16,0}
        THEN PREDICT 1
        WITH PRECISION 1.0 AND COVERAGE 0.002
        
Note that the bracketed values describe the added precision and coverage the inclusion of the respective feature 
effected. These values can be used to quickly infer a less precise anchor having a superior coverage.        

### Optimizations 

The required time to obtain explanations depends almost exclusively on the model and its latencies.
Depending on the explained instances and set parameters, this runtime can range from a few seconds to multiple hours.

The above examples should terminate in a few seconds, due to the random forest's high performance.

So, it shall not go unnoticed that the previous examples can be sped up significantly by configuring Anchors to 
utilize different forms of parallelization:

#### Threading

Enabling threading is easily achieved by configuring the 
<code>AnchorConstructionBuilder</code>:

    defaultBuilder.enableThreading(10 /*ThreadCount*/, Executors.newFixedThreadPool(10), null);
    
This leads to single explanations being explained significantly fast (depending on your machine's performance and the 
model's latency).

#### FastMPJ and Apache Spark

Furthermore, multiple approaches are included in this project to load balance the creation of multiple explanations
among a cluster of computers.
This is especially useful for global explanations.

* A message passing interface (MPI) implementation is included in the <code>FastMPJExtension</code> package.
* An Apache Spark adapter can be found in the <code>SparkExtension</code>.
* A default Threading approach is included in anchorj core.

These methods provide implementations of the <code>BatchExplainer</code> interface which can be plugged in to the global
explainers as follows:

    new CoveragePick<>(false, new SparkBatchExplainer(sparkContext), defaultBuilder);
    
However, these methods require advanced setups and configurations. 
For further information, please refer to respective project documentations.

# Collaboration

The project is operated and further developed by the viadee Consulting AG in Münster, Westphalia. Results from theses at the WWU Münster and the FH Münster have been incorporated.
* Further theses are planned: Contact person is Dr. Frank Köhne from viadee.
    Community contributions to the project are welcome: Please open Github-Issues with suggestions (or PR), which we can then edit in the team. For general discussions please refer to the [main repository](https://github.com/viadee/javaAnchorExplainer).
*   We are looking for further partners who have interesting process data to refine our tooling as well as partners that are simply interested in a discussion about AI in the context of business process automation and explainability.
