package cn.edu.nju.se.lcy.mains.notest;

public class PipeLine {

	public static void mainAll(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String[] projectNames = new String[] {"KeePass", "Winmerge" /*,  ,"Mumble"/*,*/  };

		for (String project : projectNames) {
			
			/**��������Ҫѵ����������������ϣ�
			 * ���������ɵ�������ֵ��Ӧ���ļ�����ȷ����
			 * ��ExtractMain��ȷ���˵��������ļ�������**/
			String[] featureNames = new String[] {"keywordcounts", "adjkeywordcounts", "heuristicrules",
					"tfidf-keywordcounts", "tfidf-adjkeywordcounts", 
					"tfidf-keywordcounts-heuristicrules", "tfidf-adjkeywordcounts-heuristicrules"};
			
			String[] types = new String[] {"numeric", "numeric", "numeric", 
					"numeric-numeric", "numeric-numeric",
					"numeric-numeric-numeric", "numeric-numeric-numeric"};

			String filePath = "E:\\JSS\\Experiments1103ReallyReallyFormer\\" + project + "\\";
			
			ExtractMain.main(new String[] { filePath });

			for (int featureIndex = 0; featureIndex < featureNames.length; featureIndex++) {
				String feature = featureNames[featureIndex];
				String type = types[featureIndex];
				String[] arg = new String[] { filePath, feature, type };

				OrganizeMain.main(arg);
				SplitMain.main(arg);
				LearnMain.main(arg);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String[] projectNames = new String[] {"Winmerge"};

		for (String project : projectNames) {
			
			/**��������Ҫѵ����������������ϣ�
			 * ���������ɵ�������ֵ��Ӧ���ļ�����ȷ����
			 * ��ExtractMain��ȷ���˵��������ļ�������**/
			String[] featureNames = new String[] {"heuristicrules"};
			
			String[] types = new String[] {"numeric"};

			String filePath = "E:\\JSS\\Experiments1110Heuristic\\" + project + "\\";
			
			ExtractMain.main(new String[] { filePath });

			for (int featureIndex = 0; featureIndex < featureNames.length; featureIndex++) {
				String feature = featureNames[featureIndex];
				String type = types[featureIndex];
				String[] arg = new String[] { filePath, feature, type };

				OrganizeMain.main(arg);
				SplitMain.main(arg);
				LearnMain.main(arg);
			}
		}
	}

}
