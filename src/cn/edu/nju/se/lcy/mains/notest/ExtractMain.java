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
		
		/**��������������ļ����ƣ�
		 * 1. ������request�ļ���proposed.txt����ÿһ�о���request���ݣ������á� ��������û�о��Ӹ���;
		 * 2. keywords�ļ���ÿһ����һ������Ĺؼ��ʣ����ʹ��á�:����������֮���á� ������**/
		String sourceFileName = "processed.txt";
		//String keywordnpsFileName = "keywordsnps.txt";
		String keywordFileName1 = "keywords1.txt";
		String keywordFileName2 = "keywords2.txt";
		String keywordFileName3 = "keywords3.txt";
		String keywordFileName4 = "keywords4.txt";
		String keywordFileName5 = "keywords5.txt";
		//String adjkeywordnpsFileName = "adjkeywordsnps.txt";
		//String adjkeywordFileName = "adjkeywords.txt";
		
		/**���涼�ǵ��������ļ�������**/
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
//		String[] rules = new String[]{"but", "��no"};

		/**����TFIDF**/
		//CalculateTFIDF.startSingleFile(filePath, tfidfDesName, sourceFileName, false, 1000);
//		CalculateTFIDF.writeIDF(CalculateTFIDF.generateIDF(filePath, sourceFileName, "", " "), filePath + destName);
//		CalculateTFIDF.startWithIDF(CalculateTFIDF.generateIDF(filePath, "processed-all.txt", "", " "), filePath, sourceFileName, false, "", " ");
		
		/**����word unigram**/
		//WordUnigram wu = new WordUnigram(filePath, sourceFileName, wordUnigramDestName);
		//wu.start(false);
		
		/**����ؼ���count��unigram������keywords�ļ����������Ӧ������ļ���
		 * �������ε��ô������������keywords�б��Ӧ�Ĳ�ͬ����ֵ�ļ���
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
		
		/**�õ������keywords��������**/
//		KeywordCounts adjkcnps = new KeywordCounts(filePath, adjkeywordnpsFileName, sourceFileName, adjkeywordCountNPSDestName);
//		adjkcnps.start();
//		KeywordUnigram adjkwunps = new KeywordUnigram(filePath, adjkeywordnpsFileName, sourceFileName, adjkeywordUnigramNPSDestName);
//		adjkwunps.startSingleWord();
		
		//KeywordCounts adjkc = new KeywordCounts(filePath, adjkeywordFileName, sourceFileName, adjkeywordCountDestName);
		//adjkc.start();
		//KeywordUnigram adjkwu = new KeywordUnigram(filePath, adjkeywordFileName, sourceFileName, adjkeywordUnigramDestName);
		//adjkwu.startSingleWord();
		
		/**��������ʽֵ�����룺
		 * 1. source data.txt�ļ���һ�д���һ��request�������ÿո������������.�ӿո����
		 * 2. pos.txt�ļ���һ�ж�Ӧһ��request��Ԫ��ʹ��\t�������������Ҳ����\t����**/
		String[] hArgs = new String[]{filePath, "source data.txt", "pos.txt", heuristicDesName};
		RuleValueCalculator.main(hArgs);
			
//		ExtractSentences.extractline(filePath + sourceFileName, filePath + ruleOutName, filePath + remainName, rules);
	}

}
