package cn.edu.nju.se.lcy.mains.notest;

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

		String rootPath = args[0];
		String subFolder = args[1];
		String type = args[2];
		String filePath = rootPath + subFolder + "\\";

		String[] options1 = new String[] { "-K", "0", "-C", "1", "-M", "1000", "-H", "1", "-B" };
		String[] options2 = new String[] { "-K", "0", "-C", "10", "-M", "1000", "-H", "1", "-B" };
		String[] options3 = new String[] { "-K", "0", "-C", "100", "-M", "1000", "-H", "1", "-B" };
		//String[] options4 = new String[] { "-K", "0", "-C", "1000", "-M", "1000", "-H", "1", "-B" };
		//String[] options5 = new String[] { "-K", "0", "-C", "10000", "-M", "1000", "-H", "1", "-B" };
		String[][] options = new String[][] { options1,
				options2, options3 }; //,, options4 options5};

		String[] options6 = new String[] { "" };
		String[][] optionsall = new String[][] { options6 };

		String[] algorithmNames1 = new String[] { "naivebayes", "naivebayesmultinomial" };
		String[] algorithmNames = new String[] { "libsvm" };
		
		for (String algorithmName : algorithmNames) {
			if (Classify.checkAlgorithm(algorithmName)) {

				for (String[] option : options) {
					if (!type.contains("-")) {
						simpleExecute(filePath, algorithmName, subFolder, option);
					} else {
						dupExecute(filePath, algorithmName, subFolder, option, type);
					}
				}
			}
		}
		/*for (String algorithmName : algorithmNames1) {
			if (Classify.checkAlgorithm(algorithmName)) {

				for (String[] option : optionsall) {
					if (!type.contains("-")) {
						simpleExecuteNB(filePath, algorithmName, subFolder, option);
					} else {
						dupExecuteNB(filePath, algorithmName, subFolder, option, type);
					}
				}
			}
		}*/

		// subFolder = "keywordcounts-heuristicrules";//args[1];
		// filePath = rootPath + subFolder + "\\";
		// for (String algorithmName : algorithmNames) {
		// if (Classify.checkAlgorithm(algorithmName)) {
		//
		// for(String[] option: options){
		//// simpleExecute(filePath, algorithmName, subFolder, option);
		// dupExecute(filePath, algorithmName, subFolder, option);
		// }
		// }
		// }
		// for (String algorithmName : algorithmNames1) {
		// if (Classify.checkAlgorithm(algorithmName)) {
		//
		// for(String[] option: optionsall){
		//// simpleExecute(filePath, algorithmName, subFolder, option);
		// dupExecute(filePath, algorithmName, subFolder, option);
		// }
		// }
		// }
	}

	public static void dupExecute(String filePath, String algorithmName, String subFolder, String[] option, String type)
			throws Exception {
		String filePathBinary = filePath + "Binary\\";
		Classify.binaryClassify(filePathBinary, algorithmName, 5, 7, ".arff", option.clone(), "g");

		// filePathBinary = filePath + "BinaryPositive\\";
		// Classify.binaryClassify(filePathBinary, algorithmName, 5, 7, ".arff",
		// option.clone(), "g");

		String sourceFile = subFolder + "-" + type + ".arff";
		Classify.multiManualClassify(filePathBinary, algorithmName, 5, ".arff", option.clone(), "g");

		// filePathBinary = filePath + "DUP Binary\\";
		// Classify.binaryClassify(filePathBinary, algorithmName, 5, 7, ".arff",
		// option.clone(), "g");
		//
		// filePathBinary = filePath + "Multi\\";
		// Classify.duplicateMultiClassify(filePathBinary, algorithmName, 5,
		// ".arff", option.clone(), "g");
	}

	public static void simpleExecute(String filePath, String algorithmName, String subFolder, String[] option)
			throws Exception {
		String filePathBinary = filePath + "Numeric Binary\\";
		Classify.binaryClassify(filePathBinary, algorithmName, 5, 7, ".arff", option.clone(), "g");

		String sourceFile = subFolder + "-numeric.arff";
		Classify.multiManualClassify(filePathBinary, algorithmName, 5, ".arff", option.clone(), "g");

		// filePathBinary = filePath + "Nominal Binary\\";
		// Classify.binaryClassify(filePathBinary, algorithmName, 5, 7, ".arff",
		// option.clone(), "g");
		//
		// sourceFile = subFolder + "-nominal.arff";
		// Classify.multiClassify(filePath, sourceFile, false, algorithmName, 5,
		// ".arff", option.clone());
	}
	
	public static void dupExecuteNB(String filePath, String algorithmName, String subFolder, String[] option, String type)
			throws Exception {
		String filePathBinary = filePath + "Binary\\";
		Classify.binaryClassify(filePathBinary, algorithmName, 5, 7, ".arff", option.clone(), "g");

		// filePathBinary = filePath + "BinaryPositive\\";
		// Classify.binaryClassify(filePathBinary, algorithmName, 5, 7, ".arff",
		// option.clone(), "g");

		String sourceFile = subFolder + "-" + type + ".arff";
		Classify.multiClassify(filePath, sourceFile, false, algorithmName, 5, ".arff", option.clone());

		// filePathBinary = filePath + "DUP Binary\\";
		// Classify.binaryClassify(filePathBinary, algorithmName, 5, 7, ".arff",
		// option.clone(), "g");
		//
		// filePathBinary = filePath + "Multi\\";
		// Classify.duplicateMultiClassify(filePathBinary, algorithmName, 5,
		// ".arff", option.clone(), "g");
	}

	public static void simpleExecuteNB(String filePath, String algorithmName, String subFolder, String[] option)
			throws Exception {
		String filePathBinary = filePath + "Numeric Binary\\";
		Classify.binaryClassify(filePathBinary, algorithmName, 5, 7, ".arff", option.clone(), "g");

		String sourceFile = subFolder + "-numeric.arff";
		Classify.multiClassify(filePath, sourceFile, false, algorithmName, 5, ".arff", option.clone());

		// filePathBinary = filePath + "Nominal Binary\\";
		// Classify.binaryClassify(filePathBinary, algorithmName, 5, 7, ".arff",
		// option.clone(), "g");
		//
		// sourceFile = subFolder + "-nominal.arff";
		// Classify.multiClassify(filePath, sourceFile, false, algorithmName, 5,
		// ".arff", option.clone());
	}
}
