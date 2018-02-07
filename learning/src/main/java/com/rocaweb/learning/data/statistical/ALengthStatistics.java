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
package com.rocaweb.learning.data.statistical;

import java.util.Locale;

import com.rocaweb.learning.validation.statistical.Chebychev;

/**
 * Summarize the statistics 'mean and standard deviation) about the data set.
 * 
 * @author Djibrilla Amadou Kountche
 * @since 1.0.0
 */
public class ALengthStatistics extends AbstractLengthStatistics {

	private double threshold = DEFAULT_THRESHOLD_VALUE;

	public ALengthStatistics(double mean, double std) {
		super(mean, std);

	}

	@Override
	public boolean validate(String sequence) {
		double current = (double) sequence.length();

		if (this.getStandarddeviation() == 0) {
			if (this.getMean() == current)
				return true;

			else
				return false;
		}

		else
			return Chebychev.getAnomalyProbability(getMean(), getStandarddeviation(), current) <= this.getThreshold();
	}

	public String toString() {
		String msg = String.format(Locale.US, ",threshold=%.3f", this.getThreshold());
		return super.toString() + msg;

	}

	/**
	 * @return
	 */
	public double getThreshold() {

		return this.threshold;
	}

	/**
	 * @param treshold
	 */
	public void setTreshold(double treshold) {
		this.threshold = treshold;

	}

}
