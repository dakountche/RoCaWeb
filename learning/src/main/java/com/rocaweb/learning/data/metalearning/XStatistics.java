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
package com.rocaweb.learning.data.metalearning;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class XStatistics {

	private transient int totalcount = 0;
	private transient int norulecount = 0;
	private transient int infkcount = 0;
	private transient int onlyonecount = 0;

	private transient Map<String, Integer> rulecount = new HashMap<String, Integer>();

	public String printXStats() {
		StringBuilder msg = new StringBuilder();
		
		msg.append(format("Total Number of rules", totalcount));

		for (Entry<String, Integer> entry : rulecount.entrySet()) {
			msg.append(format("Number of rules by " + entry.getKey(), entry.getValue()));
		}

		msg.append(format("Number of datasize < K", infkcount));
		msg.append(format("Number of singleton: ", onlyonecount));
		msg.append(format("Number of norule: ", norulecount));

		return msg.toString();
	}

	private String format(String string, int value) {
		return String.format("%s:\t%d\n", string, value);
	}

	/**
	 * 
	 */
	public void updateTotalCount() {
		totalcount++;
	}

	/**
	 * 
	 */
	public void updateOnlyOneCount() {
		onlyonecount++;
	}

	/**
	 * 
	 */
	public void updateStatInfCount() {
		infkcount++;
	}

	/**
	 * 
	 */
	public void updateNoRuleCount() {
		norulecount++;

	}

	/**
	 * @param j
	 */
	public void updateRuleCount(String algoName) {
		if (!rulecount.containsKey(algoName))
			rulecount.put(algoName, 1);
		else
			rulecount.put(algoName, rulecount.get(algoName) + 1);

	}

}
