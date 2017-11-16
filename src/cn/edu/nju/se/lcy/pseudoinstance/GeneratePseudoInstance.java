package cn.edu.nju.se.lcy.pseudoinstance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratePseudoInstance {

	private String keywordFilePath;
	private List<String> classNames;
	private Map<String, List<String>> keywordsOfClasses;

	public void initialize(String keywordFilePath) {
		this.keywordFilePath = keywordFilePath;
		this.classNames = new ArrayList<String>();
		this.keywordsOfClasses = new HashMap<String, List<String>>();

		readKeywords();
	}

	private void readKeywords() {
		// TODO Auto-generated method stub
		try {
			BufferedReader sourceReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(this.keywordFilePath)));
			String line = "";
			while ((line = sourceReader.readLine()) != null) {
				if (!line.equals("")) {
					String[] keywordLine = line.split(":");
					this.classNames.add(keywordLine[0]);

					List<String> keywords = new ArrayList<String>();
					keywords.addAll(Arrays.asList(keywordLine[1].split(" ")));
					this.keywordsOfClasses.put(keywordLine[0], keywords);
				}
			}
			sourceReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generatePseudoSeperately(String filePath, String fileName, String initFile, String heruisticFile, String posFile, String tfidfFile) {
		try {
			BufferedReader sourceReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filePath + fileName)));
			BufferedReader initReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filePath + initFile)));
//			BufferedReader heuristicSourceReader = new BufferedReader(
//					new InputStreamReader(new FileInputStream(filePath + heruisticFile)));
//			BufferedReader posSourceReader = new BufferedReader(
//					new InputStreamReader(new FileInputStream(filePath + posFile)));
//			BufferedReader tfidfSourceReader = new BufferedReader(
//					new InputStreamReader(new FileInputStream(filePath + tfidfFile)));

//			BufferedWriter destWriter = new BufferedWriter(
//					new OutputStreamWriter(new FileOutputStream(filePath + "pseudoprocessed.txt")));
//			BufferedWriter mapWriter = new BufferedWriter(
//					new OutputStreamWriter(new FileOutputStream(filePath + "normal-pseudo-pair.txt")));
//			BufferedWriter trueWriter = new BufferedWriter(
//					new OutputStreamWriter(new FileOutputStream(filePath + "pseudotrueClasses.txt")));
//			BufferedWriter heuristicWriter = new BufferedWriter(
//					new OutputStreamWriter(new FileOutputStream(filePath + "fakepseudo" + heruisticFile)));
//			BufferedWriter posWriter = new BufferedWriter(
//					new OutputStreamWriter(new FileOutputStream(filePath + "pseudo" + posFile)));
//			BufferedWriter tfidfWriter = new BufferedWriter(
//					new OutputStreamWriter(new FileOutputStream(filePath + "fakepseudo" + tfidfFile)));
			BufferedWriter initWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filePath + "pseudo" + initFile)));

			String sourceLine = "";
			String initLine = "";
			String heuristicLine = "";
			String posLine = "";
			String tfidfLine = "";
			int normalIndex = 0;
			int pseudoIndex = 0;
			while ((sourceLine = sourceReader.readLine()) != null) {
				if (!sourceLine.equals("")) {
//					heuristicLine = heuristicSourceReader.readLine();
//					posLine = posSourceReader.readLine();
//					tfidfLine = tfidfSourceReader.readLine();
					initLine = initReader.readLine();
					String[] tmpLine = sourceLine.split("\t");
					String className = tmpLine[0].toLowerCase();
					String[] instanceandmark = generateSingleForSingleLineSeperately(tmpLine[1], className);
					String instance = instanceandmark[1];
					String mark = instanceandmark[0];
					if (mark.equals("pseudo")) {
//						trueWriter.append(className);
//						destWriter.append(instance);
//						mapWriter.append(normalIndex + "\t" + pseudoIndex);
//						posWriter.append(posLine);
//						tfidfWriter.append(tfidfLine);
						initWriter.append(replaceAllOtherClassKeywords(initLine, className));
//						heuristicWriter.append(heuristicLine);
						
//						trueWriter.newLine();
//						destWriter.newLine();
//						mapWriter.newLine();
//						posWriter.newLine();
						initWriter.newLine();
//						tfidfWriter.newLine();
//						trueWriter.flush();
//						destWriter.flush();
//						mapWriter.flush();
//						posWriter.flush();
//						tfidfWriter.flush();
						initWriter.flush();
//						heuristicWriter.newLine();
//						heuristicWriter.flush();
						
						pseudoIndex++;
					}
					normalIndex++;
				}
			}
			sourceReader.close();
