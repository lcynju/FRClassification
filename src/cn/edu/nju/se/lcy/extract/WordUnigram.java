package cn.edu.nju.se.lcy.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import edu.northwestern.at.morphadorner.corpuslinguistics.lemmatizer.EnglishLemmatizer;

public class WordUnigram {

	private String filePath;
	private String sourceName;
	private String destName;

	public WordUnigram(String sourcePath, String sourceName, String destName) {
		// TODO Auto-generated method stub
		this.filePath = sourcePath;
		this.sourceName = sourceName;
		this.destName = destName;
	}

	public void start(boolean removeStopWords) {
		try {
			
			BufferedReader sourceReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(this.filePath + this.sourceName)));
			
			TreeSet<String> featureSet = new TreeSet<String>();

			String line = "";
			while ((line = sourceReader.readLine()) != null) {
				String[] words = line.split(" ");
				featureSet.addAll(Arrays.asList(words));
			}
			sourceReader.close();

			String[] featureArray = new String[featureSet.size()];
			featureSet.toArray(featureArray);

			String outputFile = this.filePath + this.destName;
			if (removeStopWords) {
				StopWords.removeStopWords(featureSet);
				outputFile = this.filePath + "removestopwords-" + this.destName;
			}
			BufferedWriter outputWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputFile)));

			BufferedReader sourceReaderNew = new BufferedReader(
					new InputStreamReader(new FileInputStream(this.filePath + this.sourceName)));
			
			while ((line = sourceReaderNew.readLine()) != null) {
				String[] words = line.split(" ");
				TreeSet<String> wordSet = new TreeSet<String>();
				wordSet.addAll(Arrays.asList(words));
				String vector = "";
				for (int i = 0; i < featureSet.size(); i++) {
					String feature = featureArray[i];
					if (wordSet.contains(feature)) {
						vector += "1" + "\t";
					} else {
						vector += "0" + "\t";
					}
				}
				outputWriter.append(vector);
				outputWriter.newLine();
				outputWriter.flush();
			}

			outputWriter.close();
			sourceReaderNew.close();
		} catch (Exception e) {

		}
	}

}
