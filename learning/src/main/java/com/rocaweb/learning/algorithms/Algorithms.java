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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.JsonElement;

/**
 * A factory class for creating instances of <code>Algorithm</code>.
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 */
public final class Algorithms {

	private static Logger logger = LogManager.getLogger(Algorithms.class);

	/**
	 * Create an instance given the FQDN of the class.
	 * 
	 * @param algoName the class name
	 * @return an <code>Algorithm</code> instance
	 * 
	 */
	public static Algorithm<?> createAlgorithm(String name, String configFileName) {
		Algorithm<?> algo = getAlgo(name);
		algo.loadConfiguration(configFileName);
		return algo;
	}

	/**
	 * Create an instance given the FQDN of the class and a <code>JSonElement</code>
	 * containing the parameters of the instance.
	 * 
	 * @param the FQDN of the algorithm
	 * @param jsonElement a Json object containing the configuration
	 * 
	 * @return an instance of the algorithm.
	 */
	public static Algorithm<?> createAlgorithm(String name, JsonElement jsonElement) {
		Algorithm<?> algo = getAlgo(name);
		algo.loadConfiguration(jsonElement);
		return algo;
	}

	/**
	 * Create an instance of an algorithm based of the algorithm name.
	 * 
	 * @param name the algorithm name
	 * @return an instance of Algorithm.
	 */
	private static Algorithm<?> getAlgo(String name) {
		Algorithm<?> algo = null;
		try {
			algo = (Algorithm<?>) Class.forName(name).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return algo;
	}

}
