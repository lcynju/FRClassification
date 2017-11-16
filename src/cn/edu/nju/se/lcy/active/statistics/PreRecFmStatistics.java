package cn.edu.nju.se.lcy.active.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class PreRecFmStatistics {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int totalLineNumber = 794;

		String rootFolder = "E:\\JSS\\Active Learning\\Winmerge\\";
		String fileFolderPre = rootFolder + "Active Learning Fold";
		String activeType = "active learning";

		String[] valueNames = new String[] { "recall", "precision", "fm" };
		
		SingleFoldPreRecFmStatistics.main(new String[]{fileFolderPre, activeType});
		
		for (String valueName : valueNames) {
			String valueResultFile = rootFolder + activeType + "-" + valueName + "-all next types.txt";
			BufferedWriter valueResultWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(valueResultFile)));

			List<BufferedReader> resultsReaders = new ArrayList<BufferedReader>();

			for (int i = 0; i < 5; i++) {
				String fileFolder = fileFolderPre + i + "\\" + activeType + "\\";
				String allFeatureValueFile = fileFolder + valueName + "-all nexttype and features.txt";
				BufferedReader allFeatureValueReader = new BufferedReader(
						new InputStreamReader(new FileInputStream(allFeatureValueFile)));
				resultsReaders.add(allFeatureValueReader);
			}

			String line = "";
			for (int i = 0; i < 5; i++) {
				line = resultsReaders.get(i).readLine();
			}
			valueResultWriter.append(line);
			valueResultWriter.newLine();
			valueResultWriter.flush();

			int lineNumber = 1;
			while (lineNumber < totalLineNumber) {
				List<Double> total = new ArrayList<Double>();
				for (int i = 0; i < 5; i++) {
					line = resultsReaders.get(i).readLine();
					String[] singleLine = line.split("\t");
					if (total.size() == 0) {
						for (int j = 0; j < singleLine.length; j++) {
							total.add(Double.parseDouble(singleLine[j]));
						}
					} else {
						for (int j = 0; j < singleLine.length; j++) {
							total.set(j, total.get(j) + Double.parseDouble(singleLine[j]));
						}
					}
				}
				for (int j = 0; j < total.size(); j++) {
					double average = total.get(j) / 5;
					valueResultWriter.append(average + "\t");
				}
				valueResultWriter.newLine();
				valueResultWriter.flush();

				lineNumber++;
			}

			for (int i = 0; i < 5; i++) {
				resultsReaders.get(i).close();
			}
			valueResultWriter.close();
		}
	}

}
