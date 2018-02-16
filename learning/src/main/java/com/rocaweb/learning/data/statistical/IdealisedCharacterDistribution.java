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

import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.inference.TestUtils;

import com.rocaweb.learning.exceptions.CharacterDistribitonSizeException;
import com.rocaweb.learning.validation.statistical.ChiSquare;

/**
 * 
 * Represents a the "normal" characters distribution. It is assume that all the
 * sequences are driven from a grammar. If we have all the production of this
 * grammar the IdelisedCharater distribution will represent them.
 *
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 */

public final class IdealisedCharacterDistribution extends AbstractDistribution {

	private int numberOfBins = 6;

	private double epsilon = 0.000001;

	private double threshold = 0.25;

	public IdealisedCharacterDistribution(List<String> sequences) {
		super(sequences);
	}

	public double[] getBins() {
		return getBins(getFrequency(), 1);
	}

	/**
	 * Create bins for a given character distribution.
	 * 
	 * @param characterDistribution
	 *            the frequencies of the characters.
	 * @param k
	 *            is the number of sequences.
	 * @return an array of k values corresponding to the mean value of the element
	 *         of each bin.
	 */
	public double[] getBins(Frequency characterDistribution, int k) {

		List<Double> values = CharacterDistribution.getValues(characterDistribution, k);

		double[] bins = new double[numberOfBins];

		if (values.size() == numberOfBins) {

			for (int i = 0; i < values.size(); i++) {
				bins[i] = values.get(i);
			}
		} else {
			bins[0] = values.get(0);
			bins[1] = sum(values, 1, 3);
			bins[2] = sum(values, 4, 6);
			bins[3] = sum(values, 7, 11);
			bins[4] = sum(values, 12, 15);
			bins[5] = sum(values, 16, values.size());
		}

		return bins;
	}

	/**
	 * Realizes the test as describe by {@see Kruegel et al.}
	 * 
	 * @param observedValue
	 *            the sequence to test
	 * @return the p-value of the KHI2 test representing the probability that the
	 *         observed value is normal.
	 * 
	 * @throws CharacterDistribitonSizeException
	 */
	public double kruegelEtAlTestModel(String observedValue) throws CharacterDistribitonSizeException {

		double[] expected = getBins();
		double[] observed = getBins(CharacterDistribution.createFrequency(observedValue), 1);
		int multi = observedValue.length();

		expected = this.getLengths(expected, multi);
		observed = this.getLengths(observed, multi);

		return ChiSquare.chiSquareTest(expected, observed);
	}

	private double[] getLengths(double[] values, int multi) {
		double[] occurencies = new double[values.length];

		for (int i = 0; i < values.length; i++) {
			occurencies[i] = values[i] * multi;
		}
		return occurencies;

	}

	private double sum(List<Double> values, int i, int j) {
		double sumOfValues = 0;
		double value = 0;
		for (int k = i; k <= j; k++) {
			if (k >= values.size())
				value = 0;
			else
				value = values.get(k);
			sumOfValues += value;
		}
		if (sumOfValues == 0.0)
			sumOfValues = epsilon;
		return sumOfValues;
	}

	@Override
	public String toString() {
		StringBuilder chaine = new StringBuilder();

		int i = 0;
		for (double value : getBins()) {
			String formetedValue = String.format(Locale.US, "icd%d=%f", i++, value);
			if (chaine.toString().isEmpty())
				chaine.append(formetedValue);
			else
				chaine.append("," + formetedValue);
		}

		logger.trace(this.getCharacterDistribution().getFrequency());
		logger.info(this.getTreshold());
		return chaine.toString();

	}

	@Override
	public boolean validate(String sequence) {
		try {
			double[] expected = getBins();
			double[] observed = getBins(CharacterDistribution.createFrequency(sequence), 1);
			int multi = sequence.length();

			expected = this.getLengths(expected, multi);
			observed = this.getLengths(observed, multi);

			return TestUtils.chiSquareTest(expected, ChiSquare.getLong(observed), this.getAlpha());
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	/**
	 * @param treshold
	 */
	public void setTreshold(double treshold) {
		this.threshold = treshold;

	}

	public double getTreshold() {
		return this.threshold;
	}

}
