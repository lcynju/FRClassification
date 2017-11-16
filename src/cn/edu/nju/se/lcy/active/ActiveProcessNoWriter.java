package cn.edu.nju.se.lcy.active;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.se.lcy.active.util.InstanceManagement;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ActiveProcessNoWriter {
	public static void main(String[] args) {
		String fileFolder = args[0] + "active learning\\";
//		String fileFolder = "D:\\Experiments-0724-Fold1\\active learning\\";
		
		String[] featureNames = new String[] { /*"heuristicrules", */"keywordcounts", "tfidf"/*, "tfidf-keywordcounts-numeric",
				"tfidf-heuristicrules-numeric", "keywordcounts-heuristicrules-numeric",
				"tfidf-keywordcounts-heuristicrules-numeric-numeric" */};
		for (String featureName : featureNames) {
			String trainFile = fileFolder + "train-" + featureName + "-numeric.arff";
			String testFile = fileFolder + "test-" + featureName + "-numeric.arff";
			String[] types = new String[] { "random", "max", "gap"};
			
			File fileTest = new File(trainFile);
			if(!fileTest.exists()){
				continue;
			}
			
			for (String type : types) {
				String curveFile = fileFolder + type + "-" + featureName + "-curve.txt";
				String recallFile = fileFolder + type + "-" + featureName + "-recall.txt";
				String precisionFile = fileFolder + type + "-" + featureName + "-precision.txt";
				String fmFile = fileFolder + type + "-" + featureName + "-fm.txt";

				String[] option1 = new String[] { "-K", "0", "-C", "1", "-M", "1000", "-H", "1", "-B" };
//				String[] option2 = new String[] { "-K", "0", "-C", "10", "-M", "1000", "-H", "1", "-B" };
//				String[] option3 = new String[] { "-K", "0", "-C", "100", "-M", "1000", "-H", "1", "-B" };
				String[][] options = new String[][] { option1 };

				try {
					BufferedWriter curveWriter = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(curveFile)));
					BufferedWriter recallWriter = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(recallFile)));
					BufferedWriter precisionWriter = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(precisionFile)));
					BufferedWriter fmWriter = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fmFile)));

					Instances totalTestData = DataSource.read(testFile);
					totalTestData.setClassIndex(totalTestData.numAttributes() - 1);

					InstanceManagement inManager = new InstanceManagement(trainFile);
					
					List<Integer> zeroKeysOfInstance = null;
					List<Double> keyVarOfInstance = null;
					if(type.contains("key")){
						zeroKeysOfInstance = new ArrayList<Integer>();
						keyVarOfInstance = new ArrayList<Double>();
						BufferedReader keyValueReader = new BufferedReader(
								new InputStreamReader(new FileInputStream(fileFolder + "keyCountsValues.txt")));
						String valueLine = "";
						while((valueLine = keyValueReader.readLine()) != null){
							String[] values = valueLine.split("\t");
							int zeros = Integer.parseInt(values[0]);
							double var = Double.parseDouble(values[1]);
							zeroKeysOfInstance.add(zeros);
							keyVarOfInstance.add(var);
						}
						keyValueReader.close();
					}

					Instances firstBatch = inManager.getFirstBatch(zeroKeysOfInstance, keyVarOfInstance);
					Instances testData = inManager.getInitialInstances();
					
					testData.setClassIndex(testData.numAttributes() - 1);

					while (testData.size() != 0) {
						Object[] results = DataSetOperatorNoWriter.train(firstBatch, testData, options.clone(), type,
								totalTestData, zeroKeysOfInstance, keyVarOfInstance);
						double accuracy = (Double) results[0];
						double[][] matrix = (double[][]) results[1];
						curveWriter.append(accuracy/100 + "");
						curveWriter.newLine();
						curveWriter.flush();
						
						for(double precision : matrix[0]){
							precisionWriter.append(precision + "\t");
						}
						precisionWriter.newLine();
						precisionWriter.flush();
						for(double recall : matrix[1]){
							recallWriter.append(recall + "\t");
						}
						recallWriter.newLine();
						recallWriter.flush();
						for(double fm : matrix[2]){
							fmWriter.append(fm + "\t");
						}
						fmWriter.newLine();
						fmWriter.flush();
					}

					double accuracy = DataSetOperatorNoWriter.testDirectly(firstBatch, totalTestData, options);
					curveWriter.append(accuracy/100 + "");
					curveWriter.newLine();
					curveWriter.flush();

					curveWriter.close();
					precisionWriter.close();
					recallWriter.close();
					fmWriter.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
