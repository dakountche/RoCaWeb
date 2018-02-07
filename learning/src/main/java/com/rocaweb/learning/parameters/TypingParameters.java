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
public class TypingParameters extends AlgorithmParameters {

	private Boolean bounded = false;

	public TypingParameters() {

	}

	public TypingParameters(String defaultConfigFile) {
		this.loadConfig(defaultConfigFile);

	}

	@Override
	public String printParameters() {
		String message = super.printParameters();
		message += String.format(format, "# Bounded", this.getBounded());
		return message;
	}

	/**
	 * @return the bounded
	 */
	public Boolean getBounded() {
		return bounded;
	}

	/**
	 * @param bounded
	 *            the bounded to set
	 */
	public void setBounded(Boolean bounded) {
		this.bounded = bounded;
	}

	public String getAttributeName() {

		return "typing";
	}

	@Override
	public String specificStringForJson() {

		return String.format("bounded:%s", this.getBounded());
	}

	@Override
	protected void specificConfig(IConfiguration config) {
		this.setBounded(config.getBoolean("bounded"));

	}

}
