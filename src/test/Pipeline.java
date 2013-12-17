package test;

import java.util.ArrayList;
import java.util.List;

import processing.ActCalculator;
import processing.BM25Calculator;
import processing.BaselineCalculator;
import processing.FolkRankCalculator;
import processing.LanguageModelCalculator;
import processing.MetricsCalculator;
import processing.RecCalculator;
import file.WikipediaReader;
import file.WikipediaSplitter;

public class Pipeline {

	private static int TRAIN_SIZE;
	private static int TEST_SIZE;

	public static void main(String[] args) {

		if (args.length < 2) {
			return;
		}
		String op = "split";
		String dataset = "";
		String samplePath = "";
		String sampleDir = "";
		int param1 = 0;
		String paramString1 = "0";
		op = args[0];
		if (args[1].equals("cul")) {
			dataset = "citeulike_wiki";
			samplePath = "cul_core/cul_sample";
			sampleDir = "cul_core";
		} else if (args[1].equals("flickr")) {
			dataset = "flickr_wiki";
			samplePath = "flickr_core/flickr_sample";
			sampleDir = "flickr_core";
		} else if (args[1].equals("bib")) {
			dataset = "bib_wiki";
			samplePath = "bib_core/bib_sample";
			sampleDir = "bib_core";
		} else {
			System.out.println("Dataset not available");
			return;
		}
		if (args.length > 2) {
			paramString1 = args[2];
			try {
				param1 = Integer.parseInt(paramString1);
			} catch (Exception e) {
				param1 = 0;
			}
		}
		int sampleCount = 1;
		if (op.equals("split")) {
			System.out.println("Start splitting");
			if (args.length == 3) {
				WikipediaSplitter.splitSample(dataset, samplePath, sampleCount,
						param1);
			} else if (args.length == 5) {
				WikipediaSplitter.splitSample(dataset, samplePath, sampleCount,
						param1, Integer.parseInt(args[3]),
						Integer.parseInt(args[4]));
			}
		} else if (op.equals("cf")) {
			startCfTagCalculator(sampleDir, samplePath, sampleCount, 20, -5);
		} else if (op.equals("fr")) {
			startFolkRankCalculator(sampleDir, samplePath, sampleCount);
		} else if (op.equals("bll_c")) {
			startActCalculator(sampleDir, samplePath, sampleCount, -5, -5,
					true);
		} else if (op.equals("girptm")) {
			startRecCalculator(sampleDir, samplePath);
		} else if (op.equals("mp_ur")) {
			startModelCalculator(sampleDir, samplePath, sampleCount, -5);
		} else if (op.equals("mp")) {
			startBaselineCalculator(sampleDir, samplePath, sampleCount);
		}
	}

	private static void startActCalculator(String sampleDir, String sampleName,
			int sampleCount, int dUpperBound, int betaUpperBound, boolean all) {
		getTrainTestSize(sampleName + "_" + 1);
		List<Integer> dValues = getBetaValues(dUpperBound);
		List<Integer> betaValues = getBetaValues(betaUpperBound);

		for (int i = 1; i <= sampleCount; i++) {
			for (int dVal : dValues) {
				ActCalculator.predictSample(sampleName + "_" + i, TRAIN_SIZE,
						TEST_SIZE, true, false, dVal, 5);
				writeMetrics(sampleDir, sampleName,
						"useract_" + 5 + "_" + dVal, sampleCount, 10, null);
				if (all) {
					for (int betaVal : betaValues) {
						ActCalculator.predictSample(sampleName + "_" + i,
								TRAIN_SIZE, TEST_SIZE, true, true, dVal,
								betaVal);
						writeMetrics(sampleDir, sampleName, "act_" + betaVal
								+ "_" + dVal, sampleCount, 10, null);
					}
					ActCalculator.predictSample(sampleName + "_" + i,
							TRAIN_SIZE, TEST_SIZE, false, true, dVal, 5);
					writeMetrics(sampleDir, sampleName, "resact_" + 5 + "_"
							+ dVal, sampleCount, 10, null);
				}
			}
		}
		// n, p, q
	}

	private static void startRecCalculator(String sampleDir, String sampleName) {
		getTrainTestSize(sampleName + "_" + 1);
		RecCalculator.predictSample(sampleName + "_" + 1, TRAIN_SIZE,
				TEST_SIZE, true, false);
		writeMetrics(sampleDir, sampleName, "userrec", 1, 10, null);
		RecCalculator.predictSample(sampleName + "_" + 1, TRAIN_SIZE,
				TEST_SIZE, true, true);
		writeMetrics(sampleDir, sampleName, "rec", 1, 10, null);
		// l, m
	}

	private static void startModelCalculator(String sampleDir,
			String sampleName, int sampleCount, int betaUpperBound) {
		getTrainTestSize(sampleName + "_" + 1);
		List<Integer> betaValues = getBetaValues(betaUpperBound);

		for (int i = 1; i <= sampleCount; i++) {
			LanguageModelCalculator.predictSample(sampleName + "_" + i,
					TRAIN_SIZE, TEST_SIZE, true, false, 5);
			LanguageModelCalculator.predictSample(sampleName + "_" + i,
					TRAIN_SIZE, TEST_SIZE, false, true, 5);
		}
		writeMetrics(sampleDir, sampleName, "usermodel_" + 5, sampleCount, 10,
				null);
		writeMetrics(sampleDir, sampleName, "resmodel_" + 5, sampleCount, 10,
				null);
		for (int beta : betaValues) {
			for (int i = 1; i <= sampleCount; i++) {
				LanguageModelCalculator.predictSample(sampleName + "_" + i,
						TRAIN_SIZE, TEST_SIZE, true, true, beta);
			}
			writeMetrics(sampleDir, sampleName, "model_" + beta, sampleCount,
					10, null);
		}
		// b, c, d
	}

