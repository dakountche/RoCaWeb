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

import com.rocaweb.learning.data.statistical.KSDistribution;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.rules.Statistic;

/**
 * Application ogf the Kolmogorov Smirnov test to intrusion detection.
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class KolmogorvSmirnov extends AbstractCharacterDistributionAlgorithm<KSDistribution> {

	private KSDistribution ksd = null;

	private transient Statistic<KSDistribution> contract = null;

	@Override
	public KSDistribution process(List<String> sequences) {
		this.setSequences(sequences);
		return this.getKsd();
	}

	@Override
	public Contract<KSDistribution> getContract() {
		if (contract == null) {
			contract = new Statistic<KSDistribution>(this);
		}
		return contract;
	}

	/**
	 * @return the ksd
	 */
	public KSDistribution getKsd() {

		ksd = new KSDistribution(this.getSequences());

		return ksd;
	}

	/**
	 * @param ksd
	 *            the ksd to set
	 */
	public void setKsd(KSDistribution ksd) {
		this.ksd = ksd;
	}
}
