package cn.edu.nju.se.lcy.learn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.se.lcy.learnalgorithms.LearnAlgorithm;
import cn.edu.nju.se.lcy.learnalgorithms.LearnAlgorithmFactory;
import weka.core.Instances;

public class BinaryLearner extends Learner {

	private int groupNumber;
	private int classNumber;
	private String groupName;

	public BinaryLearner(String filePath, String extension, int classNumber, String[] options){
		super(filePath, extension, options);
		this.classNumber = classNumber;
	}
	
	public BinaryLearner(String filePath, String extension, int groupNumber, int classNumber, String[] options,
			String groupName) {
		super(filePath, extension, options);
		// TODO Auto-generated constructor stub
		this.groupNumber = groupNumber;
		this.classNumber = classNumber;
		this.groupName = groupName;
	}

	@Override
	public double start(String type) {
		// TODO Auto-generated method stub
		try {
			String trueClassFile = super.getFilePath() + this.groupName + "trueClass.txt";
			String resultFile = super.getFilePath() + this.groupName + type + super.getOptionString() + "-result.txt";
			BufferedWriter resultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile)));
			BufferedReader trueReader = new BufferedReader(new InputStreamReader(new FileInputStream(trueClassFile)));

			for (int groupIndex = 0; groupIndex < this.groupNumber; groupIndex++) {
				List<String> classNames = new ArrayList<String>();

				String groupPath = super.getFilePath() + this.groupName + groupIndex + "\\";
				for (int classIndex = 0; classIndex < this.classNumber; classIndex++) {
					String trainFile = groupPath + "binary" + classIndex + super.getDataFileExtension();
					String testFile = groupPath + "test" + classIndex + super.getDataFileExtension();

					Instances trainData = super.readDataSet(trainFile);
					Instances testData = super.readDataSet(testFile);
					String className = testData.attribute(testData.numAttributes() - 1).value(0);
					classNames.add(className);

					String result = groupPath + type + "-output" + classIndex + ".txt";
					LearnAlgorithm la = LearnAlgorithmFactory.createAlgorithm(type);
					la.setOptions(super.getOptions().clone());
					la.binaryLearner(trainData, testData, result);
				}
				List<BufferedReader> outReaders = new ArrayList<BufferedReader>();
				for (int classIndex = 0; classIndex < this.classNumber; classIndex++) {
					String result = groupPath + type + "-output" + classIndex + ".txt";
					BufferedReader tmpOutReader = new BufferedReader(
							new InputStreamReader(new FileInputStream(result)));
					outReaders.add(tmpOutReader);
				}
				String line = "";
				while ((line = outReaders.get(0).readLine()) != null) {
					if (line.equals("")) {
						break;
					}
					String maxClass = classNames.get(0);
					double maxProbability = Double.parseDouble(line);
					for (int classIndex = 1; classIndex < this.classNumber; classIndex++) {
						line = outReaders.get(classIndex).readLine();
						double tmpProbability = Double.parseDouble(line);
						if (tmpProbability > maxProbability) {
							maxClass = classNames.get(classIndex);
							maxProbability = tmpProbability;
						}
					}
					String trueClass = trueReader.readLine();
					resultWriter.append(trueClass + "\t" + maxClass + "\t" + maxProbability);
					resultWriter.newLine();
					resultWriter.flush();
				}
			}

			resultWriter.close();
			trueReader.close();
			return CalculateResult.calculateResults(resultFile,
					super.getFilePath() + type + super.getOptionString() + "-output.txt");
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void startSingle(String algorithmName) {
		// TODO Auto-generated method stub
		try {
			String resultFile = this.getFilePath() + algorithmName + "-singleTest-result.txt";
			String trueFilePath = this.getFilePath() + this.groupName + "testTrueClass.txt";
			BufferedWriter resultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile)));
			BufferedReader trueReader = new BufferedReader(new InputStreamReader(new FileInputStream(trueFilePath)));

			List<String> classNames = new ArrayList<String>();
			for (int classIndex = 0; classIndex < classNumber; classIndex++) {
				String trainFile = this.getFilePath() + this.groupName + (this.groupNumber - 1) + "\\binary"
						+ classIndex + this.getDataFileExtension();
				String testFile = this.getFilePath() + this.groupName + (this.groupNumber - 1) + "\\test" + classIndex
						+ super.getDataFileExtension();

				Instances trainData = super.readDataSet(trainFile);
				Instances testData = super.readDataSet(testFile);
				String className = testData.attribute(testData.numAttributes() - 1).value(0);
				classNames.add(className);

				String result = this.getFilePath() + this.groupName + (this.groupNumber - 1) + "\\" + algorithmName
						+ "-output" + classIndex + ".txt";
				LearnAlgorithm la = LearnAlgorithmFactory.createAlgorithm(algorithmName);
				la.setOptions(super.getOptions().clone());
				la.binaryLearner(trainData, testData, result);
			}
			List<BufferedReader> outReaders = new ArrayList<BufferedReader>();
			for (int classIndex = 0; classIndex < this.classNumber; classIndex++) {
				String result = this.getFilePath() + this.groupName + (this.groupNumber - 1) + "\\" + algorithmName
						+ "-output" + classIndex + ".txt";
				BufferedReader tmpOutReader = new BufferedReader(new InputStreamReader(new FileInputStream(result)));
				outReaders.add(tmpOutReader);
			}
			String line = "";
			while ((line = outReaders.get(0).readLine()) != null) {
				if (line.equals("")) {
					break;
				}
				String maxClass = classNames.get(0);
				double maxProbability = Double.parseDouble(line);
				for (int classIndex = 1; classIndex < this.classNumber; classIndex++) {
					line = outReaders.get(classIndex).readLine();
					double tmpProbability = Double.parseDouble(line);
					if (tmpProbability > maxProbability) {
						maxClass = classNames.get(classIndex);
						maxProbability = tmpProbability;
					}
				}
				String trueClass = trueReader.readLine();
				resultWriter.append(trueClass + "\t" + maxClass + "\t" + maxProbability);
				resultWriter.newLine();
				resultWriter.flush();
			}

			resultWriter.close();
			trueReader.close();
			CalculateResult.calculateResults(resultFile,
					super.getFilePath() + algorithmName + super.getOptionString() + "-reserved-test-output.txt");
		} catch (Exception e) {

		}
	}
	
	public void startSingleFromDifferentPath(String algorithmName, String testFileFolder, String trueFile) {
		try {
			String resultFile = testFileFolder + algorithmName + "-binary-result.txt";
			BufferedWriter resultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile)));
			BufferedReader trueReader = new BufferedReader(new InputStreamReader(new FileInputStream(trueFile)));

			List<String> classNames = new ArrayList<String>();
			for (int classIndex = 0; classIndex < classNumber; classIndex++) {
				String trainFile = this.getFilePath() + "binary" + classIndex + this.getDataFileExtension();
				String testFile = testFileFolder + "binary" + classIndex + this.getDataFileExtension();

				Instances trainData = super.readDataSet(trainFile);
				Instances testData = super.readDataSet(testFile);
				String className = trainData.attribute(trainData.numAttributes() - 1).value(0);
				classNames.add(className);

				String result = testFileFolder + algorithmName + "-binary-output" + classIndex + ".txt";
				LearnAlgorithm la = LearnAlgorithmFactory.createAlgorithm(algorithmName);
				la.setOptions(super.getOptions().clone());
				la.binaryLearner(trainData, testData, result);
			}
			List<BufferedReader> outReaders = new ArrayList<BufferedReader>();
			for (int classIndex = 0; classIndex < this.classNumber; classIndex++) {
				String result = testFileFolder + algorithmName + "-binary-output" + classIndex + ".txt";
				BufferedReader tmpOutReader = new BufferedReader(new InputStreamReader(new FileInputStream(result)));
				outReaders.add(tmpOutReader);
			}
			String line = "";
			while ((line = outReaders.get(0).readLine()) != null) {
				if (line.equals("")) {
					break;
				}
				String maxClass = classNames.get(0);
				double maxProbability = Double.parseDouble(line);
				for (int classIndex = 1; classIndex < this.classNumber; classIndex++) {
					line = outReaders.get(classIndex).readLine();
					double tmpProbability = Double.parseDouble(line);
					if (tmpProbability > maxProbability) {
						maxClass = classNames.get(classIndex);
						maxProbability = tmpProbability;
					}
				}
				String trueClass = trueReader.readLine();
				resultWriter.append(trueClass + "\t" + maxClass + "\t" + maxProbability);
				resultWriter.newLine();
				resultWriter.flush();
			}

			resultWriter.close();
			trueReader.close();
			CalculateResult.calculateResults(resultFile,
					testFileFolder + algorithmName + super.getOptionString() + "-binary-statistic.txt");
		} catch (Exception e) {

		}
	}
}
