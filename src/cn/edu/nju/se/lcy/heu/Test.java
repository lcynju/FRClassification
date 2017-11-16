package cn.edu.nju.se.lcy.heu;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) throws Exception{
//		String[] hArgs = new String[]{"E:\\Mumble Experiments\\Experiments 0813\\", "source data.txt", "pos.txt", "heuristicrules.txt"};
//		
//		RuleValueCalculator.main(hArgs);
		for(int i = 0; i < 100; i ++){
			System.out.println(i * 0.01);
			System.out.println(1 / Math.exp(i * 0.01));
		}
	}
}
