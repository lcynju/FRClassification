package cn.edu.nju.se.lcy.learn;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

import cn.edu.nju.se.lcy.learnalgorithms.LearnAlgorithm;
import cn.edu.nju.se.lcy.learnalgorithms.LearnAlgorithmFactory;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.core.Instances;

public class AutoMultiLearner extends Learner {

	private String trainFileName;
	private int crossval;
	
	public AutoMultiLearner(String filePath, String sourceFileName, int crossval, String extension, String[] options) {
		super(filePath, extension, options);
		// TODO Auto-generated constructor stub
		this.trainFileName = sourceFileName;
		this.crossval = crossval;
	}

	public Evaluation multiClassLearning(String type, int crossVal, Instances trainData){
		Classifier classifier = null;
		if (type.equals("naivebayes")) {
			classifier = new NaiveBayes();
		} else if (type.equals("naivebayesmultinomial")){
			classifier = new NaiveBayesMultinomial();
		}else if(type.equals("smo")){
			classifier = new SMO();
		}else if(type.equals("libsvm")){
			classifier = new LibSVM();
		}else{
			
		}
		try {
			Evaluation eval = new Evaluation(trainData);
			eval.crossValidateModel(classifier, trainData, crossVal, new Random(1));
			return eval;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void writeResult(Evaluation eval, String fileName){
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
			
			writer.append(eval.toSummaryString() + "\r\n");
			writer.append(eval.toMatrixString() + "\r\n");
			writer.append(eval.toClassDetailsString() + "\r\n");
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public double start(String type) {
		// TODO Auto-generated method stub
		String resultFile = super.getFilePath() + this.trainFileName.split("\\.")[0] + "-" + type + super.getOptionString() + "-output.txt";
		String trainFile = super.getFilePath() + this.trainFileName;
		
		Instances trainData = super.readDataSet(trainFile);
		
		LearnAlgorithm la = LearnAlgorithmFactory.createAlgorithm(type);
		la.setOptions(super.getOptions().clone());
		la.multiLearner(this.crossval, trainData, resultFile);
		return -1;
	}

	@Override
	public void startSingle(String algorithmName) {
		// TODO Auto-generated method stub
		
	}
}
