package cn.edu.nju.se.lcy.mains.notest;

import cn.edu.nju.se.lcy.extract.CalculateTFIDF;
import cn.edu.nju.se.lcy.extract.ExtractSentences;
import cn.edu.nju.se.lcy.extract.KeywordCounts;
import cn.edu.nju.se.lcy.extract.KeywordUnigram;
import cn.edu.nju.se.lcy.extract.WordUnigram;
import cn.edu.nju.se.lcy.heu.RuleValueCalculator;

public class ExtractMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		//String filePath = "C:\\Users\\Chuanyi Li\\Desktop\\Experiments-0717\\";
		String filePath = args[0]; //"E:\\All Independent Keywords Experiments\\";
		
		/**定义好两种输入文件名称：
		 * 1. 处理后的request文件“proposed.txt”，每一行就是request内容，单词用‘ ’隔开，没有句子概念;
		 * 2. keywords文件：每一行是一类需求的关键词，类别和词用‘:’隔开，词之间用‘ ’隔开**/
		String sourceFileName = "processed.txt";
		//String keywordnpsFileName = "keywordsnps.txt";
		String keywordFileName1 = "keywords1.txt";
		String keywordFileName2 = "keywords2.txt";
		String keywordFileName3 = "keywords3.txt";
		String keywordFileName4 = "keywords4.txt";
		String keywordFileName5 = "keywords5.txt";
		//String adjkeywordnpsFileName = "adjkeywordsnps.txt";
		//String adjkeywordFileName = "adjkeywords.txt";
		
		/**下面都是单个特征文件的名称**/
		String tfidfDesName = "tfidf.txt";
		//String wordUnigramDestName = "wordunigram.txt";
		
//		String keywordCountNPSDestName = "keywordcountsnps.txt";
//		String keywordUnigramNPSDestName = "keywordunigramnps.txt";
		
		String keywordCountDestName1 = "keywordcounts1.txt";
		String keywordCountDestName2 = "keywordcounts2.txt";
		String keywordCountDestName3 = "keywordcounts3.txt";
		String keywordCountDestName4 = "keywordcounts4.txt";
		String keywordCountDestName5 = "keywordcounts5.txt";
		//String keywordUnigramDestName = "keywordunigram.txt";
		
//		String adjkeywordCountNPSDestName = "adjkeywordcountsnps.txt";
//		String adjkeywordUnigramNPSDestName = "adjkeywordunigramnps.txt";
		
		//String adjkeywordCountDestName = "adjkeywordcounts.txt";
		//String adjkeywordUnigramDestName = "adjkeywordunigram.txt";
		
		String heuristicDesName = "heuristicrules.txt";
		
//		String ruleOutName = "withRules-reliability.txt";
//		String remainName = "withoutRules-reliability.txt";
//		String[] rules = new String[]{"but", "口no"};

		/**计算TFIDF**/
		//CalculateTFIDF.startSingleFile(filePath, tfidfDesName, sourceFileName, false, 1000);
//		CalculateTFIDF.writeIDF(CalculateTFIDF.generateIDF(filePath, sourceFileName, "", " "), filePath + destName);
//		CalculateTFIDF.startWithIDF(CalculateTFIDF.generateIDF(filePath, "processed-all.txt", "", " "), filePath, sourceFileName, false, "", " ");
		
		/**计算word unigram**/
		//WordUnigram wu = new WordUnigram(filePath, sourceFileName, wordUnigramDestName);
		//wu.start(false);
		
		/**计算关键词count和unigram；传入keywords文件名并定义对应的输出文件，
		 * 以下两次调用代表计算了两种keywords列表对应的不同特征值文件。
		 * **/
//		KeywordCounts kcnps = new KeywordCounts(filePath, keywordnpsFileName, sourceFileName, keywordCountNPSDestName);
//		kcnps.start();
//		KeywordUnigram kwunps = new KeywordUnigram(filePath, keywordnpsFileName, sourceFileName, keywordUnigramNPSDestName);
//		kwunps.startSingleWord();
		
		//KeywordCounts kc1 = new KeywordCounts(filePath, keywordFileName1, sourceFileName, keywordCountDestName1);
		//kc1.start();
		KeywordCounts kc2 = new KeywordCounts(filePath, keywordFileName2, sourceFileName, keywordCountDestName2);
		//kc2.start();
		KeywordCounts kc3 = new KeywordCounts(filePath, keywordFileName3, sourceFileName, keywordCountDestName3);
		//kc3.start();
		KeywordCounts kc4 = new KeywordCounts(filePath, keywordFileName4, sourceFileName, keywordCountDestName4);
		//kc4.start();
		KeywordCounts kc5 = new KeywordCounts(filePath, keywordFileName5, sourceFileName, keywordCountDestName5);
		//kc5.start();
		//KeywordUnigram kwu = new KeywordUnigram(filePath, keywordFileName, sourceFileName, keywordUnigramDestName);
		//kwu.startSingleWord();
		
		/**用调整后的keywords计算特征**/
//		KeywordCounts adjkcnps = new KeywordCounts(filePath, adjkeywordnpsFileName, sourceFileName, adjkeywordCountNPSDestName);
//		adjkcnps.start();
//		KeywordUnigram adjkwunps = new KeywordUnigram(filePath, adjkeywordnpsFileName, sourceFileName, adjkeywordUnigramNPSDestName);
//		adjkwunps.startSingleWord();
		
		//KeywordCounts adjkc = new KeywordCounts(filePath, adjkeywordFileName, sourceFileName, adjkeywordCountDestName);
		//adjkc.start();
		//KeywordUnigram adjkwu = new KeywordUnigram(filePath, adjkeywordFileName, sourceFileName, adjkeywordUnigramDestName);
		//adjkwu.startSingleWord();
		
		/**计算启发式值，传入：
		 * 1. source data.txt文件：一行代表一个request，词语用空格隔开，句子用.加空格隔开
		 * 2. pos.txt文件：一行对应一个request，元素使用\t隔开，包括句点也是用\t隔开**/
		String[] hArgs = new String[]{filePath, "source data.txt", "pos.txt", heuristicDesName};
		RuleValueCalculator.main(hArgs);
			
//		ExtractSentences.extractline(filePath + sourceFileName, filePath + ruleOutName, filePath + remainName, rules);
	}

}
