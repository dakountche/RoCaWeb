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

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */

public class Partition<E> {

	private List<E> collection;
	private int numberOfPartition = 1;
	private boolean isShuffle = false;
	private boolean partionned = false;
	private List<List<E>> subSets = null;

	public Partition() {

	}

	public Partition(List<E> collection, int numberOfPartitions, boolean isShuffle) {
		setCollection(collection);
		setNumberOfPartition(numberOfPartitions);
		setShuffle(isShuffle);
		initialise();
	}

	/**
	 * 
	 */
	private void initialise() {
		shuffle();
		createSubSets();
	}

	/**
	 * 
	 */
	private void createSubSets() {
		int partsize = (int) Math.ceil((double) getCollection().size() / getNumberOfPartition());

		List<List<E>> partitions = Lists.partition(getCollection(), partsize);

		setSubSets(partitions);

	}

	/**
	 * 
	 */
	private void shuffle() {
		if (isShuffle())
			Collections.shuffle(getCollection());

	}

	/**
	 * @return the collection
	 */
	public List<E> getCollection() {
		return collection;
	}

	/**
	 * @param collection
	 *            the collection to set
	 */
	public void setCollection(List<E> collection) {
		this.collection = collection;
	}

	/**
	 * @return the numberOfPartition
	 */
	public int getNumberOfPartition() {
		return numberOfPartition;
	}

	/**
	 * @param numberOfPartition
	 *            the numberOfPartition to set
	 */
	public void setNumberOfPartition(int numberOfPartition) {
		this.numberOfPartition = numberOfPartition;
	}

	/**
	 * @return the isShuffle
	 */
	public boolean isShuffle() {
		return isShuffle;
	}

	/**
	 * @param isShuffle
	 *            the isShuffle to set
	 */
	public void setShuffle(boolean isShuffle) {
		this.isShuffle = isShuffle;
	}

	/**
	 * @return the subSets
	 */
	public List<List<E>> getSubSets() {
		return subSets;
	}

	/**
	 * @param subSets
	 *            the subSets to set
	 */
	public void setSubSets(List<List<E>> subSets) {
		this.subSets = subSets;
	}

	/**
	 * @return the partion
	 */
	public boolean isPartionned() {
		return partionned;
	}

	/**
	 * @param partionned
	 *            the partion to set
	 */
	public void setPartionned(boolean partionned) {
		this.partionned = partionned;
	}

}
