package cn.edu.nju.se.lcy.active.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.se.lcy.pseudoinstance.GeneratePseudoInstance;

public class CalculateKeywordValues {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String fileFolderPre = "E:\\Mumble Experiments\\Active Learning Fold";
		for (int i = 0; i < 5; i++) {
			String fileFolder = fileFolderPre + i + "\\";
			String trainFolder = "train data\\";
			String[] folders = new String[] { trainFolder };

			for (String folder : folders) {
				String sourceFile = fileFolder + folder + "keywordcounts.txt";

				String destKeyCountsFile = fileFolder + folder + "keyCountsValues.txt";
				
				BufferedReader keywordCountsReader = new BufferedReader(
						new InputStreamReader(new FileInputStream(sourceFile)));
				BufferedWriter keyCountsValueWriter = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(destKeyCountsFile)));
				
				String line = "";
				while((line = keywordCountsReader.readLine()) != null){
					if(!line.equals("")){
						String[] numbers = line.split("\t");
						int zeros = 0;
						int nonZeroSum = 0;
						List<Integer> nonZero = new ArrayList<Integer>();
						for(String number : numbers){
							if(number.equals("0")){
								zeros ++;
							}else{
								int tmpNumber = Integer.parseInt(number);
								nonZero.add(tmpNumber);
								nonZeroSum += tmpNumber;
							}
						}
						double nonZeroAve = (double) nonZeroSum / nonZero.size();
						double nonZeroVar = 0.0;
						for(int tmpNumber : nonZero){
							nonZeroVar += (tmpNumber - nonZeroAve) * (tmpNumber - nonZeroAve);
						}
						
						keyCountsValueWriter.append(zeros + "\t" + nonZeroVar);
						keyCountsValueWriter.newLine();
						keyCountsValueWriter.flush();
					}
				}
				
				keyCountsValueWriter.close();
				keywordCountsReader.close();
				
				File file = new File(destKeyCountsFile);
				file.renameTo(new File(fileFolder + "active learning\\" + "keyCountsValues.txt"));
			}
		}
	}

}
