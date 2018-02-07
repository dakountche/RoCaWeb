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

import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class BDistribution extends AbstractDistribution {

	private Percentile percentile = null;

	private double factor = 3.0;

	/**
	 * @param sequences
	 */
	public BDistribution(List<String> sequences) {
		super(sequences);
	}

	@Override
	public boolean validate(String sequence) {
		double current = sequence.length();

		double iqr = this.getTheThirdPercentile() - this.getTheFirstQuartile();
		return Math.abs(current - this.getTheMedian()) > factor * iqr;
	}

	@Override
	public String toString() {
		return String.format(Locale.US, "iqr=%.3f", this.getTheThirdPercentile() - this.getTheFirstQuartile());
	}

	/**
	 * Determines the third quartile.
	 * 
	 */
	public double getTheThirdPercentile() {
		return this.getPercentile().evaluate(75.0);
	}

	/**
	 * Determines the first quartile.
	 *
	 * @return the quartile
	 */
	public double getTheFirstQuartile() {
		return this.getPercentile().evaluate(25.0);
	}

	public double getTheMedian() {
		return this.getPercentile().evaluate(50.0);
	}

	private Percentile getPercentile() {
		double[] values = CharacterDistribution.getArraytValues(getFrequency());
		percentile = new Percentile();
		percentile.setData(values);

		return percentile;
	}

}
