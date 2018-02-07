/** 
* This file is part of RoCaWeb.
*
* RoCaWeb is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package com.rocaweb.learning.algorithms.alignment.pair;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import cern.colt.matrix.DoubleMatrix2D;

/**
 * Implementations of the Smith-Waterman algorithm.
 * 
 * SmithWaterman alignment algorithm is a local alignment algorithm.
 * 
 * @see {@link https://en.wikipedia.org/wiki/Smith-Waterman_algorithm}
 * 
 * @author Djibrilla Amadou Kountche
 *
 * @since 1.0.0
 */
public class SmithWaterman extends NeedlemanWunsch {

	/**
	 * Constructor
	 */
	public SmithWaterman() {

	}

	/**
	 * Constructor
	 * 
	 * @param configFile
	 */
	public SmithWaterman(String configFile) {
		super(configFile);
	}

	/**
	 * Implement the algorithm described in
	 * 
	 * @see https://en.wikipedia.org/wiki/Smith-Waterman_algorithm
	 * @return aligned locally aligned sequences
	 */
	private List<String> sw() {
		double max = this.getScoreMatrix().get(0, 0);
		int posmin = 0;
		int posmax = 0;

		for (int i = 0; i < this.getScoreMatrix().rows(); i++) {
			for (int j = 0; j < this.getScoreMatrix().columns(); j++) {
				if (max <= this.getScoreMatrix().get(i, j)) {
					max = this.getScoreMatrix().get(i, j);
					posmin = i;
					posmax = j;
				}

			}
		}
		logger.info(posmin + "," + posmax);
		return this.nw(posmin, posmax);
	}

	@Override
	public List<String> align(String firstSequence, String secondSequence) {
		this.setPairOfSequence(Pair.of(firstSequence, secondSequence));
		return sw();

	}

	@Override
	public DoubleMatrix2D getScoreMatrix() {

		return getScoreMatrix(0, 0, getPenalty());
	}

	@Override
	public double getMax(double match, double delete, double insert) {
		double max = super.getMax(match, delete, insert);
		return Math.max(max, 0);
	}

	@Override
	protected void paddSequences(Pair<StringBuffer, StringBuffer> alignedStrings, int sizeFirst, int sizeSecond) {

	}

}
