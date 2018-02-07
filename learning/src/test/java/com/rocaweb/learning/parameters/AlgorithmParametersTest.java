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

import java.io.IOException;

import org.junit.Test;

import com.rocaweb.learning.parameters.AlgorithmParameters;
import com.rocaweb.learning.utils.LearningUtils;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class AlgorithmParametersTest extends AbstractAlgorithmParametersTest {

	private AlgorithmParameters params = null;
	protected AlgorithmParameters source = null;
	protected AlgorithmParameters target = null;

	public void testSpecificParameters() {
		// Nothing to do here

	}

	@Override
	public AlgorithmParameters getAlgoParam() {
		if (this.params == null) {
			this.params = new AlgorithmParameters(LearningUtils.DEFAULT_CONFIG_FILE);
		}
		return params;
	}

	@Override
	@Test
	public void testJsonConfig() {
		this.params = getJSon();

		this.testAllParameters();
	}

	@Override
	public String getDefaultJSonConfig() {
		return "src/test/resources/json/default.json";
	}

	@Override
	public void testXMLConfig() throws IOException {
		this.testAllParameters();

	}

	@Override
	public void testXMLandJSON() {

		assertEquals(getSource().getBase(), getTarget().getBase());
		assertEquals(getSource().getClusterNumber(), getTarget().getClusterNumber());
		assertEquals(getSource().hasClustering(), getTarget().hasClustering());
		assertEquals(getSource().getLearningFile(), getTarget().getLearningFile());
		assertEquals(getSource().hasCleanning(), getTarget().hasCleanning());
		assertEquals(getSource().hasDecode(), getTarget().hasDecode());
		assertEquals(getSource().hasCasse(), getTarget().hasCasse());
		assertEquals(getSource().getLearningFile(), getTarget().getLearningFile());
		assertEquals(getSource().getMahoutConfigFilePath(), getTarget().getMahoutConfigFilePath());
		assertEquals(getSource().getRuleType(), getTarget().getRuleType());

	}

	private void testAllParameters() {
		this.testDefaultConfig();
		this.testSpecificParameters();
	}

	public AlgorithmParameters getJSon() {
		return new AlgorithmParameters(this.getDefaultJSonConfig());
	}

	public AlgorithmParameters getSource() {
		if (source == null) {
			source = new AlgorithmParameters(this.getDefaultJSonConfig());
		}
		return source;
	}

	public AlgorithmParameters getTarget() {
		if (target == null) {
			target = new AlgorithmParameters(LearningUtils.DEFAULT_CONFIG_FILE);
		}
		return target;
	}

}
