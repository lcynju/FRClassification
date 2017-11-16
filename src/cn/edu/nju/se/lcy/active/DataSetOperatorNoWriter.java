package cn.edu.nju.se.lcy.active;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import cn.edu.nju.se.lcy.active.util.MultiValueIndexItem;
import cn.edu.nju.se.lcy.active.util.NextInstanceListManager;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DataSetOperatorNoWriter {
	public static double testDirectly(Instances trainData, Instances testData, String[][] options) {
		try {
			double accuracy = -1.0;
			for (String[] option : options) {
				LibSVM classifier = new LibSVM();
				classifier.setOptions(option.clone());

				classifier.buildClassifier(trainData);
				Evaluation eval = new Evaluation(trainData);
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

	public static Object[] train(Instances trainData, Instances testData, String[][] options, String type,
			Instances totalTestData, List<Integer> keyZeros, List<Double> keyVars) {
		try {
			LibSVM bestClassifier = null;
			double maxAccuracy = -0.1;
			double[][] maxMatrix = new double[3][7];
			for (String[] option : options) {
				LibSVM classifier = new LibSVM();
				classifier.setOptions(option.clone());

				classifier.buildClassifier(trainData);
				Evaluation eval = new Evaluation(trainData);
				eval.evaluateModel(classifier, totalTestData);
				if (eval.pctCorrect() > maxAccuracy) {
					maxAccuracy = eval.pctCorrect();
					for(int i = 0; i < 7; i ++){
						maxMatrix[0][i] = eval.precision(i);
						maxMatrix[1][i] = eval.recall(i);
						maxMatrix[2][i] = eval.fMeasure(i);
					}
					bestClassifier = classifier;
				}
			}

			Evaluation newEval = new Evaluation(trainData);
			newEval.evaluateModel(bestClassifier, testData);

			Instance selected = null;
			if (type.equals("max")) {
				selected = updateDataSetLowInHigh(trainData, testData, newEval);
			} else if (type.equals("gap")) {
				selected = updateDataSetMinGap(trainData, testData, newEval);
			} else if (type.equals("gapmax")) {
				selected = updateDataSetGapWithLowHigh(trainData, testData, newEval, 1, 2);
			} else if (type.equals("twogap")) {
				selected = updateDataSetTwoGap(trainData, testData, newEval);
			} else if (type.equals("gapkey")) {
				selected = updateDataSetGapKeyvar(trainData, testData, newEval, keyZeros, keyVars);
			} else if (type.equals("keyvar")) {
				selected = updateDataSetKeyvar(trainData, testData, newEval, keyZeros, keyVars);
			} else if (type.equals("maxkey")) {
				selected = updateDataSetMaxKeyvar(trainData, testData, newEval, keyZeros, keyVars);
			} else if (type.equals("coin")) {
				int iteration = trainData.size() - 6;
				double p = calculateBound(iteration);
				double randomCoin = Math.random();
				if(randomCoin < p){
					selected = updateDataSetMinGap(trainData, testData, newEval);
				}else{
					selected = randomlyGetNext(trainData, testData);
				}
			} else if (type.equals("coinreverse")) {
				int iteration = trainData.size() - 6;
				double p = calculateBound(iteration);
				double randomCoin = Math.random();
				if(randomCoin < p){
					selected = randomlyGetNext(trainData, testData);
				}else{
					selected = updateDataSetMinGap(trainData, testData, newEval);
				}
			} else if (type.equals("keyonly")) {
				selected = updateDataSetKeyOnly(trainData, testData, newEval, keyZeros);
			} else if (type.equals("maxgap")) {
				selected = updateDataSetMaxGap(trainData, testData, newEval);
			} else {
				selected = randomlyGetNext(trainData, testData);
			}

			return new Object[]{maxAccuracy, maxMatrix};
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Instance updateDataSetMaxGap(Instances trainData, Instances testData, Evaluation newEval) {
		// TODO Auto-generated method stub
		List<MultiValueIndexItem> indexList = new ArrayList<MultiValueIndexItem>();
		for (int instanceIndex = 0; instanceIndex < newEval.predictions().size(); instanceIndex++) {
			Prediction p = newEval.predictions().get(instanceIndex);
			NominalPrediction p1 = (NominalPrediction) p;
			double[] distribution = p1.distribution();
			Arrays.sort(distribution);
			double max = distribution[distribution.length - 1];
			double secondMax = distribution[distribution.length - 2];
			double tmpGap = max - secondMax;

			MultiValueIndexItem item = new MultiValueIndexItem(instanceIndex);
			item.setGap(tmpGap);
			item.setMax(max);
			indexList.add(item);
		}

		NextInstanceListManager.sortByTypeValue(indexList, "gap");
		List<MultiValueIndexItem> gapIndexList = new ArrayList<MultiValueIndexItem>();
		if (indexList.size() > 1) {
			gapIndexList.addAll(indexList.subList(0, 2));
		} else {
			gapIndexList.addAll(indexList.subList(0, indexList.size()));
		}
		NextInstanceListManager.sortByTypeValue(gapIndexList, "max");
		int minGapInstanceIndex = gapIndexList.get(0).index;

		Instance selected = testData.remove(minGapInstanceIndex);
		trainData.add(selected);
		return selected;
	}

	private static Instance updateDataSetKeyOnly(Instances trainData, Instances testData, Evaluation newEval,
			List<Integer> keyZeros) {
		// TODO Auto-generated method stub
		List<MultiValueIndexItem> indexList = new ArrayList<MultiValueIndexItem>();
		for (int instanceIndex = 0; instanceIndex < newEval.predictions().size(); instanceIndex++) {
			double keyZero = (double) keyZeros.get(instanceIndex);

			MultiValueIndexItem item = new MultiValueIndexItem(instanceIndex);
			item.setKeyzero(keyZero);
			indexList.add(item);
		}

		NextInstanceListManager.sortByTypeValue(indexList, "keyzero");
		int minGapInstanceIndex = indexList.get(0).index;

		Instance selected = testData.remove(minGapInstanceIndex);
		keyZeros.remove(minGapInstanceIndex);
		trainData.add(selected);
		return selected;
	}

	private static double calculateBound(int iteration) {
		// TODO Auto-generated method stub
		double exp = (double) iteration * 0.01;
		return 1 / Math.exp(exp);
	}

	private static Instance updateDataSetKeyvar(Instances trainData, Instances testData, Evaluation newEval,
			List<Integer> keyZeros, List<Double> keyVars) {
		// TODO Auto-generated method stub
		List<MultiValueIndexItem> indexList = new ArrayList<MultiValueIndexItem>();
		for (int instanceIndex = 0; instanceIndex < newEval.predictions().size(); instanceIndex++) {
			double keyZero = (double) keyZeros.get(instanceIndex);
			double keyVar = keyVars.get(instanceIndex);

			MultiValueIndexItem item = new MultiValueIndexItem(instanceIndex);
			item.setKeyzero(keyZero);
			item.setKeyvar(keyVar);
			indexList.add(item);
		}

		NextInstanceListManager.sortByTypeValue(indexList, "keyzero");
		List<MultiValueIndexItem> zeroIndexList = new ArrayList<MultiValueIndexItem>();
		if (indexList.size() > 3) {
			zeroIndexList.addAll(indexList.subList(0, 4));
		} else {
			zeroIndexList.addAll(indexList.subList(0, indexList.size()));
		}
		NextInstanceListManager.sortByTypeValue(zeroIndexList, "keyvar");
		int minGapInstanceIndex = zeroIndexList.get(0).index;

		Instance selected = testData.remove(minGapInstanceIndex);
		keyZeros.remove(minGapInstanceIndex);
		keyVars.remove(minGapInstanceIndex);
		int trainSize = trainData.size();
		int insertIndex = (int) (Math.random() * trainSize);
		trainData.add(insertIndex, selected);
		return selected;
	}

	private static Instance updateDataSetGapKeyvar(Instances trainData, Instances testData, Evaluation newEval,
			List<Integer> keyZeros, List<Double> keyVars) {
		// TODO Auto-generated method stub
		List<MultiValueIndexItem> indexList = new ArrayList<MultiValueIndexItem>();
		for (int instanceIndex = 0; instanceIndex < newEval.predictions().size(); instanceIndex++) {
			Prediction p = newEval.predictions().get(instanceIndex);
			NominalPrediction p1 = (NominalPrediction) p;
			double[] distribution = p1.distribution();
			Arrays.sort(distribution);
			double max = distribution[distribution.length - 1];
			double secondMax = distribution[distribution.length - 2];
			double tmpGap = max - secondMax;
			double keyZero = (double) keyZeros.get(instanceIndex);
			double keyVar = keyVars.get(instanceIndex);

			MultiValueIndexItem item = new MultiValueIndexItem(instanceIndex);
			item.setGap(tmpGap);
			item.setKeyzero(keyZero);
			item.setKeyvar(keyVar);
			indexList.add(item);
		}

		NextInstanceListManager.sortByTypeValue(indexList, "gap");
		List<MultiValueIndexItem> gapIndexList = new ArrayList<MultiValueIndexItem>();
		if (indexList.size() > 3) {
			gapIndexList.addAll(indexList.subList(0, 4));
		} else {
			gapIndexList.addAll(indexList.subList(0, indexList.size()));
		}
//		NextInstanceListManager.sortByTypeValue(gapIndexList, "keyzero");
//		List<MultiValueIndexItem> keyZeroIndexList = new ArrayList<MultiValueIndexItem>();
//		if (gapIndexList.size() > 1) {
//			keyZeroIndexList.addAll(gapIndexList.subList(0, gapIndexList.size() - 1));
//		} else {
//			keyZeroIndexList.add(gapIndexList.get(0));
//		}
		NextInstanceListManager.sortByTypeValue(gapIndexList, "keyzero");
		int minGapInstanceIndex = gapIndexList.get(0).index;

		Instance selected = testData.remove(minGapInstanceIndex);
		keyZeros.remove(minGapInstanceIndex);
		keyVars.remove(minGapInstanceIndex);
		int trainSize = trainData.size();
		int insertIndex = (int) (Math.random() * trainSize);
		trainData.add(insertIndex, selected);
		return selected;
	}
	
	private static Instance updateDataSetMaxKeyvar(Instances trainData, Instances testData, Evaluation newEval,
			List<Integer> keyZeros, List<Double> keyVars) {
		// TODO Auto-generated method stub
		List<MultiValueIndexItem> indexList = new ArrayList<MultiValueIndexItem>();
		for (int instanceIndex = 0; instanceIndex < newEval.predictions().size(); instanceIndex++) {
			Prediction p = newEval.predictions().get(instanceIndex);
			NominalPrediction p1 = (NominalPrediction) p;
			double[] distribution = p1.distribution();
			Arrays.sort(distribution);
			double max = distribution[distribution.length - 1];
			double keyZero = (double) keyZeros.get(instanceIndex);
			double keyVar = keyVars.get(instanceIndex);

			MultiValueIndexItem item = new MultiValueIndexItem(instanceIndex);
			item.setMax(max);
			item.setKeyzero(keyZero);
			item.setKeyvar(keyVar);
			indexList.add(item);
		}

		NextInstanceListManager.sortByTypeValue(indexList, "max");
		List<MultiValueIndexItem> gapIndexList = new ArrayList<MultiValueIndexItem>();
		if (indexList.size() > 3) {
			gapIndexList.addAll(indexList.subList(0, 4));
		} else {
			gapIndexList.addAll(indexList.subList(0, indexList.size()));
		}
//		NextInstanceListManager.sortByTypeValue(gapIndexList, "keyzero");
//		List<MultiValueIndexItem> keyZeroIndexList = new ArrayList<MultiValueIndexItem>();
//		if (gapIndexList.size() > 1) {
//			keyZeroIndexList.addAll(gapIndexList.subList(0, gapIndexList.size() - 1));
//		} else {
//			keyZeroIndexList.add(gapIndexList.get(0));
//		}
		NextInstanceListManager.sortByTypeValue(gapIndexList, "keyzero");
		int minGapInstanceIndex = gapIndexList.get(0).index;

		Instance selected = testData.remove(minGapInstanceIndex);
		keyZeros.remove(minGapInstanceIndex);
		keyVars.remove(minGapInstanceIndex);
		trainData.add(selected);
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
			if (thirdMax > 0) {
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

	private static Instance updateDataSetGapWithLowHigh(Instances trainData, Instances testData,
			Evaluation bestEvaluation, int i, int j) {
		// TODO Auto-generated method stub
		List<MultiValueIndexItem> indexList = new ArrayList<MultiValueIndexItem>();
		for (int instanceIndex = 0; instanceIndex < bestEvaluation.predictions().size(); instanceIndex++) {
			Prediction p = bestEvaluation.predictions().get(instanceIndex);
			NominalPrediction p1 = (NominalPrediction) p;
			double[] distribution = p1.distribution();
			Arrays.sort(distribution);
			double max = distribution[distribution.length - i];
			double secondMax = distribution[distribution.length - j];
			double tmpGap = max - secondMax;

			MultiValueIndexItem item = new MultiValueIndexItem(instanceIndex);
			item.setGap(tmpGap);
			item.setMax(max);
			indexList.add(item);
		}

		NextInstanceListManager.sortByTypeValue(indexList, "max");
		List<MultiValueIndexItem> gapIndexList = new ArrayList<MultiValueIndexItem>();
		if (indexList.size() > 1) {
			gapIndexList.addAll(indexList.subList(0, 2));
		} else {
			gapIndexList.addAll(indexList.subList(0, indexList.size()));
		}
		NextInstanceListManager.sortByTypeValue(gapIndexList, "gap");
		int minGapInstanceIndex = gapIndexList.get(0).index;

		Instance selected = testData.remove(minGapInstanceIndex);
		int trainSize = trainData.size();
		int insertIndex = (int) (Math.random() * trainSize);
		trainData.add(insertIndex, selected);
		return selected;
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

	private static Instance updateDataSetMinGap(Instances trainData, Instances testData, Evaluation bestEvaluation) {
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
		trainData.add(selected);
		return selected;
	}

	public static double trainDup(ArrayList<Attribute> attributeList, Instances firstBatch, Instances testData,
			List<Instance> trainDuplicate, String[][] clone, String type, Instances totalTestData) {
		// TODO Auto-generated method stub
		try {
			Instances tmpTrain = new Instances("tmpTrain", attributeList, firstBatch.size() + trainDuplicate.size());
			tmpTrain.addAll(firstBatch.subList(0, firstBatch.size()));
			tmpTrain.addAll(trainDuplicate);
			tmpTrain.setClassIndex(tmpTrain.numAttributes() - 1);
			LibSVM bestClassifier = null;
			double maxAccuracy = -0.1;
			for (String[] option : clone) {
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
				selected = updateDataSetLowInHigh(firstBatch, testData, newEval);
			} else if (type.equals("gap")) {
				selected = updateDataSetMinGap(firstBatch, testData, newEval);
			} else if (type.equals("gapwithlowhigh")) {
				selected = updateDataSetGapWithLowHigh(firstBatch, testData, newEval, 1, 2);
			} else if (type.equals("twogap")) {
				selected = updateDataSetTwoGap(firstBatch, testData, newEval);
			} else {
				selected = randomlyGetNext(firstBatch, testData);
			}

			updatePseudo(selected, trainDuplicate);

			return maxAccuracy;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
	}

	private static void updatePseudo(Instance selected, List<Instance> trainDuplicate) {
		// TODO Auto-generated method stub
		String className = selected.classAttribute().value((int) selected.classValue());
		if (className.equals("lifecycle") || className.equals("performance") || className.equals("reliability")) {
			Instance dupSelceted = selected.copy(selected.toDoubleArray());
			trainDuplicate.add(dupSelceted);
		}
	}

	public static double testDupDirectly(ArrayList<Attribute> attributeList, Instances firstBatch,
			Instances totalTestData, List<Instance> trainDuplicate, String[][] options) {
		// TODO Auto-generated method stub
		try {
			Instances tmpTrain = new Instances("tmpTrain", attributeList, firstBatch.size() + trainDuplicate.size());
			tmpTrain.addAll(firstBatch.subList(0, firstBatch.size()));
			tmpTrain.addAll(trainDuplicate);
			tmpTrain.setClassIndex(tmpTrain.numAttributes() - 1);
			double accuracy = -1.0;
			for (String[] option : options) {
				LibSVM classifier = new LibSVM();
				classifier.setOptions(option.clone());

				classifier.buildClassifier(tmpTrain);
				Evaluation eval = new Evaluation(tmpTrain);
				eval.evaluateModel(classifier, totalTestData);
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

}
