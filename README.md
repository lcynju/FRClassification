# FRClassification
Automatically generate n-folds cross-validation groups and organize .arff for Weka. Use Weka packages for training and testing. Write evaluations in .txt files.

Packages:
    1. All packages starts with 'cn.edu.nju.se.lcy' (~) means Chuanyi Li in Department of Software Engineering at Nanjing University, China.
    2. ~.active is the package for main classes of Active Learning. AllMain.java is the port of this package.
    3. ~.active.dataprepare splits data set into n-folds corss validation distributions, extract all features for all distributions and generate input files with features for Weka's training packages.
    4. ~.active.statistics is for collect learning curves of ActiveLearning. 
    5. ~.active.util are detailed steps used in ActiveLearning, such as initilize training data, instances comparator and instances manager.
    ************************
    6. Following four packages (7,8,9) are not specifically for active learning strategy.
    7. ~.extract contains concrete Objects for calculate each single feature for intial data sets
    8. ~.generate contains concrete Objects for split data represented by single feature files or combined features files into n-folds and generate features files for Weka.
    9. ~.learn and ~.learnalgorithms contain concrete classes for training, testing and recording resutls.
    ************************
    10. ~.mains.notest contains a pipeline for conducting n-folds on preprosessed data sets automatically.
    11. ~.mains.testreserved contains a pipeline for conducting training and testing on splited data sets.
    12. ~.heu is used for calculating HeuristicProperties for each feature request.
    13. ~.pseudoinstance is used for generating Pseudo Instances according to different strategies. 

Usage:
    1. Non-Active Learning: 
       --Pipeline.java
           --ExtractMain.java
               --CalculateTFIDF.java, WordUnigram.java, KeywordCoungs.java, ... (classes in ~.extract)
           --SplitMain.java
               --SplitGroups.java (in ~.generate)
           --OrganizeMain.java
               --Organize4Weka.java (in ~.generate)
           --LearnMain.java
               --Classify.java (in ~.learn)
                 --Learner.java (BinaryLearner.java, AutoMultiLearner.java, ManualMultiLearner.java in ~.learn)
                   --LearnAlgorithm.java (LearnAlgorithmFactory.java: LibSVMDo.java, NaiveBayesDo.java, NaiveBayesMultinomialDo.java)
    2. Active Learning:
       --SplitTrainAndTest.java (~.active.dataprepare)
       --ExtractTSTTALL.java
       --OrganizeSTTALL.java
       --AllMain.java (~.active)
           --ActiveProcessNoWriter.java (ActiveProcessPseudo.java, ActiveProcessPseudoDuplicate.java, ~.active)
               --InstanceManagement.java (~.active.util)
               --DataSetOperatorNoWriter.java (~.active)
                   --NextInstanceListManager.java (~.active.util)
                       --NextInstanceComparator.java (~.active.util)
                           --IndexComparator.java (~.active.util)

Folder Structure:
    1. Non-Active Learning:
       ----Parent Folder
                 |------Project 1 Folder
                             |------source data.txt
                             |------processed.txt
                             |------pos.txt
                             |------trueClasses.txt
                             |------keywords.txt
                 |------Project 2 Folder
                             |------source data.txt
                             |------processed.txt
                             |------pos.txt
                             |------trueClasses.txt
                             |------keywords.txt
    2. Active Learning:
       ----Parent Folder
                 |------Project 1 Folder
                             |------active sources folder
                                             |------source data.txt
                                             |------processed.txt
                                             |------processed with class.txt
                                             |------pos.txt
                                             |------trueClasses.txt
                                             |------keywords.txt
                             |------Active Learning Fold0 /*will automatically generated*/
                             |------Active Learning Fold1 /*will automatically generated*/
                             |------(Active Learning Foldi) /*will automatically generated*/
                             |------Active Learning Foldk /*will automatically generated*/
                 |------Project 2 Folder
                             |------active sources folder
                                             |------source data.txt
                                             |------processed.txt
                                             |------processed with class.txt
                                             |------pos.txt
                                             |------trueClasses.txt
                                             |------keywords.txt
                             |------Active Learning Fold0 /*will automatically generated*/
                             |------Active Learning Fold1 /*will automatically generated*/
                             |------(Active Learning Foldi)
                             |------Active Learning Foldk /*will automatically generated*/
File Struectures:
    1. source data.txt, e.g., 
    "I would like to suggest real hierarchical password group to be add to keepass. this would have the follow advantage. better understanding of the explorer-like treeview. consistent drag and drop indenting delete add pruning grafting of part of the tree." 
    2. processed.txt, e.g., 
    "i will like to sug real hierarchical password group to be add to thi will have the follow advantage good understand have the explore like treeview consistent drag and drop indent delete add prune graft have part have the tree " <split with space>
    3. trueClasses.txt, e.g., 
    "Capability
     Performance
     Reliability"
    4. pos.txt, e.g., 
    "PRP	MD	VB	TO	VB	JJ	JJ	NN	NNS	TO	VB	VBN	TO	VB	.	DT	MD	VB	DT	VBG	NNS	.	JJR	NN	IN	DT	JJ	NN	.	JJ	NN	CC	NN	NN	VBG	VBG	NN	NN	IN	NNS	IN	DT	NN	.	" <split with \t>
    5. keywords.txt, e.g., 
    "capability:store automatically allow great add database nice group different rather drag column ability show drop merge customize feature add group ability drag drop certain expiration merge customize integrate allow function feature capability able"
    6. processed with class.txt, e.g., 
    "Capability	i will like to sug real hierarchical password group to be add to thi will have the follow advantage good understand have the explore like treeview consistent drag and drop indent delete add prune graft have part have the tree "
