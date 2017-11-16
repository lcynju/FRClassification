package cn.edu.nju.se.lcy.active.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class InstanceManagement {

	private int firstBatchNumber;

	private Instances initialInstances;
	private Enumeration<Attribute> attributes;

	private ArrayList<Attribute> attributeList;
	
	private int[] firstBatchIndexes = new int[]{1, 330, 375, 410, 444, 460, 510};

	public InstanceManagement(String dataFileName) {
		this.firstBatchNumber = this.firstBatchIndexes.length;
		try {
			this.initialInstances = DataSource.read(dataFileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.attributeList = new ArrayList<Attribute>();

		this.attributes = this.initialInstances.enumerateAttributes();

		while (this.attributes.hasMoreElements()) {
			this.attributeList.add(this.attributes.nextElement());
		}
	}

	public ArrayList<Attribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(ArrayList<Attribute> attributeList) {
		this.attributeList = attributeList;
	}

	public Instances getFirstBatch(List<Integer> zeroKeysOfInstance, List<Double> keyVarOfInstance) {
		Instances firstBatch = new Instances("firstBatch", this.attributeList, this.firstBatchNumber);

		for (int nextIndex = 0; nextIndex < this.firstBatchNumber; nextIndex++) {
			int index = this.firstBatchIndexes[nextIndex];
			firstBatch.add(this.initialInstances.remove(index));
			if(zeroKeysOfInstance != null){
				zeroKeysOfInstance.remove(index);
				keyVarOfInstance.remove(index);
			}
		}

		firstBatch.setClassIndex(firstBatch.numAttributes() - 1);

		return firstBatch;
	}

	public void addInstances(Instances remainedTestData) {
		this.initialInstances = new Instances("newone", this.attributeList, remainedTestData.size());
		List<Instance> remainedInstances = remainedTestData.subList(0, remainedTestData.size());
		this.initialInstances.addAll(remainedInstances);
	}

	public Instances getInitialInstances() {
		return initialInstances;
	}

	public void setInitialInstances(Instances initialInstances) {
		this.initialInstances = initialInstances;
	}

	public Instances getFirstBatchAndInitTrainPseudo(List<Instance> trainPseudo, Map<Instance, Instance> leftPseudo) {
		// TODO Auto-generated method stub
		Instances firstBatch = new Instances("firstBatch", this.attributeList, this.firstBatchNumber);

		for (int nextIndex = 0; nextIndex < this.firstBatchNumber; nextIndex++) {
			int index = this.firstBatchIndexes[nextIndex];
			Instance tmpInstance = this.initialInstances.get(index);
			if(leftPseudo.containsKey(tmpInstance)){
				trainPseudo.add(leftPseudo.remove(tmpInstance));
			}
			firstBatch.add(this.initialInstances.remove(index));
		}

		firstBatch.setClassIndex(firstBatch.numAttributes() - 1);

		return firstBatch;
	}

}
