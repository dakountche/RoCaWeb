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
package com.rocaweb.learning.serialization.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.parameters.LearningJobParameters;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class JobJSD extends JSonConfig<LearningJobParameters> {

	@Override
	public JsonElement serialize(LearningJobParameters src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jobj = new JsonObject();

		for (Algorithm<?> algo : src.getAlgorithms()) {
			JsonElement algoParam = context.serialize(algo.getParameters());

			jobj.add(algo.getClass().getName(), algoParam);
		}

		return jobj;
	}

	@Override
	public LearningJobParameters deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		LearningJobParameters params = new LearningJobParameters();
		JsonObject jobj = json.getAsJsonObject();

		params.setAlgorithms(getAlgorithms(jobj.entrySet()));
		return params;
	}

}
