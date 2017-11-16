package cn.edu.nju.se.lcy.learn;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public abstract class Learner {

	private String filePath;
	private String dataFileExtension;
	private String[] options;
	private String optionString;
	
	public Learner(String filePath, String extension, String[] options){
		this.filePath = filePath;
		this.dataFileExtension = extension;
		this.options = options;
		
		this.setOptionString("");
		for(String optionItem : this.getOptions()){
			this.optionString += optionItem;
		}
	}
	
	public String getDataFileExtension() {
		return dataFileExtension;
	}

	public void setDataFileExtension(String dataFileExtension) {
		this.dataFileExtension = dataFileExtension;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public Instances readDataSet(String fileName){
		try {
			Instances data = DataSource.read(fileName);
			if (data.classIndex() == -1)
				data.setClassIndex(data.numAttributes() - 1);
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public abstract double start(String type);

	public String getOptionString() {
		return optionString;
	}

	public void setOptionString(String optionString) {
		this.optionString = optionString;
	}

	public abstract void startSingle(String algorithmName);
}
