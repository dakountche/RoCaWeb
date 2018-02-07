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
package com.rocaweb.learning.rules;

import java.util.Set;

import com.google.common.collect.Sets;
import com.rocaweb.learning.algorithms.metalearning.CrossValidation;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
@SuppressWarnings("rawtypes")
public final class MetaContract extends Contract<String> {

	private Set<Contract<?>> contracts = null;

	/**
	 * @param crossValidation
	 */
	public MetaContract(CrossValidation crossValidation) {
		this.algorithm = crossValidation;
	}

	/**
	 * @return the contract
	 */
	public Set<Contract<?>> getContracts() {
		if (contracts == null)
			contracts = Sets.newHashSet();
		return contracts;
	}

	/**
	 * @param contract
	 *            the contract to set
	 */
	public void setContract(Set<Contract<?>> contract) {
		this.contracts = contract;
	}

	@Override
	public String generateRule() {
		StringBuilder welcomeMsg = new StringBuilder(getAlgorithm().printWelcomeMsg());
		logger.info(getContracts().size());
		for (Contract subContract : getContracts()) {

			welcomeMsg.append("\n" + subContract.generateRule());
			// System.out.println(subContract.getResultToFormat());
		}
		return welcomeMsg.toString();
	}

}
