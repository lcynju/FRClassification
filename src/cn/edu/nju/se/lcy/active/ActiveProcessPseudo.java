package cn.edu.nju.se.lcy.active;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.nju.se.lcy.active.util.InstanceManagement;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ActiveProcessPseudo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileFolder = args[0] + args[1] + "\\";
		String[] types = new String[] { "gapwithlowhigh", "gap", "lowinhigh", "random", "twogap" };
		String normalPseudoPairFile = fileFolder + "train-normal-pseudo-pair.txt";
		
		String[] featureNames = new String[] {"heuristicrules", "keywordcounts", "tfidf", "tfidf-keywordcounts-numeric",
				"tfidf-heuristicrules-numeric", "keywordcounts-heuristicrules-numeric",
				"tfidf-keywordcounts-heuristicrules-numeric-numeric" };
		
		for (String featureName : featureNames) {
			String trainFile = fileFolder + "train-" + featureName + "-numeric.arff";
			String pseudoFile = fileFolder + "train-pseudo-" + featureName + "-numeric.arff";
			String testFile = fileFolder + "test-" + featureName + "-numeric.arff";
			
			File fileTest = new File(trainFile);
			if(!fileTest.exists()){
				continue;
			}
			
			for (String type : types) {
				String curveFile = fileFolder + "pseudo-" + type + "-" + featureName + "-curve.txt";

				String[] option1 = new String[] { "-K", "0", "-C", "1", "-M", "1000", "-H", "1", "-B" };
//				String[] option2 = new String[] { "-K", "0", "-C", "10", "-M", "1000", "-H", "1", "-B" };
//				String[] option3 = new String[] { "-K", "0", "-C", "100", "-M", "1000", "-H", "1", "-B" };
				String[][] options = new String[][] { option1 };

				try {
					BufferedWriter curveWriter = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(curveFile)));

					Instances totalTestData = DataSource.read(testFile);
					totalTestData.setClassIndex(totalTestData.numAttributes() - 1);

					InstanceManagement trainInstancesManager = new InstanceManagement(trainFile);
					InstanceManagement pseudoInstanceManager = new InstanceManagement(pseudoFile);

					Map<Instance, Instance> leftPseudo = DataSetOperatorPseudo.generateNorPsePair(trainInstancesManager.getInitialInstances(), pseudoInstanceManager.getInitialInstances(), normalPseudoPairFile);
					
					List<Instance> trainPseudo = new ArrayList<Instance>();
					Instances firstBatch = trainInstancesManager.getFirstBatchAndInitTrainPseudo(trainPseudo, leftPseudo);
					
					Instances testData = trainInstancesManager.getInitialInstances();
					testData.setClassIndex(testData.numAttributes() - 1);

					ArrayList<Attribute> attributeList = trainInstancesManager.getAttributeList();
					while (testData.size() != 0) {
						double accuracy = DataSetOperatorPseudo.train(attributeList, firstBatch, trainPseudo, testData, leftPseudo, options.clone(), type,
								totalTestData, false);

						curveWriter.append(accuracy/100 + "");
						curveWriter.newLine();
						curveWriter.flush();
					}

					double accuracy = DataSetOperatorPseudo.testDirectly(attributeList, firstBatch, trainPseudo, totalTestData, options);
					curveWriter.append(accuracy/100 + "");
					curveWriter.newLine();
					curveWriter.flush();

					curveWriter.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
