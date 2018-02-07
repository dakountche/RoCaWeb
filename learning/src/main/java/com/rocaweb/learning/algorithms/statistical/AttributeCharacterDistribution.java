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
package com.rocaweb.learning.algorithms.statistical;

import java.util.List;

import com.rocaweb.learning.data.statistical.IdealisedCharacterDistribution;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.rules.Statistic;

/**
 * 
 * Implements the Character Distribution model described by Kruegel et al.
 * 
 * <p>
 * <h3>Hypothesis</h3>
 * <ul>
 * <li>Attributes have a regular structure</li>
 * <li>Attributes are human readable and almost always contain printable
 * characters</li>
 * <li>in the case of binaries, a completely different character distribution
 * can be observed.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * <h3>Learning phase</h3>
 * <p>
 * Determines the Idealized Character Distribution (ICD) for the input
 * sequences. An ICD represents the normal character distribution for all the
 * sequences.
 * </p>
 * <p>
 * To determine the ICD :
 * <ul>
 * <li>A character distribution is calculated for each sequence. The character
 * distribution contains the frequencies for each character in the
 * sequence.</li>
 * 
 * <li>These character distribution are merged and for each character frequency
 * the mean is Computed</li>
 * 
 * <li>These frequencies are sorted from the highest to the smallest. The sorted
 * mean values of the frequencies is the ICD which represented the model of the
 * character distribution of each sequence</li>
 * </ul>
 * </p>
 * </p>
 * 
 * <p>
 * <h3>Detection phase</h3>
 * <p>
 * Given the ICD and a sequence to validate, the model determines :
 * <ul>
 * <li>The <bold>observed</bold> values which are the occurrences for each
 * character in the sequence to be evaluated. As described above, six bins are
 * constructed.</li>
 * <li>The <bold>expected</bold> values which are the values of the ICD
 * multiplied by the length of the sequence to be evaluated. here the six bins
 * are already constructed.</li>
 * 
 * <li>Use KHI2 test to assess if the given character distribution is driven
 * from the ICD.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * 
 * <h2>The KHI2 test</h2>
 * <ol>
 * 
 * <li>Hypothesis</li>
 * <ul>
 * <li>H0 : The value of the attributes are from the ICD</li>
 * <li>H1 : The values are not form the ICD and thus are abnormal.</li>
 * </ul>
 * 
 * <li>Compute the KHI2 values</li>
 * <li>Determine the degree of freedom and then the significance</li></li>
 * Return the values of the probability that the sample is derived from the
 * idealized character distribution.</li>
 * 
 * </ol>
 * </p>
 * </p>
 * 
 * 
 * <h3>Advantages</h3>
 * <p>
 * Very long sequences can be detected.
 * </p>
 *
 * <h3>Limitations</h3>
 * <p>
 * The KHI2 test is sensitive to the choice of the bins. Also, the choice of the
 * bins are not related to the characters present in the sequences. Therefore,
 * the bins constructed does not reflect the distribution of each character.
 * </p>
 * 
 * </p>
 *
 * <p>
 * <h3>Improvements</h3>
 * <ul>
 * 
 * <li>Sometimes, the learning data set is not diverse enough to construct 6
 * bins. Therefore, the method will try the best to construct an appropriate
 * number of bins.</li>
 * <li>
 * 
 * <p>
 * Determine the bins corresponding to the observed and the expected values
 * </p>
 * . The authors used six bins corresponding to values of the ICD : [0], [1, 3],
 * [4, 6], [7, 11], [12, 15], [16, 255].
 * 
 * <p>
 * As an improvement, we propose to adapt the bins to the characters. If the
 * characters is present in both the ICD and the sequences, we create a bin,
 * otherwise the characters are put in another bin.
 * </p>
 * </li>
 * </ul>
 * </p>
 * 
 * </p>
 * </p>
 * 
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 * 
 * @see Kruegel, C., Vigna, G., & Robertson, W. (2005). A multi-model approach
 *      to the detection of web-based attacks. Computer Networks, 48(5),
 *      717-738.}
 * 
 * @see https://en.wikipedia.org/wiki/Chi-squared_test
 * 
 * @see The Khi squarre test is also used in cyptanalysis
 *      http://practicalcryptography.com/cryptanalysis/text-characterisation/chi
 *      -squared-statistic/
 * 
 *      TODO Freedman-Diaconis
 */
public class AttributeCharacterDistribution
		extends AbstractCharacterDistributionAlgorithm<IdealisedCharacterDistribution> {

	private transient Statistic<IdealisedCharacterDistribution> contract = null;

	/**
	 * The default constructor
	 */
	public AttributeCharacterDistribution() {

	}

	/**
	 * Constructor
	 * 
	 * @param sequences
	 *            representing a parameter
	 */
	public AttributeCharacterDistribution(List<String> sequences) {
		super(sequences);

	}

	/**
	 * @param configFileName
	 */
	public AttributeCharacterDistribution(String configFileName) {
		super(configFileName);
	}

	@Override
	public IdealisedCharacterDistribution process(List<String> sequences) {

		this.setSequences(sequences);
		return getIcd();
	}

	public IdealisedCharacterDistribution getIcd() {
		IdealisedCharacterDistribution icd = new IdealisedCharacterDistribution(this.getSequences());
		icd.setTreshold(this.getTreshold());

		return icd;
	}

	@Override
	public Contract<IdealisedCharacterDistribution> getContract() {
		if (contract == null) {
			contract = new Statistic<IdealisedCharacterDistribution>(this);
		}
		return contract;
	}

	/**
	 * Determines the threshold to use for the validation of future sequence.
	 * 
	 * @return the minimum probability.
	 */
	public double getTreshold() {
		double min = Double.MAX_VALUE;
		IdealisedCharacterDistribution icd = new IdealisedCharacterDistribution(this.getSequences());
		for (String sequence : this.getSequences()) {
			try {
				if (min > icd.kruegelEtAlTestModel(sequence)) {
					min = icd.kruegelEtAlTestModel(sequence);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return min;
	}

}
