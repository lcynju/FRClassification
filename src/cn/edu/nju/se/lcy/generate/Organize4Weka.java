package cn.edu.nju.se.lcy.generate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Generate .arff file from source files of data features for Weka.
 */
public class Organize4Weka {

	private String filePath;
	private String destSubFolder;
	private String trueClassFileName;
	private String classes;

	/**
	 * Generate .arff file from source files of data features for Weka. You can
	 * generate from both single file and multifiles that each one represent one
	 * kind of feature.
	 * 
	 * @param filePath
	 *            the root folder containing the feature source file, end with
	 *            '\\'
	 * @param sourceFile
	 *            the name of source file or files of features and if there are
	 *            multi-files, they should be connected by '-'. it is also the
	 *            destination folder where all data related to this 'sourceFile'
	 *            be saved. and it is also used as the result file name.
	 *            single example 'keywordcounts', multi example 'keywordcounts-tfidf'
	 * @param trueName
	 *            the file name of true classes of each data in the source file.
	 *            only the name and file extension. example 'trueClass.txt'
	 * @param classes
	 *            the string of all true classes, by passing this there is no
	 *            need to read trueName file twice. classes are connected with
	 *            ',', no blankets, example 'security,capability,usability'.
	 */
	public Organize4Weka(String filePath, String sourceFile, String trueName, String classes) {
		this.filePath = filePath;
		this.destSubFolder = sourceFile;
		this.trueClassFileName = trueName;
		this.classes = classes;
		File featureRootFolder = new File(this.filePath + this.destSubFolder + "\\");
		if(!featureRootFolder.exists()){
			featureRootFolder.mkdir();
		}
		
	}

