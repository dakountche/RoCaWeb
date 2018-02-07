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

import static org.junit.Assert.assertEquals;

import com.rocaweb.learning.parameters.DefaultLearningAParameters;
import com.rocaweb.learning.parameters.TypingParameters;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class TypingParametersTest extends AlgorithmParametersTest {

	private TypingParameters params;

	@Override
	public void testSpecificParameters() {
		assertEquals(this.getAlgoParam().getBounded(), false);

	}

	@Override
	public TypingParameters getAlgoParam() {

		if (params == null) {
			params = new TypingParameters(DefaultLearningAParameters.DEFAULT_CONFIG_FILE);
		}
		return params;
	}

	@Override
	public String getDefaultJSonConfig() {

		return "src/test/resources/json/typage.json";
	}

}
