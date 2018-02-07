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
package com.rocaweb.learning.data.metalearning;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class Corpus<E> extends Partition<E> {

	private LinkedList<List<E>> learningPartions;
	private List<E> testSet;
	private List<E> trainingSet;

	/**
	 * @param sequences
	 * @param k
	 * @param shuffle
	 */
	public Corpus(List<E> sequences, int k, boolean shuffle) {
		super(sequences, k, shuffle);
	}

	public List<E> getTestSet() {
		testSet = getLearningPartions().removeFirst();
		
		return testSet;
	}

	public List<E> getTrainingSet() {

		if (trainingSet == null) {
			trainingSet = new ArrayList<E>();
		} else
			trainingSet.clear();

		for (List<E> set : getLearningPartions()) {
			trainingSet.addAll(set);
		}

		return trainingSet;
	}

	/**
	 * @return
	 */
	public boolean hasCorrectCollectionSize() {

		return getCollection().size() >= 1;
	}

	/**
	 * @return
	 */
	public boolean collectionHasOneElement() {

		return getCollection().size() == 1;
	}

	/**
	 * @return the learningPartions
	 */
	public LinkedList<List<E>> getLearningPartions() {

		if (learningPartions == null) {
			learningPartions = new LinkedList<List<E>>();
			for (List<E> subSet : this.getSubSets()) {
				learningPartions.add(subSet);
			}

		}
		return learningPartions;
	}

	/**
	 * @param learningPartions
	 *            the learningPartions to set
	 */
	public void setLearningPartions(LinkedList<List<E>> learningPartions) {
		this.learningPartions = learningPartions;
	}

	public void putTheFirstAtTheEnd() {
		learningPartions.addLast(testSet);
	}

}
