package cn.edu.nju.se.lcy.mains.notest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.TreeSet;

import cn.edu.nju.se.lcy.generate.Organize4Weka;
import cn.edu.nju.se.lcy.generate.SplitGroups;
import cn.edu.nju.se.lcy.learn.CalculateResult;
import edu.northwestern.at.morphadorner.corpuslinguistics.lemmatizer.EnglishLemmatizer;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Test {

	public static void main(String[] args) throws Exception {
		// String rootPath = "C:\\Users\\Chuanyi
		// Li\\Desktop\\weka\\keywordunigram\\Numeric Binary\\g0\\";
		// String training = rootPath + "group0.arff";
		// String testing = rootPath + "others0.arff";
		// Instances trainingdata = DataSource.read(training);
		// if (trainingdata.classIndex() == -1)
		// trainingdata.setClassIndex(trainingdata.numAttributes() - 1);
		// Instances testData = DataSource.read(testing);
		// if (testData.classIndex() == -1)
		// testData.setClassIndex(testData.numAttributes() - 1);
		//
		// LibSVM classifier = new LibSVM();
		//
		// String[] option = new String[1];
		// option[0] = "-B";
		// //option[1] = "1";
		// classifier.setOptions(option);
		//// classifier.setProbabilityEstimates(true);
		//// System.out.println(classifier.probabilityEstimatesTipText());
		// classifier.buildClassifier(trainingdata);
		//// for(int index = 0; index < testData.size(); index ++){
		//// double[] tmpdis =
		// classifier.distributionForInstance(testData.instance(index));
		//// System.out.println(tmpdis[0] + "----" + tmpdis[1]);
		//// }
		// Attribute attribute = testData.attribute(testData.numAttributes() -
		// 1);
		// Evaluation eval = new Evaluation(trainingdata);
		// eval.evaluateModel(classifier, testData);
		// for (Prediction p : eval.predictions()) {
		// NominalPrediction p1 = (NominalPrediction) p;
		// System.out.print(attribute.value((int) p.predicted()));
		// System.out.println(p.predicted() + " " + p1.distribution()[0] + " " +
		// p1.distribution()[1]);
		// }

		// SplitGroups.simpleSplit(3, "C:\\Users\\Chuanyi
		// Li\\Desktop\\weka\\EOCOtfidfkeywordcounts\\caus", ".txt");
		// SplitGroups.simpleSplit(2, "C:\\Users\\Chuanyi
		// Li\\Desktop\\weka\\EOCOtfidfkeywordcounts\\lprss", ".txt");

		// SplitGroups.simpltDuplicate("C:\\Users\\Chuanyi
		// Li\\Desktop\\weka\\EOCOtfidfkeywordcounts\\lprss-group0", ".txt",
		// "lifecycle-performance-reliability-security-softwareinterface",
		// "2-4-5-7-11");
//		BufferedReader sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Chuanyi Li\\Desktop\\weka\\EOCOtfidfkeywordcounts\\test1.txt")));
//		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Chuanyi Li\\Desktop\\weka\\EOCOtfidfkeywordcounts\\test1-new.txt")));
//
//		String line = "";
//		while ((line = sourceReader.readLine()) != null) {
//			writer.append(line.replaceAll("\t", ","));
//			writer.newLine();
//			writer.flush();
//		}
//
//		sourceReader.close();
//		writer.close();
		
//		File file = new File("C:\\Users\\Chuanyi Li\\Desktop\\Experiments-0704\\keywordcounts\\keywordcounts-nominal.arff");
//		System.out.println(file.getParent());
//		BufferedReader sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Chuanyi Li\\Desktop\\weka\\tfidf-keywordcounts-result.txt")));
//		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Chuanyi Li\\Desktop\\weka\\tfidf-keywordcounts-result-result.txt")));
//		String line = "";
//		while ((line = sourceReader.readLine()) != null) {
//			String[] values = line.split("\t");
//			if(Double.parseDouble(values[1]) > Double.parseDouble(values[3])){
//				writer.append(values[0]);
//			}else{
//				writer.append(values[2]);
//			}
//			writer.newLine();
//			writer.flush();
//		}
//
//		sourceReader.close();
//		writer.close();
		
//		CalculateResult.calculateResults("E:\\WinMerge Experiments\\Experiments 0805\\keyword counts directly\\result of keywords.txt",
//				"E:\\WinMerge Experiments\\Experiments 0805\\keyword counts directly\\output of keywords.txt");
		
		String[] fileFolderPres = new String[] { "E:\\Mumble Experiments\\",
				"E:\\WinMerge Experiments\\", "E:\\Keepass Experiments\\" };
		
		for(String fileFolder : fileFolderPres){
			String[] argHere = new String[] { fileFolder + "source data\\", "wordunigram", "nominal" };
			String rootPath = argHere[0];
			String destFolder = argHere[1];
			String type = argHere[2];
			String trueName = "trueClasses.txt";
			String classes = "security,reliability,performance,softwareinterface,lifecycle,usability,capability";

			Organize4Weka o4w = new Organize4Weka(rootPath, destFolder, trueName, classes);
			o4w.startSingle(type);
		}
	}

}
