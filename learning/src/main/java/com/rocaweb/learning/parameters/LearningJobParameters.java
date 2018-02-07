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

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.rocaweb.learning.algorithms.Algorithm;

/**
 * The parameters of the learning job.
 * 
 * /**
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
@SuppressWarnings("rawtypes")
public class LearningJobParameters {

	@SerializedName("algorithms")
	private List<Algorithm> algorithms = new ArrayList<Algorithm>();

	public List<Algorithm> getAlgorithms() {

		return algorithms;
	}

	public void setAlgorithms(List<Algorithm> algorithms) {
		this.algorithms = algorithms;

	}

}
