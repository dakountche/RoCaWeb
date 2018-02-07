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

import com.rocaweb.learning.rules.Contract;

/**
 * Implements the RoCaWeb Rule Format
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public final class RocawebFormat extends Format {

	private String type = "URL";
	private String mode = "ids";
	private static int ruleId = 0;
	private static final String DEFAULT_SEC_MODE = "wl";
	private String sec = DEFAULT_SEC_MODE;

	/**
	 * Format indicates the simplest case of a rule generated for the reverse proxy.
	 * An example is "URL:'/login',POST:'user[login]',"[A-Za-z]+" "
	 * id:'1',sec:'wl',mode:'ids'"
	 */
	private static final String rocwebRuleFormat = "SecRule \"%s:'%s',%s:'%s'\" \"%s\" \"id:'%d',sec:'%s',mode:'%s'\" ";

	/**
	 * Print the generated rule as a string.
	 * 
	 * @param type
	 *            indicates if the target of the rule. A target can be an URI or a
	 *            file on the local file system.
	 * @param currentFile
	 *            the name of the file which was used for generation the rules by
	 *            the alignment algorithm.
	 * @param rule
	 *            the regular expression generated after the alignment.
	 * @param id
	 *            the id of the rule.
	 * @param sec
	 *            the paradigm of security: white or black list model.
	 * @param mode
	 *            the mode of reverse proxy (IDS or IPS).
	 */
	public String format(String learningPathName, String currentFile, String rule) {
		// Determines which king of request we have to treat and store it in
		// request
		init(learningPathName, currentFile);
		String formatedRule = "";

		// Search if the file is related to one of the HTTP requests.
		if (!isFile) {
			formatedRule = String.format(rocwebRuleFormat, type, paramName, httpMethodName, url[1], rule, ruleId++, sec,
					mode);
		} else {
			formatedRule = String.format(rocwebRuleFormat, "FILE", currentFile, "", "", rule, ruleId++, sec, mode);
		}

		return formatedRule;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public String format(String learningPathName, String currentFile, String rule, Contract contract) {

		return this.format(learningPathName, currentFile, rule);
	}

}