	private static void startCfTagCalculator(String sampleDir,
			String sampleName, int sampleCount, int neighbors,
			int betaUpperBound) {
		getTrainTestSize(sampleName + "_" + 1);
		List<Integer> betaValues = getBetaValues(betaUpperBound);
		for (int i = 1; i <= sampleCount; i++) {
			BM25Calculator.predictTags(sampleName + "_" + i, TRAIN_SIZE,
					TEST_SIZE, neighbors, true, false, 5);
			BM25Calculator.predictTags(sampleName + "_" + i, TRAIN_SIZE,
					TEST_SIZE, neighbors, false, true, 5);
		}
		writeMetrics(sampleDir, sampleName, "usercf_" + 5, sampleCount, 10,
				null);
		writeMetrics(sampleDir, sampleName, "rescf_" + 5, sampleCount, 10, null);
		for (int beta : betaValues) {
			for (int i = 1; i <= sampleCount; i++) {
				BM25Calculator.predictTags(sampleName + "_" + i, TRAIN_SIZE,
						TEST_SIZE, neighbors, true, true, beta);
			}
			writeMetrics(sampleDir, sampleName, "cf_" + beta, sampleCount, 10,
					null);
		}
		// e, f, g
	}

	private static void startFolkRankCalculator(String sampleDir,
			String sampleName, int size) {
		getTrainTestSize(sampleName + "_" + 1);
		for (int i = 1; i <= size; i++) {
			FolkRankCalculator.predictSample(sampleName + "_" + i, TRAIN_SIZE,
					TEST_SIZE, true);
		}
		writeMetrics(sampleDir, sampleName, "fr", size, 10, null);
		writeMetrics(sampleDir, sampleName, "pr", size, 10, null);
		// "k_fr", "j_pr"
	}

	private static void startBaselineCalculator(String sampleDir,
			String sampleName, int size) {
		getTrainTestSize(sampleName + "_" + 1);
		for (int i = 1; i <= size; i++) {
			BaselineCalculator.predictPopularTags(sampleName + "_" + i,
					TRAIN_SIZE, TEST_SIZE);
		}
		writeMetrics(sampleDir, sampleName, "pop", size, 10, null);
	}

	// Helpers
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------
	private static void writeMetrics(String sampleDir, String sampleName,
			String prefix, int sampleCount, int k, String posfix) {
		String topicString = ((posfix == null || posfix == "0") ? "" : "_"
				+ posfix);
		for (int i = 1; i <= k; i++) {
			for (int j = 1; j <= sampleCount; j++) {
				// MetricsCalculator.calculateMetrics(sampleName + "_" + j +
				// topicString + "_res_" + prefix, i, null, false);
				MetricsCalculator.calculateMetrics(sampleName + "_" + j
						+ topicString + "_res_" + prefix, i, sampleDir + "/"
						+ prefix + topicString + "_metrics", false);
			}
			MetricsCalculator.writeAverageMetrics(sampleDir + "/" + prefix
					+ topicString + "_metrics", i, (double) sampleCount);
		}
		MetricsCalculator.resetMetrics();
	}

	private static List<Integer> getBetaValues(int betaUpperBound) {
		List<Integer> betaValues = new ArrayList<Integer>();
		if (betaUpperBound < 0) {
			betaValues.add(betaUpperBound * (-1));
		} else {
			for (int betaVal = 1; betaVal <= betaUpperBound; betaVal++) {
				betaValues.add(betaVal);
			}
		}
		return betaValues;
	}

	private static void getStatistics(String dataset) {
		WikipediaReader reader = new WikipediaReader(0, false);
		reader.readFile(dataset);
		int bookmarks = reader.getUserLines().size();
		System.out.println("Bookmarks: " + bookmarks);
		int users = reader.getUsers().size();
		System.out.println("Users: " + users);
		int resources = reader.getResources().size();
		System.out.println("Resources: " + resources);
		int tags = reader.getTags().size();
		System.out.println("Tags: " + tags);
		int tagAssignments = reader.getTagAssignmentsCount();
		System.out.println("Tag-Assignments: " + tagAssignments);

		reader = new WikipediaReader(0, false);
		reader.readFile(dataset + "_test");
		System.out.println("Max P@10: "
				+ (double) reader.getTagAssignmentsCount()
				/ (double) reader.getUserLines().size() / 10.0);
	}

	private static void getTrainTestSize(String sample) {
		WikipediaReader trainReader = new WikipediaReader(-1, false);
		trainReader.readFile(sample + "_res_train");
		TRAIN_SIZE = trainReader.getUserLines().size();
		System.out.println("Train-size: " + TRAIN_SIZE);
		WikipediaReader testReader = new WikipediaReader(-1, false);
		testReader.readFile(sample + "_res_test");
		TEST_SIZE = testReader.getUserLines().size();
		System.out.println("Test-size: " + TEST_SIZE);
	}
}
