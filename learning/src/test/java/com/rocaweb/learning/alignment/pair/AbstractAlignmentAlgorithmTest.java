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
package com.rocaweb.learning.alignment.pair;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.rocaweb.learning.alignment.InputAlignments;

public abstract class AbstractAlignmentAlgorithmTest implements InputAlignments {

	protected Pair<String, String> pair = null;;

	@Test
	public abstract void testPairAlignement();

	@Test
	public abstract void TestAlignAllTheSequences();

	@Test
	public abstract void TestAlignAllTheSequencesToTheLongest();

	@Test
	public abstract void TestAlignAllTheSequencesRandomLy();

	@Test
	public abstract void TestSimilarity();

	@Test
	public void testLengths() {
		for (String[] a : alignedGarfield) {

			String sequence1 = a[0];
			String sequence2 = a[1];
			assertEquals(sequence1.length(), sequence2.length());
		}
	}

	@Test
	public void testWeigths() {
		/*
		 * for (int i=0; i< alignedGarfield.length; i++) { PairAlignment alignment = new
		 * PairAlignment(alignedGarfield[i][0],alignedGarfield[i][1], alignmentChar);
		 * System.out.println(alignment.getWeight());
		 * assertEquals(alignment.getWeight(), weigths[i]); }
		 */
	}

	/**
	 * @return the pair
	 */
	public abstract Pair<String, String> getPair();

	/**
	 * @param pair
	 *            the pair to set
	 */
	public void setPair(Pair<String, String> pair) {
		this.pair = pair;
	}

}
