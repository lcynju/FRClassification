package cn.edu.nju.se.lcy.active;

public class AllMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] fileFolderPres = new String[] { "E:\\JSS\\Active Learning\\Mumble\\Active Learning Fold", "E:\\JSS\\Active Learning\\Winmerge\\Active Learning Fold"};
		for (String fileFolderPre : fileFolderPres) {
			for (int i = 0; i < 5; i++) {
				String fileFolder = fileFolderPre + i + "\\";

				String[] parameters2 = new String[2];
				parameters2[0] = fileFolder;
				ActiveProcessNoWriter.main(parameters2);

				// parameters2[1] = "active learning hr nopseudo";
				// ActiveProcessPseudo.main(parameters2);
				//
				// parameters2[1] = "active learning pseudo";
				// ActiveProcessPseudo.main(parameters2);
				//
				// parameters2[1] = "active learning pseudo only keyword";
				// ActiveProcessPseudo.main(parameters2);
			}
		}
	}

}
