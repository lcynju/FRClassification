package cn.edu.nju.se.lcy.active.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.nju.se.lcy.active.util.MultiValueIndexItem;
import cn.edu.nju.se.lcy.active.util.NextInstanceComparator;

public class ActiveSingleFoldStatistic {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int totalLineNumber = 794;
		//String fileFolderPre = "E:\\JSS\\Active Learning\\KeePass\\Active Learning Fold";
		String fileFolderPre = args[0];
		String activeType = args[1];
		for (int i = 0; i < 5; i++) {
			String fileFolder = fileFolderPre + i + "\\" + activeType + "\\";
			String allResultFile = fileFolder + "all-results.txt";
			String allOrderOfBigIndex = fileFolder + "all-order-of-bigindex.txt";
			String allOrderOfBig = fileFolder + "all-order-of-big.txt";

			List<BufferedReader> curveReaders = new ArrayList<BufferedReader>();
			List<List<MultiValueIndexItem>> allResults = new ArrayList<List<MultiValueIndexItem>>();
			BufferedWriter allResultWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(allResultFile)));
			BufferedWriter allOrderWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(allOrderOfBigIndex)));
			BufferedWriter allOrderBigWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(allOrderOfBig)));

			File parent = new File(fileFolder);
			for (File file : parent.listFiles()) {
				if (!file.isDirectory()) {
					if (file.getName().contains("-curve")) {
						BufferedReader curveReader = new BufferedReader(
								new InputStreamReader(new FileInputStream(file)));
						curveReaders.add(curveReader);

						List<MultiValueIndexItem> resultList = new ArrayList<MultiValueIndexItem>();
						allResults.add(resultList);

						String title = file.getName().split("\\.")[0];
						title = title.replaceAll("heuristicrules", "hr");
						title = title.replaceAll("keywordcounts", "kc");
						title = title.replaceAll("tfidf", "ti");
						title = title.replaceAll("-numeric", "");
						title = title.replaceAll("-curve", "");
						title = title.replaceAll("gapwithlowhigh", "gwlh");
						title = title.replaceAll("lowinhigh", "lh");
						title = title.replaceAll("pseudo", "p");
						allResultWriter.append(title + "\t");
						allOrderWriter.append(title + "\t");
						allOrderBigWriter.append(title + "\t");
					}
				}
			}
			allResultWriter.newLine();
			allOrderWriter.newLine();
			allOrderBigWriter.newLine();
			allResultWriter.flush();
			allOrderWriter.flush();
			allOrderBigWriter.flush();

			String resultLine = "";
			int lineNumber = 0;
			while (lineNumber < totalLineNumber) {
				for (int curveIndex = 0; curveIndex < curveReaders.size(); curveIndex++) {
					resultLine = curveReaders.get(curveIndex).readLine();
					allResultWriter.append(resultLine + "\t");

					MultiValueIndexItem glhii = new MultiValueIndexItem(lineNumber);
					glhii.setMax(Double.parseDouble(resultLine));
					allResults.get(curveIndex).add(glhii);
				}
				allResultWriter.newLine();
				allResultWriter.flush();
				lineNumber++;
			}

			for (int curveIndex = 0; curveIndex < curveReaders.size(); curveIndex++) {
				NextInstanceComparator maxComparator = new NextInstanceComparator("max");
				Collections.sort(allResults.get(curveIndex), maxComparator);

				curveReaders.get(curveIndex).close();
			}

			lineNumber = 0;
			while (lineNumber < totalLineNumber) {
				for (int curveIndex = 0; curveIndex < curveReaders.size(); curveIndex++) {
					allOrderWriter.append(allResults.get(curveIndex).get(totalLineNumber -1 - lineNumber).index + "\t");
					allOrderBigWriter.append(allResults.get(curveIndex).get(totalLineNumber - 1 - lineNumber).getMax() + "\t");
				}
				allOrderWriter.newLine();
				allOrderBigWriter.newLine();
				allOrderWriter.flush();
				allOrderBigWriter.flush();
				lineNumber++;
			}

			allResultWriter.close();
			allOrderWriter.close();
			allOrderBigWriter.close();
		}
	}

}
