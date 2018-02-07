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
package com.rocaweb.learning.rules;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Strings;
import com.rocaweb.commons.fs.FSManager;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.algorithms.alignment.pair.AbstractPairAlignmentAlgorithm;
import com.rocaweb.learning.rules.format.Format;

/**
 * Implements the concept of contract in RoCaWeb.
 * 
 * 
 * <h3>Definitions</h3>
 * 
 * 
 * 
 * @since 1.0.0
 * 
 * @author Djibrila Amadou Kountche
 * 
 */
public abstract class Contract<T> {

	/**
	 * The method used to generate the contract. Example (Alignment, statistics,
	 * etc.)
	 */
	protected Algorithm<T> algorithm = null;

	/**
	 * A data structure to hold the results of the algorithm for the formating step
	 */
	private Map<String, T> resultToFormat = null;

	protected Logger logger = LogManager.getLogger(getClass());

	/** A data structure for the clustering case */
	private Map<String, Map<Integer, T>> clusteringResultToFormat = null;

	/** A virtual file system holding the results. */

	private FileObject fileObject = null;

	private String baseName = null;

	/**
	 * The default constructor
	 */
	public Contract() {}

	/**
	 * A constructor which takes an algorithm as argument.
	 * 
	 * @param algorithm
	 */
	public Contract(Algorithm<T> algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * A generic method, where a sub-class of Contract implements the specific way
	 * to generate a rule
	 * 
	 * @param t
	 *            is the result of the computation of the algorithm
	 * @return a string representing the rule
	 */
	public String createRule(T t) {
		return t.toString();
	}

	@Override
	public String toString() {
		return bruteRegex();
	}

	private String bruteRegex() {
		StringBuilder nonFormated = new StringBuilder();
		for (String entry : getResultToFormat().keySet()) {
			nonFormated.append(entry + " : " + getResultToFormat().get(entry));
		}
		return nonFormated.toString();
	}

	public void addToResultToFormat(String fileName, T value) {

		if (fileName != null && !fileName.isEmpty()) {
			getResultToFormat().put(fileName, value);

			String prefix = FilenameUtils.getPrefix(fileName);
			if (prefix != null && prefix.contains(":"))
				fileName = fileName.substring(prefix.length());

			addToFileObject(fileName, createRule(value));
		}
	}

	public void addToClusteringResult(String fileName, Map<Integer, T> map) {
		this.getClusteringResultToFormat().put(fileName, map);
	}

	public Map<String, T> getResultToFormat() {
		if (resultToFormat == null) {
			resultToFormat = new HashMap<String, T>();
		}
		return resultToFormat;
	}

	public Algorithm<T> getAlgorithm() {

		return algorithm;
	}

	/**
	 * Generate a rule in a given format for a specific algorithm class. For
	 * example, statistic rule formating is different than a regular expression.
	 * 
	 * @return the rule in a specific format.
	 */
	public String generateRule() {

		if (isNomFormatableResult()) {
			return nonFormatedResult();
		} else {
			String ruleType = getAlgorithm().getParameters().getRuleType();

			return format(ruleType);
		}
	}

	/**
	 * @param ruleType
	 * @param learningPathName
	 * @param fileName
	 * @param rule
	 * @return a formated rule
	 */
	public String format(String ruleType) {
		return Format.getFormater(ruleType).format(this);
	}

	private String nonFormatedResult() {
		StringBuilder message = new StringBuilder();
		for (String fileName : getResultToFormat().keySet()) {
			message.append(this.getAlgorithm().printWelcomeMsg() + "\n");
			message.append((this.getResultToFormat().get(fileName)));
			message.append("\n " + Strings.repeat("-", 70) + "\n");
		}
		return message.toString();
	}

	/**
	 * Determines if an algorithms produces formatable rules.
	 * 
	 * @return boolean
	 */
	private boolean isNomFormatableResult() {
		return (getAlgorithm() instanceof AbstractPairAlignmentAlgorithm);

	}

	/**
	 * @return the clusteringResultToFormat
	 */
	public Map<String, Map<Integer, T>> getClusteringResultToFormat() {
		if (clusteringResultToFormat == null) {
			clusteringResultToFormat = new HashMap<String, Map<Integer, T>>();
		}
		return clusteringResultToFormat;
	}

	/**
	 * @param clusteringResultToFormat
	 *            the clusteringResultToFormat to set
	 */
	public void setClusteringResultToFormat(Map<String, Map<Integer, T>> clusteringResultToFormat) {
		this.clusteringResultToFormat = clusteringResultToFormat;
	}

	public String nonFormatedContract() {
		StringBuilder nonFormated = new StringBuilder();
		for (String entry : getResultToFormat().keySet()) {

			nonFormated.append("\n" + entry + " : " + this.createRule(getResultToFormat().get(entry)));
		}
		return nonFormated.toString();
	}

	public FileObject getFileObject() {
		if (fileObject == null) {
			baseName = "tmp:///" + "" + this.hashCode();
			fileObject = FSManager.createFolder(baseName);

		}
		return fileObject;
	}

	private void addToFileObject(String FileName, String content) {
		String host = FilenameUtils
				.getName(FilenameUtils.getPathNoEndSeparator(getAlgorithm().getParameters().getLearningPath()));

		String[] list = FileName.split(host);
		logger.trace(host);
		String RelativeName = list[list.length - 1];

		logger.trace(RelativeName);
		String fileToAddName = "tmp:///" + FSManager.getURL(getFileObject()).getPath() + RelativeName;

		FileObject fileToAdd = FSManager.createFile(fileToAddName);

		FSManager.write(content, fileToAdd);
		FSManager.close(fileToAdd);

	}

	/**
	 * @param fileName
	 * @return
	 */
	public String fetchRule(String fileName) {
		return this.createRule(this.getResultToFormat().get(fileName));
	}

	public String getBaseName() {
		return baseName;
	}

	/**
	 * Determines if the String sequence is validate according to the learned model
	 * t.
	 * 
	 * @param sequence
	 *            the String to validate.
	 * @param t
	 *            the learned model
	 * @return a boolean
	 */
	public boolean validate(String sequence, T t) {
		return false;
	}

	/**
	 * Validate a sequence using the first model available. Which means that for
	 * using this method, we need to use one file.
	 * 
	 * @param sequence
	 *            the string to validate.
	 * 
	 */
	public boolean validate(String sequence) {

		T t = this.getResultToFormat().values().iterator().next();
		return this.validate(sequence, t);
	}

}
