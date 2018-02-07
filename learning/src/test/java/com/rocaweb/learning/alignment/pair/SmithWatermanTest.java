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
package com.rocaweb.learning.alignment.pair;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.rocaweb.learning.algorithms.alignment.pair.NeedlemanWunsch;
import com.rocaweb.learning.algorithms.alignment.pair.SmithWaterman;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class SmithWatermanTest extends GlobalAlignmentTest {

	public NeedlemanWunsch getAlgorithm() {
		if (nw == null) {
			nw = new SmithWaterman("src/test/resources/json/sw.json");

		}
		return nw;
	}

	public Pair<String, String> getResult() {
		if (result == null) {
			result = Pair.of("AAAAA", "AAAAA");
			;
		}
		return result;
	}

	public Pair<String, String> getPair() {
		if (pair == null) {
			pair = Pair.of("AAAAACTCTCTCT", "GCGCGCGCAAAAA");
		}
		return pair;
	}

	public double getScore() {
		return 5;
	}

	@Test
	public void testAnotherPair() {

		List<String> result = this.getAlgorithm().align("ACTTTATGCCTGCT", "ACAGGGCT");
		assertEquals(result.get(0), "GCT");
		assertEquals(result.get(0), result.get(1));
	}

}
