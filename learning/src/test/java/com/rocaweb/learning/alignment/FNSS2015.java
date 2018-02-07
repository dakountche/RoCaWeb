/*******************************************************************************
 * This file is part of RoCaWeb.
 * 
 *  RoCaWeb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package com.rocaweb.learning.alignment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.rocaweb.learning.algorithms.alignment.multipleSequence.AMAA;
import com.rocaweb.learning.algorithms.statistical.AttributeLength;
import com.rocaweb.learning.data.statistical.ALengthStatistics;
import com.rocaweb.learning.evaluation.RegexBasedClassifier;
import com.rocaweb.learning.evaluation.StatisticalContractBasedClassifier;
import com.rocaweb.learning.input.Preprocessing;
import com.rocaweb.learning.rules.Regex;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomSeedGenerator;

/**
 * This class contains the experimentations the FNSS 2015 conference.
 * 
 * @author Amadou Kountché Djibrilla
 *
 */
public class FNSS2015 {

	private static Logger logger = LogManager.getLogger(FNSS2015.class);
	private String DATA_PATH = "src/test/resources/fnss2015/";
	private String algorithmsConfig = DATA_PATH + "conf/";
	private int savingRation = 10;
	private int numberOfExperimentation = 101;
	private String format = "%s\t%s\t%s\t%s\t%s\n";
	private String csvFormat = "%s,%s,%s,%s\n";

	private final Preprocessing pre = new Preprocessing();
	private Map<String, List<Set<String>>> evaldata = null;

	public static void main(String[] args) {
		(new FNSS2015()).run();
	}

