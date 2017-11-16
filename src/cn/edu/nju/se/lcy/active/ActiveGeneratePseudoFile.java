package cn.edu.nju.se.lcy.active;

import java.io.File;

import cn.edu.nju.se.lcy.pseudoinstance.GeneratePseudoInstance;

public class ActiveGeneratePseudoFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileFolderPre = "D:\\Experiments-0724-Fold";
		for (int i = 2; i < 6; i++) {
			String fileFolder = fileFolderPre + i + "\\";
			String trainFolder = "large train data\\";
			String testFolder = "test data\\";
			String keywordFile = fileFolder + "keywords.txt";
			String[] folders = new String[] { trainFolder, testFolder };

			for (String folder : folders) {
				String sourcePath = fileFolder + folder;
				String processedFileName = "processed with class.txt";
				String sourceFileName = "source data.txt";
				String heuristicFileName = "heuristicrules.txt";
				String posFileName = "pos.txt";
				String tfidfFileName = "tfidf.txt";

				GeneratePseudoInstance gpi = new GeneratePseudoInstance();
				gpi.initialize(keywordFile);

				gpi.generatePseudoSeperately(sourcePath, processedFileName, sourceFileName, heuristicFileName,
						posFileName, tfidfFileName);
			}
		}
	}

}
