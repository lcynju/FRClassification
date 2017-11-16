package cn.edu.nju.se.lcy.generate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GenerateNominalSet {

	public static List<Set<String>> generateNominal(String sourceFile){
		List<Set<String>> nominalSetList = new ArrayList<Set<String>>();
		try {
			BufferedReader sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
			String line = "";
			boolean isFirstLine = true;
			while((line = sourceReader.readLine()) != null && !line.equals("")){
				String[] features = line.split("\t");
				if(isFirstLine){
					for(String featureValue : features){
						Set<String> featureNominal = new TreeSet<String>();
						featureNominal.add(featureValue);
						nominalSetList.add(featureNominal);
					}
					isFirstLine = false;
				}
				for(int featureIndex = 0; featureIndex < features.length; featureIndex ++){
					nominalSetList.get(featureIndex).add(features[featureIndex]);
				}
			}
			sourceReader.close();
			
			return nominalSetList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
