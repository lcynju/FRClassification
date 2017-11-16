package cn.edu.nju.se.lcy.active;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.nju.se.lcy.active.util.MultiValueIndexItem;
import cn.edu.nju.se.lcy.active.util.NextInstanceListManager;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class DataSetOperatorPseudo {

	public static double train(ArrayList<Attribute> attributeList, Instances trainData, List<Instance> trainPseudo,
			Instances testData, Map<Instance, Instance> leftPseudo, String[][] options, String type,
			Instances totalTestData, boolean duplicate) {
		try {
			Instances tmpTrain = new Instances("tmpTrain", attributeList, trainData.size() + trainPseudo.size());
			tmpTrain.addAll(trainData.subList(0, trainData.size()));
			tmpTrain.addAll(trainPseudo);
			tmpTrain.setClassIndex(tmpTrain.numAttributes() - 1);
			LibSVM bestClassifier = null;
			double maxAccuracy = -0.1;
			for (String[] option : options) {
				LibSVM classifier = new LibSVM();
				classifier.setOptions(option.clone());

				classifier.buildClassifier(tmpTrain);
				Evaluation eval = new Evaluation(tmpTrain);
				eval.evaluateModel(classifier, totalTestData);
				if (eval.pctCorrect() > maxAccuracy) {
					maxAccuracy = eval.pctCorrect();
					bestClassifier = classifier;
				}
			}

			Evaluation newEval = new Evaluation(tmpTrain);
			newEval.evaluateModel(bestClassifier, testData);

			Instance selected = null;
			if (type.equals("lowinhigh")) {
				selected = updateDataSetLowInHigh(trainData, testData, newEval);
			} else if (type.equals("gap")) {
				selected = updateDataSetMinGap(trainData, testData, newEval, 1, 2);
			} else if(type.equals("gapwithlowhigh")){
				selected = updateDataSetGapWithLowHigh(trainData, testData, newEval, 1, 2);
			} else if(type.equals("twogap")){
				selected = updateDataSetTwoGap(trainData, testData, newEval);
			}else{
				selected = randomlyGetNext(trainData, testData);
			}

			updatePseudo(selected, leftPseudo, trainPseudo, duplicate);

			return maxAccuracy;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
	}

	private static void updatePseudo(Instance selected, Map<Instance, Instance> leftPseudo,
			List<Instance> trainPseudo, boolean duplicate) {
		// TODO Auto-generated method stub
		if (leftPseudo.containsKey(selected)) {
			Instance toBeAdded = leftPseudo.remove(selected);
			trainPseudo.add(toBeAdded);
			if(duplicate){
				String className = selected.classAttribute().value((int)selected.classValue());
				if(className.equals("lifecycle") || className.equals("performance") || className.equals("reliability")){
					Instance dupPseudo = toBeAdded.copy(toBeAdded.toDoubleArray());
					Instance dupSelceted = selected.copy(selected.toDoubleArray());
					trainPseudo.add(dupSelceted);
					trainPseudo.add(dupPseudo);
				}
			}
		}
	}

	private static Instance randomlyGetNext(Instances trainData, Instances testData) {
		// TODO Auto-generated method stub
		int instanceSize = testData.size();
		int randomIndex = (int) (Math.random() * instanceSize);
		Instance selected = testData.remove(randomIndex);
		int trainSize = trainData.size();
		int insertIndex = (int) (Math.random() * trainSize);
		trainData.add(insertIndex, selected);
		return selected;
	}

	private static Instance updateDataSetMinGap(Instances trainData, Instances testData, Evaluation bestEvaluation, int gapFirst, int gapSecond) {
		// TODO Auto-generated method stub
		double minProbabilityGap = 1.0;
		int minGapInstanceIndex = 0;
		for (int instanceIndex = 0; instanceIndex < bestEvaluation.predictions().size(); instanceIndex++) {
			Prediction p = bestEvaluation.predictions().get(instanceIndex);
			NominalPrediction p1 = (NominalPrediction) p;
			double[] distribution = p1.distribution();
			Arrays.sort(distribution);
			double max = distribution[distribution.length - gapFirst];
			double secondMax = distribution[distribution.length - gapSecond];
			double tmpGap = max - secondMax;
			if (tmpGap < minProbabilityGap) {
				minProbabilityGap = tmpGap;
				minGapInstanceIndex = instanceIndex;
			}
		}
		Instance selected = testData.remove(minGapInstanceIndex);
		int trainSize = trainData.size();
		int insertIndex = (int) (Math.random() * trainSize);
		trainData.add(insertIndex, selected);
		return selected;
	}

	private static Instance updateDataSetTwoGap(Instances trainData, Instances testData, Evaluation bestEvaluation) {
		// TODO Auto-generated method stub
		double minProbabilityGap = 1.0;
		int minGapInstanceIndex = 0;
		for (int instanceIndex = 0; instanceIndex < bestEvaluation.predictions().size(); instanceIndex++) {
			Prediction p = bestEvaluation.predictions().get(instanceIndex);
			NominalPrediction p1 = (NominalPrediction) p;
			double[] distribution = p1.distribution();
			Arrays.sort(distribution);
			double max = distribution[distribution.length - 1];
			double secondMax = distribution[distribution.length - 2];
			double thirdMax = distribution[distribution.length - 3];
			double tmpGap = max - secondMax;
			if(thirdMax > 0){
				tmpGap = max - thirdMax;
			}
			if (tmpGap < minProbabilityGap) {
				minProbabilityGap = tmpGap;
				minGapInstanceIndex = instanceIndex;
			}
		}
		Instance selected = testData.remove(minGapInstanceIndex);
		int trainSize = trainData.size();
		int insertIndex = (int) (Math.random() * trainSize);
		trainData.add(insertIndex, selected);
		return selected;
	}
	
	private static Instance updateDataSetLowInHigh(Instances trainData, Instances testData, Evaluation bestEvaluation) {
		// TODO Auto-generated method stub
		double minProbability = 1.0;
		int minInstanceIndex = 0;
		for (int instanceIndex = 0; instanceIndex < bestEvaluation.predictions().size(); instanceIndex++) {
			Prediction p = bestEvaluation.predictions().get(instanceIndex);
			NominalPrediction p1 = (NominalPrediction) p;
			int classIndex = (int) p.predicted();
			double tmpProbability = p1.distribution()[classIndex];
			if (tmpProbability < minProbability) {
				minProbability = tmpProbability;
				minInstanceIndex = instanceIndex;
			}
		}
		Instance selected = testData.remove(minInstanceIndex);
		int trainSize = trainData.size();
		int insertIndex = (int) (Math.random() * trainSize);
		trainData.add(insertIndex, selected);
		return selected;
	}

	public static Map<Instance, Instance> generateNorPsePair(Instances normalInstances, Instances pseudoInstances,
			String normalPseudoPairFile) {
		// TODO Auto-generated method stub
		try {
			Map<Instance, Instance> normalPseudoPairMap = new HashMap<Instance, Instance>();
			BufferedReader pairIndexReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(normalPseudoPairFile)));
			String pairLine = "";
			while ((pairLine = pairIndexReader.readLine()) != null) {
				if (!pairLine.equals("")) {
					String[] indexs = pairLine.split("\t");
					int normalIndex = Integer.parseInt(indexs[0]);
					int pseudoIndex = Integer.parseInt(indexs[1]);
					normalPseudoPairMap.put(normalInstances.get(normalIndex), pseudoInstances.get(pseudoIndex));
				}
			}

			pairIndexReader.close();
			return normalPseudoPairMap;
		} catch (Exception e) {

		}
		return null;
	}

	public static List<Instance> getFirstPseudoBatch(Instances firstBatch, Map<Instance, Instance> leftPseudo) {
		// TODO Auto-generated method stub
		List<Instance> trainPseudo = new ArrayList<Instance>();
		for (Instance normal : firstBatch.subList(0, firstBatch.size())) {
			if (leftPseudo.containsKey(normal)) {
				trainPseudo.add(leftPseudo.remove(normal));
			}
		}
		return trainPseudo;
	}

	public static double testDirectly(ArrayList<Attribute> attributeList, Instances trainData,
			List<Instance> trainPseudo, Instances testData, String[][] options) {
		// TODO Auto-generated method stub
		try {
			Instances tmpTrain = new Instances("tmpTrain", attributeList, trainData.size() + trainPseudo.size());
			tmpTrain.addAll(trainData.subList(0, trainData.size()));
			tmpTrain.addAll(trainPseudo);
			tmpTrain.setClassIndex(tmpTrain.numAttributes() - 1);
			double accuracy = -1.0;
			for (String[] option : options) {
				LibSVM classifier = new LibSVM();
				classifier.setOptions(option.clone());

				classifier.buildClassifier(tmpTrain);
				Evaluation eval = new Evaluation(tmpTrain);
				eval.evaluateModel(classifier, testData);
				if (eval.pctCorrect() >= accuracy) {
					accuracy = eval.pctCorrect();
				}
			}
			return accuracy;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}
	
	private static Instance updateDataSetGapWithLowHigh(Instances trainData, Instances testData, Evaluation bestEvaluation, int gapFirst, int gapSecond) {
		// TODO Auto-generated method stub
		List<MultiValueIndexItem> indexList = new ArrayList<MultiValueIndexItem>();
		for (int instanceIndex = 0; instanceIndex < bestEvaluation.predictions().size(); instanceIndex++) {
			Prediction p = bestEvaluation.predictions().get(instanceIndex);
			NominalPrediction p1 = (NominalPrediction) p;
			double[] distribution = p1.distribution();
			Arrays.sort(distribution);
			double max = distribution[distribution.length - gapFirst];
			double secondMax = distribution[distribution.length - gapSecond];
			double tmpGap = max - secondMax;
			
			MultiValueIndexItem item = new MultiValueIndexItem(instanceIndex);
			item.setGap(tmpGap);
			item.setMax(max);
			indexList.add(item);
		}

		NextInstanceListManager.sortByTypeValue(indexList, "gap");
		NextInstanceListManager.sortByTypeValue(indexList, "max");
		int minGapInstanceIndex = NextInstanceListManager.sortByIndex(indexList, new String[]{"gap", "max"}, "max");
		
		Instance selected = testData.remove(minGapInstanceIndex);
		int trainSize = trainData.size();
		int insertIndex = (int) (Math.random() * trainSize);
		trainData.add(insertIndex, selected);
		return selected;
	}

}
