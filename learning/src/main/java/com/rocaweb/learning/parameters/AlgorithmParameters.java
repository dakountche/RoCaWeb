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
package com.rocaweb.learning.parameters;

import org.apache.logging.log4j.util.Strings;

import com.rocaweb.commons.configuration.IConfiguration;

/**
 * This class represents the basic parameters. Some algorithms, does not require
 * special parameters. Therefore they use this class.
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class AlgorithmParameters extends AbstractAlgorithmParameters {

	public AlgorithmParameters() {

	}

	AlgorithmParameters(AbstractAlgorithmParameters aParameters) {
		super(aParameters);
	}

	public AlgorithmParameters(String defaultConfigFile) {
		super(defaultConfigFile);
	}

	@Override
	public String specificStringForJson() {
		return Strings.EMPTY;
	}

	@Override
	public String toJson() {
		return this.getGson().toJson(this, getClass());
	}

	@Override
	protected void specificConfig(IConfiguration config) {
		// nothing todo.

	}

}
