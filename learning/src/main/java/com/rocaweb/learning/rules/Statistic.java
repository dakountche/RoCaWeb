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
package com.rocaweb.learning.rules;

import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.data.statistical.Validation;

/**
 * A Generic statistical <code>contract</code>.
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 *
 * @param <T>
 */

public class Statistic<T extends Validation> extends Contract<T> {

	public Statistic() {

	}

	public Statistic(Algorithm<T> algorithm) {
		this.algorithm = algorithm;
	}

	public String getValidatorName() {
		return this.getAlgorithm().getClass().getSimpleName().toLowerCase();

	}

	@Override
	public boolean validate(String sequence, T validator) {
		return validator.validate(sequence);
	}

}
