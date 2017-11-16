package cn.edu.nju.se.lcy.active.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ActiveImproveAverage {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int nextTypeNumber = 5;
		int featuresNumber = 7;
		int totalLineNumber = 791;
		String rootFolder = "E:\\Keepass Experiments\\";
		
		double[] maxOfFeatures = new double[] { 0.636, 0.695, 0.753, 0.672, 0.684, 0.756, 0.716 };

		String[] subFolderNames = new String[] { "active learning", "active learning pseudo" };

		for (String subFolder : subFolderNames) {
			String averageIndexFile = rootFolder + subFolder + "-average-max-index.txt";
			String allResultFile = rootFolder + subFolder + "-average-results.txt";

				BufferedReader resultReader = new BufferedReader(
						new InputStreamReader(new FileInputStream(allResultFile)));
				
				List<List<Integer>> indexMaxOfFolds = new ArrayList<List<Integer>>();
				for (int nextIndex = 0; nextIndex < nextTypeNumber; nextIndex++) {
					List<Integer> indexNextMax = new ArrayList<Integer>();
					for (int featureIndex = 0; featureIndex < featuresNumber; featureIndex++) {
						indexNextMax.add(0);
					}
					indexMaxOfFolds.add(indexNextMax);
				}

			BufferedWriter averageMaxIndexWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(averageIndexFile)));

			String line = resultReader.readLine();

			int lineNumber = 0;
			while (lineNumber < totalLineNumber) {
				line = resultReader.readLine();
					String[] singleFoldLine = line.split("\t");
					for (int columnIndex = 0; columnIndex < singleFoldLine.length; columnIndex++) {
						double tmpValue = Double.parseDouble(singleFoldLine[columnIndex]);
						int nextTypeIndex = columnIndex / featuresNumber;
						int featureIndex = columnIndex % featuresNumber;
						if (indexMaxOfFolds.get(nextTypeIndex).get(featureIndex) == 0
								&& tmpValue >= maxOfFeatures[featureIndex]) {
							indexMaxOfFolds.get(nextTypeIndex).set(featureIndex, lineNumber);
						}
					}
				lineNumber++;
			}
			resultReader.close();
			
			for (int nextTypeIndex = 0; nextTypeIndex < nextTypeNumber; nextTypeIndex++) {
				for (int featureIndex = 0; featureIndex < featuresNumber; featureIndex++) {
					double tmpTotal = indexMaxOfFolds.get(nextTypeIndex).get(featureIndex);
					averageMaxIndexWriter.append(tmpTotal + "\t");
				}
				averageMaxIndexWriter.newLine();
				averageMaxIndexWriter.flush();
			}
			averageMaxIndexWriter.close();
		}
	}

}
