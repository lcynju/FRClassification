package cn.edu.nju.se.lcy.mains.notest;

import cn.edu.nju.se.lcy.generate.Organize4Weka;

public class OrganizeMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String rootPath = args[0];
		String destFolder = args[1];
		String type = args[2];
		String trueName = "trueClasses.txt";
		String classes = "security,reliability,performance,softwareinterface,lifecycle,usability,capability";

		Organize4Weka o4w = new Organize4Weka(rootPath, destFolder, trueName, classes);
		// o4w.startSingle("nominal");
		// o4w.startSingle("numeric");
		if (type.contains("-")) {
			o4w.startMulti(type);// -numeric");
		} else {
			o4w.startSingle(type);
		}
		// o4w.startMulti("numeric-numeric-numeric");
	}
}
