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
/**
 * 
 */
package com.rocaweb.learning.experimentations;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rocaweb.commons.fs.FSManager;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.algorithms.alignment.multipleSequence.AbstractMultipleAlignmentAlgorithm;
import com.rocaweb.learning.algorithms.metalearning.CrossValidation;
import com.rocaweb.learning.algorithms.classification.AbstractWekaClassificationAlgorithm;
import com.rocaweb.learning.algorithms.grammatical.AbstractGrammaticalAlgorithm;
import com.rocaweb.learning.algorithms.statistical.AbstractStatisticalAlgorithm;
import com.rocaweb.learning.algorithms.typing.RegexLearner;
import com.rocaweb.learning.evaluation.AbstractEvaluation;
import com.rocaweb.learning.evaluation.RegexBasedClassifier;
import com.rocaweb.learning.evaluation.StatisticalContractBasedClassifier;
import com.rocaweb.learning.evaluation.WekaEvaluation;
import com.rocaweb.learning.input.Preprocessing;
import com.rocaweb.learning.parameters.LearningJobParameters;
import com.rocaweb.learning.utils.Worker;

import weka.core.Instances;

/**
 * Represent an experience. We distinguish 3 cases :
 * 
 * <ul>
 * <li>Parameter</li>
 * <li>Data</li>
 * <li>Algorithm</li>
 * 
 * <p>
 * For the first case,the performances of an algorithm are measured on a given
 * data set, by varying the parameters.
 * 
 * <p>
 * The second case, evaluates the performances by varying the size of the data
 * set.
 * 
 * <p>
 * Finally the third case evaluates all the algorithms against each other.
 * </p>
 * </ul>
 * 
 * @author Djirilla AMADOU KOUNTCHE
 * 
 * @since 1.0.0
 * 
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Experience {

	/**
	 * The output file for each experimentation
	 */
	private String outputFile = "/tmp/report";

	/** Algorithms to test */

	private Preprocessing pre = new Preprocessing();

	/**
	 * The number of iterations
	 */
	private int iterations = 0;

	/**
	 * Indicates how often snapshots must be take for constructing a graph.
	 */
	private int ratio = 1;

	/**
	 * Indicates the data source to use for the training can be a file or a
	 * directory containing the training and the test data sets.
	 */
	private String dataSource = "src/test/resources/rapport";

	/**
	 * Every parametric method will use an automatic generation of the case related
	 * to this experimentation. It means for example, that when applicable, the
	 * parameters of the algorithm or the data will be generated automatically.
	 * 
	 * This flag is used to avoid to the user to put the value manually.
	 */
	private boolean auto = true;

	/**
	 * The configuration file path.
	 */
	private String configFile = "src/test/resources/json/experience.json";

	/**
	 * Parameters of the experimentation.
	 */
	private LearningJobParameters parameters = null;

	private static Logger logger = LogManager.getLogger(Experience.class);

	/**
	 * Default constructor
	 */
	public Experience() {

	}

	/**
	 * Executes the experimentation scenario.
	 */
	public void run() {

		for (Entry<String, DataSet> entry : getDataSet().entrySet()) {
			int currentDataSet = 0;
			StringBuilder algorithmCSv = new StringBuilder("datasetsize,algorithm,fp,fn,tn,tp,time(ms)\n");
			for (List<String> training : entry.getValue().getTrainings()) {
				currentDataSet = training.size();

				for (Algorithm algorithm : this.getAlgorithms()) {

					StringBuilder csv = new StringBuilder("iteration,fp,fn,tn,tp,time(ms)\n");
					StringBuilder classes = new StringBuilder("sequence,class\n");
					AbstractEvaluation evaluator = createClassifier(algorithm, entry.getValue().getTestSet());

					for (int i = 0; i <= this.getIterations(); i++) {

						if (isAuto())
							algorithm.getParameters().randomlyGenerateParamValues();
						logger.info("\n" + algorithm.printWelcomeMsg());

						Object model = algorithm.expreriment(training);
						if (model instanceof Iterable)
							model = algorithm.getContract().createRule(model);

						System.out.println("Generated model : \t" + model.toString());
						classes.append(model.toString() + ",contract\n");
						evaluator.setPattern(model);

						csv.append(toCSV(i, evaluator) + ',' + algorithm.getTimeStatistics().toString() + "\n");
						algorithmCSv.append(currentDataSet + "," + algorithm.getClass().getSimpleName() + ","
								+ evaluator.toString() + ',' + algorithm.getTimeStatistics().toString() + "\n");
						classes.append(evaluator.getClassification());
					}
					String filepath = this.getOutputFile() + "/" + entry.getKey() + "/" + currentDataSet + "/"
							+ algorithm.getClass().getSimpleName() + "/";

					write(filepath + "evals.csv", csv.toString());
					write(filepath + "classes.csv", classes.toString());
				}

			}
			String filepath = this.getOutputFile() + "/" + entry.getKey() + "/" + "dataset.csv";
			write(filepath, algorithmCSv.toString());

		}

	}

	/**
	 * Create a line containing the value of the performances measures
	 * 
	 * @param i
	 *            the current iteration
	 * @param evaluator
	 *            the evaluator
	 * @return a CSV line
	 */
	private String toCSV(int i, AbstractEvaluation evaluator) {
		StringBuilder csvline = new StringBuilder();

		if (i % this.getRatio() == 0) {
			csvline.append(i + "," + evaluator.toString());
		}
		return csvline.toString();
	}

	/**
	 * Get the data set used for the learning
	 * 
	 * @return
	 */
	private Map<String, DataSet> getDataSet() {
		Map<String, DataSet> dataset = Maps.newHashMap();
		try {
			dataset = fillEvalData(getDataSource());
		} catch (IOException e) {

			logger.error(e.getMessage(),e);

		}
		return dataset;
	}

	@Override
	public String toString() {

		return "";
	}

	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return iterations;
	}

	/**
	 * @param iterations
	 *            the iterations to set
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	/**
	 * @return the algorithms
	 */
	public List<Algorithm> getAlgorithms() {
		return this.getParameters().getAlgorithms();
	}

	/**
	 * @param algorithms
	 *            the algorithms to set
	 */
	public void setAlgorithms(List<Algorithm> algorithms) {
		this.getParameters().setAlgorithms(algorithms);

	}

	/**
	 * Creates the appropriate validator for the given algorithm.
	 * 
	 * @param algorithm
	 *            the candidate algorithm to evaluate
	 * @param value
	 * @return
	 */
	protected AbstractEvaluation createClassifier(Algorithm algorithm, Pair<HashSet<String>, HashSet<String>> pair) {
		AbstractEvaluation evaluator = null;

		if (algorithm instanceof AbstractStatisticalAlgorithm || algorithm instanceof AbstractGrammaticalAlgorithm) {
			evaluator = new StatisticalContractBasedClassifier(pair.getRight(), pair.getLeft());
		} else if (algorithm instanceof AbstractMultipleAlignmentAlgorithm || algorithm instanceof RegexLearner
				|| algorithm instanceof CrossValidation) {
			evaluator = new RegexBasedClassifier(pair.getRight(), pair.getLeft());
		} else if (algorithm instanceof AbstractWekaClassificationAlgorithm) {
			AbstractWekaClassificationAlgorithm algo = (AbstractWekaClassificationAlgorithm) algorithm;
			Instances testInst = algo.createTestSet(Lists.newArrayList(pair.getRight()),
					Lists.newArrayList(pair.getLeft()));
			evaluator = new WekaEvaluation(algo.getInstances(), testInst);
		}
		return evaluator;
	}

	/**
	 * Build the evaluation data set.
	 * 
	 * @param evalData
	 *            the path to the learning base.
	 * 
	 * @return a map containing the data set for each case.
	 * 
	 * @throws IOException
	 */
	public Map<String, DataSet> fillEvalData(String evalData) throws IOException {

		Map<String, DataSet> evaldata = new HashMap<String, DataSet>();
		List<Set<String>> tests = Lists.newArrayList();
		List<List<String>> training = Lists.newArrayList();

		File file = new File(evalData);
		if (file.exists() && file.isDirectory()) {

			for (File subFile : file.listFiles()) {
				DataSet datas = new DataSet();
				for (File dataset : subFile.listFiles()) {
					if (!dataset.isDirectory()) {
						Set<String> set = Sets
								.newHashSet(getPreprocessing().readFile(dataset.getAbsolutePath(), false));
						tests.add(set);
					}

					else {
						for (File subsubFile : dataset.listFiles()) {
							List<String> set = getPreprocessing().readFile(subsubFile.getAbsolutePath(), false);
							training.add(set);
						}
					}
				}
				datas.setTestSet(Pair.of(new HashSet<String>(tests.get(1)), new HashSet<String>(tests.get(0))));
				datas.setTrainings(Lists.newArrayList(training));
				evaldata.put(subFile.getName(), datas);
				training.clear();
				tests.clear();
			}
		}
		return evaldata;
	}

	/**
	 * @return the auto
	 */
	public boolean isAuto() {
		return auto;
	}

	/**
	 * @param auto
	 *            the auto to set
	 */
	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	/**
	 * @return the ratio
	 */
	public int getRatio() {
		return ratio;
	}

	/**
	 * @param ratio
	 *            the ratio to set
	 */
	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	/**
	 * @return the outputFile
	 */
	public String getOutputFile() {
		return outputFile;
	}

	/**
	 * @param outputFile
	 *            the outputFile to set
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	private void write(String filepath, String content) {
		FileObject fo = FSManager.resolveFile(filepath);
		FSManager.write(content, fo);
		FSManager.close(fo);
	}

	/**
	 * @return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public static void main(String[] args) {
		Experience exp = new Experience();

		try {
			logger.trace(exp.fillEvalData(exp.getDataSource()));
		} catch (IOException e) {

			logger.error(e.getMessage());
		}

		exp.run();

	}

	/**
	 * @return the parameters
	 */
	public LearningJobParameters getParameters() {
		if (parameters == null) {
			try {
				parameters = Worker.getLearningJobParameters(getConfigFile(), "", "");
			} catch (IOException e) {
				logger.error(e.getMessage());
			}

		}
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(LearningJobParameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the configFile
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * @param configFile
	 *            the configFile to set
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	/**
	 * @return the pre
	 */
	public Preprocessing getPreprocessing() {
		return pre;
	}

	/**
	 * @param pre
	 *            the pre to set
	 */
	public void setPreprocessing(Preprocessing pre) {
		this.pre = pre;
	}

}
