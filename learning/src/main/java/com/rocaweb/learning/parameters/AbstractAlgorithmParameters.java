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
package com.rocaweb.learning.parameters;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rocaweb.commons.configuration.CommonConfiguration;
import com.rocaweb.commons.configuration.IConfiguration;
import com.rocaweb.commons.configuration.JsonConfigurator;
import com.rocaweb.commons.configuration.XMLConfigurator;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.algorithms.Algorithms;
import com.rocaweb.learning.serialization.json.JSonConfig;
import com.rocaweb.learning.utils.LearningUtils;

/**
 * Abstract parameter type for all the algorithms.
 * 
 * @author Djibrilla Amadou Kountche
 * @since 1.0.0
 *
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractAlgorithmParameters {

	/** The file containing the learning data set. It can be a directory */
	private String learningFileName = "tutos/";

	/** Indicates whether to use clustering at the pre-processing phase */
	private boolean clustering = false;

	/** Indicates whether the files has to be decode for the learning */
	private boolean decode = false;

	/** Indicates whether to change the case of the the files */
	private boolean casse = false;

	/** Indicates whether to remove redundant values */
	private boolean clean = false;

	/** The target encoding */
	private String base = "UTF-8";

	/** The number of clusters */
	private int clusterNumber = 1;

	/** The format in which the <code>contract </code> will be formated */
	private String ruleType = "ModSecurity";

	/** The base path to the learning set */
	private String learningPath = "learningDir";

	/** The configuration file */
	private String configFileName = null;

	private transient Logger logger = LogManager.getLogger(getClass());

	private transient XMLConfigurator subConfig = null;

	private transient XMLConfigurator classSubConfig = null;

	protected transient String format = "%-11s: %s\n";

	private transient static final String attributeName = "input";

	/** The configuration of Apache Mahout */
	private String mahout = null;

	public AbstractAlgorithmParameters() {

	}

	/**
	 * Construct an instance from a configuration file. This file must have an .json
	 * or .xml extension.
	 * 
	 * @param configFileName
	 *            the configuration file.
	 */
	public AbstractAlgorithmParameters(String configFileName) {

		loadConfig(configFileName);

	}

	/**
	 * Build a configuration form another <code>AbstractAlgorithmParameters <code>
	 * object.
	 * 
	 * @param aParameters
	 */
	public AbstractAlgorithmParameters(AbstractAlgorithmParameters aParameters) {
		clone(aParameters);

	}

	/**
	 * Clone a given instance into this one;
	 * 
	 * @param aParameters
	 */
	private void clone(AbstractAlgorithmParameters aParameters) {
		setLearningFile(aParameters.getLearningFile());
		setClustering(aParameters.hasClustering());
		setClusterNumber(aParameters.getClusterNumber());
		setCleanning(aParameters.hasCleanning());
		setCasse(aParameters.hasCasse());
		setBase(aParameters.getBase());
		setDecode(aParameters.hasDecode());
		setRuleType(aParameters.getRuleType());
		setLearningPathName(aParameters.getLearningPath());
		setConfigFileName(aParameters.getConfigFileName());

	}

	/**
	 * Load a configuration from a XML or JSON file.
	 * 
	 * @param configFileName
	 *            the configuration file
	 */
	public void loadConfig(String configFileName) {
		String ext = FilenameUtils.getExtension(configFileName);

		logger.trace(configFileName + " " + ext);

		if (ext.equals("xml")) {
			this.setConfigFileName(configFileName);
			this.baseConfig(getDefaultSubConfig());
			this.configureWithXML();

		} else if (ext.equals("json")) {
			this.setConfigFileName(configFileName);
			this.baseConfig(getJsonConfigurator());
			this.configureWithJson();

		} else {
			logger.error("File not supported");

			return;
		}

	}

	/**
	 * Get the XML sub-configuration node for the common parameters.
	 * 
	 * @return an instance of XMLConfigurator.
	 */
	private XMLConfigurator getDefaultSubConfig() {

		try {
			classSubConfig = getSubConfig(attributeName);
		} catch (Exception e) {
			logger.error(e.getMessage());

		}
		return classSubConfig;
	}

	/**
	 * Determines the sub configuration for the given attribute name.
	 * 
	 * @param attributename
	 * @return the configurator for the given attribute.
	 * @throws Exception
	 */
	private XMLConfigurator getSubConfig(String attributename) throws Exception {

		return getXMLConfigurator().configAt(attributename);

	}

	/**
	 * Configure the parameters of the sub class using an XML file.
	 * 
	 * @throws Exception
	 */
	private void configureWithXML() {

		this.specificConfig(getSubConfig());

	}

	/**
	 * Configure the parameters of the sub class using an Json file.
	 */
	protected void configureWithJson() {
		this.specificConfig(getJsonConfigurator());
	}

	/**
	 * Get the attribute name in the XML file corresponding to this class.
	 * 
	 * @return the attribute name.
	 * 
	 */

	protected String getAttributeName() {
		return attributeName;
	}

	public String getBase() {
		return base;
	}

	public int getClusterNumber() {

		return clusterNumber;
	}

	public String getConfigFileName() {

		if (configFileName == null) {
			configFileName = LearningUtils.DEFAULT_CONFIG_FILE;
		}

		return configFileName;
	}

	public String getLearningFile() {

		return learningFileName;
	}

	public String getLearningPath() {
		return learningPath;
	}

	/**
	 * @return the ruleType
	 */
	public String getRuleType() {
		return ruleType;
	}

	/**
	 * Creates the sub algorithms parameters if this instance contains
	 * sub-algorithms.
	 * 
	 * @return a list of <code>Algorithm</code>
	 */
	public List<Algorithm> getSubAlgorithmsFromJson() {
		return Lists.newArrayList();

	}

	/**
	 * Creates the sub algorithms parameters if this instance contains
	 * sub-algorithms.
	 * 
	 * @return a list of <code>Algorithm</code>
	 */
	public List<Algorithm> getSubAlgorithms() {

		List<Algorithm> algos = Lists.newArrayList();

		List<XMLConfigurator> subAlgoParams = getSubConfig().configsAt("sub-algorithm");

		logger.trace("Number of sub-algorithms : " + subAlgoParams.size());
		for (XMLConfigurator subAlgoParam : subAlgoParams) {

			algos.add(this.getSubAlgorithm(subAlgoParam));
		}

		return algos;
	}

	/**
	 * Create an algorithm given the XMLConfigurator instance.
	 * 
	 * @param subAlgoParam
	 *            XMLConfigurator instance
	 * @return an algorithm.
	 */
	private Algorithm getSubAlgorithm(XMLConfigurator subAlgoParam) {
		String algoName = subAlgoParam.getString("name");
		logger.trace(algoName);
		Algorithm<?> algo = Algorithms.createAlgorithm(algoName, getConfigFileName());

		boolean dconfig = subAlgoParam.getBoolean("default-config");
		if (dconfig) {
			logger.trace("Default config");
			algo.setDefaultParameters();
		} else {
			logger.trace("Sub params");
			algo.getParameters().reload(subAlgoParam.configAt("sub-params"));
		}

		algo.getParameters().setLearningFile(getLearningFile());
		return algo;
	}

	/**
	 * Get a sub algorithm from the XML configuration file;
	 * 
	 * @return an Algorithm instance.
	 */
	public Algorithm getSubAlgorithm() {
		XMLConfigurator subAlgoParam = getSubConfig().configAt("sub-algorithm");
		return this.getSubAlgorithm(subAlgoParam);

	}

	/**
	 * Reload the configuration of a parameters instance from XMLConfigurator.
	 * 
	 * @param configurationAt
	 *            the instance XMLConfigurator
	 * @throws Exception
	 */
	private void reload(XMLConfigurator configurationAt) {
		setSubConfig(configurationAt);
		configureWithXML();

	}

	/**
	 * 
	 * @param configurationAt
	 *            the subnode of the XMLConfigurator
	 */
	private void setSubConfig(XMLConfigurator configurationAt) {
		this.subConfig = configurationAt;

	}

	/**
	 * Determine the XMLConfigurator given the attribute name corresponding to the
	 * algorithm.
	 * 
	 * @return XMLConfigurator instance.
	 */
	public XMLConfigurator getSubConfig() {
		if (subConfig == null)
			try {
				logger.trace(getAttributeName());
				subConfig = getXMLConfigurator().configAt(getAttributeName());
			} catch (Exception e) {
				logger.error(e.getMessage());

			}
		return subConfig;
	}

	/**
	 * Get the common XMLConfigurator.
	 * 
	 * @return an instance of XMLConfigurator
	 * @throws Exception
	 */
	public XMLConfigurator getXMLConfigurator() throws Exception {

		return CommonConfiguration.getInstance().getXMLConfig(getConfigFileName());
	}

	public boolean hasCasse() {
		return casse;
	}

	public boolean hasCleanning() {
		return clean;
	}

	public boolean hasClustering() {
		return clustering;
	}

	public boolean hasDecode() {
		return decode;
	}

	/**
	 * Print the parameter.
	 * 
	 * @return a message containing the values of the field.
	 */
	public String printParameters() {
		StringBuilder message = new StringBuilder("#\t Global Parameters\n");
		message.append(String.format(format, "# LearningDir", getLearningPath()));
		message.append(String.format(format, "# LearningFile", getLearningFile()));
		message.append(String.format(format, "# Decode", this.decode));
		if (decode)
			message.append(String.format(format, "# Base", this.base));
		message.append(String.format(format, "# Clean", this.clean));
		message.append(String.format(format, "# Cluster", this.clustering));
		if (clustering)
			message.append(String.format(format, "# Cluster number", this.clusterNumber));

		message.append(String.format(format, "# Rule Type", this.ruleType));
		message.append("#\t Sub parameters\n");

		return message.toString();
	}

	public void setBase(String base) {

		this.base = base;
	}

	public void setCasse(boolean casse) {

		this.casse = casse;
	}

	public void setCleanning(boolean clean) {

		this.clean = clean;
	}

	public void setClustering(boolean clustering) {

		this.clustering = clustering;
	}

	public void setClusterNumber(int clusteringNumber) {

		clusterNumber = clusteringNumber;
	}

	/**
	 * @param configFileName
	 */
	public void setConfigFileName(String configFileName) {

		if (configFileName != null)
			this.configFileName = configFileName;
		else
			this.configFileName = LearningUtils.DEFAULT_CONFIG_FILE;

	}

	public void setDecode(boolean decode) {

		this.decode = decode;
	}

	public void setLearningFile(String learningFile) {
		this.learningFileName = learningFile;
	}

	public void setLearningPathName(String learningPath) {
		this.learningPath = learningPath;
	}

	/**
	 * @param ruleType
	 *            the ruleType to set
	 */
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String toString() {

		String string = String.format(
				"\"learningPath\": \"%s\", \"learningFileName\": \"%s\", \"clustering\": %s, \"clusterNumber\": %d, \"decode\": %b, \"base\": \"%s\", \"casse\": %b, \"ruleType\": \"%s\", \"configFile\": \"%s\"",
				this.getLearningPath(), this.getLearningFile(), this.hasClustering(), this.getClusterNumber(),
				this.hasDecode(), this.getBase(), this.getCasse(), this.getRuleType(), this.getConfigFileName());
		return string;
	}

	/**
	 * @return
	 */
	private boolean getCasse() {

		return casse;
	}

	public abstract String specificStringForJson();

	/**
	 * @return
	 */
	public abstract String toJson();

	public Gson getGson() {
		return JSonConfig.gson;
	}

	/**
	 * Get the JsonConfigurator given the configuration file.
	 * 
	 * @return an instance of JsonConfigurator
	 */
	public JsonConfigurator getJsonConfigurator() {
		return CommonConfiguration.getInstance().getJsonConfigurator(getConfigFileName());
	}

	/**
	 * Configure, given the IConfiguration (JSON or XML), the basic parameters.
	 * 
	 * @param config
	 *            the configurator.
	 */
	protected void baseConfig(IConfiguration config) {
		setClustering(config.getBoolean("clustering"));
		setDecode(config.getBoolean("decode"));
		setCleanning(config.getBoolean("clean"));
		setCasse(config.getBoolean("casse"));
		setBase(config.getString("base"));
		setRuleType(config.getString("ruleType"));
		setLearningPathName(config.getString("learningPath"));
		setLearningFile(config.getString("learningFileName"));
		setClusterNumber(config.getInt("clusterNumber"));
		setMahoutConfigFilePath(config.getString("mahout"));

	}

	/**
	 * Configure the specific parameters of tehe sub classes.
	 * 
	 * @param config
	 *            the configurator.
	 */
	protected abstract void specificConfig(IConfiguration config);

	/**
	 * @return the mahoutConfigFilePath
	 */
	public String getMahoutConfigFilePath() {
		return mahout;
	}

	/**
	 * @param mahoutConfigFilePath
	 *            the mahoutConfigFilePath to set
	 */
	public void setMahoutConfigFilePath(String mahoutConfigFilePath) {
		this.mahout = mahoutConfigFilePath;
	}

	/**
	 * @param jsonElement
	 */
	/**
	 * @param jsonElement
	 */
	public void loadConfig(JsonElement jsonElement) {

		JsonConfigurator jsc = new JsonConfigurator();
		jsc.setJsonObject(jsonElement.getAsJsonObject());
		this.baseConfig(jsc);
		this.specificConfig(jsc);
	}

	/**
	 * Generates randomly the parameters this instance. TODO
	 */
	public void randomlyGenerateParamValues() {

	}

}
