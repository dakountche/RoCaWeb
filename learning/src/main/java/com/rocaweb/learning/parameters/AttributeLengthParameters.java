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

import com.rocaweb.commons.configuration.IConfiguration;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class AttributeLengthParameters extends AlgorithmParameters {

	/** The threshold use to do the validation */
	private double threshold = 0.10;

	public AttributeLengthParameters() {

	}

	public AttributeLengthParameters(String configFileName) {
		this.loadConfig(configFileName);

	}

	public double getThreshold() {
		return threshold;
	}

	@Override
	public String printParameters() {

		String message = super.printParameters();
		message += String.format(format, "# Threshold", threshold);
		return message;
	}

	public String toString() {
		StringBuilder string = new StringBuilder(super.toString());

		string.append(String.format("threshold: %s", this.getThreshold()));
		return string.toString();
	}

	@Override
	protected void specificConfig(IConfiguration config) {
		this.threshold = config.getDouble("threshold");

	}

	public String getAttributeName() {
		return "attributeLentgth";
	}

}
