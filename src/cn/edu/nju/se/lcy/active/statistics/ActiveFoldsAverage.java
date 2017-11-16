package cn.edu.nju.se.lcy.active.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.nju.se.lcy.active.util.MultiValueIndexItem;
import cn.edu.nju.se.lcy.active.util.NextInstanceComparator;

public class ActiveFoldsAverage {
//直接计算曲线值，不需要执行其它的函数
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int totalLineNumber = 794;
		
		String rootFolder = "E:\\JSS\\Active Learning\\Winmerge\\";
		String fileFolderPre =rootFolder + "Active Learning Fold";
		String activeType = "active learning";
		
		//这里计算了单独的folds，直接运行这个函数就可以了。
		ActiveSingleFoldStatistic.main(new String[]{fileFolderPre, activeType});
		
		String averageResultsFile = rootFolder + activeType + "-average-results.txt";
		String averageIndexFile = rootFolder + activeType + "-average-index.txt";
		
		List<BufferedReader> resultsReaders = new ArrayList<BufferedReader>();
		List<List<MultiValueIndexItem>> allResults = new ArrayList<List<MultiValueIndexItem>>();
		
		for (int i = 0; i < 5; i++) {
			String fileFolder = fileFolderPre + i + "\\" + activeType + "\\";
			String allResultFile = fileFolder + "all-results.txt";
			BufferedReader resultReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(allResultFile)));
			resultsReaders.add(resultReader);
		}
		
		BufferedWriter averageWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(averageResultsFile)));
		BufferedWriter allOrderWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(averageIndexFile)));
		
		String line = "";
		for (int i = 0; i < 5; i++) {
			line = resultsReaders.get(i).readLine();
		}
		int columNumber = line.split("\t").length;
		for(int columIndex = 0; columIndex < columNumber; columIndex ++){
			List<MultiValueIndexItem> resultList = new ArrayList<MultiValueIndexItem>();
			allResults.add(resultList);
		}
		averageWriter.append(line);
		allOrderWriter.append(line);
		averageWriter.newLine();
		averageWriter.flush();
		allOrderWriter.newLine();
		allOrderWriter.flush();
		
		int lineNumber = 0;
		while(lineNumber < totalLineNumber){
			List<Double> total = new ArrayList<Double>();
			for (int i = 0; i < 5; i++) {
				line = resultsReaders.get(i).readLine();
				String[] singleLine = line.split("\t");
				if(total.size() == 0){
					for(int j = 0; j < singleLine.length; j ++){
						total.add(Double.parseDouble(singleLine[j]));
					}
				}else{
					for(int j = 0; j < singleLine.length; j ++){
						total.set(j, total.get(j) + Double.parseDouble(singleLine[j]));
					}
				}
			}
			for(int j = 0; j < total.size(); j ++){
				double average = total.get(j) / 5;
				averageWriter.append(average + "\t");
				MultiValueIndexItem mvii = new MultiValueIndexItem(lineNumber);
				mvii.setMax(average);
				allResults.get(j).add(mvii);
			}
			averageWriter.newLine();
			averageWriter.flush();
			
			lineNumber ++;
		}
		
		for (int i = 0; i < 5; i++) {
			resultsReaders.get(i).close();
		}
		
		for (int curveIndex = 0; curveIndex < columNumber; curveIndex++) {
			NextInstanceComparator maxComparator = new NextInstanceComparator("max");
			Collections.sort(allResults.get(curveIndex), maxComparator);
		}
		
		lineNumber = 0;
		while(lineNumber < totalLineNumber){
			for (int curveIndex = 0; curveIndex < columNumber; curveIndex++) {
				allOrderWriter.append(allResults.get(curveIndex).get(totalLineNumber - 1 - lineNumber).index + "\t");
			}
			allOrderWriter.newLine();
			allOrderWriter.flush();
			lineNumber++;
		}
		
		averageWriter.close();
		allOrderWriter.close();
	}

}
