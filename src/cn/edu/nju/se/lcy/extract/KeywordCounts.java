package cn.edu.nju.se.lcy.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.northwestern.at.morphadorner.corpuslinguistics.lemmatizer.EnglishLemmatizer;

public class KeywordCounts {

	private String filePath;
	private String keywordFileName;
	private String sourceName;
	private String destName;

	public KeywordCounts(String filePath, String keywordFileName, String sourceName, String destName) {
		this.filePath = filePath;
		this.keywordFileName = keywordFileName;
		this.sourceName = sourceName;
		this.destName = destName;
	}

	public void start() throws Exception {
		EnglishLemmatizer el = new EnglishLemmatizer();
		BufferedReader keywordsReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(this.filePath + this.keywordFileName)));
		BufferedReader docReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(this.filePath + this.sourceName)));

		Map<String, TreeSet<String>> keywordsMap = new HashMap<String, TreeSet<String>>();

		BufferedWriter counterWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(this.filePath + this.destName)));

		String line = "";
		while ((line = keywordsReader.readLine()) != null) {
			String[] type_keys = line.split(":");
			String[] keys = type_keys[1].split(" ");
			TreeSet<String> keySet = new TreeSet<String>();
			for (String key : keys) {
				keySet.add(el.lemmatize(key));
			}
			keywordsMap.put(type_keys[0], keySet);
		}

		while ((line = docReader.readLine()) != null) {
			String[] words = line.split(" ");
			Map<String, Integer> typeCounter = new HashMap<String, Integer>();

			TreeSet<String> wordSet = new TreeSet<String>();
			
			wordSet.addAll(Arrays.asList(words));
			wordSet.toArray(words);

			for (String type : keywordsMap.keySet()) {
				int matchCounter = 0;

				int docWordIndex = 0;
				for (String keyWord : keywordsMap.get(type)) {
					for (int i = docWordIndex; i < wordSet.size(); i++) {
						if (keyWord.equals(words[i])) {
							matchCounter++;
							docWordIndex = i + 1;
							break;
						}
						if (keyWord.compareTo(words[i]) < 0) {
							docWordIndex = i;
							break;
						} else {
							continue;
						}
					}
				}

				typeCounter.put(type, matchCounter);
			}

			String[] keyType = new String[typeCounter.keySet().size()];
			typeCounter.keySet().toArray(keyType);
			for (int i = 0; i < keyType.length; i++) {
				int counter = typeCounter.get(keyType[i]);
				if (counter > 0) {
					counterWriter.append(typeCounter.get(keyType[i]) + "\t");
				} else {
					counterWriter.append("0\t");
				}
			}
			counterWriter.newLine();
			counterWriter.flush();
		}

		counterWriter.close();
		keywordsReader.close();
		docReader.close();
	}
	
	public static void keywordCount(String sourceFile, String keywordFile, String destFile, String sourceSentenceSpliter, String sourceWordSpliter) throws Exception{
		BufferedReader keywordsReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(keywordFile)));
		BufferedReader docReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(sourceFile)));

		
		BufferedWriter counterWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(destFile)));
		
		Map<String, TreeSet<String>> keywordsMap = generateKeywordsMap(keywordsReader);
		String line = "";
		while ((line = docReader.readLine()) != null) {
			line = line.split("\t")[1];
			line = line.replaceAll(sourceSentenceSpliter, "");
			String[] words = line.split(sourceWordSpliter);
			Map<String, Integer> typeCounter = generateTypeCounter(keywordsMap, words);

			String[] keyType = new String[typeCounter.keySet().size()];
			typeCounter.keySet().toArray(keyType);
			for (int i = 0; i < keyType.length; i++) {
				int counter = typeCounter.get(keyType[i]);
				if (counter > 0) {
					counterWriter.append(typeCounter.get(keyType[i]) + "\t");
				} else {
					counterWriter.append("0\t");
				}
			}
			counterWriter.newLine();
			counterWriter.flush();
		}

		counterWriter.close();
		keywordsReader.close();
		docReader.close();
	}

	public static Map<String, Integer> generateTypeCounter(Map<String, TreeSet<String>> keywordsMap, String[] words) {
		// TODO Auto-generated method stub
		Map<String, Integer> typeCounter = new TreeMap<String, Integer>();

		TreeSet<String> wordSet = new TreeSet<String>();
		wordSet.addAll(Arrays.asList(words));
		wordSet.toArray(words);

		for (String type : keywordsMap.keySet()) {
			int matchCounter = 0;

			int docWordIndex = 0;
			for (String keyWord : keywordsMap.get(type)) {
				for (int i = docWordIndex; i < wordSet.size(); i++) {
					if (keyWord.equals(words[i])) {
						matchCounter++;
						docWordIndex = i + 1;
						break;
					}
					if (keyWord.compareTo(words[i]) < 0) {
						docWordIndex = i;
						break;
					} else {
						continue;
					}
				}
			}

			typeCounter.put(type, matchCounter);
		}
		return typeCounter;
	}

	public static Map<String, TreeSet<String>> generateKeywordsMap(BufferedReader keywordsReader) throws IOException {
		// TODO Auto-generated method stub
		Map<String, TreeSet<String>> keywordsMap = new TreeMap<String, TreeSet<String>>();

		String line = "";
		while ((line = keywordsReader.readLine()) != null) {
			String[] type_keys = line.split(":");
			String[] keys = type_keys[1].split(" ");
			TreeSet<String> keySet = new TreeSet<String>();
			for (String key : keys) {
				keySet.add(key);
			}
			keywordsMap.put(type_keys[0], keySet);
		}

		return keywordsMap;
	}

	public static void mergeKeyAndExkey(String destKeywordCount, String destFile1, String destFile2) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader kcReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(destFile1)));
		BufferedReader ekcReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(destFile2)));

		
		BufferedWriter kcWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(destKeywordCount)));
		
		String kcLine = "";
		String ekcLine = "";
		while((kcLine = kcReader.readLine()) != null){
			if(!kcLine.equals("")){
				ekcLine = ekcReader.readLine();
				kcWriter.append(kcLine + ekcLine);
				kcWriter.newLine();
				kcWriter.flush();
			}
		}
		
		kcReader.close();
		ekcReader.close();
		kcWriter.close();
	}
}
