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

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.rocaweb.learning.algorithms.alignment.pair.NeedlemanWunsch;
import com.rocaweb.learning.alignment.InputAlignments;
import com.rocaweb.learning.data.alignment.PairAlignment;

/**
 * This test case uses the values given in the book
 * 
 * @see
 * @author Amadou Kountché Djibrilla
 *
 */
public class GlobalAlignmentTest extends AbstractAlignmentAlgorithmTest {

	protected NeedlemanWunsch nw = null;
	protected Pair<String, String> result = null;

	protected Logger logger = LogManager.getLogger(getClass());

	@Override
	public void testPairAlignement() {
		this.setParametersFromTheBook();

		List<String> alignment = this.getAlgorithm().align(this.getPair().getLeft(), this.getPair().getRight());
		assertEquals(alignment.size(), 2);

		Pair<String, String> target = Pair.of(alignment.get(0), alignment.get(1));
		assertEquals(target, getResult());
		logger.info(getResult());
		logger.info(target);

	}

	@Override
	public void TestAlignAllTheSequences() {
		List<PairAlignment> alignments = getAlgorithm()
				.alignAllPairOfSequences(Lists.newArrayList(InputAlignments.garfield));
		int size = InputAlignments.garfield.length;

		assertEquals(alignments.size(), (size - 1) * size / 2);
	}

	@Override
	public void TestAlignAllTheSequencesToTheLongest() {
		List<PairAlignment> alignments = this.getAlgorithm()
				.alignAllTheSequencesToTheLongest(Lists.newArrayList(InputAlignments.garfield));
		int size = InputAlignments.garfield.length;

		assertEquals(alignments.size(), size - 1);

	}

	@Override
	public void TestAlignAllTheSequencesRandomLy() {
		List<PairAlignment> alignments = this.getAlgorithm()
				.alignRandomlyAllThePairOfSequence(Lists.newArrayList(InputAlignments.garfield));
		int size = InputAlignments.garfield.length;

		assertEquals(alignments.size(), (size - 1) * size / 2);

	}

	@Override
	public void TestSimilarity() {
		this.setParametersFromTheBook();
		List<String> strings = Lists.newArrayList();
		strings.add(this.getPair().getLeft());
		strings.add(this.getPair().getRight());
		List<PairAlignment> alignments = getAlgorithm().alignAllPairOfSequences(strings);
		logger.info("TestSimilarity " + alignments.get(0).getSequences());
		assertEquals(alignments.size(), 1);

		assertEquals(this.getScore(), alignments.get(0).getScore(), 0.0001);

	}

	protected void setParametersFromTheBook() {
		getAlgorithm().setMatch(1.0);
		getAlgorithm().setMismatch(-1.0);
		getAlgorithm().setPenalty(-2.0);
		getAlgorithm().setGapCharacter('-');
	}

	public NeedlemanWunsch getAlgorithm() {
		if (nw == null) {
			nw = new NeedlemanWunsch("src/test/resources/json/nw.json");
		}
		return nw;
	}

	public Pair<String, String> getResult() {
		if (result == null) {
			result = Pair.of("ACTTTATGCCTGCT", "AC---A-G---GCT");
		}
		return result;
	}

	public double getScore() {
		return -7.0;
	}

	public Pair<String, String> getPair() {
		if (pair == null) {
			pair = Pair.of("ACTTTATGCCTGCT", "ACAGGCT");
		}
		return pair;
	}

}
