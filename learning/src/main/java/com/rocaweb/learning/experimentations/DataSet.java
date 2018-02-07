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
package com.rocaweb.learning.experimentations;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class DataSet {

	private List<List<String>> trainings;

	private Pair<HashSet<String>, HashSet<String>> testSet;

	public DataSet() {
		this.trainings = Lists.newArrayList();
		this.testSet = Pair.of(new HashSet<String>(), new HashSet<String>());
	}

	/**
	 * @return the trainings
	 */
	public List<List<String>> getTrainings() {
		return trainings;
	}

	/**
	 * @param trainings
	 *            the training to set
	 */
	public void setTrainings(List<List<String>> trainings) {
		this.trainings = trainings;
	}

	/**
	 * @return the testSet
	 */
	public Pair<HashSet<String>, HashSet<String>> getTestSet() {
		return testSet;
	}

	/**
	 * @param testSet
	 *            the testSet to set
	 */
	public void setTestSet(Pair<HashSet<String>, HashSet<String>> testSet) {
		this.testSet = testSet;
	}

}
