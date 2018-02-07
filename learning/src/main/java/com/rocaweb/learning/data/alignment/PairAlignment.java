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
package com.rocaweb.learning.data.alignment;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.rocaweb.learning.algorithms.alignment.pair.AbstractPairAlignmentAlgorithm;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class PairAlignment implements Comparable<PairAlignment> {

	private Pair<String, String> aligned = null;
	private Pair<SimpleCharSequence, SimpleCharSequence> simpleCharSequences = null;
	private Pair<String, String> original = null;

	private static Logger logger = LogManager.getLogger(PairAlignment.class);

	private double score = 0.0;
	private int weigth = 0;
	private char gapChar = Character.MAX_VALUE;

	private AbstractPairAlignmentAlgorithm pairAAlgorithm;

	private Map<Pair<SimpleCharacter, SimpleCharacter>, Integer> maps = null;

	public PairAlignment() {

	}

	private Pair<String, String> createPair(List<String> sequences) {
		Pair<String, String> pair = Pair.of("", "");
		if (sequences.size() == 2) {
			pair = Pair.of(sequences.get(0), sequences.get(1));
		}
		return pair;
	}

	public PairAlignment(AbstractPairAlignmentAlgorithm pairAlignmentAlgorithm, String query, String target) {
		this.pairAAlgorithm = pairAlignmentAlgorithm;
		setOriginalPair(Pair.of(query, target));
		setGapChar(pairAlignmentAlgorithm.getGapCharacter());

		List<String> alignedSequences = pairAlignmentAlgorithm.align(query, target);

		setSequences(createPair(alignedSequences));
	}

	public void setOriginalPair(Pair<String, String> of) {
		this.original = of;
	}

	private void setSequences(Pair<String, String> sequences) {
		this.aligned = sequences;
	}

	public void setWeigth(int weigth) {
		this.weigth = weigth;
	}

	public Pair<String, String> getSequences() {

		return aligned;
	}

	/**
	 * This process is based on the <b> percent of identity</b> indicator. It is a
	 * "reasonable indicator when aligning sequences with more than 30% identity" .
	 * The percent of identity is calculated on the matching positions.
	 * 
	 * The weight is calculated as follow :
	 * 
	 * @param sequence1
	 *            : the first sequence
	 * @param sequence2
	 *            : the second sequence
	 * @return the weight (the percent of identity)
	 */
	public int getWeight() {

		if (weigth == 0 && aligned != null) {

			weigth = getWeight(aligned.getLeft(), aligned.getRight(), getGapChar());
		}
		return weigth;

	}

	private static int getWeight(String sequence1, String sequence2, char alignmentChar) {
		double diff = 0;
		double equal = 0;
		double percent = 0;

		assert (sequence1.length() == sequence2.length());
		assert (alignmentChar != Character.MAX_VALUE);

		for (int i = 0; i < sequence1.length(); i++) {
			if ((sequence1.charAt(i) != alignmentChar && sequence2.charAt(i) != alignmentChar)) {
				if (sequence1.charAt(i) == sequence2.charAt(i) && sequence1.charAt(i) != ' ')
					equal++;
				else if (sequence1.charAt(i) != sequence2.charAt(i)) {

					diff++;
				}
			}
		}

		logger.debug("Equal " + equal + "  diff " + diff);
		if (equal + diff != 0)
			percent = (equal / (equal + diff)) * 100;

		return (int) percent;
	}

	/**
	 * @return the maps
	 */
	private Map<Pair<SimpleCharacter, SimpleCharacter>, Integer> getMaps(boolean initialized) {

		if (maps == null) {
			maps = Maps.newHashMap();
			for (int i = 0; i < aligned.getLeft().length(); i++) {
				SimpleCharacter fsc = getSimpleCharSequences().getLeft().getACharacters().get(i);
				SimpleCharacter ssc = getSimpleCharSequences().getRight().getACharacters().get(i);

				Pair<SimpleCharacter, SimpleCharacter> pair = Pair.of(fsc, ssc);
				int weigth = 0;
				if (initialized) {
					if (fsc.getChar() == this.getGapChar() || ssc.getChar() == this

							.getGapChar()) {
						weigth = 0;
					}

				} else
					weigth = getWeight();

				maps.put(pair, weigth);
			}
		}
		return maps;
	}

	public Map<Pair<SimpleCharacter, SimpleCharacter>, Integer> getMaps() {
		return this.getMaps(false);
	}

	/**
	 * @param maps
	 *            the maps to set
	 */
	public void setMaps(Map<Pair<SimpleCharacter, SimpleCharacter>, Integer> maps) {
		this.maps = maps;
	}

	public Pair<SimpleCharSequence, SimpleCharSequence> getSimpleCharSequences() {

		if (simpleCharSequences == null) {

			SimpleCharSequence first = createSimpleCharSequence(aligned.getLeft());
			SimpleCharSequence second = createSimpleCharSequence(aligned.getRight());
			simpleCharSequences = Pair.of(first, second);
		}

		return simpleCharSequences;
	}

	/**
	 * @param first
	 * @return
	 */
	private SimpleCharSequence createSimpleCharSequence(String first) {
		int j = 0;
		SimpleCharacter gapChar = new SimpleCharacter(getGapChar(), -1);
		SimpleCharSequence scSequence = new SimpleCharSequence();
		for (int i = 0; i < first.length(); i++) {
			if (first.charAt(i) != getGapChar()) {
				scSequence.getACharacters().add(new SimpleCharacter(first.charAt(i), j));
				j++;
			} else
				scSequence.getACharacters().add(gapChar);
		}

		return scSequence;
	}

	void addMap(Map<Pair<SimpleCharacter, SimpleCharacter>, Integer> map) {
		for (Pair<SimpleCharacter, SimpleCharacter> pair : map.keySet()) {
			if (getMaps().containsKey(pair)) {
				int value = map.get(pair) + this.getMaps().get(pair);
				getMaps().put(pair, value);

			}
		}
		// System.out.println(getMaps());

	}

	/**
	 * Determines the score of a given alignment A score is determined based on the
	 * match mismatch and penalty.
	 * 
	 * @param theFirstAlignedSequence
	 * @param theSecondAlignedSequence
	 * @return the score of the alignment
	 */
	public double getScore() {

		score = 0;
		String theFirstAlignedSequence = this.getSequences().getLeft();
		String theSecondAlignedSequence = this.getSequences().getRight();

		if (theFirstAlignedSequence.length() != theSecondAlignedSequence.length()) {
			logger.error("The alignment is not yet done ! ");

		} else {

			logger.trace(this.getSequences());
			this.getAlgorithm().setPairOfSequence(getSequences());
			for (int i = 0; i < theFirstAlignedSequence.length(); i++) {
				double value = getAlgorithm().getSimilarity(i, i);
				logger.trace(value);

				score += value;
			}
		}

		return score;
	}

	private AbstractPairAlignmentAlgorithm getAlgorithm() {
		return pairAAlgorithm;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public int compareTo(PairAlignment arg0) {

		return Double.compare(this.getScore(), arg0.getScore());
	}

	public Pair<String, String> getOriginalPair() {
		return original;
	}

	public char getGapChar() {
		return this.gapChar;
	}

	public void setGapChar(char gapChar) {
		this.gapChar = gapChar;
	}

}
