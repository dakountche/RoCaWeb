/** 
* This file is part of RoCaWeb.
*
* RoCaWeb is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package com.rocaweb.learning.algorithms.grammatical;

import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;
import com.rocaweb.learning.parameters.AlgorithmParameters;
import com.rocaweb.learning.parameters.DefaultLearningAParameters;

/**
 * @author Djibrilla Amadou Kountche
 *
 */
public abstract class AbstractGrammaticalAlgorithm<T> extends Algorithm<T> {

	private AlgorithmParameters parameters = null;

	@Override
	public AlgorithmParameters getParameters() {
		if (parameters == null) {
			parameters = new AlgorithmParameters();
		}

		return parameters;
	}

	@Override
	public void setDefaultParameters() {
		this.parameters = DefaultLearningAParameters.getDefaultAlgorithmParams();

	}

	public char getFormatChar() {
		return 'g';
	}

	@Override
	public void setParameters(AbstractAlgorithmParameters parameters) {
		this.parameters = (AlgorithmParameters) parameters;

	}

}
