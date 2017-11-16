package cn.edu.nju.se.lcy.active.dataprepare;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class STTMultiLearning {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileFolder = "E:\\Cross Classification\\";
		String[] featureNames = new String[] { /*"heuristicrules", "keywordcounts", "tfidf", "tfidf-keywordcounts",
				"tfidf-heuristicrules", */"keywordcounts-heuristicrules"/*, "tfidf-keywordcounts-heuristicrules"*/ };
		for (String featureName : featureNames) {
			String trainFolder = fileFolder + "train data\\";
			String testFolder = fileFolder + "test data\\";

			String trainFile = trainFolder + featureName + "\\";
			String testFile = testFolder + featureName + "\\";
			if (featureName.split("-").length == 1) {
				trainFile = trainFile + featureName + "-numeric.arff";
				testFile = testFile + featureName + "-numeric.arff";
			} else if (featureName.split("-").length == 2) {
				trainFile = trainFile + featureName + "-numeric-numeric.arff";
				testFile = testFile + featureName + "-numeric-numeric.arff";
			} else {
				trainFile = trainFile + featureName + "-numeric-numeric-numeric.arff";
				testFile = testFile + featureName + "-numeric-numeric-numeric.arff";
			}

			String[] option1 = new String[] { "-K", "0", "-C", "1", "-M", "1000", "-H", "1", "-B" };
			String[] option2 = new String[] { "-K", "0", "-C", "10", "-M", "1000", "-H", "1", "-B" };
			String[] option3 = new String[] { "-K", "0", "-C", "100", "-M", "1000", "-H", "1", "-B" };
			String[][] options = new String[][] { option1, option2, option3 };

			try {
				for (int optionIndex = 0; optionIndex < options.length; optionIndex++) {
					String resultFile = testFolder + featureName + "\\output" + optionIndex + ".txt";
					BufferedWriter resultWriter = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(resultFile)));
					
					LibSVM classifier = new LibSVM();
					classifier.setOptions(options[optionIndex].clone());

					Instances trainData = DataSource.read(trainFile);
					Instances testData = DataSource.read(testFile);
					trainData.setClassIndex(trainData.numAttributes() - 1);
					testData.setClassIndex(testData.numAttributes() - 1);

					classifier.buildClassifier(trainData);
					Evaluation eval = new Evaluation(trainData);
					eval.evaluateModel(classifier, testData);

					resultWriter.append(eval.toSummaryString());
					resultWriter.flush();
					resultWriter.close();
				}
			} catch (Exception e) {

			}
		}
	}

}
