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
package com.rocaweb.learning.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rocaweb.learning.parameters.LearningJobParameters;
import com.rocaweb.learning.serialization.json.JobJSD;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class JobTest {

	Job job = new Job();

	LearningJobParameters ljp = null;

	String config = null;
	GsonBuilder gsb = new GsonBuilder();

	Gson gson = null;

	public void init() throws IOException {
		gsb.registerTypeAdapter(LearningJobParameters.class, new JobJSD());
		gson = gsb.create();
		config = FileUtils.readFileToString(new File("src/test/resources/json/job.json"), Charsets.UTF_8);
		ljp = (LearningJobParameters) gson.fromJson(config, LearningJobParameters.class);
	}

	@Test
	public void testJson() throws IOException {

		init();

		LearningJobParameters ljp = (LearningJobParameters) gson.fromJson(config, LearningJobParameters.class);

		assertEquals(ljp.getAlgorithms().size(), 3);

		// job.execute();

		// System.out.println(job.toJSon());
	}

	@Test
	public void testDeserialization() throws IOException {
		init();
		job.setParameters(ljp);
		JsonParser parser = new JsonParser();
		JsonElement source = parser.parse(config);
		JsonElement target = parser.parse(job.toJSon());

		assertEquals(source, target);
	}

}
