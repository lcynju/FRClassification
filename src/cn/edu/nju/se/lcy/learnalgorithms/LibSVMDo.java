package cn.edu.nju.se.lcy.learnalgorithms;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.Instances;

public class LibSVMDo implements LearnAlgorithm {

	String[] options;
	
	public void binaryLearner(Instances trainData, Instances testData, String resultFile) {
		// TODO Auto-generated method stub
		try {
			
			LibSVM classifier = new LibSVM();
			classifier.setOptions(this.options);
			
//			String[] option = new String[1];
//			option[0] = "-B";
//			classifier.setOptions(option);
			
			classifier.buildClassifier(trainData);
			Evaluation eval = new Evaluation(trainData);
			eval.evaluateModel(classifier, testData);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile)));
			for (Prediction p : eval.predictions()) {
				NominalPrediction p1 = (NominalPrediction) p;
				writer.append(p1.distribution()[0] + "");
				writer.newLine();
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void multiLearner(int crossVal, Instances trainData, String resultFile) {
		// TODO Auto-generated method stub
		try {
			LibSVM classifier = new LibSVM();
			classifier.setOptions(this.options);
			Evaluation eval = new Evaluation(trainData);
			eval.crossValidateModel(classifier, trainData, crossVal, new Random(1));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile)));

			writer.append(eval.toSummaryString() + "\r\n");
			writer.append(eval.toMatrixString() + "\r\n");
			writer.append(eval.toClassDetailsString() + "\r\n");

			Attribute attribute = trainData.attribute(trainData.numAttributes() - 1);
			BufferedWriter tmpResultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile + "multi-predictions")));
			for (Prediction p : eval.predictions()) {
				NominalPrediction p1 = (NominalPrediction) p;
				int classIndex = (int) p.predicted();
				tmpResultWriter.append(attribute.value(classIndex) + "\t" + p1.distribution()[classIndex]);
				tmpResultWriter.newLine();
				tmpResultWriter.flush();
			}
			
			writer.flush();
			writer.close();
			tmpResultWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void manualMultiLearner(Instances trainData, Instances testData, BufferedWriter tmpResultWriter, Attribute attr) {
		// TODO Auto-generated method stub
		try {
			LibSVM classifier = new LibSVM();
			classifier.setOptions(this.options);
			classifier.buildClassifier(trainData);
			Evaluation eval = new Evaluation(trainData);
			eval.evaluateModel(classifier, testData);
			for (Prediction p : eval.predictions()) {
				NominalPrediction p1 = (NominalPrediction) p;
				int classIndex = (int) p.predicted();
				tmpResultWriter.append(attr.value(classIndex) + "\t" + p1.distribution()[classIndex] );
				tmpResultWriter.newLine();
				tmpResultWriter.flush();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setOptions(String[] options) {
		// TODO Auto-generated method stub
		this.options = options;
	}

}
