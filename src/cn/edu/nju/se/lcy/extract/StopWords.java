package cn.edu.nju.se.lcy.extract;

import java.util.Arrays;
import java.util.TreeSet;

import edu.northwestern.at.morphadorner.corpuslinguistics.lemmatizer.EnglishLemmatizer;

public class StopWords {

	private static String stopwords = ",am,are,is,were,was,you,your,yours,us,our,they,their,he,his,she,"
				+ "her,him,the,there,this,here,to,of,some,a,an,many,much,not,about,and,or,it,"
				+ "its,how,hope,can,that,what,on,for,more,I,as,also,if,when,which,who,where,"
				+ "several,on,out,would,should,shall,will,can,could,why,but,very,just,have,has,"
				+ "need,up,other,with,one,do,does,did,done,hi,hello,thank,thanks,great,already,";
		
		

	public static TreeSet<String> removeStopWords(TreeSet<String> featureSet) throws Exception {
		String[] stopWordsArray = StopWords.stopwords.split(",");
		EnglishLemmatizer rbl = new EnglishLemmatizer();
		TreeSet<String> stopWordSet = new TreeSet<String>();
		stopWordSet.addAll(Arrays.asList(stopWordsArray));
		for (String stopWord : stopWordSet) {
			stopWord = rbl.lemmatize(stopWord);
			featureSet.remove(stopWord);
		}
		return featureSet;
	}

	public static boolean contains(String word) {
		// TODO Auto-generated method stub
		if(StopWords.stopwords.contains(","+word+",")){
			return true;
		}
		return false;
	}
	
}
