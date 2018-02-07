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
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;
import com.rocaweb.learning.parameters.AlgorithmParameters;
import com.rocaweb.learning.parameters.CrossValidationParam;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */

public class CrossValidationJSD extends JSonConfig<CrossValidationParam> {

	@Override
	public JsonElement serialize(CrossValidationParam src, Type typeOfSrc, JsonSerializationContext context) {

		JsonElement je = context.serialize((AbstractAlgorithmParameters) src, AbstractAlgorithmParameters.class);
		final JsonObject jobj = je.getAsJsonObject();

		serialiseSpecificParameters(jobj, src, context);
		serialiseAlgorithms(jobj, src);

		return jobj;
	}

	/**
	 * @param jobj
	 * @param src
	 */
	private void serialiseAlgorithms(JsonObject jobj, CrossValidationParam src) {
		jobj.addProperty("k", src.getK());
		jobj.addProperty("shuffle", src.isShuffle());
		jobj.addProperty("ratio", src.getRatio());

	}

	/**
	 * @param jobj
	 * @param src
	 */
	private void serialiseSpecificParameters(JsonObject jobj, CrossValidationParam src,
			JsonSerializationContext context) {
		JsonArray ja = new JsonArray();

		for (Algorithm<?> algo : src.getAlgorithms()) {
			JsonObject jo = new JsonObject();
			JsonElement algoparam = context.serialize(algo.getParameters(), algo.getParameters().getClass());
			jo.add(algo.getClass().getSimpleName(), algoparam);
			ja.add(jo);
		}

		jobj.add("algorithms", ja);

	}

	@Override
	public CrossValidationParam deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		AlgorithmParameters aap = (AlgorithmParameters) context.deserialize(json, AlgorithmParameters.class);

		CrossValidationParam cvp = new CrossValidationParam(aap);
		setSpecificParameters(cvp, json);
		setAlgorithms(cvp, json);

		return cvp;
	}

	private void setSpecificParameters(CrossValidationParam cvp, JsonElement json) {
		int k = json.getAsJsonObject().get("k").getAsInt();
		boolean shuffle = json.getAsJsonObject().get("shuffle").getAsBoolean();
		double ratio = json.getAsJsonObject().get("ratio").getAsDouble();
		cvp.setShuffle(shuffle);
		cvp.setRatio(ratio);
		cvp.setK(k);

	}

	private void setAlgorithms(CrossValidationParam cvp, JsonElement json) {
		JsonArray algorithms = json.getAsJsonObject().get("algorithms").getAsJsonArray();
		@SuppressWarnings("rawtypes")
		List<Algorithm> algos = Lists.newArrayList();
		for (JsonElement element : algorithms) {

			algos.addAll(getAlgorithms(element.getAsJsonObject().entrySet()));
		}
		cvp.setAlgorithms(algos);

	}

}