	/**
	 * Used for generate .arff files from only one source file. This source file
	 * represent only one feature of the data.
	 * 
	 * @param attrType
	 *            the type of attributes you want to define for each feature in
	 *            the source file. {'numeric', 'nominal'}
	 */
	public String startSingle(String attrType) {
		try {
			String sourceFile = this.filePath + this.destSubFolder + ".txt";
			String destFile;
			destFile = this.filePath + this.destSubFolder + "\\" + this.destSubFolder + "-" + attrType + ".arff";

			BufferedReader sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
			BufferedReader trueReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(this.filePath + this.trueClassFileName)));
			BufferedWriter destWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile)));

			List<Set<String>> scopesOfFeatures = GenerateNominalSet.generateNominal(sourceFile);
			//List<Integer> indexOfSingleValueFeature = preReadFeatures(scopesOfFeatures);

			writeHeadHeader(destWriter);
			writeHeadSingleFeatures(0, destWriter, attrType, scopesOfFeatures);
			writeHeadClasses(destWriter);

			String line = "";
			while ((line = sourceReader.readLine()) != null && !line.equals("")) {
				String className = trueReader.readLine();
				className = className.toLowerCase();
				className = className.replace(" ", "");
				String[] features = line.split("\t");
				String newLine = "";
				for (int featureIndex = 0; featureIndex < features.length; featureIndex++) {
//					if (indexOfSingleValueFeature.contains(featureIndex)) {
//						continue;
//					}
					newLine += features[featureIndex] + ",";
				}
				newLine = newLine + className;
				destWriter.append(newLine + "\r\n");
				destWriter.flush();
			}

			trueReader.close();
			sourceReader.close();
			destWriter.close();

			return destFile;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * This is a multi-features version of generating '.arff' for Weka. if you
	 * pass more than one feature in the constructor, you should use this
	 * function to generate .arff file
	 * 
	 * @param type
	 *            defines the feature type of each source file, connected by
	 *            '-'; {'numeric','nominal'}
	 * @return
	 */
	public String startMulti(String type) {
		try {
			List<BufferedReader> sourceReaders = new ArrayList<BufferedReader>();
			BufferedReader trueReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(this.filePath + this.trueClassFileName)));

			String destFile = this.filePath + this.destSubFolder + "\\" + this.destSubFolder + "-" + type + ".arff";
			BufferedWriter destWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile)));

			writeHeadHeader(destWriter);
			String[] sourceFiles = this.destSubFolder.split("-");
			String[] sourceTypes = type.split("-");
			//List<List<Integer>> indexOfSingleValueFeatures = new ArrayList<List<Integer>>();
			int startNum = 0;
			for (int sourceIndex = 0; sourceIndex < sourceFiles.length; sourceIndex++) {
				String sourceFile = this.filePath + sourceFiles[sourceIndex] + ".txt";
				BufferedReader sourceReader = new BufferedReader(
						new InputStreamReader(new FileInputStream(sourceFile)));
				sourceReaders.add(sourceReader);
				List<Set<String>> scopesOfFeatures = GenerateNominalSet.generateNominal(sourceFile);
				//List<Integer> indexOfSingleValueFeature = preReadFeatures(scopesOfFeatures);
				//indexOfSingleValueFeatures.add(indexOfSingleValueFeature);
				writeHeadSingleFeatures(startNum, destWriter, sourceTypes[sourceIndex], scopesOfFeatures);
				startNum += scopesOfFeatures.size();
			}
			writeHeadClasses(destWriter);

			String line = "";
			boolean readAll = false;
			while (true) {
				String className = trueReader.readLine();
				if (className == null){
					break;
				}
				className = className.toLowerCase();
				className = className.replace(" ", "");
				String newLine = "";
				for (int sourceIndex = 0; sourceIndex < sourceFiles.length; sourceIndex++) {
					line = sourceReaders.get(sourceIndex).readLine();
					if (line == null) {
						readAll = true;
						break;
					}
					String[] features = line.split("\t");
					for (int featureIndex = 0; featureIndex < features.length; featureIndex++) {
						newLine += features[featureIndex] + ",";
					}
				}
				if (readAll) {
					break;
				}
				newLine = newLine + className;
				destWriter.append(newLine + "\r\n");
				destWriter.flush();
			}
			destWriter.close();
			trueReader.close();
			for (int sourceIndex = 0; sourceIndex < sourceFiles.length; sourceIndex++) {
				sourceReaders.get(sourceIndex).close();
			}

			return destFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<Integer> preReadFeatures(List<Set<String>> scopesOfFeatures) {
		List<Integer> indexOfSingleValueFeature = new ArrayList<Integer>();
		for (int featureIndex = 0; featureIndex < scopesOfFeatures.size(); featureIndex++) {
			if (scopesOfFeatures.get(featureIndex).size() == 1) {
				indexOfSingleValueFeature.add(featureIndex);
			}
		}
		return indexOfSingleValueFeature;
	}

	public void writeHeadHeader(BufferedWriter destWriter) {
		try {
			String headofHeader = "% 1. Title: Feature Request Classification\r\n% \r\n% 2. Sources:\r\n%      (a) Creator: Chuanyi Li\r\n%      (b) Email: lcynju AT 126 DOT com\r\n%      (c) Date: June, 2016\r\n% \r\n";
			destWriter.append(headofHeader);
			destWriter.append("@RELATION " + this.destSubFolder + "\r\n");
			destWriter.newLine();
			destWriter.flush();
		} catch (Exception e) {

		}
	}

	public void writeHeadSingleFeatures(int startNum, BufferedWriter destWriter, String attrType, List<Set<String>> scopesOfFeatures) {
		try {
			if (!attrType.equals("nominal")) {
				for (int featureIndex = 0; featureIndex < scopesOfFeatures.size(); featureIndex++) {
					destWriter.append("@ATTRIBUTE feature" + (featureIndex + startNum) + "	NUMERIC");
					destWriter.newLine();
					destWriter.flush();
				}
			} else {
				for (int featureIndex = 0; featureIndex < scopesOfFeatures.size(); featureIndex++) {
					destWriter.append("@ATTRIBUTE feature" + (featureIndex + startNum) + "	");
					String featureValue = "{";
					String[] featureValues = new String[scopesOfFeatures.get(featureIndex).size()];
					scopesOfFeatures.get(featureIndex).toArray(featureValues);
					for (int featureValueIndex = 0; featureValueIndex < featureValues.length - 1; featureValueIndex++) {
						featureValue += featureValues[featureValueIndex] + ",";
					}
					featureValue += featureValues[featureValues.length - 1] + "}";
					destWriter.append(featureValue);
					destWriter.newLine();
					destWriter.flush();
				}
			}
		} catch (Exception e) {

		}
	}

	public void writeHeadClasses(BufferedWriter destWriter) {
		try {
			destWriter.append("@ATTRIBUTE class\t{" + classes + "}\r\n");
			destWriter.newLine();
			destWriter.append("@DATA");
			destWriter.newLine();
			destWriter.flush();
		} catch (Exception e) {

		}
	}
}
