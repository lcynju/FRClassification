package cn.edu.nju.se.lcy.learn;

public class Classify {

	public static double binaryClassify(String filePath, String algorithmName, int folds, int classNum, String fileExtension, String[] option, String groupName)
			throws Exception {
		Learner nblb = new BinaryLearner(filePath, fileExtension, folds, classNum, option, groupName);
		return nblb.start(algorithmName);
	}

	public static void multiClassify(String filePath, String sourceFile, boolean duplicated, String algorithmName,
			int folds, String fileExtension, String[] option) {
		Learner nblm = new AutoMultiLearner(filePath, sourceFile, folds, fileExtension, option);
		nblm.start(algorithmName);
	}
	
	public static void multiManualClassify(String filePath, String algorithmName,
			int folds, String fileExtension, String[] option, String groupName) {
		Learner nblm = new ManualMultiLearner(filePath, fileExtension, folds, option, groupName);
		nblm.start(algorithmName);
	}
	
	public static double duplicateMultiClassify(String filePath, String algorithmName,
			int folds, String fileExtension, String[] option, String groupName) {
		Learner nblm = new ManualMultiLearner(filePath, fileExtension, folds, option, groupName);
		return nblm.start(algorithmName);
	}
	
	public static boolean checkAlgorithm(String algorithmName) {
		String supportNames = ",naivebayes,naivebayesmultinomial,smo,libsvm,";
		if (supportNames.contains("," + algorithmName + ",")) {
			return true;
		}
		return false;
	}

	public static void conductReservedTest(String groupParentFolder, String algorithmName, String[] option, int totalFolds, String groupName, int classNumber, String fileExtension) {
		// TODO Auto-generated method stub
		if(groupParentFolder.contains("Binary")){
			Learner nblb = new BinaryLearner(groupParentFolder, fileExtension, totalFolds, classNumber, option, groupName);
			nblb.startSingle(algorithmName);
		}else{
			Learner nblm = new ManualMultiLearner(groupParentFolder, fileExtension, totalFolds, option, groupName);
			nblm.startSingle(algorithmName);
		}
	}
}
