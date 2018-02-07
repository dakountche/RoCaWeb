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
package com.rocaweb.learning.data.statistical;

import java.util.Locale;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public abstract class AbstractLengthStatistics implements Validation {

	private double mean = 0.0;
	private double std = 0.0;

	public static double DEFAULT_THRESHOLD_VALUE = 0.10;

	public AbstractLengthStatistics(double mean, double std) {
		setMean(mean);
		setStandarddeviantion(std);
	}

	public void setStandarddeviantion(double std) {
		this.std = std;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getMean() {

		return mean;
	}

	public double getStandarddeviation() {

		return std;
	}

	public String toString() {
		return String.format(Locale.US, "mean=%.3f,std=%.3f", getMean(), getStandarddeviation());

	}

}
