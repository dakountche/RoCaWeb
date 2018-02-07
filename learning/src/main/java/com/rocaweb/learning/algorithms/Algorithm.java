/** 
* This file is part of RoCaWeb.
*
* RoCaWeb is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package com.rocaweb.learning.algorithms;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.rocaweb.commons.configuration.CommonConfiguration;
import com.rocaweb.commons.fs.FSManager;
import com.rocaweb.learning.evaluation.TimeStatistics;
import com.rocaweb.learning.input.Preprocessing;
import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;
import com.rocaweb.learning.rules.Contract;

/**
 * 
 * This abstract class represents all the algorithms used in the learning
 * module. An algorithm can run on a single file or recursively on a directory.
 * It can also clustered the file into many clusters.
 * 
 * An algorithm has 3 phases :
 * 
 * <ol>
 * <li>The preprocessing</li>
 * <li>Process</li>
 * <li>The post process</li>
 * </ol>
 * 
 * <p>
 * <h3>The preprocessing
 * </p>
 * This phase is algorithm dependent. It might consist of:
 * <ul>
 * <li>Cleaning the redundant data</li>
 * <li>Lower case or Upper case</li>
 * <li>Change the encoding</li>
 * <li>Clustering the data</li>
 * </ul>
 * </p>
 * 
 * <p>
 * <h3>Processing</h3> At this phase the algorithm made its computing. For
 * example:
 * <ul>
 * <li>Sequence alignment</li>
 * <li>Grammatical inference</li>
 * <li>CrossValidation</li>
 * <li>Statistical methods</li>
 * <li>Machine learning</li>
 * </ul>
 * </p>
 * 
 * 
 * <p>
 * <h3>Post-processing</h3> The post processing is mostly the Rule generation.
 * </p>
 * </p>
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 * 
 */

public abstract class Algorithm<T> implements Callable<Contract<T>> {

	protected transient Logger logger = LogManager.getLogger(getClass());
	private transient String configFileName = null;
	private transient String fileName = null;
	private transient TimeStatistics timeStats = null;	
	private transient Preprocessing pre = null;

	public Algorithm() {}

	/**
	 * Create an instance using a configuration file.
	 * @param configFileName the configuration file
	 */
	public Algorithm(String configFileName) {
		setConfigFileName(configFileName);
		loadConfiguration(configFileName);
	}

	/**
	 * This abstract method is where the <code>Algorithm</code> made its main
	 * processing.
	 * @param sequences learning set.
	 * @return the result of the processing of type T.
	 */
	public abstract T process(List<String> sequences);

	/**
	 * Getter of the parameter.
	 * @return the parameters of the algorithm.
	 */
	public abstract AbstractAlgorithmParameters getParameters();

	
	/**
	 * Load the configuration from a XML file.
	 * @param configFilePath the path to the XML file.
	 */
	public void loadConfiguration(String configFilePath) {
		if (this.getConfigFileName() == null) {
			this.setConfigFileName(configFilePath);
			this.getParameters();
		} else
			getParameters().loadConfig(configFilePath);
	}

	/**
	 * Cluster the input set before applying the
	 * <code>process</code> method on the partition of <code>k</code> clusters.
	 * 
	 * @param sequences the learning set.
	 * @return a Map having as <code>key</code> the number of clusters and
	 *         <code>values</code> for each cluster.
	 */
	private Map<Integer, T> processClustering(List<String> sequences) {
		Set<Set<String>> dendro = determineClusters(sequences);
		return createMapForClusteringResult(dendro);
	}

	/**
	 * Creates a map, given a clustering, to be used by the processing
	 * method.
	 * 
	 * @param partition the result of the clustering.
	 * @return all the clusters.
	 */
	private Map<Integer, T> createMapForClusteringResult(Set<Set<String>> partition) {
		Map<Integer, T> map = new HashMap<Integer, T>();
		/* This loop starts with k=1 because the k=0 contains all the data sets */
		for (int k = 1; k <= getParameters().getClusterNumber(); ++k) {
			for (Set<String> set : partition) {
				T processReturn = process(Lists.newArrayList(set));
				map.put(k, processReturn);
			}
		}
		return map;
	}

	private Set<Set<String>> determineClusters(List<String> inputs) {
		return getPreprocessing().createCluster(inputs);
	}

	/**
	 * Preprocess a file given the parameters. To recall, these processing can be:
	 * <ol>
	 * <li>Removing redundant elements</li>
	 * <li>Setting to lower or upper case</li>
	 * <li>Clustering</li>
	 * <li>etc. </li>
	 * </ol>
	 * 
	 * @param directory
	 * @return the sequences.
	 * @throws IOException
	 */
	private List<String> preprocess(FileObject directory) throws IOException {
		return getPreprocessing().readFile(directory, getParameters().hasCleanning(), getParameters().hasDecode(),
				getParameters().getBase(), getParameters().hasCasse());
	}

	/**
	 * Apply the preprocessing defined by the parameters
	 * 
	 * @param sequences the list odd sequences to preprocess
	 * @return the preprocessed list.
	 */
	public List<String> preprocess(List<String> sequences) {
		return getPreprocessing().filter(sequences, getParameters().hasCleanning(), getParameters().hasDecode(),
				getParameters().getBase(), getParameters().hasCasse());
	}

	public Preprocessing getPreprocessing() {
		if (pre == null) {
			pre = new Preprocessing();
		}
		return pre;
	}

