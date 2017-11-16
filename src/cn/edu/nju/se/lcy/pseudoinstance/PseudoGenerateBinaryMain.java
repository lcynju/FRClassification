package cn.edu.nju.se.lcy.pseudoinstance;

import java.io.IOException;

import cn.edu.nju.se.lcy.generate.Generate4Binary;

public class PseudoGenerateBinaryMain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filePath = "C:\\Users\\Chuanyi Li\\Desktop\\Experiments-0707\\tfidfnewpseudo\\Binary\\";
		String classStrings = "security,reliability,performance,softwareinterface,lifecycle,usability,capability";
		
		Generate4Binary.duplicate4Binary(filePath, "g", 5, classStrings, ".arff", false);
		
		filePath = "C:\\Users\\Chuanyi Li\\Desktop\\Experiments-0707\\tfidfnewpseudo\\BinaryPositive\\";
		Generate4Binary.duplicate4Binary(filePath, "g", 5, classStrings, ".arff", true);
	}

}
