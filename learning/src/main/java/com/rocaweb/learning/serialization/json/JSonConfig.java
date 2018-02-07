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

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.algorithms.Algorithms;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
@SuppressWarnings("rawtypes")
public abstract class JSonConfig<T> implements JsonSerializer<T>, JsonDeserializer<T> {

	public static final Gson gson = new Gson();

	/**
	 * Add a Json element as "AlgotrithmeName" : {parameters} to a JsonOject.
	 */
	private void addAlgorithm(JsonObject obj, Algorithm<?> algorithm) {

		JsonPrimitive jp = new JsonPrimitive(algorithm.toJSon());
		obj.add(algorithm.getClass().getSimpleName(), jp);
	}

	/**
	 * And Json elements for a collection of Algorithms.
	 * 
	 * @param obj
	 *            The Json Object
	 * @param collection
	 *            The collection of Algorithms.
	 */
	public void addAlgorithms(JsonObject obj, Collection<Algorithm<?>> collection) {
		for (Algorithm<?> algo : collection) {
			addAlgorithm(obj, algo);
		}
	}

	/**
	 * Instantiates an Algorithm object given the name
	 * 
	 * @param name
	 *            the simple class name of the algorithm
	 * @param jsonElement
	 *            the configuration
	 * @return An instance of Algorithm.
	 */
	private Algorithm<?> getAlgorithm(String name, JsonElement jsonElement) {

		return Algorithms.createAlgorithm(name, jsonElement);
	}

	public List<Algorithm> getAlgorithms(Set<Entry<String, JsonElement>> entries) {
		List<Algorithm> algorithms = Lists.newArrayList();
		for (Entry<String, JsonElement> entry : entries) {

			Algorithm<?> algo = getAlgorithm(entry.getKey(), entry.getValue());

			if (algo != null) {
				algorithms.add(algo);
			}
		}
		return algorithms;
	}

}
