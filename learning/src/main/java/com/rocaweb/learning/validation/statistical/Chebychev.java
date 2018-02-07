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
package com.rocaweb.learning.validation.statistical;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implement the Chebychev inequality.
 * 
 * This inequality put a bound between a value and the mean of observed values.
 * 
 *
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */

public class Chebychev {

	private static Logger logger = LogManager.getLogger(Chebychev.class);

	/**
	 * 
	 * @return
	 */
	public static double getAnomalyProbability(double mean, double std, double current) {

		double bound = (current - mean) / (std);

		double proba = bound * bound;
		logger.trace(proba);
		return (current < mean) ? 1.0 : proba;
	}

}
