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

import com.google.gson.Gson;
import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public abstract class AbstractAlgorithmParametersTest {

	public void testDefaultConfig() {

		assertEquals(getAlgoParam().getLearningPath(), "learningDir");
		assertEquals(getAlgoParam().getLearningFile(), "fileName");
		assertEquals(getAlgoParam().hasClustering(), false);
		assertEquals(getAlgoParam().hasCleanning(), false);
		assertEquals(getAlgoParam().getClusterNumber(), 1);
		assertEquals(getAlgoParam().hasCasse(), false);
		assertEquals(getAlgoParam().hasDecode(), false);
		assertEquals(getAlgoParam().getBase(), "utf-8");
		assertEquals(getAlgoParam().getRuleType(), "ModSecurity");
		assertEquals(getAlgoParam().getMahoutConfigFilePath(), "");

	}

	protected static Gson gson = new Gson();;

	@Test
	public abstract void testJsonConfig() throws IOException;

	@Test
	public abstract void testXMLConfig() throws IOException;;

	@Test
	public abstract void testXMLandJSON();

	public abstract AbstractAlgorithmParameters getAlgoParam();

	public abstract String getDefaultJSonConfig();

	public abstract void testSpecificParameters();

}
