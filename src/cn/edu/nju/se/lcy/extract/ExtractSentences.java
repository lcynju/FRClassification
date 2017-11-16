package cn.edu.nju.se.lcy.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ExtractSentences {

	public static void extractsentence(String sourceFile, String ruledOutFile, String remainFile, String[] rules)
			throws IOException {
		BufferedReader sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
		BufferedWriter ruledOutWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruledOutFile)));
		BufferedWriter remainWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(remainFile)));

		String line = "";
		while ((line = sourceReader.readLine()) != null) {
			String[] docElements = line.split("\t");
			ruledOutWriter.append(docElements[0] + "\t");
			remainWriter.append(docElements[0] + "\t");
			String[] sentences = docElements[1].split("¾ä");
			boolean right = false;
			for (String sentence : sentences) {
				String newSentence = "¿Ú" + sentence + "¿Ú";
				if((newSentence.contains(rules[0]) || newSentence.contains(rules[1]))){
					right = true;
					break;
				}
			}
			if(right){
				ruledOutWriter.append(docElements[1]);
			}else{
				remainWriter.append(docElements[1]);
			}
			ruledOutWriter.newLine();
			remainWriter.newLine();
			
			ruledOutWriter.flush();
			remainWriter.flush();
		}
		
		sourceReader.close();
		ruledOutWriter.close();
		remainWriter.close();
	}
	
	public static void extractline(String sourceFile, String ruledOutFile, String remainFile, String[] rules)
			throws IOException {
		BufferedReader sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
		BufferedWriter ruledOutWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruledOutFile)));
		BufferedWriter remainWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(remainFile)));

		String line = "";
		while ((line = sourceReader.readLine()) != null) {
			String[] docElements = line.split("\t");
			ruledOutWriter.append(docElements[0] + "\t");
			remainWriter.append(docElements[0] + "\t");
			if(docElements[1].contains(rules[0]) && docElements[1].contains(rules[1])){
				ruledOutWriter.append(docElements[1]);
			}else{
				remainWriter.append(docElements[1]);
			}
			ruledOutWriter.newLine();
			remainWriter.newLine();
			
			ruledOutWriter.flush();
			remainWriter.flush();
		}
		
		sourceReader.close();
		ruledOutWriter.close();
		remainWriter.close();
	}
}