	/**
	 * Print the a message to describe the algorithm name and its parameters.
	 * 
	 * @return a string containing the message.
	 */
	public String printWelcomeMsg() {
		String algoname = getClass().getName();
		String title = CommonConfiguration.ALGO_MSG_TITLE;
		StringBuilder message = new StringBuilder();
		message.append(StringUtils.repeat("#", algoname.length() + title.length()) + '\n');
		message.append(title + algoname + "\n");
		message.append(CommonConfiguration.ALGO_MSG_LEARN + getParameters().getLearningFile() + "\n");
		message.append(CommonConfiguration.ALGO_MSG_CONFIG+ this.getParameters().getConfigFileName() + "\n");
		message.append(CommonConfiguration.ALGO_MSG_PARAM);
		message.append(getParameters().printParameters());

		return message.toString();
	}

	/**
	 * Determines a contract given a file containing the learning data.
	 * 
	 * @param learningDataSource
	 * @throws IOException
	 * 
	 * TODO this method can be adapted to MapReduce or Spark
	 * 
	 */
	private void runAlgorithmAndGenerateContract(FileObject learningDataSource) throws IOException {

		if (learningDataSource == null)
			learningDataSource = FSManager.resolveFile(getParameters().getLearningFile());

		if (learningDataSource.getType() == FileType.FOLDER) {
			recurseOnSubDirectories(learningDataSource);
		} else {
			long start = System.currentTimeMillis();
			T resutlSequences = null;
			Map<Integer, T> map = null;
			List<String> sequences = preprocess(learningDataSource);
			
			if (sequences.isEmpty()) {
				logger.warn(String.format(CommonConfiguration.ALGO_EMPTY_FILE,  learningDataSource.getURL().getFile()));
				return;
			}
			fileName = learningDataSource.getURL().getPath();
			logger.trace(fileName);

			if (!getParameters().hasClustering()) {
				resutlSequences = process(sequences);
				getContract().addToResultToFormat(fileName, resutlSequences);
			} else {
				map = processClustering(sequences);
				getContract().addToClusteringResult(fileName, map);
			}
			long end = System.currentTimeMillis();
			getTimeStatistics().update(end - start);
		}

	}

	/**
	 * Runs the main processing of the algorithm.
	 * 
	 * @param learningDataSource an URL to the resource containing the learning base.
	 * @throws IOException
	 */
	private void runAlgorithmAndGenerateContract(String learningDataSource) throws IOException {
		logger.trace(learningDataSource);
		getParameters().setLearningFile(learningDataSource);
		FileObject fo = FSManager.resolveFile(learningDataSource);
		runAlgorithmAndGenerateContract(fo);
	}

	
	private void recurseOnSubDirectories(FileObject directory) throws IOException {
		FileObject[] subfiles = directory.getChildren();
		if (subfiles != null) {
			for (FileObject file : subfiles) {
				runAlgorithmAndGenerateContract(file);
			}
		} else {
			logger.error(String.format(CommonConfiguration.ALGO_FILE_ERROR, FSManager.getURL(directory).toString()));
		}
	}

	
	private String initializePath() {
		String learningFile = this.getParameters().getLearningFile();
		String learningPath = this.getParameters().getLearningPath();

		if (learningFile.contains(learningPath))
			return learningFile;
		else if (learningFile.isEmpty())
			return learningPath;
		else
			return learningPath + CommonConfiguration.FILE_SEPARATOR + learningFile;
	}

	/**
	 * A generic method used by sub-algorithms to create specific contact.
	 * 
	 * @return the contract generated by the algorithms
	 */
	public abstract Contract<T> getContract();

	
	@Override
	public Contract<T> call() throws IOException {
		runAlgorithmAndGenerateContract(initializePath());
		return getContract();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	/**
	 * @return the configFileName
	 */
	public String getConfigFileName() {
		return configFileName;
	}

	/**
	 * @param configFileName
	 *            the configFileName to set
	 */
	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	/** Set the default parameters */
	public abstract void setDefaultParameters();

	/** Serialize the parameters to JSon */
	public String toJSon() {
		return getParameters().toJson();
	}

	/**
	 * 
	 * @return the formatting character associated to each algorithm.
	 */
	public abstract char getFormatChar();

	/**
	 * Use to test the result of the processing.
	 * 
	 * @param source a set of strings
	 * @param target are the strings to be processed and then compare to the source.
	 * @return a boolean
	 */
	public boolean validate(List<String> source, List<String> target) {
		boolean res = true;
		T result = process(target);
		for (String s : source) {
			if (!getContract().validate(s, result)) {
				res = false;
				break;
			}
		}
		return res;
	}

	public String getFileName() {
		if (fileName != null) {
			if (fileName.contains(":")) {
				String[] files = fileName.split(":");
				fileName = files[files.length - 1];
			}
		}
		return fileName;
	}

	/**
	 * @param parameters
	 */
	public abstract void setParameters(AbstractAlgorithmParameters parameters);

	/**
	 * @param jsonElement
	 */
	public void loadConfiguration(JsonElement jsonElement) {
		this.setDefaultParameters();
		this.getParameters().loadConfig(jsonElement);
	}

	public TimeStatistics getTimeStatistics() {
		if (timeStats == null) {
			this.timeStats = new TimeStatistics();
		}
		return timeStats;
	}

	/**
	 * Method use for experimenting this <code>Algorithm</code>
	 * @param sequence the sequence to evaluate
	 * @return the model.
	 */
	public T expreriment(List<String> sequences) {
		List<String> filtered = this.preprocess(sequences);
		long start = System.currentTimeMillis();
		T result = this.process(filtered);
		long end = System.currentTimeMillis();
		getTimeStatistics().update(end - start);
		return result;
	}

}