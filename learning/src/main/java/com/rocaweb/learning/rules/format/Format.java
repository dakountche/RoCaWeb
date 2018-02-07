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
package com.rocaweb.learning.rules.format;

import org.apache.commons.io.FilenameUtils;

import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rocaweb.learning.rules.Contract;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public abstract class Format {

	private static RocawebFormat rocawebFormat = null;

	private static ModSecurityFormat modSecFormat = null;

	protected static Logger logger = LogManager.getLogger();

	/**
	 * This field is used for the rule formating. It serves to determine the type of
	 * the method.
	 */
	protected static final String[] httpRequestMethod = { "GET", "POST", "HEADER", "PUT", "DELETE", "OPTIONS",
			"CONNECT" };

	protected String[] url = { "", "" };
	protected String paramName = "";
	protected String httpMethodName = "";
	protected boolean isFile = true;
	protected String location = "";

	public static long ruleCount = 1;

	@SuppressWarnings("rawtypes")
	public abstract String format(String learningPathName, String currentFile, String rule, Contract contract);

	/**
	 * TODO make this process dynamic by using class loaders. Creates a Formatter
	 * based on ruleType.
	 * 
	 * @param ruleType
	 * @return the Formatter
	 */
	public static Format getFormater(String ruleType) {

		switch (ruleType.toLowerCase()) {

		case "ro":
		case "rocaweb":

			return getRocawebFormater();
		case "ms":
		case "modsecurity":
			return newModSecurityFormater();

		case "br":
		case "brute regex":

		default:
			return newDeFaultFormater();
		}
	}

	public void init(String learningPathName, String currentFile) {

		logger.trace("Entering init");
		logger.trace(learningPathName);
		logger.trace(currentFile);
		String cutter = "";

		if (learningPathName.isEmpty() || currentFile.isEmpty())
			return;

		for (String http : httpRequestMethod) {
			// For example, we search /GET/ or /POST/ in the current file name.
			String modifiedhttp = "/" + http + "/";

			if (currentFile.contains(modifiedhttp)) {
				httpMethodName = http;

				cutter = modifiedhttp;

				url = currentFile.split(cutter);

				paramName = FilenameUtils.getName(currentFile);

				isFile = false;
				break;
			}
		}

		if (isFile) {

			paramName = currentFile.substring(learningPathName.length());
		}

	}

	public String format(Contract<?> contract) {

		StringBuffer welcomeMsg = new StringBuffer(contract.getAlgorithm().printWelcomeMsg());

		String learningPathName = contract.getAlgorithm().getParameters().getLearningPath();

		if (!this.isGrouped()) {
			for (String fileName : contract.getResultToFormat().keySet()) {

				String rule = contract.fetchRule(fileName);
				logger.trace(fileName + " : " + rule);
				logger.trace(fileName);
				logger.trace(learningPathName);
				welcomeMsg.append("\n" + format(learningPathName, fileName, rule, contract));
			}
		} else {
			welcomeMsg.append( "\n" + format(contract.getFileObject(), contract.getBaseName(),
					contract.getAlgorithm().getClass().getSimpleName().toLowerCase(),
					contract.getAlgorithm().getFormatChar()));
		}
		return welcomeMsg.toString();

	}

	/**
	 * @return
	 */
	public boolean isGrouped() {

		return false;
	}

	private static Format newModSecurityFormater() {
		if (modSecFormat == null) {
			modSecFormat = new ModSecurityFormat();
		}
		return modSecFormat;
	}

	private static Format getRocawebFormater() {
		if (rocawebFormat == null) {
			rocawebFormat = new RocawebFormat();
		}
		return rocawebFormat;
	}

	private static Format newDeFaultFormater() {

		return getRocawebFormater();
	}

	public String format(FileObject fileObject, String baseName, String algoName, char c) {
		return "";
	}

}
