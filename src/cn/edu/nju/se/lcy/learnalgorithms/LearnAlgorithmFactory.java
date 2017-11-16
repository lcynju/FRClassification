package cn.edu.nju.se.lcy.learnalgorithms;

public class LearnAlgorithmFactory {

	public static LearnAlgorithm createAlgorithm(String algorithmName){
		if(algorithmName.equals("naivebayes")){
			return new NaiveBayesDo();
		}
		if(algorithmName.equals("naivebayesmultinomial")){
			return new NaiveBayesMultinomialDo();
		}
		if(algorithmName.equals("libsvm")){
			return new LibSVMDo();
		}
		return null;
	}
	
}
