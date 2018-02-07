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

import java.util.List;

import com.rocaweb.learning.data.statistical.BDistribution;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.rules.Statistic;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class BoxPlot extends AbstractCharacterDistributionAlgorithm<BDistribution> {

	private BDistribution distribution = null;
	private Statistic<BDistribution> contract;

	@Override
	public BDistribution process(List<String> sequences) {
		this.setSequences(sequences);

		return this.getDistribution();
	}

	@Override
	public Contract<BDistribution> getContract() {
		if (contract == null) {
			contract = new Statistic<BDistribution>(this);
		}
		return contract;
	}

	/**
	 * @return the distribution
	 */
	public BDistribution getDistribution() {
		distribution = new BDistribution(this.getSequences());
		return distribution;
	}

	/**
	 * @param distribution
	 *            the distribution to set
	 */
	public void setDistribution(BDistribution distribution) {
		this.distribution = distribution;
	}

}
