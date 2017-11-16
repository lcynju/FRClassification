package cn.edu.nju.se.lcy.active.dataprepare;

import java.io.File;

import cn.edu.nju.se.lcy.generate.Organize4Weka;

public class OrganizeSTTALL {

	public static void main(String[] args) {
		String classes = "security,reliability,performance,softwareinterface,lifecycle,usability,capability";
		String trueName = "trueClasses.txt";
		
		String fileFolderPre = "E:\\JSS\\Active Learning\\Winmerge\\Active Learning Fold";
		for(int i = 0; i < 5; i ++){
			String fileFolder = fileFolderPre + i + "\\";
			String trainFolder = "train data\\";
			String testFolder = "test data\\";
			
			String[] folders = new String[]{trainFolder, testFolder};
			
			for(String folder : folders){
				String[] featureNames = new String[] { "keywordcounts", "tfidf"};
				
//				String[] featureNames = new String[] { "pseudoheuristicrules", /*"pseudokeywordcounts", "pseudotfidf", "pseudotfidf-pseudokeywordcounts",*/
//						"pseudotfidf-pseudoheuristicrules", "pseudokeywordcounts-pseudoheuristicrules",
//						"pseudotfidf-pseudokeywordcounts-pseudoheuristicrules" };
				
//				String[] featureNames = new String[] { /*"pseudoheuristicrules", "pseudokeywordcounts", "pseudotfidf",*/ "fakepseudotfidf-pseudokeywordcounts",
//						/*"fakepseudotfidf-fakepseudoheuristicrules", "pseudokeywordcounts-fakepseudoheuristicrules",*/
//						"fakepseudotfidf-pseudokeywordcounts-fakepseudoheuristicrules" };
				
				String[] types = new String[] { "numeric", "numeric"};
				
				for(int featureIndex = 0; featureIndex < featureNames.length; featureIndex ++){
					Organize4Weka o4w = new Organize4Weka(fileFolder + folder, featureNames[featureIndex], trueName, classes);
					if(!types[featureIndex].contains("-")){
						o4w.startSingle(types[featureIndex]);
					}else{
						o4w.startMulti(types[featureIndex]);
					}
					
					String formerFilePath = fileFolder + folder + featureNames[featureIndex] + "\\";
					String destFilePath = fileFolder + "active learning" + "\\";
					File desfolder = new File(destFilePath);
					if(!desfolder.exists()){
						desfolder.mkdir();
					}
					String formerFileName =  formerFilePath + featureNames[featureIndex] + "-" + types[featureIndex] + ".arff";
					File file = new File(formerFileName);
					String destFileName = featureNames[featureIndex] + "-" + types[featureIndex] + ".arff";
					destFileName = destFileName.replaceAll("pseudo", "");
					destFileName = destFileName.replaceAll("fake", "");
					if(folder.contains("train")){
						file.renameTo(new File(destFilePath + "train-" + destFileName));
					}else{
						file.renameTo(new File(destFilePath + "test-" + destFileName));
					}
				}
			}
		}
	}

}
