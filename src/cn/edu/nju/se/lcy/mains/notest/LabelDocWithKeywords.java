package cn.edu.nju.se.lcy.mains.notest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import cn.edu.nju.se.lcy.extract.KeywordCounts;
import cn.edu.nju.se.lcy.learn.CalculateResult;

public class LabelDocWithKeywords {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String[] projectNames = new String[] { "Keepass", "Mumble", "Winmerge" };

		for (String project : projectNames) {
			String fileFolder = "E:\\All Independent Keywords Experiments\\" + project + " Experiments\\Experiments 0815\\";
			String resultFolder = fileFolder + "keyword counts directly\\";
			File resultFileFolder = new File(resultFolder);
			if (!resultFileFolder.exists()) {
				resultFileFolder.mkdir();
			}

			String keywordFile = fileFolder + "keywords.txt";
			String sourceFile = fileFolder + "processed.txt";
			String trueFile = fileFolder + "trueClasses.txt";
			String destFile = resultFolder + "label by keywords.txt";
			String firstDecisionFile = resultFolder + "decision feature.txt";

			BufferedReader keywordsReader = new BufferedReader(new InputStreamReader(new FileInputStream(keywordFile)));
			BufferedReader docReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
			BufferedReader trueReader = new BufferedReader(new InputStreamReader(new FileInputStream(trueFile)));

			BufferedWriter counterWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile)));
			BufferedWriter decisionFeatureWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(firstDecisionFile)));

			Map<String, TreeSet<String>> keywordsMap = KeywordCounts.generateKeywordsMap(keywordsReader);
			String line = "";
			while ((line = docReader.readLine()) != null) {
				String trueClass = trueReader.readLine();
				String[] words = line.split(" ");
				Map<String, Integer> typeCounter = KeywordCounts.generateTypeCounter(keywordsMap, words);

				String[] keyType = new String[typeCounter.keySet().size()];
				typeCounter.keySet().toArray(keyType);
				int maxCounts = 0;
				String maxType = "";
				String secondMax = "";
				int maxTypeIndex = -1;
				int secondTypeIndex = -1;
				for (int i = 0; i < keyType.length; i++) {
					int counter = typeCounter.get(keyType[i]);
					if (counter > maxCounts) {
						maxCounts = counter;
						maxType = keyType[i];
						secondMax = "";
						maxTypeIndex = i;
						secondTypeIndex = -1;
					} else {
						if (counter == maxCounts) {
							secondMax = maxType;
							maxType = keyType[i];
							maxCounts = counter;
							secondTypeIndex = maxTypeIndex;
							maxTypeIndex = i;
						}
					}
				}
				if (!secondMax.equals("")) {
					String[] results = new String[] { maxType, secondMax };
					int[] indexs = new int[] { maxTypeIndex, secondTypeIndex };
					int random = (int) (Math.random() * results.length);
					maxType = results[random];
					maxTypeIndex = indexs[random];
				}
				counterWriter.append(trueClass + "\t" + maxType);
				counterWriter.newLine();
				counterWriter.flush();

				List<String> decisionFeatures = new ArrayList<String>();
				decisionFeatures.addAll(Arrays.asList(new String[] { "0", "0", "0", "0", "0", "0", "0" }));
				decisionFeatures.set(maxTypeIndex, "1");
				for (String feature : decisionFeatures) {
					decisionFeatureWriter.append(feature + "\t");
				}
				decisionFeatureWriter.newLine();
				decisionFeatureWriter.flush();
			}

			counterWriter.close();
			keywordsReader.close();
			docReader.close();
			trueReader.close();
			decisionFeatureWriter.close();

			CalculateResult.calculateResults(destFile, resultFolder + "output of keywords.txt");
		}
	}

}
