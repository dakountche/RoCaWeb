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

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class MultipleAlignmentParametersTest extends AlgorithmParametersTest {

	private MultipleAlignmentParameters params = null;

	@Override
	public void testSpecificParameters() {

		assertEquals(getAlgoParam().isLibraryAligned(), false);
		assertEquals(getAlgoParam().getPairAlignmentAlgorithm().getMatch(), 1.0, 0.0001);
		assertEquals(getAlgoParam().getPairAlignmentAlgorithm().getMismatch(), 0.0, 0.0001);
		assertEquals(getAlgoParam().getPairAlignmentAlgorithm().getPenalty(), -1.0, 0.0001);
	}

	@Override
	public MultipleAlignmentParameters getAlgoParam() {
		if (params == null) {
			params = new MultipleAlignmentParameters(DefaultLearningAParameters.DEFAULT_CONFIG_FILE);
		}
		return (MultipleAlignmentParameters) params;
	}

	@Override
	public String getDefaultJSonConfig() {

		return "src/test/resources/json/amaa.json";
	}

	@Test
	public void test() {
		MultipleAlignmentParameters paramters = new MultipleAlignmentParameters(getDefaultJSonConfig());
		System.out.println(paramters.getPairAlignmentAlgorithm().printWelcomeMsg());
		assertEquals(paramters.getPairAlignmentAlgorithm().getMatch(), 1.0, 0.0001);
		assertEquals(paramters.getPairAlignmentAlgorithm().getMismatch(), -1.0, 0.0001);
		assertEquals(paramters.getPairAlignmentAlgorithm().getPenalty(), -2.0, 0.0001);

	}

	@Test
	public void testJsonElement() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonParser parser = new JsonParser();
		JsonElement je = parser.parse(new FileReader("src/test/resources/json/amaa.json"));
		System.out.println(je);
		MultipleAlignmentParameters paramters = new MultipleAlignmentParameters();
		paramters.loadConfig(je);

		System.out.println(paramters.printParameters());

	}

}
