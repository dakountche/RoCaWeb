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

import com.google.common.collect.Lists;
import com.rocaweb.learning.data.statistical.AbstractLengthStatistics;
import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;
import com.rocaweb.learning.parameters.AttributeLengthParameters;
import com.rocaweb.learning.parameters.DefaultLearningAParameters;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;

/**
 * @author Djibrilla Amadou Kountche
 * @since 1.0.0
 */

public abstract class AbstractLengthAlgorithm<T extends AbstractLengthStatistics>
		extends AbstractStatisticalAlgorithm<T> {

	public AbstractLengthAlgorithm() {

	}

	/**
	 * @param configFileName
	 */
	public AbstractLengthAlgorithm(String configFileName) {
		super(configFileName);
	}

	/**
	 * Parameters of the algorithm. <code>AttributeCharacterDistribution</code> does
	 * not require specific parameters.
	 */
	private AttributeLengthParameters parameters;

	/**
	 * The sequences to learn from.
	 */
	private List<String> sequences = null;

	/**
	 * Determines the statistical model for a list of strings.
	 * 
	 * @param sequences
	 *            : the values of the parameters
	 * @return the model
	 */
	protected T calculateMeanAndStd(List<String> sequences) {
		this.setSequences(sequences);
		DoubleArrayList lengths = new DoubleArrayList();
		for (String sequence : sequences) {
			lengths.add((double) sequence.length());
		}

		return createLengthStatistics(lengths);

	}

	/**
	 * Determines the statistical model for a list of strings.
	 * 
	 * @param lengths
	 *            of the sequences
	 * @return the model
	 */
	private T createLengthStatistics(DoubleArrayList lengths) {
		double mean = Descriptive.mean(lengths);
		double variance = Descriptive.variance(lengths.size(), Descriptive.sum(lengths),
				Descriptive.sumOfSquares(lengths));
		double std = Descriptive.standardDeviation(variance);

		T ls = create(mean, std);
		logger.trace(ls.toString());

		return ls;
	}

	/**
	 * @param mean
	 * @param std
	 * @return
	 */
	protected abstract T create(double mean, double std);

	@Override
	public double getAnomalyProbabilty() {
		return 0;
	}

	@Override
	public T process(List<String> sequences) {
		this.setSequences(sequences);
		return this.getLengthStatistics();
	}

	/**
	 * @return
	 */
	public abstract T getLengthStatistics();

	@Override
	public AttributeLengthParameters getParameters() {

		if (parameters == null) {
			parameters = new AttributeLengthParameters(getConfigFileName());
		}
		return parameters;
	}

	@Override
	public void setDefaultParameters() {
		this.parameters = DefaultLearningAParameters.getDefaultAttributeLengthParams();

	}

	@Override
	public void setParameters(AbstractAlgorithmParameters parameters) {
		this.parameters = (AttributeLengthParameters) parameters;

	}

	/**
	 * @return the sequences
	 */
	public List<String> getSequences() {
		if (sequences == null) {
			sequences = Lists.newArrayList();
		}
		return sequences;
	}

	/**
	 * @param sequences
	 *            the sequences to set
	 */
	public void setSequences(List<String> sequences) {
		this.sequences = sequences;
	}

}
