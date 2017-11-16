package cn.edu.nju.se.lcy.mains.testreserved;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.se.lcy.generate.SplitGroups;

public class SplitMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath = "C:\\Users\\Chuanyi Li\\Desktop\\Experiments-0715\\";
		String destFolder = "tfidf-keywordcounts-heuristicrules";
		String type = "numeric-numeric-numeric";

		simpleSplit(filePath, destFolder, type, 5);
		//duplicateSplit(filePath, destFolder, type, "performance-reliability", "5-4");
	}

	public static void simpleSplit(String filePath, String destFolder, String type, int folds) {
		if (!destFolder.contains("-")) {
			conductSplit(filePath, destFolder, "nominal", folds, false, null, null, ".arff");
			conductSplit(filePath, destFolder, "numeric", folds, false, null, null, ".arff");
		} else {
			conductSplit(filePath, destFolder, type, folds, false, null, null, ".arff");
		}
	}
	
	public static void duplicateSplit(String filePath, String destFolder, String type, String classes, String times, int folds) {
		List<String> classNames = new ArrayList<String>();
		List<Integer> dupTimes = new ArrayList<Integer>();
		for(String className : classes.split("-")){
			classNames.add(className);
		}
		for(String dupTime : times.split("-")){
			dupTimes.add(Integer.parseInt(dupTime));
		}
		if (!destFolder.contains("-")) {
			conductSplit(filePath, destFolder, "nominal", folds, true, classNames, dupTimes, ".arff");
			conductSplit(filePath, destFolder, "numeric", folds, true, classNames, dupTimes, ".arff");
		} else {
			conductSplit(filePath, destFolder, type, folds, true, classNames, dupTimes, ".arff");
		}
	}
	
	public static void conductSplit(String filePath, String destFolder, String type, int folds, boolean duplicate, List<String> classNames, List<Integer>dupTimes, String fileExtension){
		String sourceName = destFolder + "-" + type + ".arff";
		String sourceFile = filePath + destFolder + "\\" + sourceName;
		
		String groupFolderName = !duplicate ? type + "Binary" : "DUP" + type + "Binary";
		String groupFolder = filePath + destFolder + "\\" + groupFolderName + "\\";
		SplitGroups sg = new SplitGroups(sourceFile, groupFolder, 5, duplicate, classNames, dupTimes, fileExtension);
		sg.startTestReserved(true);
		//sg.start(true);
		groupFolderName = !duplicate ? type + "Multi" : "DUP" + type + "Multi";
		groupFolder = filePath + destFolder + "\\" + groupFolderName + "\\";
		sg.setGroupFolder(groupFolder);
		sg.startTestReserved(false);
//		if(duplicate){
//			sg.start(false);
//		}
	}
}
