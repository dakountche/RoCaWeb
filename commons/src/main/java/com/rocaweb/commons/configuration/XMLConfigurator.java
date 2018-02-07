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

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import com.google.common.collect.Lists;

/**
 * Implements the XML configuration using the Apache commons Configuration.
 * 
 * @author Djibrilla AMADOU KOUNTCHE
 *
 * @see https://commons.apache.org/proper/commons-configuration/
 * 
 * @since 1.0.0
 * 
 */
public class XMLConfigurator implements IConfiguration {

	private HierarchicalConfiguration xmlConfig = null;

	
	/**
	 * The default constructor
	 */
	public XMLConfigurator() {

	}

	/**
	 * Constructor
	 * 
	 * @param configFileName
	 * @throws ConfigurationException
	 */
	public XMLConfigurator(String configFileName) throws ConfigurationException {
		setXmlConfig(new XMLConfiguration(configFileName));
	}


    /**
     * Determine the appropriate XML node for the parameter
     * @param subNodeName
     * @return an instance of this class
     */
	public XMLConfigurator configAt(String subNodeName) {

		return createConfig(getXmlConfig().configurationAt(subNodeName));
	}

	
	/**
	 * Create an instance of this class from a sub-node of an existing instance
	 * @param configurationAt: the sub-node
	 * @return an instance of this class
	 */
	private XMLConfigurator createConfig(HierarchicalConfiguration configurationAt) {
		XMLConfigurator xmlConfig = new XMLConfigurator();
		xmlConfig.setXmlConfig(configurationAt);
		return xmlConfig;
	}

	
	/**
	 * Determines all the sub-node having the same name given by the parameter
	 * @param subNodeName: the name to look for
	 * @return a List of all the same sub-nodes
	 */
	public List<XMLConfigurator> configsAt(String subNodeName) {
		List<XMLConfigurator> xmlConfigs = Lists.newArrayList();

		for (HierarchicalConfiguration subConfig : getXmlConfig().configurationsAt(subNodeName)) {

			xmlConfigs.add(createConfig(subConfig));
		}
		return xmlConfigs;
	}

	/**
	 * @return the xmlConfig
	 */
	public HierarchicalConfiguration getXmlConfig() {
		return xmlConfig;
	}

	/**
	 * @param xmlConfig
	 *            the xmlConfig to set
	 */
	public void setXmlConfig(HierarchicalConfiguration xmlConfig) {
		this.xmlConfig = xmlConfig;
	}

	
	@Override
	public String getString(String name) {

		return getXmlConfig().getString(name);
	}

	@Override
	public boolean getBoolean(String name) {

		return getXmlConfig().getBoolean(name);
	}

	@Override
	public int getInt(String name) {

		return getXmlConfig().getInt(name);
	}

	@Override
	public double getDouble(String name) {

		return getXmlConfig().getDouble(name);
	}
	
	
	@Override
	public boolean containsKey(String string) {

		return getXmlConfig().containsKey(string);
	}

}
