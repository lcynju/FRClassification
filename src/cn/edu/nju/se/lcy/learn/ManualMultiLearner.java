package cn.edu.nju.se.lcy.learn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cn.edu.nju.se.lcy.learnalgorithms.LearnAlgorithm;
import cn.edu.nju.se.lcy.learnalgorithms.LearnAlgorithmFactory;
import weka.core.Attribute;
import weka.core.Instances;

public class ManualMultiLearner extends Learner {

	private int groupNumber;
	private String groupName;

	public ManualMultiLearner(String filePath, String extension, int groupNumber, String[] options, String groupName) {
		super(filePath, extension, options);
		// TODO Auto-generated constructor stub
		this.groupNumber = groupNumber;
		this.groupName = groupName;
	}

	@Override
	public double start(String type) {
		// TODO Auto-generated method stub
		try {
			String trueClassFile = super.getFilePath() + this.groupName + "trueClass.txt";
			String resultFile = super.getFilePath() + this.groupName + type + super.getOptionString() + "-manual-multi-result.txt";
			BufferedWriter resultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile)));
			BufferedReader trueReader = new BufferedReader(new InputStreamReader(new FileInputStream(trueClassFile)));

			String result = super.getFilePath() + type + "-manual-multi-tmpresult.txt";
			BufferedWriter tmpResultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(result)));

			for (int groupIndex = 0; groupIndex < this.groupNumber; groupIndex++) {
				String groupPath = super.getFilePath() + this.groupName + groupIndex + "\\";
				String trainFile = groupPath + "others" + groupIndex + super.getDataFileExtension();
				String testFile = groupPath + "group" + groupIndex + super.getDataFileExtension();

				Instances trainData = super.readDataSet(trainFile);
				Instances testData = super.readDataSet(testFile);
				Attribute attribute = testData.attribute(testData.numAttributes() - 1);

				LearnAlgorithm la = LearnAlgorithmFactory.createAlgorithm(type);
				la.setOptions(super.getOptions().clone());
				la.manualMultiLearner(trainData, testData, tmpResultWriter, attribute);
			}
			tmpResultWriter.close();

			BufferedReader tmpResultReader = new BufferedReader(new InputStreamReader(new FileInputStream(result)));
			String line = "";
			while ((line = tmpResultReader.readLine()) != null) {
				if (line.equals("")) {
					break;
				}
				String trueClass = trueReader.readLine();
				resultWriter.append(trueClass + "\t" + line);
				resultWriter.newLine();
				resultWriter.flush();
			}
			tmpResultReader.close();
			trueReader.close();

			resultWriter.close();
			return CalculateResult.calculateResults(resultFile,
					super.getFilePath() + type + super.getOptionString() + "-manual-multi-output.txt");
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public void startSingle(String algorithmName) {
		try {
			// TODO Auto-generated method stub
			String trueClassFile = super.getFilePath() + this.groupName + "testTrueClass.txt";
			String resultFile = super.getFilePath() + algorithmName + "-singleTest-result.txt";
			BufferedWriter resultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile)));
			BufferedReader trueReader = new BufferedReader(new InputStreamReader(new FileInputStream(trueClassFile)));

			String result = super.getFilePath() + algorithmName + "-test-tmpresult.txt";
			BufferedWriter tmpResultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(result)));

			String groupPath = super.getFilePath() + this.groupName + (this.groupNumber - 1) + "\\";
			String trainFile = groupPath + "others" + (this.groupNumber - 1) + super.getDataFileExtension();
			String testFile = groupPath + "group" + (this.groupNumber - 1) + super.getDataFileExtension();

			Instances trainData = super.readDataSet(trainFile);
			Instances testData = super.readDataSet(testFile);
			Attribute attribute = testData.attribute(testData.numAttributes() - 1);

			LearnAlgorithm la = LearnAlgorithmFactory.createAlgorithm(algorithmName);
			la.setOptions(super.getOptions().clone());
			la.manualMultiLearner(trainData, testData, tmpResultWriter, attribute);

			tmpResultWriter.close();

			BufferedReader tmpResultReader = new BufferedReader(new InputStreamReader(new FileInputStream(result)));
			String line = "";
			while ((line = tmpResultReader.readLine()) != null) {
				if (line.equals("")) {
					break;
				}
				String trueClass = trueReader.readLine();
				resultWriter.append(trueClass + "\t" + line);
				resultWriter.newLine();
				resultWriter.flush();
			}
			tmpResultReader.close();
			trueReader.close();

			resultWriter.close();
			CalculateResult.calculateResults(resultFile,
					super.getFilePath() + algorithmName + super.getOptionString() + "-reserved-test-output.txt");
		} catch (Exception e) {
		}
	}

}
