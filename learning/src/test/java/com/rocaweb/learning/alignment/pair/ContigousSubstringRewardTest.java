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

import com.rocaweb.learning.algorithms.alignment.pair.ContigousSubstringReward;
import com.rocaweb.learning.data.alignment.ExtendedCharSequence;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class ContigousSubstringRewardTest extends GlobalAlignmentTest {

	public ContigousSubstringReward getAlgorithm() {
		if (nw == null) {
			nw = new ContigousSubstringReward("src/test/resources/json/csr.json");
		}
		return (ContigousSubstringReward) nw;
	}

	public Pair<String, String> getResult() {
		if (result == null) {
			result = Pair.of("oxnxexzxtwo-------x", "-------ytwoyoynyeyz");
		}
		return result;
	}

	public double getScore() {
		return -6.5;
	}

	public Pair<String, String> getPair() {
		if (pair == null) {
			pair = Pair.of("oxnxexzxtwox", "ytwoyoynyeyz");
		}
		return pair;
	}

	@Test
	public void testMatch() {
		assertEquals(this.getAlgorithm().getMatch(), 0.5, 0.0001);
	}

	@Test
	public void testMismaatch() {
		assertEquals(this.getAlgorithm().getMismatch(), 0.0, 0.0001);
	}

	@Test
	public void testPenalty() {
		assertEquals(this.getAlgorithm().getPenalty(), -1.0, 0.0001);
	}

	@Test
	public void testGapChar() {
		assertEquals(this.getAlgorithm().getGapCharacter(), '-');
	}

	@Test
	public void testCSR() {

		this.getAlgorithm().setPairOfSequence(this.getPair());
		List<String> result = this.getAlgorithm().align(this.getPair().getLeft(), this.getPair().getRight());
		assertEquals(Pair.of(result.get(0), result.get(1)), this.getResult());
		assertEquals(this.getAlgorithm().csr().toString(), "*******?two*******?");
	}

	@Test
	public void testLookup() {
		ExtendedCharSequence esq = new ExtendedCharSequence("oxnxexzxtwo-------x");
		ExtendedCharSequence esp = new ExtendedCharSequence("-------ytwoyoynyeyz");
		ExtendedCharSequence[] seqs = new ExtendedCharSequence[2];
		seqs[0] = esp;
		seqs[1] = esq;
		assertEquals(this.getAlgorithm().lookUpSequences(seqs).toString(), "*******?two*******?");
	}

	@Test
	public void testContiguityMatrix() {
		this.getAlgorithm().setPairOfSequence(this.getPair());

		assertEquals(this.getAlgorithm().getContiguityMatrix().get(10, 3), 3, 0.0001);
	}

	protected void setParametersFromTheBook() {

	}

}
