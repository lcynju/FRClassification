package cn.edu.nju.se.lcy.generate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class SplitGroups {

	private String sourceFile;
	private String destFileFolder;
	private int groupNumber;
	private int classNumber;

	private boolean duplicate;
	private List<String> duplicateClasses;
	private List<Integer> duplicateTimes;

	private List<String> classes;
	private String fileHeader = "";

	private String fileType;

	/**
	 * 将给定的.arff数据文件进行分组，并且写到指定的目录下。
	 * 
	 * @param sourceFile
	 *            源数据文件目录及名称
	 * @param groupFolder
	 *            分组的目标目录，和源数据文件在同一个目录下,所有的组都会在这个目录下
	 * @param groupNumber
	 *            需要分组的组数
	 * @param dup
	 *            是否有一些需要重复的类
	 * @param dupClasses
	 *            需要重复类的名称，是一个列表
	 * @param dupNumbers
	 *            需要重复类的重复次数，是一个列表，需要和类名称保持相同的顺序
	 * @param fileType
	 *            文件后缀，目前没有什么用，只支持.arff
	 */
	public SplitGroups(String sourceFile, String groupFolder, int groupNumber, boolean dup, List<String> dupClasses,
			List<Integer> dupNumbers, String fileType) {
		this.sourceFile = sourceFile;
		this.destFileFolder = groupFolder;
		this.groupNumber = groupNumber;
		this.duplicate = dup;
		this.duplicateClasses = dupClasses;
		this.duplicateTimes = dupNumbers;
		this.fileType = fileType;

		this.classes = new ArrayList<String>();
	}

	public SplitGroups(String sourceFile, String fileType) {
		this.sourceFile = sourceFile;
		this.fileType = fileType;

		this.classes = new ArrayList<String>();
	}
	
	public void setGroupFolder(String groupFolder) {
		this.destFileFolder = groupFolder;
	}

	public void simpleBinary(String filePath){
		this.classes.clear();
		this.fileHeader = "";
		try {
			BufferedReader sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));

			String line = "";
			while (!(line = sourceReader.readLine()).equals("@DATA")) {
				addToHeader(line);
			}
			List<BufferedWriter> writers = new ArrayList<BufferedWriter>();
			for (int j = 0; j < classNumber; j++) {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(filePath + "binary" + j + fileType)));
				writeHead(writer, j);
				writers.add(writer);
			}

			while ((line = sourceReader.readLine()) != null) {
				if (!line.equals("")) {
					String[] features = line.split(",");
					String classType = features[features.length - 1];
					for (int binaryIndex = 0; binaryIndex < classNumber; binaryIndex++) {
						if (classType.equals(classes.get(binaryIndex))) {
							writers.get(binaryIndex).append(line);
							writers.get(binaryIndex).newLine();
							writers.get(binaryIndex).flush();
						} else {
							String newLine = line.replaceFirst(classType, "others");
							writers.get(binaryIndex).append(newLine);
							writers.get(binaryIndex).newLine();
							writers.get(binaryIndex).flush();
						}
					}
				}
			}
			
			sourceReader.close();
			for (int j = 0; j < classNumber; j++) {
				writers.get(j).close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 开始执行分组，可以通过指定是否是Binary实验需要的数据来决定是否在组内生成Binary训练数据和测试数据
	 * 
	 * @param binary
	 *            指定为true则会在每个组文件中继续生成两类分类实验需要的数据，以binary和test命名
	 */
	public void start(boolean binary) {
		this.classes.clear();
		this.fileHeader = "";
		try {
			File groupFolder = new File(this.destFileFolder);
			if (!groupFolder.exists()) {
				groupFolder.mkdir();
			}
			List<BufferedWriter> singleWriters = new ArrayList<BufferedWriter>();
			List<BufferedWriter> groupWriters = new ArrayList<BufferedWriter>();

			BufferedReader sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));

			for (int i = 0; i < this.groupNumber; i++) {
				File file = new File(this.destFileFolder + "g" + i);
				file.mkdir();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(file.getPath() + "\\group" + i + fileType)));
				BufferedWriter gwriter = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(file.getPath() + "\\others" + i + fileType)));
				singleWriters.add(writer);
				groupWriters.add(gwriter);
			}

			String line = "";
			while (!(line = sourceReader.readLine()).equals("@DATA")) {
				addToHeader(line);
			}

			for (BufferedWriter w : singleWriters) {
				writeHead(w, -1);
			}

			for (BufferedWriter w : groupWriters) {
				writeHead(w, -1);
			}

			int lineNumber = 0;
			while ((line = sourceReader.readLine()) != null) {
				singleWriters.get(lineNumber % groupNumber).append(line);
				singleWriters.get(lineNumber % groupNumber).newLine();
				for (BufferedWriter w : groupWriters) {
					if (groupWriters.indexOf(w) != lineNumber % groupNumber) {
						if (duplicate) {
							String[] features = line.split(",");
							String classType = features[features.length - 1];
							if (duplicateClasses.contains(classType)) {
								duplicate(w, line, duplicateTimes.get(duplicateClasses.indexOf(classType)));
							} else {
								w.append(line);
								w.newLine();
								w.flush();
							}
						} else {
							w.append(line);
							w.newLine();
							w.flush();
						}
					}
				}
				singleWriters.get(lineNumber % groupNumber).flush();
				lineNumber++;
			}

			for (BufferedWriter w : singleWriters) {
				w.close();
			}
			for (BufferedWriter w : groupWriters) {
				w.close();
			}
			sourceReader.close();

			readTrueClass(this.destFileFolder, "g", this.groupNumber);
			if (binary) {
				duplicate4Binary(this.destFileFolder, "g");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The last group is reserved for testing. As if there are only 'groupNumber
	 * - 1' folds in the learning process.
	 * 
	 * @param binary
	 */
	public void startTestReserved(boolean binary) {
		this.classes.clear();
		this.fileHeader = "";
		try {
			File groupFolder = new File(this.destFileFolder);
			if (!groupFolder.exists()) {
				groupFolder.mkdir();
			}
			List<BufferedWriter> singleWriters = new ArrayList<BufferedWriter>();
			List<BufferedWriter> groupWriters = new ArrayList<BufferedWriter>();

			BufferedReader sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));

			for (int i = 0; i < this.groupNumber; i++) {
				File file = new File(this.destFileFolder + "reserved-g" + i);
				file.mkdir();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(file.getPath() + "\\group" + i + fileType)));
				singleWriters.add(writer);
				BufferedWriter gwriter = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(file.getPath() + "\\others" + i + fileType)));
				groupWriters.add(gwriter);
			}

			String line = "";
			while (!(line = sourceReader.readLine()).equals("@DATA")) {
				addToHeader(line);
			}

			for (BufferedWriter w : singleWriters) {
				writeHead(w, -1);
			}

			for (BufferedWriter w : groupWriters) {
				writeHead(w, -1);
			}

			int lineNumber = 0;
			while ((line = sourceReader.readLine()) != null) {
				singleWriters.get(lineNumber % this.groupNumber).append(line);
				singleWriters.get(lineNumber % this.groupNumber).newLine();
				singleWriters.get(lineNumber % this.groupNumber).flush();
				if ((lineNumber % this.groupNumber) == (this.groupNumber - 1)) {
					lineNumber++;
					continue;
				}
				for (BufferedWriter w : groupWriters) {
					if (groupWriters.indexOf(w) != lineNumber % this.groupNumber) {
						if (groupWriters.indexOf(w) != this.groupNumber - 1 && duplicate) {
							String[] features = line.split(",");
							String classType = features[features.length - 1];
							if (this.duplicateClasses.contains(classType)) {
								duplicate(w, line, this.duplicateTimes.get(this.duplicateClasses.indexOf(classType)));
							} else {
								w.append(line);
								w.newLine();
								w.flush();
							}
						} else {
							w.append(line);
							w.newLine();
							w.flush();
						}
					}
				}
				lineNumber++;
			}

			for (BufferedWriter w : singleWriters) {
				w.close();
			}
			for (BufferedWriter w : groupWriters) {
				w.close();
			}
			sourceReader.close();

			readTrueClass(this.destFileFolder, "reserved-g", this.groupNumber - 1);
			if (binary) {
				duplicate4Binary(this.destFileFolder, "reserved-g");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addToHeader(String line) {
		// TODO Auto-generated method stub
		if (line.contains("class")) {
			String[] attribute = line.split("\t");
			String classNamesString = attribute[attribute.length - 1].replaceAll("\\{", "").replaceAll("\\}", "");
			String[] classNames = classNamesString.split(",");
			for (String className : classNames) {
				this.classes.add(className);
			}
			this.classNumber = this.classes.size();
			return;
		} else {
			this.fileHeader += line + "\r\n";
		}
	}

	private static void duplicate(BufferedWriter w, String line, int times) throws IOException {
		// TODO Auto-generated method stub
		for (int i = 0; i < times; i++) {
			w.append(line);
			w.newLine();
		}
		w.flush();
	}

	private void readTrueClass(String filePath, String groupFolderName, int groupNumber) throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filePath + groupFolderName + "trueClass.txt")));

		for (int i = 0; i < groupNumber; i++) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath + groupFolderName + i + "\\group" + i + fileType)));
			String line = "";

			readUntilData(reader);

			while ((line = reader.readLine()) != null) {
				if (!line.equals("")) {
					String[] features = line.split(",");
					String classType = features[features.length - 1];
					writer.append(classType);
					writer.newLine();
					writer.flush();
				}
			}
			reader.close();
		}
		writer.close();

		if (groupNumber < this.groupNumber) {
			BufferedWriter reservedTestTrueWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filePath + groupFolderName + "testTrueClass.txt")));
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
					filePath + groupFolderName + groupNumber + "\\group" + groupNumber + fileType)));
			String line = "";
			readUntilData(reader);
			while ((line = reader.readLine()) != null) {
				if (!line.equals("")) {
					String[] features = line.split(",");
					String classType = features[features.length - 1];
					reservedTestTrueWriter.append(classType);
					reservedTestTrueWriter.newLine();
					reservedTestTrueWriter.flush();
				}
			}
			reader.close();
			reservedTestTrueWriter.close();
		}
	}

	private void readUntilData(BufferedReader reader) throws IOException {
		// TODO Auto-generated method stub
		String line = "";
		while (!(line = reader.readLine()).equals("@DATA")) {

		}
	}

	private void duplicate4Binary(String filePath, String groupFolderName) throws IOException {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.groupNumber; i++) {
			String groupPath = filePath + groupFolderName + i + "\\";
			BufferedReader othersReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(groupPath + "others" + i + fileType)));
			List<BufferedWriter> writers = new ArrayList<BufferedWriter>();
			for (int j = 0; j < classNumber; j++) {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(groupPath + "binary" + j + fileType)));
				writeHead(writer, j);
				writers.add(writer);
			}

			readUntilData(othersReader);

			String othersLine = "";
			while ((othersLine = othersReader.readLine()) != null) {
				if (!othersLine.equals("")) {
					String[] features = othersLine.split(",");
					String classType = features[features.length - 1];
					for (int binaryIndex = 0; binaryIndex < classNumber; binaryIndex++) {
						if (classType.equals(classes.get(binaryIndex))) {
							writers.get(binaryIndex).append(othersLine);
							writers.get(binaryIndex).newLine();
							writers.get(binaryIndex).flush();
						} else {
							String newLine = othersLine.replaceFirst(classType, "others");
							writers.get(binaryIndex).append(newLine);
							writers.get(binaryIndex).newLine();
							writers.get(binaryIndex).flush();
						}
					}
				}
			}

			BufferedReader gReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(groupPath + "group" + i + fileType)));
			List<BufferedWriter> testWriters = new ArrayList<BufferedWriter>();
			for (int j = 0; j < classNumber; j++) {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(groupPath + "test" + j + fileType)));
				writeHead(writer, j);
				testWriters.add(writer);
			}

			readUntilData(gReader);

			String gLine = "";
			while ((gLine = gReader.readLine()) != null) {
				if (!gLine.equals("")) {
					String[] features = gLine.split(",");
					String classType = features[features.length - 1];
					for (int binaryIndex = 0; binaryIndex < classNumber; binaryIndex++) {
						if ((classType).equals(classes.get(binaryIndex))) {
							testWriters.get(binaryIndex).append(gLine);
							testWriters.get(binaryIndex).newLine();
							testWriters.get(binaryIndex).flush();
						} else {
							String newLine = gLine.replaceFirst(classType, "others");
							testWriters.get(binaryIndex).append(newLine);
							testWriters.get(binaryIndex).newLine();
							testWriters.get(binaryIndex).flush();
						}
					}
				}
			}
			for (int j = 0; j < classNumber; j++) {
				writers.get(i).close();
				testWriters.get(i).close();
			}
			gReader.close();
			othersReader.close();
		}
	}

	private void writeHead(BufferedWriter writer, int j) {
		// TODO Auto-generated method stub
		try {
			writer.append(fileHeader);
			if (j > -1) {
				writer.append("@ATTRIBUTE class\t{" + classes.get(j) + ",others}");
			} else {
				writer.append("@ATTRIBUTE class\t{");
				for (int classIndex = 0; classIndex < classNumber; classIndex++) {
					writer.append(classes.get(classIndex));
					if (classIndex < classNumber - 1) {
						writer.append(",");
					}
				}
				writer.append("}");
			}
			writer.newLine();
			writer.newLine();
			writer.append("@DATA");
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void simpleSplit(int groupNumber, String groupName, String filePath, String fileName,
			String fileExtension) throws IOException {
		File newDir = new File(filePath + fileName);
		if (!newDir.exists()) {
			newDir.mkdir();
		}
		BufferedReader sourceReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath + fileName + fileExtension)));
		List<BufferedWriter> singleWriters = new ArrayList<BufferedWriter>();
		List<BufferedWriter> othersWriters = new ArrayList<BufferedWriter>();

		filePath = newDir.getAbsolutePath() + "\\";

		for (int groupIndex = 0; groupIndex < groupNumber; groupIndex++) {
			File groupDir = new File(filePath + groupName + groupIndex + "\\");
			if (!groupDir.exists()) {
				groupDir.mkdir();
			}
			BufferedWriter groupWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(groupDir.getAbsolutePath() + "\\group" + groupIndex + fileExtension)));
			singleWriters.add(groupWriter);
			BufferedWriter othersWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(groupDir.getAbsolutePath() + "\\others" + groupIndex + fileExtension)));
			othersWriters.add(othersWriter);
		}
		String line = "";
		int lineNumber = 0;
		while ((line = sourceReader.readLine()) != null) {
			singleWriters.get(lineNumber % groupNumber).append(line);
			singleWriters.get(lineNumber % groupNumber).newLine();
			for (BufferedWriter w : othersWriters) {
				if (othersWriters.indexOf(w) != lineNumber % groupNumber) {
					w.append(line);
					w.newLine();
					w.flush();
				}
			}
			singleWriters.get(lineNumber % groupNumber).flush();
			lineNumber++;
		}
		sourceReader.close();
		for (int groupIndex = 0; groupIndex < groupNumber; groupIndex++) {
			singleWriters.get(groupIndex).close();
			othersWriters.get(groupIndex).close();
		}
	}

	public static void simpleReadGroupTrueClass(String filePath, String groupFolderName, int groupNumber)
			throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filePath + groupFolderName + "-trueClass.txt")));

		for (int i = 0; i < groupNumber; i++) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath + groupFolderName + i + "\\group" + i + ".txt")));
			String line = "";

			while ((line = reader.readLine()) != null) {
				if (!line.equals("")) {
					String[] features = line.split("\t");
					String classType = features[0];
					writer.append(classType);
					writer.newLine();
					writer.flush();
				}
			}
			reader.close();
		}
		writer.close();
	}

	public static void simpleReadSingleTrueClass(String filePath, String sourceFileName)
			throws IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath + sourceFileName)));
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filePath + "trueClass" + sourceFileName)));

		String line = "";

		while ((line = reader.readLine()) != null) {
			if (!line.equals("")) {
				String[] features = line.split("\t");
				String classType = features[0];
				writer.append(classType);
				writer.newLine();
				writer.flush();
			}
		}
		reader.close();
		writer.close();
	}

	public static void simpltDuplicate(String filePathName, String fileExtension, String classes, String times)
			throws IOException {
		List<String> dupClasses = new ArrayList<String>();
		List<Integer> dupTimes = new ArrayList<Integer>();
		String[] classesarray = classes.split("-");
		String[] timesarray = times.split("-");
		for (int classIndex = 0; classIndex < classesarray.length; classIndex++) {
			dupClasses.add(classesarray[classIndex]);
			dupTimes.add(Integer.parseInt(timesarray[classIndex]));
		}

		BufferedReader sourceReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePathName + fileExtension)));
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filePathName + "-dup" + fileExtension)));

		String line = "";
		while ((line = sourceReader.readLine()) != null) {
			if (!line.equals("")) {
				String[] features = line.split("\t");
				String classType = features[features.length - 1];
				if (dupClasses.contains(classType)) {
					duplicate(writer, line, dupTimes.get(dupClasses.indexOf(classType)));
				} else {
					writer.append(line);
					writer.newLine();
					writer.flush();
				}
			}
		}

		sourceReader.close();
		writer.close();
	}
}
