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

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rocaweb.commons.configuration.IConfiguration;
import com.rocaweb.commons.configuration.JsonConfigurator;
import com.rocaweb.commons.configuration.XMLConfigurator;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.serialization.json.CrossValidationJSD;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */

@SuppressWarnings("rawtypes")
public class CrossValidationParam extends AlgorithmParameters {

	/** Indicates whether the corpus has to be randomize */
	private boolean shuffle = false;

	/** The list of sub-algorithms */
	private List<Algorithm> algorithms = null;

	/** The number of partitions */
	private int k = 5;

	/** Uses to validates */
	private double ratio = 0.8;

	/** The GSon serializer */
	private GsonBuilder gb = null;

	public CrossValidationParam() {

	}

	public CrossValidationParam(AbstractAlgorithmParameters parseParameters) {
		super(parseParameters);
	}

	public CrossValidationParam(String configFileName) {
		this.loadConfig(configFileName);

	}

	public List<Algorithm> getAlgorithms() {
		if (algorithms == null) {
			algorithms = Lists.newArrayList();

		}

		return algorithms;

	}

	/**
	 * @return the shuffle
	 */
	public boolean isShuffle() {
		return shuffle;
	}

	/**
	 * @param shuffle
	 *            the shuffle to set
	 */
	public void setShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}

	/**
	 * @return the k
	 */
	public int getK() {
		return k;
	}

	/**
	 * @param k
	 *            the k to set
	 */
	public void setK(int k) {
		this.k = k;
	}

	/**
	 * @return the ratio
	 */
	public double getRatio() {
		return ratio;
	}

	/**
	 * @param ratio
	 *            the ratio to set
	 */
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	@Override
	public String printParameters() {
		StringBuffer message = new StringBuffer(super.printParameters());
		message.append(String.format(format, "# List of algorithms", this.getAlgorithms()));
		message.append(String.format(format, "# K", this.getK()));
		message.append(String.format(format, "# Ratio", this.getRatio()));
		message.append(String.format(format, "# Shuffled", this.isShuffle()));
		return message.toString();
	}

	/**
	 * @param subAlgoritms
	 */
	public void setAlgorithms(List<Algorithm> subAlgoritms) {

		this.algorithms = subAlgoritms;

	}

	@Override
	public String getAttributeName() {
		return "crossvalidation";
	}

	@Override
	public String specificStringForJson() {

		return String.format(", shuffle: %s, ratio: %s, k: %s", this.isShuffle(), this.getRatio(), this.getK());
	}

	@Override
	protected void specificConfig(IConfiguration config) {

		if (config instanceof XMLConfigurator) {

			updateField(config.getBoolean("shuffle"), config.getInt("k"), config.getDouble("ratio"),
					getSubAlgorithms());
		} else {
			JsonConfigurator jsc = (JsonConfigurator) config;
			CrossValidationParam cvp = this.getGson().fromJson(jsc.getJsonObject(), getClass());
			updateField(cvp.isShuffle(), cvp.getK(), cvp.getRatio(), cvp.getAlgorithms());

		}

	}

	private void updateField(boolean ishuffe, int k, double ratio, List<Algorithm> algos) {
		this.setShuffle(ishuffe);
		this.setK(k);
		this.setRatio(ratio);
		this.setAlgorithms(algos);
	}

	public Gson getGson() {
		if (gb == null) {
			gb = new GsonBuilder();
			gb.registerTypeAdapter(CrossValidationParam.class, new CrossValidationJSD());

		}

		return gb.create();
	}

}
