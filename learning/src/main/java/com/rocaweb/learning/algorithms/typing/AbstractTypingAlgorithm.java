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
package com.rocaweb.learning.algorithms.typing;

import com.rocaweb.learning.algorithms.Algorithm;

import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;
import com.rocaweb.learning.parameters.DefaultLearningAParameters;
import com.rocaweb.learning.parameters.TypingParameters;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.rules.Type;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public abstract class AbstractTypingAlgorithm extends Algorithm<String> {

	private TypingParameters parameters = null;
	private transient Type contract = null;

	AbstractTypingAlgorithm() {

	}

	/**
	 * @param configFileName
	 */
	AbstractTypingAlgorithm(String configFileName) {
		super(configFileName);
	}

	@Override
	public TypingParameters getParameters() {
		if (parameters == null)
			parameters = new TypingParameters(getConfigFileName());
		return parameters;
	}

	public Contract<String> getContract() {
		if (contract == null) {
			contract = new Type(this);
		}
		return contract;
	}

	@Override
	public void setDefaultParameters() {
		this.parameters = DefaultLearningAParameters.getDefaultTypingParameters();

	}

	public char getFormatChar() {
		return 'r';
	}

	@Override
	public void setParameters(AbstractAlgorithmParameters parameters) {
		this.parameters = (TypingParameters) parameters;

	}

}
