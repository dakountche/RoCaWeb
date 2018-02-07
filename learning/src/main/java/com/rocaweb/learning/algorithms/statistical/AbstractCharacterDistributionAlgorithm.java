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

import java.util.ArrayList;
import java.util.List;

import com.rocaweb.learning.data.statistical.AbstractDistribution;
import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;
import com.rocaweb.learning.parameters.AlgorithmParameters;
import com.rocaweb.learning.parameters.DefaultLearningAParameters;

/**
 * @author Djibrilla Amadou Kountche
 * @since 1.0.0
 */
public abstract class AbstractCharacterDistributionAlgorithm<T extends AbstractDistribution>
		extends AbstractStatisticalAlgorithm<T> {

	public AbstractCharacterDistributionAlgorithm() {

	}

	public AbstractCharacterDistributionAlgorithm(String configFileName) {
		super(configFileName);
	}

	/**
	 * Parameters of the algorithm. <code>AttributeCharacterDistribution</code> does
	 * not require specific parameters.
	 */
	private AlgorithmParameters parameters = null;

	private List<String> sequences = null;

	/**
	 * @param sequences2
	 */
	public AbstractCharacterDistributionAlgorithm(List<String> sequences) {
		this.sequences = sequences;
	}

	@Override
	public void setDefaultParameters() {
		this.parameters = DefaultLearningAParameters.getDefaultAlgorithmParams();

	}

	@Override
	public void setParameters(AbstractAlgorithmParameters parameters) {
		this.parameters = (AlgorithmParameters) parameters;

	}

	@Override
	public AlgorithmParameters getParameters() {

		if (parameters == null)
			parameters = new AlgorithmParameters(this.getConfigFileName());
		return parameters;
	}

	public void setSequences(List<String> sequences) {
		this.sequences = sequences;

	}

	public List<String> getSequences() {
		if (sequences == null) {
			sequences = new ArrayList<String>();
		}
		return sequences;
	}

	@Override
	public double getAnomalyProbabilty() {

		return 0;
	}
}
