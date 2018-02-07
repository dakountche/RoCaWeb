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
package com.rocaweb.commons.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.rocaweb.commons.fs.FSManager;

/**
 * Generate configuration from Json files using the Gson library.
 * 
 * @author Djibrilla AMADOU KOUNTCHE
 * 
 * @see https://github.com/google/gson
 * 
 * @since 1.0.0
 */
public class JsonConfigurator implements IConfiguration {

	private Logger logger = LogManager.getLogger(JsonConfigurator.class);

	/** A jsonObject containing the result of the parsed file */
	private JsonObject jsonObject = null;

	
	/**
	 * Default constructor
	 */
	public JsonConfigurator() { }

	
	/**
	 * Parses the JSON file and creates the JSON object.
	 * 
	 * @param configFileName
	 *            the JSon configuration file.
	 */
	public JsonConfigurator(String configFileName) {
		createJsonObject(configFileName);
	}

	
	/**
	 * @return the jsonObject
	 */
	public JsonObject getJsonObject() {

		return jsonObject;
	}

	
	/**
	 * @param jsonObject
	 *            the jsonObject to set
	 */
	public void setJsonObject(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	private JsonElement get(String name) {
		logger.trace(name);
		return this.getJsonObject().get(name);
	}

	/**
	 * Parses the JSON file and creates the JSON object.
	 * 
	 * @param configFileName
	 *            the JSon configuration file.
	 */
	public void createJsonObject(String configFileName) {

		JsonObject jsonObject = new JsonObject();
		JsonParser jparser = new JsonParser();

		try {
           
			JsonElement jelement = jparser.parse(FSManager.read(configFileName));
			jsonObject = jelement.getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException e ) {
			logger.error(e.getMessage(),e);
		}

		setJsonObject(jsonObject);

	}

	@Override
	public boolean containsKey(String string) {

		return this.getJsonObject().has(string);
	}
	
	@Override
	public String getString(String name) {

		return get(name).getAsString();
	}

	@Override
	public boolean getBoolean(String name) {
		return get(name).getAsBoolean();
	}

	@Override
	public int getInt(String name) {

		return get(name).getAsInt();
	}

	@Override
	public double getDouble(String name) {

		return get(name).getAsDouble();
	}


}
