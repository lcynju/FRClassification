package cn.edu.nju.se.lcy.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.northwestern.at.morphadorner.corpuslinguistics.lemmatizer.EnglishLemmatizer;

public class CalculateTFIDF {

	public static void writeIDF(Map<String, Double> idf, String fileName) throws IOException {
		BufferedWriter sourceWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
		for (String word : idf.keySet()) {
			sourceWriter.append(word + ":" + idf.get(word));
			sourceWriter.newLine();
			sourceWriter.flush();
		}
		sourceWriter.close();
	}

	public static Map<String, Double> generateIDF(String filePath, String fileName, String sentenceSpliter,
			String spliter) {
		try {
			Map<String, Double> idf = new HashMap<String, Double>();
			BufferedReader sourceReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filePath + fileName)));
			Map<String, Integer> allWords = new HashMap<String, Integer>();
			String line = "";
			int documentNumber = 0;
			while ((line = sourceReader.readLine()) != null) {
				documentNumber++;
				line = line.replaceAll(sentenceSpliter, "");
				// line = line.split("\t")[1];
				singleDoc(line, allWords, false, spliter);
			}
			for (String word : allWords.keySet()) {
				//if (allWords.get(word) > 1) {
					double wordIDF = Math.log(documentNumber / ((double) allWords.get(word)));
					idf.put(word, wordIDF);
				//}
			}
			sourceReader.close();
			return idf;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static void startWithIDF(Map<String, Double> idf, String filePath, String sourceFileName,
			boolean removeStopwords, String sentenceSpliter, String spliter) {
		try {
			String resultFileNameStart = "";
			if(sourceFileName.contains("\\")){
				resultFileNameStart = sourceFileName.split("\\\\")[0] + "\\";
			}
			if(sourceFileName.contains("pseudo")){
				resultFileNameStart += "pseudo";
			}
			String outputFile = filePath + resultFileNameStart + "tfidf.txt";
			if (removeStopwords) {
				outputFile = filePath + resultFileNameStart + "tfidfremovestop.txt";
			}
			BufferedReader sourceReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filePath + sourceFileName)));
			BufferedWriter resultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));

			String line = "";
			while ((line = sourceReader.readLine()) != null) {
				line = line.replaceAll(sentenceSpliter, "");
				//line = line.split("\t")[1];
				Map<String, Double> docWords = singleDoc(line, null, removeStopwords, spliter);
				for (String allWord : idf.keySet()) {
					if (docWords.containsKey(allWord)) {
						double idfValue = idf.get(allWord);
						double tfValue = docWords.get(allWord);
						resultWriter.append(tfValue * idfValue + "\t");
					} else {
						resultWriter.append("0\t");
					}
				}
				resultWriter.newLine();
				resultWriter.flush();
			}

			sourceReader.close();
			resultWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void startSingleFile(String filePath, String resultFileName, String sourceFileName,
			boolean removeStopwords, int documentNumber) throws Exception {
		String outputFile = filePath + resultFileName;
		if (removeStopwords) {
			outputFile = filePath + "removestopwords-" + resultFileName;
		}
		Map<String, Integer> allWords = new HashMap<String, Integer>();

		BufferedReader sourceReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath + sourceFileName)));
		BufferedWriter bwDoc = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "tfnew.txt")));

		String line = "";
		while ((line = sourceReader.readLine()) != null) {
			Map<String, Double> docWords = singleDoc(line, allWords, removeStopwords, " ");
			String docLine = "";
			for (String word : docWords.keySet()) {
				docLine += word + "Ã°ºÅ" + docWords.get(word) + "¶ººÅ";
			}
			bwDoc.append(docLine);
			bwDoc.newLine();
			bwDoc.flush();
		}

		Map<String, Double> idf = new HashMap<String, Double>();
		int index = 1;
		for (String word : allWords.keySet()) {
			double wordIDF = Math.log(documentNumber / ((double) allWords.get(word)));
			idf.put(word, wordIDF);
			allWords.put(word, index);
			index++;
		}

		sourceReader.close();
		bwDoc.close();

		BufferedReader bwDocTF = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + "tfnew.txt")));
		BufferedWriter bwDocTFIDF = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));

		String newLine = "";
		while ((newLine = bwDocTF.readLine()) != null) {
			Map<Integer, Double> featureTFIDF = new TreeMap<Integer, Double>();
			String[] words = newLine.split("¶ººÅ");
			for (String word : words) {
				String[] word_TF = word.split("Ã°ºÅ");
				int featureIndex = allWords.get(word_TF[0]);
				double tfidf = Double.parseDouble(word_TF[1]) * idf.get(word_TF[0]);

				featureTFIDF.put(featureIndex, tfidf);
			}
			for (Integer i = 0; i < allWords.size(); i++) {
				if (featureTFIDF.keySet().contains(i)) {
					bwDocTFIDF.append(featureTFIDF.get(i) + "\t");
				} else {
					bwDocTFIDF.append("0\t");
				}
			}

			bwDocTFIDF.newLine();
			bwDocTFIDF.flush();
		}

		bwDocTF.close();
		bwDocTFIDF.close();
	}

	private static Map<String, Double> singleDoc(String line, Map<String, Integer> allWords, boolean removeStopwords,
			String spliter) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Integer> innerDoc = new HashMap<String, Integer>();
		int totalWords = 0;
		String[] words = line.split(spliter);
		for (String word : words) {
			if (removeStopwords && StopWords.contains(word)) {
				continue;
			}
			totalWords++;
			if (word.matches("[a-zA-Z0-9-]{1,20}")) {
				if (innerDoc.containsKey(word)) {
					innerDoc.put(word, innerDoc.get(word) + 1);
				} else {
					innerDoc.put(word, 1);
					if (allWords != null) {
						if (allWords.containsKey(word)) {
							allWords.put(word, allWords.get(word) + 1);
						} else {
							allWords.put(word, 1);
						}
					}
				}
			}
		}
		Map<String, Double> docTF = new HashMap<String, Double>();
		for (String word : innerDoc.keySet()) {
			docTF.put(word, ((double) innerDoc.get(word)) / totalWords);
		}
		return docTF;
	}
}
