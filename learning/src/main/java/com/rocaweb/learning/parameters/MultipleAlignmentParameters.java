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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rocaweb.commons.configuration.IConfiguration;
import com.rocaweb.commons.configuration.JsonConfigurator;
import com.rocaweb.commons.configuration.XMLConfigurator;
import com.rocaweb.learning.algorithms.alignment.pair.NeedlemanWunsch;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class MultipleAlignmentParameters extends AlgorithmParameters {

	private transient Logger logger = LogManager.getLogger(getClass());

	/** The pair alignment algorithm to use */
	private NeedlemanWunsch pairAlignmentAlgorithm = null;

	/** Indicates if the library was a result of an alignment */
	private boolean isLibraryAligned = false;

	public MultipleAlignmentParameters() {

	}

	public MultipleAlignmentParameters(String defaultConfigFile) {
		this.loadConfig(defaultConfigFile);

	}

	/**
	 * @return the pairAlignmentAlgorithm
	 */

	public NeedlemanWunsch getPairAlignmentAlgorithm() {

		if (pairAlignmentAlgorithm == null) {
			NeedlemanWunsch nw = new NeedlemanWunsch();
			nw.setDefaultParameters();
			pairAlignmentAlgorithm = nw;
		}
		return pairAlignmentAlgorithm;
	}

	/**
	 * @param pairAlignmentAlgorithm
	 *            the pairAlignmentAlgorithm to set
	 */
	public void setPairAlignmentAlgorithm(NeedlemanWunsch pairAlignmentAlgorithm) {

		this.pairAlignmentAlgorithm = pairAlignmentAlgorithm;
	}

	/**
	 * @return the isLibraryAligned
	 */
	public boolean isLibraryAligned() {
		return isLibraryAligned;
	}

	/**
	 * @param isLibraryAligned
	 *            the isLibraryAligned to set
	 */
	public void setLibraryAligned(boolean isLibraryAligned) {
		this.isLibraryAligned = isLibraryAligned;
	}

	@Override
	public String printParameters() {

		StringBuilder message = new StringBuilder(super.printParameters());

		message.append(
				String.format(format, "# sub-algorithms", this.getPairAlignmentAlgorithm().getClass().getSimpleName()));
		message.append(this.getPairAlignmentAlgorithm().getParameters().specificParametersMessage());

		message.append(String.format(format, "# Library alignement", isLibraryAligned));
		logger.trace(message);
		return message.toString();
	}

	public String getAttributeName() {

		return "amaa";
	}

	@Override
	public String specificStringForJson() {

		return String.format("isLibraryAligned: %s", this.isLibraryAligned());
	}

	@Override
	protected void specificConfig(IConfiguration config) {
		if (config.containsKey("isLibraryAligned"))
			this.isLibraryAligned = config.getBoolean("isLibraryAligned");

		NeedlemanWunsch algo = null;

		if (config instanceof XMLConfigurator) {
			algo = (NeedlemanWunsch) getSubAlgorithm();

		} else {

			JsonConfigurator jc = (JsonConfigurator) config;
			JsonObject jo = jc.getJsonObject();
			logger.trace(jo);
			if (jo.has("pairAlignmentAlgorithm")) {
				algo = new NeedlemanWunsch();
				JsonElement je = jo.get("pairAlignmentAlgorithm");
				logger.trace(je);
				logger.trace(this.getConfigFileName());
				algo.setConfigFileName(this.getConfigFileName());
				logger.trace(algo.getConfigFileName());
				algo.setParameters(getGson().fromJson(je, AlignmentAParameters.class));

			} else {
				logger.error("pairAlignmentAlgorithm " + "not founnd in " + config.toString());
				
				return;
			}

		}

		this.setPairAlignmentAlgorithm(algo);
		logger.trace(this.getPairAlignmentAlgorithm().printWelcomeMsg());
	}

}
