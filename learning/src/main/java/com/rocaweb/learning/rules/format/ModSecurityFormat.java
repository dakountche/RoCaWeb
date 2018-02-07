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

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.rocaweb.commons.configuration.CommonConfiguration;
import com.rocaweb.commons.fs.FSManager;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.rules.Statistic;

/**
 * Export the contract to ModSecutiry format.
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
@SuppressWarnings("rawtypes")
public final class ModSecurityFormat extends Format {

	/** The phase at which the rule will be applied by ModSecurity. */
	private static int modSecurityPhaseID = 2;
	
	private boolean grouped = true;

	/** Format a given <code>Contract</code> to ModSecurity format */
	public String format(String learningPathName, String currentFile, String rule, Contract contract) {
		char c = ' ';
		if (contract instanceof Statistic)
			c = 's';
		return this.format(learningPathName, currentFile, rule, c,
				contract.getAlgorithm().getClass().getSimpleName().toLowerCase());
	}

	/**
	 * Add the underscore to an HTTP method.
	 * 
	 * @param httpMethodName
	 * @return _HTTPMETHODNAME for example _GET
	 */
	private String modifiedHttpMethodName() {

		return httpMethodName.isEmpty() ? httpMethodName : "_" + httpMethodName;

	}

	/**
	 * Create ModSecurity TX collection variable from the generate rule
	 * 
	 * @param rule
	 *            the contract exported as a String
	 * @return a string containing the TX variables
	 */
	public String toMs(String rule) {
		StringBuilder txVars = new StringBuilder("");
		String comma = "";
		String[] splitedRule = rule.split(",");
		for (String string : splitedRule) {

			txVars.append(comma + "setvar:TX." + string);
			if (comma.isEmpty())
				comma = ",";
		}

		return txVars.toString();
	}

	/**
	 * The main formating function.
	 */
	public String format(FileObject fileObject, String baseName, String algoName, char c) {
		Set<FileObject> sonsToVisit = Sets.newHashSet();
		StringBuilder formatedRule = new StringBuilder("");
		boolean added = false;

		try {

			if (fileObject.getType() == FileType.FOLDER) {

				for (FileObject fo : fileObject.getChildren()) {
					if (fo.getType() == FileType.FILE && fo.exists()) {

						formatedRule.append(formatFileContent(fo, baseName, algoName, c));
						added = true;

					} else if (fo.getType() == FileType.FOLDER)
						sonsToVisit.add(fo);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (added) {
			String url = FSManager.getURL(fileObject).toString();

			if (!formatedRule.toString().isEmpty())
				formatedRule = new StringBuilder("<Location " + this.modifyURL(url, baseName) + ">" + formatedRule + "\n</Location>");

		}

		for (FileObject son : sonsToVisit) {
			formatedRule.append("\n" + format(son, baseName, algoName, c));
		}

		return formatedRule.toString();
	}

	private String formatFileContent(FileObject fo, String baseName, String algoName, char c) throws IOException {

		List<String> rules = IOUtils.readLines(fo.getContent().getInputStream(), "UTF-8");
		String rule = Joiner.on("\n").join(rules);
		String path = FSManager.getURL(fo).toString();
		String url = FSManager.getURL(fo).getPath().toString();
		return "\n" + this.format(url, path, rule, c, algoName);

	}

	/**
	 * Implements one form of ModSecurity rules.
	 * 
	 * @param learningPathName
	 *            path to the learning directory
	 * @param currentFile
	 * @param rule
	 *            the generated rule from the current file.
	 * @param c
	 *            the type of format
	 * @param algorithmName
	 * @return a formated rule comprehensible by modSecurity
	 */
	private String format(String learningPathName, String currentFile, String rule, char c, String algorithmName) {

		init(learningPathName, currentFile);

		if (rule.isEmpty())
			return rule;

		String pre = "";

		StringBuilder formatedRule = new StringBuilder();
		StringBuilder chainedMsRule = new StringBuilder();
		String operator = "";
		String operand = "";
		String file = FilenameUtils.getName(currentFile);

		String actions = String.format(
				"phase:%d,id:%d,deny,log,status:406,msg:ABNORMAL,tag:ROCAWEB/WEB_ATTACK/ABNORMAL,severity:1",
				modSecurityPhaseID, ruleCount++);

		if (c == 's') {
			pre = "&";

			rule = toMs(rule) + ",chain,setvar:TX.name=" + file;
			operator = "@ge";
			operand = "1";
			chainedMsRule.append("\nSecRuleScript " + CommonConfiguration.getInstance().getModSecurityInstallDir()
					+ algorithmName.toLowerCase() + ".lua");
			actions = rule + "," + actions;

		} else {
			pre = "";
			chainedMsRule = new StringBuilder();
			operator = "!@rx";
			operand = "^" + rule + "$";
		}
		String http = modifiedHttpMethodName();
		formatedRule.append(String.format("SecRule %sARGS%s:%s \"%s %s\" \"%s\" %s", pre, http, file, operator, operand,
				actions, chainedMsRule.toString()));

		return formatedRule.toString();
	}

	public String modifyURL(String url, String baseName) {
		String urltoM = url.substring(baseName.length());

		if (urltoM.contains(httpMethodName)) {
			urltoM = urltoM.replace("/" + httpMethodName, "");
		}

		return urltoM;
	}

	@Override
	public boolean isGrouped() {
		return grouped;
	}

}
