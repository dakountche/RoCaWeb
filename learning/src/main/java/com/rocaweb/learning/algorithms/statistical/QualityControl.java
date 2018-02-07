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
package com.rocaweb.learning.algorithms.statistical;

import com.rocaweb.learning.data.statistical.QLengthStatistics;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.rules.Statistic;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class QualityControl extends AbstractLengthAlgorithm<QLengthStatistics> {

	private Contract<QLengthStatistics> contract = null;

	public QualityControl() {

	}

	/**
	 * @param configFileName
	 */
	public QualityControl(String configFileName) {
		super(configFileName);
	}

	@Override
	public Contract<QLengthStatistics> getContract() {
		if (contract == null) {
			contract = new Statistic<QLengthStatistics>(this);

		}

		return contract;
	}

	@Override
	protected QLengthStatistics create(double mean, double std) {

		return new QLengthStatistics(mean, std);
	}

	/**
	 * @see com.rocaweb.learning.algorithms.statistical.AbstractLengthAlgorithm#getLengthStatistics()
	 */
	@Override
	public QLengthStatistics getLengthStatistics() {

		return this.calculateMeanAndStd(getSequences());
	}

}
