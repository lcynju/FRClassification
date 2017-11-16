package cn.edu.nju.se.lcy.pseudoinstance;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cn.edu.nju.se.lcy.extract.CalculateTFIDF;
import cn.edu.nju.se.lcy.extract.KeywordCounts;
import cn.edu.nju.se.lcy.generate.Organize4Weka;
import cn.edu.nju.se.lcy.generate.SplitGroups;

public class PseudoInstanceMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String sourcefileName = "processedwithclass";
			String filePath = "C:\\Users\\Chuanyi Li\\Desktop\\Experiments-0707\\";
			SplitGroups.simpleSplit(5, "pseudo", filePath, sourcefileName, ".txt");
			GeneratePseudoInstance gpi = new GeneratePseudoInstance();
			gpi.initialize(filePath + "keywords.txt");
			File groupParent = new File(filePath + sourcefileName + "\\");
			for(File group : groupParent.listFiles()){
				if(group.isDirectory()){
					for(File sourceFile : group.listFiles()){
						if(sourceFile.getName().contains("others")){
							gpi.generatePseudoInstances(sourceFile.getParent()+"\\", sourceFile.getName());
						}
					}
				}
			}
			SplitGroups.simpleReadGroupTrueClass(filePath + sourcefileName + "\\", "pseudo", 5);
			
			Map<String, Double> idf = CalculateTFIDF.generateIDF(filePath, "processedwithclass.txt", "", " ");
			for(File group : groupParent.listFiles()){
				if(group.isDirectory()){
					for(File sourceFile : group.listFiles()){
						if(sourceFile.getName().contains("group") || sourceFile.getName().contains("pseudo") && !sourceFile.getName().contains("mark")){
							CalculateTFIDF.startWithIDF(idf, sourceFile.getParent()+"\\", sourceFile.getName(), false, "", " ");
							KeywordCounts.keywordCount(sourceFile.getAbsolutePath(), filePath + "keywords-new.txt", sourceFile.getParent()+"\\keywordcounts" + sourceFile.getName(), "", " ");
							SplitGroups.simpleReadSingleTrueClass(sourceFile.getParent()+"\\", sourceFile.getName());
						}
					}
				}
			}
			
			for(int groupIndex = 0; groupIndex < 5; groupIndex ++){
				String groupPath = filePath + sourcefileName + "\\pseudo" + groupIndex + "\\";
				String featureSourceFile1 = "tfidfgroup" + groupIndex + "-keywordcountsgroup" + groupIndex;
				String featureSourceFile2 = "tfidfothers" + groupIndex + "pseudo" + "-keywordcountsothers" + groupIndex + "pseudo";
				String trueFile1 = "trueClassgroup" + groupIndex + ".txt";
				String trueFile2 = "trueClassothers" + groupIndex + "pseudo.txt";
				
				String classes = "security,reliability,performance,softwareinterface,lifecycle,usability,capability";
				Organize4Weka o4w = new Organize4Weka(groupPath, featureSourceFile1, trueFile1, classes);
				o4w.startMulti("numeric-numeric");
//				o4w.startSingle("numeric");
				
				Organize4Weka o4w1 = new Organize4Weka(groupPath, featureSourceFile2, trueFile2, classes);
				o4w1.startMulti("numeric-numeric");
//				o4w1.startSingle("numeric");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
