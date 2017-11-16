package cn.edu.nju.se.lcy.mains.notest;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.se.lcy.generate.SplitGroups;

public class SplitMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath = args[0];
		String destFolder = args[1];;
		String type = args[2];

		simpleSplit(filePath, destFolder, type);
		//duplicateSplit(filePath, destFolder, type, "performance-reliability", "5-4");
	}

	public static void simpleSplit(String filePath, String destFolder, String type) {
		if (!destFolder.contains("-")) {
//			String sourceName1 = destFolder + "-nominal.arff";
//			String groupFolderName1 = "Nominal Binary";
//
//			String sourceFile1 = filePath + destFolder + "\\" + sourceName1;
//			String groupFolder1 = filePath + destFolder + "\\" + groupFolderName1 + "\\";
//			SplitGroups sg1 = new SplitGroups(sourceFile1, groupFolder1, 5, false, null, null, ".arff");
//			sg1.start(true);

			String sourceName2 = destFolder + "-numeric.arff";
			String groupFolderName2 = "Numeric Binary";

			String sourceFile2 = filePath + destFolder + "\\" + sourceName2;
			String groupFolder2 = filePath + destFolder + "\\" + groupFolderName2 + "\\";
			SplitGroups sg2 = new SplitGroups(sourceFile2, groupFolder2, 5, false, null, null, ".arff");
			sg2.start(true);
		} else {
			String sourceName1 = destFolder + "-" + type + ".arff";
			String groupFolderName1 = "Binary";

			String sourceFile1 = filePath + destFolder + "\\" + sourceName1;
			String groupFolder1 = filePath + destFolder + "\\" + groupFolderName1 + "\\";
			SplitGroups sg1 = new SplitGroups(sourceFile1, groupFolder1, 5, false, null, null, ".arff");
			sg1.start(true);
		}
	}
	
	public static void duplicateSplit(String filePath, String destFolder, String type, String classes, String times) {
		List<String> classNames = new ArrayList<String>();
		List<Integer> dupTimes = new ArrayList<Integer>();
		for(String className : classes.split("-")){
			classNames.add(className);
		}
		for(String dupTime : times.split("-")){
			dupTimes.add(Integer.parseInt(dupTime));
		}
		if (!destFolder.contains("-")) {
			String sourceName1 = destFolder + "-nominal.arff";
			String groupFolderName1 = "DUP Nominal Binary";

			String sourceFile1 = filePath + destFolder + "\\" + sourceName1;
			String groupFolder1 = filePath + destFolder + "\\" + groupFolderName1 + "\\";
			SplitGroups sg1 = new SplitGroups(sourceFile1, groupFolder1, 5, true, classNames, dupTimes, ".arff");
			sg1.start(true);

			String sourceName2 = destFolder + "-numeric.arff";
			String groupFolderName2 = "DUP Numeric Binary";

			String sourceFile2 = filePath + destFolder + "\\" + sourceName2;
			String groupFolder2 = filePath + destFolder + "\\" + groupFolderName2 + "\\";
			SplitGroups sg2 = new SplitGroups(sourceFile2, groupFolder2, 5, true, classNames, dupTimes, ".arff");
			sg2.start(true);
			
			String sourceName3 = destFolder + "-numeric.arff";
			String groupFolderName3 = "DUP Numeric Multi";

			String sourceFile3 = filePath + destFolder + "\\" + sourceName3;
			String groupFolder3 = filePath + destFolder + "\\" + groupFolderName3 + "\\";
			SplitGroups sg3 = new SplitGroups(sourceFile3, groupFolder3, 5, true, classNames, dupTimes, ".arff");
			sg3.start(false);
			
			String sourceName4 = destFolder + "-nominal.arff";
			String groupFolderName4 = "DUP Nominal Multi";

			String sourceFile4 = filePath + destFolder + "\\" + sourceName4;
			String groupFolder4 = filePath + destFolder + "\\" + groupFolderName4 + "\\";
			SplitGroups sg4 = new SplitGroups(sourceFile4, groupFolder4, 5, true, classNames, dupTimes, ".arff");
			sg4.start(false);
		} else {
			String sourceName = destFolder + "-" + type + ".arff";
			String groupFolderName1 = "DUP Binary";

			String sourceFile1 = filePath + destFolder + "\\" + sourceName;
			String groupFolder1 = filePath + destFolder + "\\" + groupFolderName1 + "\\";
			SplitGroups sg1 = new SplitGroups(sourceFile1, groupFolder1, 5, true, classNames, dupTimes, ".arff");
			sg1.start(true);
			
			String groupFolderName2 = "DUP Multi";

			String sourceFile2 = filePath + destFolder + "\\" + sourceName;
			String groupFolder2 = filePath + destFolder + "\\" + groupFolderName2 + "\\";
			SplitGroups sg2 = new SplitGroups(sourceFile2, groupFolder2, 5, true, classNames, dupTimes, ".arff");
			sg2.start(false);
		}
	}
}
