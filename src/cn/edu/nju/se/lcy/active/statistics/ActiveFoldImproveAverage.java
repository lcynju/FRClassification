package cn.edu.nju.se.lcy.active.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActiveFoldImproveAverage {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int nextTypeNumber = 5;
		int featuresNumber = 7;
		int foldNumber = 5;
		int totalLineNumber = 794;
		String rootFolder = "E:\\Keepass Experiments\\";
		String fileFolderPre = rootFolder + "Active Learning Fold";

		double[] maxOfFeatures = new double[] { 0.59, 0.60375, 0.575, 0.64375, 0.61375, 0.63125, 0.66375 };

		List<BufferedReader> resultsReaders = new ArrayList<BufferedReader>();
		List<List<List<Integer>>> indexMaxOfFeFo = new ArrayList<List<List<Integer>>>();

		String[] subFolderNames = new String[] { "active learning" };

		for (String subFolder : subFolderNames) {
			String averageIndexFile = rootFolder + subFolder + "-average-max-index.txt";
			for (int i = 1; i < (foldNumber + 1); i++) {
				String fileFolder = fileFolderPre + i + "\\" + subFolder + "\\";
				String allResultFile = fileFolder + "all-results.txt";

				BufferedReader resultReader = new BufferedReader(
						new InputStreamReader(new FileInputStream(allResultFile)));
				resultsReaders.add(resultReader);

				List<List<Integer>> indexMaxOfFolds = new ArrayList<List<Integer>>();
				for (int nextIndex = 0; nextIndex < nextTypeNumber; nextIndex++) {
					List<Integer> indexNextMax = new ArrayList<Integer>();
					for (int featureIndex = 0; featureIndex < featuresNumber; featureIndex++) {
						indexNextMax.add(0);
					}
					indexMaxOfFolds.add(indexNextMax);
				}
				indexMaxOfFeFo.add(indexMaxOfFolds);
			}

			BufferedWriter averageMaxIndexWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(averageIndexFile)));

			String line = "";
			for (int i = 0; i < foldNumber; i++) {
				line = resultsReaders.get(i).readLine();
			}

			int lineNumber = 0;
			while (lineNumber < totalLineNumber) {
				for (int i = 0; i < foldNumber; i++) {
					line = resultsReaders.get(i).readLine();
					String[] singleFoldLine = line.split("\t");
					for (int columnIndex = 0; columnIndex < singleFoldLine.length; columnIndex++) {
						double tmpValue = Double.parseDouble(singleFoldLine[columnIndex]);
						int nextTypeIndex = columnIndex / featuresNumber;
						int featureIndex = columnIndex % featuresNumber;
						if (indexMaxOfFeFo.get(i).get(nextTypeIndex).get(featureIndex) == 0
								&& tmpValue > maxOfFeatures[featureIndex]) {
							indexMaxOfFeFo.get(i).get(nextTypeIndex).set(featureIndex, lineNumber);
						}
					}
				}
				lineNumber++;
			}

			for (int i = 0; i < foldNumber; i++) {
				resultsReaders.get(i).close();
			}

			for (int nextTypeIndex = 0; nextTypeIndex < nextTypeNumber; nextTypeIndex++) {
				for (int featureIndex = 0; featureIndex < featuresNumber; featureIndex++) {
					double tmpTotal = 0;
					for (int foldIndex = 0; foldIndex < foldNumber; foldIndex++) {
						tmpTotal += indexMaxOfFeFo.get(foldIndex).get(nextTypeIndex).get(featureIndex);
					}
					averageMaxIndexWriter.append(tmpTotal / foldNumber + "\t");
				}
				averageMaxIndexWriter.newLine();
				averageMaxIndexWriter.flush();
			}
			averageMaxIndexWriter.close();
		}
	}

}
