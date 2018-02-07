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

import org.apache.commons.math3.stat.inference.TestUtils;

/**
 * Determines the KHI square test.
 * 
 * @author Djibrila Amadou Kountche
 * 
 */
public class ChiSquare {

	private double riskAlpha = 0.05;

	public static double chiSquareTest(double[] expected, double[] observed) {

		return TestUtils.chiSquareTest(expected, getLong(observed));
	}

	public static double chiSquare(double[] expected, double[] observed) {

		return TestUtils.chiSquare(expected, getLong(observed));
	}

	public static long[] getLong(double[] observed) {
		long[] longObserved = new long[observed.length];
		for (int i = 0; i < observed.length; i++)
			longObserved[i] = (long) observed[i];

		return longObserved;
	}

	/**
	 * @return the riskAlpha
	 */
	public double getRiskAlpha() {
		return riskAlpha;
	}

	/**
	 * @param riskAlpha
	 *            to set
	 */
	public void setRiskAlpha(double riskAlpha) {
		this.riskAlpha = riskAlpha;
	}

}
