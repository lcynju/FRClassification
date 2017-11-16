package cn.edu.nju.se.lcy.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import edu.northwestern.at.morphadorner.corpuslinguistics.lemmatizer.EnglishLemmatizer;

public class KeywordUnigram {

	private String filePath;
	private String keywordFileName;
	private String sourceName;
	private String destName;

	public KeywordUnigram(String filePath, String keywordFileName, String sourceName, String destName) {
		this.filePath = filePath;
		this.keywordFileName = keywordFileName;
		this.sourceName = sourceName;
		this.destName = destName;
	}

	public void startSingleWord() throws Exception {
		EnglishLemmatizer el = new EnglishLemmatizer();
		BufferedReader keywordsReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(this.filePath + this.keywordFileName)));
		BufferedReader docReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(this.filePath + this.sourceName)));

		TreeSet<String> keywordsSet = new TreeSet<String>();

		BufferedWriter counterWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(this.filePath + this.destName)));

		String line = "";
		while ((line = keywordsReader.readLine()) != null) {
			String[] type_keys = line.split(":");
			String[] keys = type_keys[1].split(" ");
			for (String key : keys) {
				keywordsSet.add(el.lemmatize(key));
			}
		}

		String[] keywordsArray = new String[keywordsSet.size()];
		keywordsSet.toArray(keywordsArray);
		while ((line = docReader.readLine()) != null) {
			String[] words = line.split(" ");

			TreeSet<String> wordSet = new TreeSet<String>();
			wordSet.addAll(Arrays.asList(words));

			String vector = "";
			for (int i = 0; i < keywordsArray.length; i++) {
				if (wordSet.contains(keywordsArray[i])) {
					vector += "1\t";
				} else {
					vector += "0\t";
				}
			}

			counterWriter.append(vector);
			counterWriter.newLine();
			counterWriter.flush();
		}

		counterWriter.close();
		keywordsReader.close();
		docReader.close();
	}

	public void startLine() throws Exception {
		BufferedReader keywordsReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(this.filePath + this.keywordFileName)));
		BufferedReader docReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(this.filePath + this.sourceName)));

		List<List<String>> keywordsSet = new ArrayList<List<String>>();

		BufferedWriter counterWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(this.filePath + this.destName)));

		String line = "";
		while ((line = keywordsReader.readLine()) != null) {
			List<String> feature = new ArrayList<String>();
			String[] keys = line.split(",");
			for (String key : keys) {
				feature.add(key);
			}
			keywordsSet.add(feature);
		}

		while ((line = docReader.readLine()) != null) {
			line = " " + line + " ";

			String vector = "";
			for (int i = 0; i < keywordsSet.size(); i++) {
				boolean contain = false;
				for (String feature : keywordsSet.get(i)) {
					if (line.contains(feature)) {
						vector += "1\t";
						contain = true;
						break;
					}
				}
				if(!contain){
					vector += "0\t";
				}
			}

			counterWriter.append(vector);
			counterWriter.newLine();
			counterWriter.flush();
		}

		counterWriter.close();
		keywordsReader.close();
		docReader.close();
	}
}
