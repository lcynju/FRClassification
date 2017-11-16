package cn.edu.nju.se.lcy.generate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Generate4Binary {

	public static void duplicate4Binary(String filePath, String groupFolderName, int groupNumber, String classStrings,
			String fileType, boolean onlyPositive) throws IOException {
		// TODO Auto-generated method stub
		String[] classNames = classStrings.split(",");
		List<String> classes = new ArrayList<String>();
		classes.addAll(Arrays.asList(classNames));

		for (int i = 0; i < groupNumber; i++) {
			String groupPath = filePath + groupFolderName + i + "\\";
			BufferedReader othersReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(groupPath + "others" + i + fileType)));
			BufferedReader marksReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(groupPath + "mark" + i + ".txt")));

			String header = readHeader(othersReader);
			List<BufferedWriter> writers = new ArrayList<BufferedWriter>();
			for (int j = 0; j < classes.size(); j++) {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(groupPath + "binary" + j + fileType)));
				writeHead(writer, j, header, classes);
				writers.add(writer);
			}

			String othersLine = "", mark = "";
			while ((othersLine = othersReader.readLine()) != null) {
				if (!othersLine.equals("")) {
					mark = marksReader.readLine();
					String[] features = othersLine.split(",");
					String classType = features[features.length - 1];
					for (int binaryIndex = 0; binaryIndex < classes.size(); binaryIndex++) {
						if (classType.equals(classes.get(binaryIndex))) {
							writers.get(binaryIndex).append(othersLine);
							writers.get(binaryIndex).newLine();
							writers.get(binaryIndex).flush();
						} else {
							if (onlyPositive) {
								if (mark.equals("former")) {
									String newLine = othersLine.replaceFirst(classType, "others");
									writers.get(binaryIndex).append(newLine);
									writers.get(binaryIndex).newLine();
									writers.get(binaryIndex).flush();
								}
							} else {
								String newLine = othersLine.replaceFirst(classType, "others");
								writers.get(binaryIndex).append(newLine);
								writers.get(binaryIndex).newLine();
								writers.get(binaryIndex).flush();
							}
						}
					}
				}
			}
			marksReader.close();

			BufferedReader gReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(groupPath + "group" + i + fileType)));
			List<BufferedWriter> testWriters = new ArrayList<BufferedWriter>();
			for (int j = 0; j < classes.size(); j++) {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(groupPath + "test" + j + fileType)));
				writeHead(writer, j, header, classes);
				testWriters.add(writer);
			}

			readUntilData(gReader);

			String gLine = "";
			while ((gLine = gReader.readLine()) != null) {
				if (!gLine.equals("")) {
					String[] features = gLine.split(",");
					String classType = features[features.length - 1];
					for (int binaryIndex = 0; binaryIndex < classes.size(); binaryIndex++) {
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
			for (int j = 0; j < classes.size(); j++) {
				writers.get(i).close();
				testWriters.get(i).close();
			}
			gReader.close();
			othersReader.close();
		}
	}

	private static void readUntilData(BufferedReader reader) {
		// TODO Auto-generated method stub
		String line = "";
		try {
			while (!(line = reader.readLine()).equals("@DATA")) {
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeHead(BufferedWriter writer, int j, String header, List<String> classes) {
		// TODO Auto-generated method stub
		try {
			writer.append(header);
			if (j > -1) {
				writer.append("@ATTRIBUTE class\t{" + classes.get(j) + ",others}");
			} else {
				writer.append("@ATTRIBUTE class\t{");
				for (int classIndex = 0; classIndex < classes.size(); classIndex++) {
					writer.append(classes.get(classIndex));
					if (classIndex < classes.size() - 1) {
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

	private static String readHeader(BufferedReader reader) {
		// TODO Auto-generated method stub
		String header = "";
		String line = "";
		try {
			while (!(line = reader.readLine()).contains("class")) {
				header += line + "\r\n";
			}
			while (!(line = reader.readLine()).equals("@DATA")) {
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return header;
	}
}
