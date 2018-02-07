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

import org.apache.commons.math3.random.EmpiricalDistribution;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.inference.TestUtils;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class KSDistribution extends AbstractDistribution {

	private EmpiricalDistribution distribution = null;

	public KSDistribution() {

	}

	public KSDistribution(List<String> sequences) {
		super(sequences);
	}

	public EmpiricalDistribution getDistribution() {
		if (this.distribution == null) {
			distribution = new EmpiricalDistribution();
			distribution.load(CharacterDistribution.getArraytValues(this.getFrequency()));
		}
		return distribution;
	}

	@Override
	public boolean validate(String sequence) {
		Frequency f = CharacterDistribution.createFrequency(sequence);
		double[] value = CharacterDistribution.getArraytValues(f);

		return TestUtils.kolmogorovSmirnovTest(getDistribution(), value, 0.05);
	}

	@Override
	public String toString() {
		StringBuilder chaine = new StringBuilder();

		int i = 0;
		for (double value : getBins()) {
			String formetedValue = String.format(Locale.US, "ks%d=%3f", i++, value);
			if (chaine.toString().isEmpty())
				chaine.append(formetedValue);
			else
				chaine.append("," + formetedValue);
		}

		logger.trace(this.getCharacterDistribution().getFrequency());

		return chaine.toString();

	}

	public double[] getBins() {
		return CharacterDistribution.getArraytValues(getFrequency());
	}

}
