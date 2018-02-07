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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Contains the global configuration of all the modules :
 * 
 * <ul>
 * <li>Commons</li>
 * <li>Learning</li>
 * <li>UI</li>
 * </ul>
 * 
 * @author Djibrilla AMADOU KOUNTCHE
 * 
 * @since 1.0.0
 *
 */
public final class CommonConfiguration {

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static Logger logger = LogManager.getLogger(CommonConfiguration.class);

	private XMLConfigurator xmlConfig = null;

	private JsonConfigurator jsonConfig = null;

	/**
	 * The unique instance of the configuration
	 */
	private static CommonConfiguration INSTANCE = null;

	
	public static String FILE_SEPARATOR = System.getProperty("file.separator");


	/**
	 * Directory where the Lua files are stored. These files contain the algorithms
	 * used to extend Modsecurity.
	 */
	public static String DEFAULT_MODSECURITY_RULES_DIR = "/opt/validation/";
    
	/**
	 * The encoding used in this project
	 */
	public static String DEFAULT_ENCODING = "UTF-8";

	
	public static String ALGO_MSG_TITLE="# Algorithm name : ";
	public static String ALGO_MSG_LEARN="# Learning from  : ";
	public static String ALGO_MSG_CONFIG="# Configuration file : ";
	public static String ALGO_MSG_PARAM="# Parameters : \n";
	public static String ALGO_EMPTY_FILE="The file %s is empty !";
	public static String ALGO_FILE_ERROR="File error on:  %s ";

	/**
	 * Default constructor
	 */
	private CommonConfiguration() { }

	
	/**
	 * Creates an instance of XMLConfiguration from an XML file
	 * 
	 * @param configFileName
	 *            the XML configuration file
	 * 
	 * @return instance of XMLConfiguration
	 */
	public XMLConfigurator getXMLConfig(String configFileName) {
		try {
			if (xmlConfig == null) {
				xmlConfig = new XMLConfigurator(configFileName);
			}
		} catch (ConfigurationException e) {
			logger.error(e.getMessage(), e);
		}
		return xmlConfig;
	}


	/**
	 * Get the singleton of this class
	 * 
	 * @return an instance of CommonConfiguration
	 */
	public static synchronized CommonConfiguration getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CommonConfiguration();
		return INSTANCE;
	}

	public String getModSecurityInstallDir() {
		return DEFAULT_MODSECURITY_RULES_DIR;
	}

	/**
	 * Create an instance of JSonConfigurator from a JSon configuration file
	 * 
	 * @param configFileName
	 *            JSon configuration file
	 * @return instance of JsonConfigurator
	 */
	public JsonConfigurator getJsonConfigurator(String configFileName) {

		if (jsonConfig == null) {
			jsonConfig = new JsonConfigurator(configFileName);
		} else
			jsonConfig.createJsonObject(configFileName);

		return jsonConfig;
	}

}
