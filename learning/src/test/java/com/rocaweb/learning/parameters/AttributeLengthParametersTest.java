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
package com.rocaweb.learning.parameters;

import com.rocaweb.learning.parameters.AttributeLengthParameters;
import com.rocaweb.learning.utils.LearningUtils;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class AttributeLengthParametersTest extends AlgorithmParametersTest {

	private AttributeLengthParameters params = null;

	@Override
	public AttributeLengthParameters getAlgoParam() {
		if (params == null) {
			params = new AttributeLengthParameters(LearningUtils.DEFAULT_CONFIG_FILE);
		}
		return params;
	}

}
