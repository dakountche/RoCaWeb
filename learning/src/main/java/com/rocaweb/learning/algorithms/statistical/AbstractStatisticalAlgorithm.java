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
package com.rocaweb.learning.algorithms.statistical;

import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.data.statistical.Validation;

/**
 * @author Djibrilla Amadou Kountche
 * @since 1.0.0
 */

public abstract class AbstractStatisticalAlgorithm<T extends Validation> extends Algorithm<T> {

	private double DEFAULT_WEIGTH = 1.0;

	public AbstractStatisticalAlgorithm() {}

	/**
	 * @param configFileName
	 */
	AbstractStatisticalAlgorithm(String configFileName) {
		super(configFileName);
	}

	/**
	 * @return the weight of this algorithm. In a multiple scheme algorithm, the
	 *         weight indicates the importance of this method in the evaluation.
	 */
	public double getWeigth() {
		return DEFAULT_WEIGTH;
	}

	/**
	 * Determines the anomaly probability for a given sequence.
	 * 
	 * @return the probability.
	 */
	public abstract double getAnomalyProbabilty();

	/**
	 * Compute the score of the algorithms. This case is used when multiple
	 * algorithms are combined each having a weight.
	 * 
	 * @return the weighted score.
	 */
	public double getScore() {
		return getWeigth() * (1.0 - getAnomalyProbabilty());
	}

	public char getFormatChar() {
		return 's';
	}

}
