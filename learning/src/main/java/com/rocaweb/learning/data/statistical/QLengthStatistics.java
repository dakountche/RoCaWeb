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

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class QLengthStatistics extends AbstractLengthStatistics {

	/**
	 * @param mean
	 * @param std
	 */
	public QLengthStatistics(double mean, double std) {
		super(mean, std);

	}

	@Override
	public boolean validate(String sequence) {
		double current = (double) sequence.length();

		return Math.abs(current - this.getMean()) <= 3 * this.getStandarddeviation();
	}

}
