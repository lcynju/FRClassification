package cn.edu.nju.se.lcy.active.dataprepare;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.ivy.util.FileUtil;

import cn.edu.nju.se.lcy.generate.SplitGroups;
import edu.northwestern.at.utils.FileUtils;

public class SplitTrainAndTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String fileFolder = "E:\\JSS\\Active Learning\\Winmerge\\active sources\\";
		String sourceName = "source data";
		String trueName = "trueClasses";
		String posName = "pos";
		String processedName = "processed";
		String processedWithClassName = "processed with class";
		
		String keywordsPath = fileFolder + "keywords.txt";
		
		SplitGroups.simpleSplit(5, "source", fileFolder, sourceName, ".txt");
		SplitGroups.simpleSplit(5, "true", fileFolder, trueName, ".txt");
		SplitGroups.simpleSplit(5, "pos", fileFolder, posName, ".txt");
		SplitGroups.simpleSplit(5, "processed", fileFolder, processedName, ".txt");
		SplitGroups.simpleSplit(5, "processedwithclass", fileFolder, processedWithClassName, ".txt");
		
		moveToDES(fileFolder, "E:\\JSS\\Active Learning\\Winmerge\\Active Learning Fold", keywordsPath);
	}

	public static void moveToDES(String sourceDir, String desDir, String keywordsPath){
		String[] sourcesubs = new String[]{"pos", "processed", "source data", "trueClasses"};
		String[] sourcenames = new String[]{"pos", "processed", "source", "true"};
		
		for(int foldIndex = 0; foldIndex < 5; foldIndex ++){
			File desDirFile = new File(desDir + foldIndex + "\\");
			
			if(!desDirFile.exists()){
				desDirFile.mkdir();
				File desTestDir = new File(desDir + foldIndex + "\\test data\\");
				desTestDir.mkdir();
				File desTrainDir = new File(desDir + foldIndex + "\\train data\\");
				desTrainDir.mkdir();
			}
			for(int sourceIndex = 0; sourceIndex < sourcesubs.length; sourceIndex ++){
				String sourceSub = sourcesubs[sourceIndex];
				String sourceName = sourcenames[sourceIndex];
				String sourceTest = sourceDir + sourceSub + "\\" + sourceName + foldIndex + "\\" + "group" + foldIndex + ".txt";
				String desTest = desDir + foldIndex + "\\" + "test data\\" + sourceSub + ".txt";
				
				File sourceTestFile = new File(sourceTest);
				sourceTestFile.renameTo(new File(desTest));
				
				String sourceTrain = sourceDir + sourceSub + "\\" + sourceName + foldIndex + "\\" + "others" + foldIndex + ".txt";
				String desTrain = desDir + foldIndex + "\\" + "train data\\" + sourceSub + ".txt";
				
				File sourceTrainFile = new File(sourceTrain);
				sourceTrainFile.renameTo(new File(desTrain));
			}
			
			String keywordsDes = desDir + foldIndex + "\\keywords.txt";
			FileUtils.copyFile(keywordsPath, keywordsDes);
		}
	}
}
