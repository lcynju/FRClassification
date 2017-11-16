package cn.edu.nju.se.lcy.learn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CalculateResult {
	
	public static double calculateResults(String filePath, String outFile) throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter resultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
		
		List<String> classNames = new ArrayList<String>();

		List<List<Integer>> counter = new ArrayList<List<Integer>>();
		List<Integer> eachTrueNumber = new ArrayList<Integer>();
		List<Integer> eachHypNumber = new ArrayList<Integer>();

		int lineNumber = generateFromFile(filePath, classNames, counter, eachTrueNumber, eachHypNumber);
		
		// print the counter for check if it is right
		for (List<Integer> subCounter : counter) {
			for (Integer number : subCounter) {
				resultWriter.append(number + ",");
			}
			resultWriter.newLine();
			resultWriter.flush();
		}

		// print each total true and total hypthesis
		for (int classIndex = 0; classIndex < classNames.size(); classIndex++) {
			resultWriter.append(classNames.get(classIndex) + ":" + eachTrueNumber.get(classIndex) + ";"
					+ eachHypNumber.get(classIndex));
			resultWriter.newLine();
			resultWriter.flush();
		}
			
		// calculate F-measures, G-means and MAUC
		double FMeasure = 0, GMeans = 1, MAUC = 0;
		double totalCorrect = 0;
		for (int indexI = 0; indexI < classNames.size(); indexI++) {
			totalCorrect += counter.get(indexI).get(indexI);
			double recallI = ((double) counter.get(indexI).get(indexI)) / eachTrueNumber.get(indexI);
			double precisionI = ((double) counter.get(indexI).get(indexI)) / eachHypNumber.get(indexI);

			double tmpF = 0;
			if(recallI > 0 && precisionI > 0){
				tmpF = 2 * recallI * precisionI / (recallI + precisionI);
			}
			resultWriter.append(classNames.get(indexI)+ "\t" + precisionI + "\t" + recallI + "\t" + tmpF);
			resultWriter.newLine();
			resultWriter.flush();
			// what if precision and recall equals 0???
			// FMeasure += 2 * precisionI * recallI / (precisionI + recallI);
			// GMeans *= recallI;

			for (int indexJ = indexI + 1; indexJ < classNames.size(); indexJ++) {
				double ii = counter.get(indexI).get(indexI);
				double ij = counter.get(indexJ).get(indexI);
				double ji = counter.get(indexI).get(indexJ);
				double jj = counter.get(indexJ).get(indexJ);

				double ijTR = 0.0, jiFR = 0.0, ijFR = 0.0, jiTR = 0.0;
				if ((ii + ij) != 0) {
					ijTR = ii / (ii + ij);
					jiFR = ij / (ii + ij);
				}
				if ((ji + jj) != 0) {
					ijFR = ji / (ji + jj);
					jiTR = jj / (ji + jj);
				}

				double aij = (ijTR * ijFR) / 2 + (1 + ijTR) * (1 - ijFR) / 2;
				double aji = (jiTR * jiFR) / 2 + (1 + jiTR) * (1 - jiFR) / 2;

				MAUC += (aij + aji) / 2;
			}
		}

		FMeasure = FMeasure / ((double) classNames.size());
		GMeans = Math.pow(GMeans, 1 / classNames.size());
		MAUC = 2 / ((double) classNames.size() - 1) / ((double) classNames.size()) * MAUC;

		double accuracy = totalCorrect / lineNumber;
		resultWriter.append("F-measure Total:" + FMeasure + "\r\n");
		resultWriter.append("G-means:" + GMeans + "\r\n");
		resultWriter.append("MAUC:" + MAUC + "\r\n");
		resultWriter.append("Accuracy:" + accuracy);

		resultWriter.flush();
		resultWriter.close();
		
		return accuracy;
	}

	public static int generateFromFile(String filePath, List<String> classNames, List<List<Integer>> counter,
			List<Integer> eachTrueNumber, List<Integer> eachHypNumber) throws IOException {
		// the input file should have two columns, the first one is 'true
		// class', the second one is 'hypothesis'
		BufferedReader brTAH = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

		String line = "";
		int lineNumber = 0;
		while ((line = brTAH.readLine()) != null) {
			line = line.toLowerCase();
			line = line.replaceAll(" ", "");
			lineNumber ++;
			String[] classes = line.split("\t");

			for (int classIndex = 0; classIndex < 2; classIndex++) {
				if (!classNames.contains(classes[classIndex])) {
					classNames.add(classes[classIndex]);
					for (int counterIndex = 0; counterIndex < counter.size(); counterIndex++) {
						counter.get(counterIndex).add(0);
					}
					List<Integer> subCounter = new ArrayList<Integer>();
					for (int newLineIndex = 0; newLineIndex < classNames.size(); newLineIndex++) {
						subCounter.add(0);
					}
					counter.add(subCounter);

					eachTrueNumber.add(0);
					eachHypNumber.add(0);
				}
			}

			int indexOfTrue = classNames.indexOf(classes[0]);
			int indexOfHyp = classNames.indexOf(classes[1]);

			int formerCount = counter.get(indexOfHyp).get(indexOfTrue);
			counter.get(indexOfHyp).set(indexOfTrue, formerCount + 1);

			int formerTrueNumber = eachTrueNumber.get(indexOfTrue);
			int formerHypNumber = eachHypNumber.get(indexOfHyp);
			eachTrueNumber.set(indexOfTrue, formerTrueNumber + 1);
			eachHypNumber.set(indexOfHyp, formerHypNumber + 1);
		}

		brTAH.close();
		
		return lineNumber;
	}
}
