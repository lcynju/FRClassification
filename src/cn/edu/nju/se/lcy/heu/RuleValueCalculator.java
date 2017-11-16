package cn.edu.nju.se.lcy.heu;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleValueCalculator {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String fileFolder = args[0];// "D:\\Experiments-0720\\large train
									// data\\";
		String sourceFile = fileFolder + args[1];// "pseudo-source data.txt";
		String posFile = fileFolder + args[2];// "pseudo-pos.txt";
		// String lemmaFile = fileFolder + "";

		String valueOutFile = fileFolder + args[3];// "pseudo-heuristicrules.txt";

		BufferedReader sourceReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
		BufferedReader posReader = new BufferedReader(new InputStreamReader(new FileInputStream(posFile)));
		// BufferedReader lemmaReader = new BufferedReader(new
		// InputStreamReader(new FileInputStream(lemmaFile)));

		BufferedWriter resultWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(valueOutFile)));

		String sourceLine = "";
		while ((sourceLine = sourceReader.readLine()) != null) {
			String posLine = posReader.readLine();
			sourceLine = sourceLine.toLowerCase();
			//posLine = posLine.toLowerCase();
			//String lemmaLine = lemmaReader.readLine();

			String[] sentences = sourceLine.split("\\. ");
			int numberOfSentence = sentences.length;
			Object[] result = calculateFeatures(sentences, posLine, sourceLine);
			int uselessNumber = (Integer) result[0];
			String otherFeatures = (String) result[1];
			double useLessPercentage = uselessNumber / (double) numberOfSentence;
			resultWriter.append(useLessPercentage + "\t" + otherFeatures);
			resultWriter.newLine();
			resultWriter.flush();
		}

		sourceReader.close();
		posReader.close();
		resultWriter.close();
	}

	private static Object[] calculateFeatures(String[] sentences, String posLine, String sourceLine) {
		// TODO Auto-generated method stub
		int uselessSentenceNumber = 0;
		boolean cs = false, rp = false, rn = false, da = false, dsn = false, dc = false, ht = false;
		int innerunsn = checkUNSN(sourceLine);
		String newSourceLine = sourceLine.replaceAll("\\.", "");
		String[] sentenceWords = newSourceLine.split(" ");
		int innerus = checkUS(sentenceWords, sourceLine);
		int innerpe = checkPE(sentenceWords);
		int innersn = checkSN(sentenceWords);
		int innerli = checkLI(sentenceWords, sourceLine);
		int innerse = checkSE(sentenceWords, sourceLine);
		int innersi = checkSI(sentenceWords);
		int innerro = checkReadOnly(sentenceWords);
		int inneror = checkOr(sentenceWords);
		int innerbut = checkButNot(sentenceWords);
		boolean innerpossible = checkPossible(sourceLine);
		cs = checkPOSVB(posLine);
		for (String sentence : sentences) {
			boolean innercs = checkCS(sentence);
			boolean innerrp = checkRP(sentence);
			boolean innerrn = checkRN(sentence);
			boolean innerda = checkDA(sentence, posLine);
			boolean innerdsn = checkDSN(sentence);
			boolean innerdc = checkDC(sentence);
			boolean innerht = checkHT(sentence);
			if (!cs && innercs) {
				cs = innercs;
			}
			if (!rp && innerrp) {
				rp = innerrp;
			}
			if (!rn && innerrn) {
				rn = innerrn;
			}
			if (!da && innerda) {
				da = innerda;
			}
			if (!dsn && innerdsn) {
				dsn = innerdsn;
			}
			if (!dc && innerdc) {
				dc = innerdc;
			}
			if (!ht && innerht) {
				ht = innerht;
			}

			if (!(innercs || innerrp || innerrn || innerda || innerdsn || innerdc || innerht)) {
				uselessSentenceNumber++;
			}
		}
		return new Object[] { uselessSentenceNumber,
				featureValue(cs, rp, rn, da, dsn, dc, ht, innerpossible) + innerunsn + "\t" + innerus + "\t" + innerpe + "\t" + innersn
						+ "\t" + innerli + "\t" + innerse + "\t" + innersi + "\t" + innerro + "\t" + inneror + "\t"
						+ innerbut + "\t" };
	}

	private static String featureValue(boolean cs, boolean rp, boolean rn, boolean da, boolean dsn, boolean dc,
			boolean ht, boolean possible) {
		// TODO Auto-generated method stub
		String features = "";
		if (cs) {
			features += "1\t";
		} else {
			features += "0\t";
		}
		if (rp) {
			features += "1\t";
		} else {
			features += "0\t";
		}
		if (rn) {
			features += "1\t";
		} else {
			features += "0\t";
		}
		if (da) {
			features += "1\t";
		} else {
			features += "0\t";
		}
		if (dsn) {
			features += "1\t";
		} else {
			features += "0\t";
		}
		// if (dc) {
		// features += "1\t";
		// } else {
		// features += "0\t";
		// }
		if (ht) {
			features += "1\t";
		} else {
			features += "0\t";
		}
		if (possible) {
			features += "1\t";
		} else {
			features += "0\t";
		}
		return features;
	}

	private static boolean checkPOSVB(String posLine) {
		// TODO Auto-generated method stub
		posLine = "口" + posLine;
		boolean hasPOSVB = false;
		if (posLine.contains(",\tVB\t") || posLine.contains("口VB\t") || posLine.contains("\\.\tVB\t")) {
			hasPOSVB = true;
		}
		return hasPOSVB;
	}

	private static boolean checkCS(String sentence) {
		// TODO Auto-generated method stub
		boolean cs = false;
		if (sentence.contains("could you ") || sentence.contains("can you ") || sentence.contains("can it ")
				|| sentence.contains("could it") || sentence.contains("will you ") || sentence.contains("would you ")
				|| sentence.contains("would it ") || sentence.contains("will it ") || sentence.contains("wow about ")
				|| sentence.contains("what about ") || sentence.contains("is it possible ")
				|| sentence.contains("why not ") || sentence.contains("you must")
				|| sentence.contains("you should add ") || sentence.contains("isn't it ")
				|| sentence.contains("can we ")) {
			cs = true;
		} else {
			Pattern p1 = Pattern.compile("it (will|would) be [a-z]{1,15} (to|if) ");
			Pattern p2 = Pattern.compile("it (will|would) be [a-z]{1,15} [a-z]{1,15} (to|if) ");
			Matcher match1 = p1.matcher(sentence);
			Matcher match2 = p2.matcher(sentence);
			if (match1.find() || match2.find()) {
				cs = true;
			} else if (sentence.contains("i would like ") || sentence.contains("i will like ")
					|| sentence.contains("i'd love ") || sentence.contains("i'd like ")
					|| sentence.contains("i would love ") || sentence.contains("i will appreciate ")
					|| sentence.contains("i would appreciate ") || sentence.contains("i suggest ")
					|| sentence.contains("i propose ") || sentence.contains("i want ") || sentence.contains("i need ")
					|| sentence.contains("i'm looking for") || sentence.contains("i am looking for")
					|| sentence.contains("i recommend")) {
				cs = true;
			} else if (sentence.contains("keepass can ") || sentence.contains("keepass could ")) {
				cs = true;
			}
		}

		return cs;
	}

	private static boolean checkRP(String sentence) {
		// TODO Auto-generated method stub
		boolean rp = false;
		if (sentence.contains(" so that ") || sentence.contains("i think ") || sentence.contains("that way ")
				|| sentence.contains("since") || sentence.contains("because") || sentence.contains("that will make ")
				|| sentence.contains("that would make ") || sentence.contains("that will allow ")
				|| sentence.contains("that would allow ") || sentence.contains("it will make ")
				|| sentence.contains("it would make ") || sentence.contains("it will allow ")
				|| sentence.contains("it would allow ")) {
			rp = true;
		} else {
			Pattern p = Pattern.compile("then [a-z]{1,15} (is|can|could|be able to)");
			Matcher match = p.matcher(sentence);
			if (match.find()) {
				rp = true;
			} else if (sentence.contains("it might ") || sentence.contains("it may ")
					|| sentence.contains("this allows ") || sentence.contains("the reason ")) {
				rp = true;
			}
		}
		return rp;
	}

	private static boolean checkRN(String sentence) {
		// TODO Auto-generated method stub
		boolean rn = false;
		if (sentence.contains("however ") || sentence.contains("but ") || sentence.contains("should not ")
				|| sentence.contains("otherwise ") || sentence.contains("instead ") || sentence.contains("in case ")
				|| sentence.contains("rather than ") || sentence.contains("the problem ")) {
			rn = true;
		}
		return rn;
	}

	private static boolean checkDA(String sentence, String posLine) {
		// TODO Auto-generated method stub
		boolean da = false;
		posLine = "口" + posLine;
		if (posLine.contains("口VBG\t") || posLine.contains(",\tVBG\t")) {
			da = true;
			return da;
		}
		if (sentence.contains("current") || sentence.contains("sometimes ") || sentence.contains("cases ")
				|| sentence.contains(" now ") || sentence.contains("everytime") || sentence.contains("every time ")
				|| sentence.contains("at the moment ") || sentence.contains("presnetly")) {
			da = true;
		} else if (sentence.contains("when ") || sentence.contains("while ") && !sentence.contains("should")
				&& !sentence.contains("then ") && !sentence.contains("shall")) {
			da = true;
		}
		return da;
	}

	private static boolean checkDSN(String sentence) {
		// TODO Auto-generated method stub
		boolean dsn = false;
		if (sentence.contains("shall") || sentence.contains("should")
				|| (sentence.contains("however") && sentence.contains("not "))
				|| (sentence.contains("but ") && sentence.contains("not")) || sentence.contains("in addition to ")
				|| sentence.contains("rather than ") || sentence.contains("instead of ")
				|| sentence.contains("or it will ") || sentence.contains("but i ") || sentence.contains("if i")
				|| (sentence.contains("if ") && (sentence.contains("it still ") || sentence.contains("it always ")))
				|| (sentence.contains("even ") && sentence.contains("not ")) || sentence.contains("unfortunately ")) {
			dsn = true;
		}
		return dsn;
	}

	private static boolean checkDC(String sentence) {
		// TODO Auto-generated method stub
		boolean dc = false;
		Pattern p1 = Pattern.compile("when .+if ");
		Pattern p2 = Pattern.compile("it .+ if .+ but .+ if ");
		Pattern p3 = Pattern.compile("if .+ then .+ if .+ then ");
		Matcher m1 = p1.matcher(sentence);
		Matcher m2 = p2.matcher(sentence);
		Matcher m3 = p3.matcher(sentence);
		if (m1.find() || m2.find() || m3.find()) {
			dc = true;
		}
		return dc;
	}

	private static boolean checkHT(String sentence) {
		// TODO Auto-generated method stub
		boolean ht = false;
		if (sentence.contains("for example") || sentence.contains("e.g.") || sentence.contains("i.e.")
				|| sentence.contains(" like in ") || sentence.contains(" as in ") || sentence.contains(" such as ")
				|| sentence.contains(" similar to ")) {
			ht = true;
		} else {
			Pattern p1 = Pattern.compile("(just)? like|as in ");
			Pattern p2 = Pattern.compile("there is .+ in ");
			Pattern p3 = Pattern.compile("first .+ (second )?then ");
			Matcher m1 = p1.matcher(sentence);
			Matcher m2 = p2.matcher(sentence);
			Matcher m3 = p3.matcher(sentence);
			if (m1.find() || m2.find() || m3.find()) {
				ht = true;
			}
		}
		return ht;
	}

	private static int checkUNSN(String sentence) {
		// TODO Auto-generated method stub
		int unsn = 0;
		String newSentence = sentence.replaceAll("i would ", "would ");
		newSentence = newSentence.replaceAll("i will ", "will ");
		newSentence = newSentence.replaceAll("i propose ", "propose ");
		newSentence = newSentence.replaceAll("i suggest ", "suggest ");
		newSentence = newSentence.replaceAll("i am looking ", "looking ");
		newSentence = newSentence.replaceAll("i recommend", "recommend ");
		newSentence = newSentence.replaceAll("i appreciate ", "appreciate ");
		newSentence = newSentence.replaceAll("i want ", "want ");
		newSentence = newSentence.replaceAll("\\.", "");
		String[] sentenceWords = newSentence.split(" ");
		String[] uns = new String[] { "i", "user", "users", "people", "who", "anyone", "you", "somebody", "your", "my",
				"their", "he", "his", "we", "our" };
		String[] sns = new String[] { "system", "keepass", "keeper", "software", "program", "database", "auto-type",
				"autotype", "password" };
		List<String> unsList = new ArrayList<String>();
		List<String> snsList = new ArrayList<String>();
		unsList.addAll(Arrays.asList(uns));
		snsList.addAll(Arrays.asList(sns));

		int unint = 0, snint = 0;
		for (String word : sentenceWords) {
			if (unsList.contains(word)) {
				unint++;
			}
			if (snsList.contains(word)) {
				snint++;
			}
		}

		// if (unint > snint) {
		// unsn = 1;
		// } else {
		// if (snint == unint) {
		// unsn = 0;
		// } else {
		// unsn = -1;
		// }
		// }
		return unint;
	}

	private static int checkUS(String[] sentenceWords, String sourceLine) {
		// TODO Auto-generated method stub
		int nus = 0;
		if (sourceLine.contains(" easier to ")) {
			nus++;
		}
		if (sourceLine.contains("dont't want to ")) {
			nus++;
		}
		if (sourceLine.contains("have to ")) {
			nus++;
		}
		if (sourceLine.contains("\"")) {
			nus++;
		}
		String[] uss = new String[] { "convenient", "useful", "hotkey", "use", "shortcut", "usage", "usability",
				"manually", "friendly", "readable", "handy", "annoying", "annoy", "annoyance", "hate", "boring",
				"easily", "happy", "prefer", "ctrl", "shift", "enter", "alt", "esc", "display", "button", "dialog",
				"click", "minimize", "menu", "default", "icon", "font", "language" };
		List<String> usList = new ArrayList<String>();
		usList.addAll(Arrays.asList(uss));
		for (String word : sentenceWords) {
			if (usList.contains(word)) {
				nus++;
			}
		}

		return nus;
	}

	private static int checkPE(String[] sentenceWords) {
		// TODO Auto-generated method stub
		int npe = 0;
		String[] pes = new String[] { "speed", "resource", "time", "efficient", "fast", "slow", "quick" };
		List<String> peList = new ArrayList<String>();
		peList.addAll(Arrays.asList(pes));
		for (String word : sentenceWords) {
			if (peList.contains(word)) {
				npe++;
			}
		}

		return npe;
	}

	private static int checkSN(String[] sentenceWords) {
		// TODO Auto-generated method stub
		int npe = 0;
		String[] pes = new String[] { "ewallet", "pin", "ftp", "vault", "keyring", "mozilla", "firefox", "truecrypt" };
		List<String> peList = new ArrayList<String>();
		peList.addAll(Arrays.asList(pes));
		for (String word : sentenceWords) {
			if (peList.contains(word)) {
				npe++;
			}
		}

		return npe;
	}

	private static int checkLI(String[] sentenceWords, String sourceLine) {
		// TODO Auto-generated method stub
		int nli = 0;
		if (sourceLine.contains(" pocket pc ")) {
			nli++;
		}
		if (sourceLine.contains(" windows 64bit ")) {
			nli++;
		}
		String[] lis = new String[] { "palm", "mac", "ppc", "linux", "symbian", "smartphone", "version", "unix", "msi",
				"package", "plugin", "plugins", "smartrive", "java" };
		List<String> liList = new ArrayList<String>();
		liList.addAll(Arrays.asList(lis));
		for (String word : sentenceWords) {
			if (liList.contains(word)) {
				nli++;
			}
		}
		return nli;
	}

	private static int checkSE(String[] sentenceWords, String sourceLine) {
		// TODO Auto-generated method stub
		int nse = 0;
		if (sourceLine.contains(" sensitive information ")) {
			nse++;
		}
		if (sourceLine.contains(" hide password ")) {
			nse++;
		}
		String[] ses = new String[] { "someone", "anyone", "keylogger", "keylogging", "security", "securily",
				"insecure", "defeat", "retype", "safe", "safety", "safest", "safely", "secure", "encrypt", "attack",
				"md5", "crucial", "securing", "protect", "pgp", "unsafe", "steal", "risk" };
		List<String> seList = new ArrayList<String>();
		seList.addAll(Arrays.asList(ses));
		for (String word : sentenceWords) {
			if (seList.contains(word)) {
				nse++;
			}
		}
		return nse;
	}

	private static int checkSI(String[] sentenceWords) {
		// TODO Auto-generated method stub
		int nsi = 0;
		String[] sis = new String[] { "import", "export", "support" };
		List<String> siList = new ArrayList<String>();
		siList.addAll(Arrays.asList(sis));
		for (String word : sentenceWords) {
			if (siList.contains(word)) {
				nsi++;
			}
		}

		return nsi;
	}

	private static int checkReadOnly(String[] sentenceWords) {
		// TODO Auto-generated method stub
		int nro = 0;
		String[] sis = new String[] { "read only", "read-only" };
		List<String> siList = new ArrayList<String>();
		siList.addAll(Arrays.asList(sis));
		for (String word : sentenceWords) {
			if (siList.contains(word)) {
				nro++;
			}
		}

		return nro;
	}

	private static int checkOr(String[] sentenceWords) {
		// TODO Auto-generated method stub
		int nor = 0;
		String[] sis = new String[] { "or" };
		List<String> siList = new ArrayList<String>();
		siList.addAll(Arrays.asList(sis));
		for (String word : sentenceWords) {
			if (siList.contains(word)) {
				nor++;
			}
		}

		return nor;
	}

	private static int checkButNot(String[] sentenceWords) {
		// TODO Auto-generated method stub
		int nbn = 0;
		String[] sis = new String[] { "but", "not" };
		List<String> siList = new ArrayList<String>();
		siList.addAll(Arrays.asList(sis));
		List<String> wordList = new ArrayList<String>();
		wordList.addAll(Arrays.asList(sentenceWords));
		if (wordList.containsAll(siList)) {
			nbn = 1;
		}

		return nbn;
	}

	private static boolean checkPossible(String sentence) {
		// TODO Auto-generated method stub
		boolean dsn = false;
		if (sentence.contains("if it is possible") || sentence.contains("is it possible")
				|| sentence.contains("would it be possible") || sentence.contains("could it be possible")
				|| sentence.contains("it would be possible")) {
			dsn = true;
		}
		return dsn;
	}
}
