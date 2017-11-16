package cn.edu.nju.se.lcy.learnalgorithms;

import java.io.BufferedWriter;

import weka.core.Attribute;
import weka.core.Instances;

public interface LearnAlgorithm {
	
	public void setOptions(String[] options);
	
	public void binaryLearner(Instances trainData, Instances testData, String resultFile);
	
	public void multiLearner(int crossVal, Instances trainData, String resultFile);

	public void manualMultiLearner(Instances trainData, Instances testData, BufferedWriter tmpResultWriter, Attribute attr);
}
