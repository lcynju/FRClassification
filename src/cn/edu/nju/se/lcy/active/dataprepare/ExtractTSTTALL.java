package cn.edu.nju.se.lcy.active.dataprepare;

import cn.edu.nju.se.lcy.extract.CalculateTFIDF;
import cn.edu.nju.se.lcy.extract.KeywordCounts;
import cn.edu.nju.se.lcy.heu.RuleValueCalculator;

public class ExtractTSTTALL {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String fileFolderPre = "E:\\JSS\\Active Learning\\Winmerge\\Active Learning Fold";
		String allProcessedFile = "E:\\JSS\\Active Learning\\Winmerge\\active sources\\";
		for(int i = 0; i < 5; i ++){
			String fileFolder = fileFolderPre + i + "\\";
			
			String trainFolder = "train data\\";
			String testFolder = "test data\\";
			
			String[] folders = new String[]{trainFolder, testFolder};
			
			String keywordsFile = "keywords.txt";
			
			for(String folder : folders){
				String processedFile = folder + "processed.txt";
				String posFile = folder + "pos.txt";
				String sourceFile = folder + "source data.txt";
				String destFile1 = folder + "keywordcounts.txt";
				
				CalculateTFIDF.startWithIDF(CalculateTFIDF.generateIDF(allProcessedFile, "processed.txt", "", " "), fileFolder, processedFile, false, "", " ");
				KeywordCounts kc = new KeywordCounts(fileFolder, keywordsFile, processedFile, destFile1);
				kc.start();
				
				//String[] hArgs = new String[]{fileFolder, sourceFile, posFile, folder + "heuristicrules.txt"};
				//RuleValueCalculator.main(hArgs);
				
			}
		}
	}

}