	public void run() {

		try {
			fillEvalData(DATA_PATH + "eval");
		} catch (IOException e) {

			e.printStackTrace();
		}
		logger.info("Eval: " + evaldata);

		this.testCats();

		try {
			sameSize(DATA_PATH + "samesize");
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		try {
			kmeans(DATA_PATH + "clustering/clustering");
		} catch (IOException e) {

			e.printStackTrace();
		}

		testAMAA();

	}

	public void testCats() {

		String regex = "[ ADEFGILR]{0,9}TH[ AELRSTVY]{1,6} FA[S]{0,1}T CAT";
		Set<String> cats = Sets.newHashSet(InputAlignments.garfield);
		Set<String> nonCats = Sets.newHashSet();
		nonCats.add("HoHa");
		nonCats.add("THIS FAT CAT");
		nonCats.add("Djibrilla");

		RegexBasedClassifier eval = new RegexBasedClassifier(cats, nonCats, regex);
		System.out.println(eval.toString());

	}

	public static void println(Iterable<String> result, String message) {
		System.out.println("\nAlignement " + message);
		for (String string : result) {
			System.out.println(string);
		}
	}

	public void sameSize(String fileName) throws IOException {
		File file = new File(fileName);

		RandomSeedGenerator rsg = new RandomSeedGenerator();
		int seed = rsg.nextSeed();
		int seed1 = rsg.nextSeed();
		int seed2 = rsg.nextSeed();
		logger.info(seed + " " + seed1 + " " + seed2);
		Uniform ma = new Uniform(1, 10, seed);
		Uniform mi = new Uniform(-1, 0, seed1);
		Uniform p = new Uniform(-2, -1, seed2);

		logger.info(String.format(format, "Iteration", "Regex", "Match", "Mistmatch", "Penalty"));
		for (File subFile : file.listFiles()) {

			AMAA amaa = createAmaaInstance();
			AttributeLength al = new AttributeLength();

			List<String> list = pre.readFile(subFile.getAbsolutePath(), false);

			List<Set<String>> evalList = evaldata.get(subFile.getName());

			System.out.println("Data set : " + subFile.getName() + " : taille : " + list.size()

					+ " : "
					+ Regex.getInstance().generateBRE(list, amaa.getPairAlignmentAlgorithm().getGapCharacter()));

			String csvResult = String.format(csvFormat, "iteration", "fscore", "tp", "fp", "accuracy");
			for (int i = 0; i < numberOfExperimentation; i++) {
				RegexBasedClassifier eval = new RegexBasedClassifier(evalList.get(1), evalList.get(0));
				StatisticalContractBasedClassifier sbc = new StatisticalContractBasedClassifier(evalList.get(1),
						evalList.get(0));
				double match = ma.nextDouble();
				double mistmatch = mi.nextDouble();
				double penalty = p.nextDouble();

				amaa.getPairAlignmentAlgorithm().setMatch(match);
				amaa.getPairAlignmentAlgorithm().setPenalty(penalty);
				amaa.getPairAlignmentAlgorithm().setMismatch(mistmatch);
				amaa.getPairAlignmentAlgorithm().getParameters().setLearningFile(subFile.getAbsolutePath());

				List<String> copy = Lists.newArrayList(list);
				List<String> result = Lists.newArrayList(amaa.align(copy));
				// System.out.println(result);
				String regex = Regex.getInstance().generateBRE(result,
						amaa.getPairAlignmentAlgorithm().getGapCharacter());
				String string = String.format(format, "" + i, regex, format(match), format(mistmatch), format(penalty));
				System.out.print(string);

				eval.setPattern(regex);

				if (i % savingRation == 0) {
					csvResult += String.format(csvFormat, "" + i, format(eval.getFMeasure()), format(eval.getFPRate()),
							format(eval.getTPRate()));

				}

				ALengthStatistics ls = al.process(list);
				sbc.setPattern(ls);
				System.out.println(sbc.getFMeasure());

			}

			System.out.println(csvResult);
			System.out.println(amaa.getTimeStatistics());

		}

	}

	public void testAMAA() {

		List<String> lists = Lists.newArrayList(InputAlignments.garfield);
		AMAA amaa = createAmaaInstance();
		amaa.getPairAlignmentAlgorithm().setGapCharacter('+');

		System.out.println(amaa.align(lists));

	}

	public void kmeans(String evalData) throws IOException {
		List<String> set = pre.readFile((new File(evalData)).getAbsolutePath(), false);

		for (Set<String> ck : pre.createCluster(set)) {
			System.out.println(ck);

			AMAA amaa = createAmaaInstance();

			List<String> list = Lists.newArrayList(ck);
			List<String> result = Lists.newArrayList(amaa.amaa(list));
			String rule = Regex.getInstance().generateBRE(result, amaa.getPairAlignmentAlgorithm().getGapCharacter());
			System.out.println(rule);
			for (String string : evaldata.keySet()) {
				List<Set<String>> evalList = evaldata.get(string);
				RegexBasedClassifier eval = new RegexBasedClassifier(evalList.get(1), evalList.get(0));
				eval.setPattern(rule);
				System.out.print("&" + format(eval.getFMeasure()) + "&" + format(eval.getAccuracy()) + "&"
						+ format(eval.getFPRate()) + "&" + format(eval.getTPRate()));

			}
			System.out.print("\\\\");
			System.out.println();
			System.out.println("\\hline");
		}

	}

	private String format(double value) {
		return String.format(Locale.US, "%.2f", value);
	}

	public void fillEvalData(String evalData) throws IOException {

		evaldata = new HashMap<String, List<Set<String>>>();
		File file = new File(evalData);
		if (file.exists() && file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				ArrayList<Set<String>> datas = new ArrayList<Set<String>>();
				for (File dataset : subFile.listFiles()) {
					Set<String> set = Sets.newHashSet(pre.readFile(dataset.getAbsolutePath(), false));
					datas.add(set);
				}
				evaldata.put(subFile.getName(), datas);
			}
		}

	}

	public Map<String, List<Set<String>>> getEval() throws IOException {
		if (evaldata == null) {
			fillEvalData(DATA_PATH + "eval");
		}
		return evaldata;
	}

	public AMAA createAmaaInstance() {
		return new AMAA(this.algorithmsConfig + "amaa.json");
	}

}
