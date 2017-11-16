package cn.edu.nju.se.lcy.active.dataprepare;

import cn.edu.nju.se.lcy.generate.SplitGroups;
import cn.edu.nju.se.lcy.learn.BinaryLearner;

public class STTBinaryLearning {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileFolder = "E:\\Cross Classification\\";
		String[] featureNames = new String[] { /*"heuristicrules", "keywordcounts", "tfidf", "tfidf-keywordcounts",
				"tfidf-heuristicrules", "keywordcounts-heuristicrules",*/ "keywordcounts-heuristicrules" };

		for (String featureName : featureNames) {
			String trainFolder = fileFolder + "train data\\";
			String testFolder = fileFolder + "test data\\";

			String trueFile = testFolder + "trueClasses.txt";
			
			String sourceFileFolder = trainFolder + featureName + "\\";
			String testFileFolder = testFolder + featureName + "\\";
			String sourceFile = "";
			String testFile = "";
			
			if (featureName.split("-").length == 1) {
				sourceFile = sourceFileFolder + featureName + "-numeric.arff";
				testFile = testFileFolder + featureName + "-numeric.arff";
			} else if (featureName.split("-").length == 2) {
				sourceFile = sourceFileFolder + featureName + "-numeric-numeric.arff";
				testFile = testFileFolder + featureName + "-numeric-numeric.arff";
			} else {
				sourceFile = sourceFileFolder + featureName + "-numeric-numeric-numeric.arff";
				testFile = testFileFolder + featureName + "-numeric-numeric-numeric.arff";
			}

			SplitGroups sgTrain = new SplitGroups(sourceFile, ".arff");
			sgTrain.simpleBinary(sourceFileFolder);
			SplitGroups sgTest = new SplitGroups(testFile, ".arff");
			sgTest.simpleBinary(testFileFolder);
			
			String[] option1 = new String[] { "-K", "0", "-C", "1", "-M", "1000", "-H", "1", "-B" };
			String[] option2 = new String[] { "-K", "0", "-C", "10", "-M", "1000", "-H", "1", "-B" };
			String[] option3 = new String[] { "-K", "0", "-C", "100", "-M", "1000", "-H", "1", "-B" };
			String[][] options = new String[][] { option1, option2, option3 };

			try {
				for (int optionIndex = 0; optionIndex < options.length; optionIndex++) {
					BinaryLearner bl = new BinaryLearner(sourceFileFolder, ".arff", 7, options[optionIndex].clone());
					String algorithmName = "libsvm";
					bl.startSingleFromDifferentPath(algorithmName, testFileFolder, trueFile);
				}
			} catch (Exception e) {

			}
		}
	}

}
