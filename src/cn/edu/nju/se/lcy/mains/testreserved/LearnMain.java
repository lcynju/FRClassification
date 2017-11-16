package cn.edu.nju.se.lcy.mains.testreserved;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.se.lcy.generate.Organize4Weka;
import cn.edu.nju.se.lcy.generate.SplitGroups;
import cn.edu.nju.se.lcy.learn.Classify;
import cn.edu.nju.se.lcy.learn.BinaryLearner;
import cn.edu.nju.se.lcy.learn.Learner;
import cn.edu.nju.se.lcy.learn.AutoMultiLearner;

public class LearnMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String rootPath = "C:\\Users\\Chuanyi Li\\Desktop\\Experiments-0715\\";
		String subFolder = "tfidf-keywordcounts-heuristicrules";
		String filePath = rootPath + subFolder + "\\";

//		String[] algorithmNames = new String[] { "naivebayes", "naivebayesmultinomial" };
		File mainDir = new File(filePath);
		if (mainDir.isDirectory()) {
			File[] subFiles = mainDir.listFiles();
//			for (File subFile : subFiles) {
//				if (subFile.isDirectory()) {
//					for (String algorithmName : algorithmNames) {
//						if (Classify.checkAlgorithm(algorithmName)) {
//							if (subFile.getAbsolutePath().contains("Binary")) {
//								Classify.binaryClassify(subFile.getAbsolutePath() + "\\", algorithmName, 5, 7, ".arff",
//										new String[] { "" }, "g");
//							}
//						}
//					}
//				} else {
//					if (subFile.getName().contains(".arff")) {
//						for (String algorithmName : algorithmNames) {
//							if (Classify.checkAlgorithm(algorithmName)) {
//								Classify.multiClassify(mainDir.getAbsolutePath() + "\\", subFile.getName(), false,
//										algorithmName, 5, ".arff", new String[] { "" });
//							}
//						}
//					}
//				}
//			}

			String[] options1 = new String[] { "-K", "0", "-C", "1", "-M", "1000", "-H", "1", "-B" };
			String[] options2 = new String[] { "-K", "0", "-C", "10", "-M", "1000", "-H", "1", "-B" };
			String[] options3 = new String[] { "-K", "0", "-C", "100", "-M", "1000", "-H", "1", "-B" };
			//String[] options4 = new String[] { "-K", "0", "-C", "1000", "-M", "1000", "-H", "1", "-B" };
			//String[] options5 = new String[] { "-K", "0", "-C", "5000", "-M", "1000", "-H", "1", "-B" };
			String[][] optionsAll = new String[][] { options1, options2, options3 };
			String algorithmName = "libsvm";
			for (File subFile : subFiles) {
				if (subFile.isDirectory()) {
					if (Classify.checkAlgorithm(algorithmName)) {
						double maxTrainResult = 0;
						int maxOptionIndex = 0;
						for (int optionIndex = 0; optionIndex < optionsAll.length; optionIndex++) {
							double tmpResult;
							if (subFile.getName().contains("Binary")) {
								tmpResult = Classify.binaryClassify(subFile.getAbsolutePath() + "\\", algorithmName, 4,
										7, ".arff", optionsAll[optionIndex], "reserved-g");
							} else {
								tmpResult = Classify.duplicateMultiClassify(subFile.getAbsolutePath() + "\\",
										algorithmName, 4, ".arff", optionsAll[optionIndex], "reserved-g");
							}
							if (tmpResult > maxTrainResult) {
								maxTrainResult = tmpResult;
								maxOptionIndex = optionIndex;
							}
						}
						Classify.conductReservedTest(subFile.getAbsolutePath() + "\\", algorithmName,
								optionsAll[maxOptionIndex], 5, "reserved-g", 7, ".arff");
					}
				}
			}
		}
	}
}
