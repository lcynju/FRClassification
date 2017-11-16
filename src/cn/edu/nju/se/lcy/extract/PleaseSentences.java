package cn.edu.nju.se.lcy.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import edu.northwestern.at.morphadorner.corpuslinguistics.lemmatizer.EnglishLemmatizer;

public class PleaseSentences {

	public static void main(String[] args) throws Exception {
		String filePath = "C:\\Users\\Chuanyi Li\\Desktop\\Experiments-0710\\pleasesentence\\";
		// TODO Auto-generated method stub
		EnglishLemmatizer el = new EnglishLemmatizer();

		String lemma = filePath + "class-lemma.txt";
		String pos = filePath + "processed pos.txt";
		String keywords = filePath + "keywords.txt";
		String docs = filePath + "processed.txt";

		String result = filePath + "pleasesentences.txt";

		BufferedReader lemmaReader = new BufferedReader(new InputStreamReader(new FileInputStream(lemma)));
		BufferedReader posReader = new BufferedReader(new InputStreamReader(new FileInputStream(pos)));
		BufferedReader keysReader = new BufferedReader(new InputStreamReader(new FileInputStream(keywords)));
		BufferedReader docsReader = new BufferedReader(new InputStreamReader(new FileInputStream(docs)));

		BufferedWriter ruledOutWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(result)));

		String[] keys = keysReader.readLine().split(" ");
		List<String> keyList = new ArrayList<String>();
		for (String key : keys) {
			keyList.add(el.lemmatize(key));
		}

		String lemmaLine = "";
		while ((lemmaLine = lemmaReader.readLine()) != null) {
			String posLine = posReader.readLine();
			String docLine = docsReader.readLine();
			posLine = "¿Ú" + posLine;
			String[] docWords = docLine.split(" ");
			boolean isCAPlease = false;
			if (posLine.contains(",\tVB\t") || posLine.contains(".\tVB\t") || posLine.contains("¿ÚVB\t")) {
				if (lemmaLine.split("¾ä").length == 1) {
					isCAPlease = true;
				}
				boolean hasKey = false;
				for (String docWord : docWords) {
					if (keyList.contains(docWord)) {
						hasKey = true;
						break;
					}
				}
				if (hasKey) {
					isCAPlease = false;
				}
			}
			if (isCAPlease) {
				ruledOutWriter.append("1\t");
			} else {
				ruledOutWriter.append("0\t");
			}
			
			boolean iwilllike = false;
			if(docLine.contains("i will like") || docLine.contains("i d like")){
				iwilllike = true;
				boolean hasKey = false;
				for (String docWord : docWords) {
					if (keyList.contains(docWord)) {
						hasKey = true;
						break;
					}
				}
				if (hasKey) {
					iwilllike = false;
				}
			}
			if(iwilllike){
				ruledOutWriter.append("1\t");
			}else{
				ruledOutWriter.append("0\t");
			}
			
			boolean itwillbe = false;
			if(docLine.contains("it will be") || docLine.contains("it d be")){
				itwillbe = true;
				boolean hasKey = false;
				for (String docWord : docWords) {
					if (keyList.contains(docWord)) {
						hasKey = true;
						break;
					}
				}
				if (hasKey) {
					itwillbe = false;
				}
			}
			if(itwillbe){
				ruledOutWriter.append("1\t");
			}else{
				ruledOutWriter.append("0\t");
			}
			
			ruledOutWriter.newLine();
			ruledOutWriter.flush();
		}

		ruledOutWriter.close();
		lemmaReader.close();
		posReader.close();
		keysReader.close();
		docsReader.close();
	}

}