//			heuristicSourceReader.close();
//			posSourceReader.close();
//			trueWriter.close();
//			destWriter.close();
//			mapWriter.close();
//			heuristicWriter.close();
//			posWriter.close();
//			tfidfWriter.close();
//			tfidfSourceReader.close();
			initWriter.close();
			initReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String[] generateSingleForSingleLineSeperately(String sourceLine, String className) {
		String resultMark = "pseudo";
		String pseudoInstance = "";

		String[] words = sourceLine.split(" ");
		boolean hasOtherTypeKeyword = false;
		for (String word : words) {
			if (isNotOtherKeyword(word, className)) {
				pseudoInstance += word + " ";
			} else {
				hasOtherTypeKeyword = true;
			}
		}
		if (!hasOtherTypeKeyword) {
			resultMark = "former";
		}
		return new String[] { resultMark, pseudoInstance };
	}

	public void generatePseudoInstances(String filePath, String fileName) {
		try {
			String[] fileNames = fileName.split("\\.");
			String fileExtension = "." + fileNames[1];
			fileName = fileNames[0];
			BufferedReader sourceReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filePath + fileName + fileExtension)));
			BufferedWriter destWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filePath + fileName + "pseudo" + fileExtension)));
			BufferedWriter markWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filePath + fileName + "pseudo-mark" + fileExtension)));
			String line = "";
			while ((line = sourceReader.readLine()) != null) {
				if (!line.equals("")) {
					String[] tmpLine = line.split("\t");
					String className = tmpLine[0];
					Object[] instancesandmarks = generateSingleForSingleLine(tmpLine[1], className);
					List<String> instances = (List<String>) instancesandmarks[0];
					List<String> marks = (List<String>) instancesandmarks[1];
					for (int instanceIndex = 0; instanceIndex < instances.size(); instanceIndex++) {
						destWriter.append(className + "\t" + instances.get(instanceIndex));
						destWriter.newLine();
						destWriter.flush();
						markWriter.append(marks.get(instanceIndex));
						markWriter.newLine();
						markWriter.flush();
					}
				}
			}
			sourceReader.close();
			destWriter.close();
			markWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Object[] generateMultiForSingleLine(String tmpLine, String className) {
		// TODO Auto-generated method stub
		List<String> instances = new ArrayList<String>();
		List<String> instancesMarks = new ArrayList<String>();
		instances.add("");

		List<String> keywords = this.keywordsOfClasses.get(className);
		// String[] sentences = tmpLine.split("");
		// for (String sentence : sentences) {
		String[] words = tmpLine.split(" ");
		for (String word : words) {
			if (isNotOtherKeyword(word, className)) {
				if (keywords.contains(word)) {
					String lastInstance = instances.get(instances.size() - 1);
					String newLastInstance = lastInstance + word + " ";
					instances.set(instances.indexOf(lastInstance), newLastInstance);
					instancesMarks.add("pseudo");
					instances.add(lastInstance);
				} else {
					for (String instance : instances) {
						instances.set(instances.indexOf(instance), instance + word + " ");
					}
				}
			}
		}
		// }
		if (instances.size() > 1) {
			instances.remove(instances.size() - 1);
		} else {
			instancesMarks.add("former");
		}
		if (instances.size() > 1) {
			instances.add(tmpLine.replaceAll("¾ä", ""));
			instancesMarks.add("former");
		}
		return new Object[] { instances, instancesMarks };
	}

	private Object[] generateSingleForSingleLine(String tmpLine, String className) {
		// TODO Auto-generated method stub
		List<String> instances = new ArrayList<String>();
		List<String> instancesMarks = new ArrayList<String>();

		List<String> keywords = this.keywordsOfClasses.get(className);
		String[] words = tmpLine.split(" ");
		String pseudoInstance = "";
		boolean hasThisTypeKeyword = false;
		boolean hasOtherTypeKeyword = false;
		for (String word : words) {
			if (isNotOtherKeyword(word, className)) {
				pseudoInstance += word + " ";
				if (keywords.contains(word)) {
					hasThisTypeKeyword = true;
				}
			} else {
				hasOtherTypeKeyword = true;
			}
		}
		if (hasThisTypeKeyword) {
			instances.add(pseudoInstance);
			instancesMarks.add("pseudo");
		}
		if (hasOtherTypeKeyword || !hasThisTypeKeyword) {
			instances.add(tmpLine);
			instancesMarks.add("former");
		}
		return new Object[] { instances, instancesMarks };
	}

	private boolean isNotOtherKeyword(String word, String className) {
		// TODO Auto-generated method stub
		boolean isNotInOthers = true;
		for (String classType : this.keywordsOfClasses.keySet()) {
			if (!classType.equals(className)) {
				if (this.keywordsOfClasses.get(classType).contains(word)) {
					isNotInOthers = false;
					break;
				}
			}
		}
		return isNotInOthers;
	}

	private String replaceAllOtherClassKeywords(String heuristicLine, String className) {
		String[] replaced = heuristicLine.toLowerCase().split("\t");
		String sentence = replaced[1];
		for (String classType : this.keywordsOfClasses.keySet()) {
			if (!classType.equals(className)) {
				for (String otherKey : this.keywordsOfClasses.get(classType)) {
					sentence = sentence.replaceAll(otherKey, "");
				}
			}
		}
		return replaced[0] + "\t" + sentence;
	}
}
