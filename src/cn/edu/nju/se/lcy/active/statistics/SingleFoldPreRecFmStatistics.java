package cn.edu.nju.se.lcy.active.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class SingleFoldPreRecFmStatistics {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int totalLineNumber = 794;
		String fileFolderPre = args[0]; //"E:\\WinMerge Experiments\\Active Learning Fold";
		String activeType = args[1]; //"active learning";
		String[] valueNames = new String[] { "recall", "precision", "fm" };
		String[] classNames = new String[]{"security", "reliability", "performance", "softinter", "lifecycle", "usability", "capability"};

		for (int i = 0; i < 5; i++) {
			String fileFolder = fileFolderPre + i + "\\" + activeType + "\\";

			for (String valueName : valueNames) {
				String allFeatureValueFile = fileFolder + valueName + "-all nexttype and features.txt";

				List<BufferedReader> curveReaders = new ArrayList<BufferedReader>();
				BufferedWriter allFeatureValueWriter = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(allFeatureValueFile)));

				File parent = new File(fileFolder);
				for (File file : parent.listFiles()) {
					if (!file.isDirectory()) {
						if (file.getName().contains("-" + valueName)) {
							BufferedReader curveReader = new BufferedReader(
									new InputStreamReader(new FileInputStream(file)));
							curveReaders.add(curveReader);

							String title = file.getName().split("\\.")[0];
							title = title.replaceAll("heuristicrules", "hr");
							title = title.replaceAll("keywordcounts", "kc");
							title = title.replaceAll("tfidf", "ti");
							title = title.replaceAll("-numeric", "");
							title = title.replaceAll("-" + valueName, "");
							
							for(String className : classNames){
								allFeatureValueWriter.append(title + "-" + className + "\t");
							}
						}
					}
				}
				allFeatureValueWriter.newLine();
				allFeatureValueWriter.flush();

				String resultLine = "";
				int lineNumber = 1;
				while (lineNumber < totalLineNumber) {
					for (int curveIndex = 0; curveIndex < curveReaders.size(); curveIndex++) {
						resultLine = curveReaders.get(curveIndex).readLine();
						allFeatureValueWriter.append(resultLine);
					}
					allFeatureValueWriter.newLine();
					allFeatureValueWriter.flush();
					lineNumber++;
				}
				allFeatureValueWriter.close();
			}
		}
	}

}
