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
package com.rocaweb.learning.metalearning;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rocaweb.learning.algorithms.metalearning.CrossValidation;
import com.rocaweb.learning.parameters.CrossValidationParam;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.serialization.json.CrossValidationJSD;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class CrossValidationTest {

	String learningConfig = null;

	GsonBuilder gb = null;
	String file = null;
	String[] strings = { "AAA", "BBBBBB22", "CCCCCCCCCCCC2", "Ali", "Mali", "Kali", "ALLAM", "SHASAM", "LAAM", "NAMMA",
			"MMMMM" };
	List<String> list = Lists.newArrayList(strings);

	Gson gson = null;
	CrossValidationParam cvp = null;
	CrossValidation cv = null;

	public void init() throws IOException {
		learningConfig = (new File("src/test/resources/json/cv.json")).getAbsolutePath();
		gb = new GsonBuilder();
		cv = new CrossValidation();
		cv.loadConfiguration(learningConfig);
		gb.registerTypeAdapter(CrossValidationParam.class, new CrossValidationJSD());
		file = org.apache.commons.io.FileUtils.readFileToString(new File("src/test/resources/json/cv.json"),
				Charsets.UTF_8);
		gson = gb.create();

	}



	@Test
	public void testSerialization() throws IOException {

		init();
		// cv.setParameters(cvp);
		assertEquals(cv.getParameters().getK(), 5);
		assertEquals(cv.getParameters().isShuffle(), false);
		assertEquals(cv.getParameters().getRatio(), 0.8, 0.0001);
		assertEquals(cv.getParameters().getAlgorithms().size(), 2);

		System.out.println(cvp.printParameters());
		System.out.println(gson.toJson(cvp, CrossValidationParam.class));
		System.out.println(cv.crossValidation(list));
		System.out.println(cv.getContract().getContracts().size());

		for (Contract<?> contract : cv.getContract().getContracts()) {

			System.out.println(contract.getResultToFormat());
		}
	}

	@Test
	public void testDeserialiazition() throws IOException {
		init();
		// cv.setParameters(cvp);
		JsonParser parser = new JsonParser();
		JsonElement source = parser.parse(file);
		JsonElement target = parser.parse(cv.toJSon());

		System.out.println(source);
		System.out.println(target);

		assertEquals(source, target);

	}

	@Test
	public void testXvalidate() throws IOException {
		init();
		 cv.setParameters(cvp);
		
		System.out.println(cv.getContract().generateRule());

	}

}
