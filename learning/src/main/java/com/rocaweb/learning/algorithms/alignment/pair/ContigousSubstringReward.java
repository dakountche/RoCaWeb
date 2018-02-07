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

import com.rocaweb.learning.data.alignment.ExtendedCharSequence;
import com.rocaweb.learning.data.alignment.ExtendedCharacter;
import com.rocaweb.learning.rules.Regex;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 * Contiguous Substring Reward (CSR) is a variant of the Needleman Wunsch
 * algorithm. The modifications introduced by the authors have the goal to
 * determine the optimal global alignment where contiguity of substrings is
 * preserved. Also, after the alignment, the method produces a simplified
 * regular expressions. We propose here an implementation of this method.
 * 
 * @see http://www.comp.polyu.edu.hk/~csbxiao/paper/2007/ATC-07-SRE.pdf
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 */

public class ContigousSubstringReward extends NeedlemanWunsch {

	/** The pair of alignment sequences */
	private ExtendedCharSequence[] sequences = null;
	private DenseDoubleMatrix2D contiguityMatrix = null;

	/**
	 * The default constructor with default value from the paper.
	 */
	public ContigousSubstringReward() {
	}

	/**
	 * Constructor
	 * 
	 * @param configFile
	 *            the configuration file
	 */
	public ContigousSubstringReward(String configFile) {
		super(configFile);
	}

	/**
	 * Align the two sequences and produces a simplified regular expression.
	 * 
	 * @return an extended character sequences. This result can be translated to a
	 *         simplified regular expression.
	 */
	public ExtendedCharSequence csr() {

		List<String> alignedSequences = align();

		return lookUpSequences(new ExtendedCharSequence(alignedSequences.get(0)),
				new ExtendedCharSequence(alignedSequences.get(1)));

	}

	/**
	 * Calculates the reward for substrings. This reward is a number added to the
	 * similarity in the score matrix.
	 * 
	 * @param posi
	 *            substring of the first sequence
	 * @param posj
	 *            substring of the second sequence
	 * @return the contiguity reward
	 */
	private double substringReward(int posi, int posj) {
		double length = 0;

		length = getContiguityMatrix().get(posi, posj);

		// double len = ((length > 0) ? 3 * (length -1) : length);
		double enc = 3 * (length - 1);

		return enc;
	}

	/**
	 * Fill the contiguity matrix for two strings. The <code>substringReward</code>
	 * method used this matrix to determine the reward.
	 * 
	 * @return a matrix containing the rewards.
	 */
	public DenseDoubleMatrix2D getContiguityMatrix() {

		if (contiguityMatrix == null) {
			contiguityMatrix = new DenseDoubleMatrix2D(getTheFirst().length(), getTheSecond().length());

			contiguityMatrix.assign(0);

			for (int i = 1; i < getTheFirst().length(); i++) {
				for (int j = 1; j < getTheSecond().length(); j++) {
					if (getTheFirst().charAt(i) == getTheSecond().charAt(j)
							&& getTheFirst().charAt(i) != this.getGapCharacter())
						contiguityMatrix.set(i, j, contiguityMatrix.get(i - 1, j - 1) + 1);
				}
			}
		}
		// logger.info(contiguityMatrix);
		return contiguityMatrix;
	}

	public ExtendedCharSequence getFirstSequence() {
		return getSequences()[0];
	}

	@Override
	public DoubleMatrix2D getScoreMatrix() {

		return this.getScoreMatrix(getPenalty(), getPenalty(), getPenalty());
	}

	public ExtendedCharSequence getSecondSequence() {
		return getSequences()[1];
	}

	public ExtendedCharSequence[] getSequences() {
		if (sequences == null) {
			sequences = new ExtendedCharSequence[2];
			sequences[0] = new ExtendedCharSequence("");
			sequences[1] = new ExtendedCharSequence("");
		}
		return sequences;
	}

	@Override
	public double getSimilarity(int firstChar, int secondChar) {
		return getSimilarity(firstChar, secondChar, getMatch(), getMismatch(), getPenalty(), getSpecialChars(), 1)
				+ substringReward(firstChar, secondChar);
	}

	/**
	 * Make a correspondence between two characters
	 * 
	 * @param x
	 *            the first character
	 * @param y
	 *            the second character
	 * 
	 * @return the an identical character of the corresponding wildcard.
	 */

	private ExtendedCharacter lookUpCharCompTab(ExtendedCharacter x, ExtendedCharacter y) {
		ExtendedCharacter result = new ExtendedCharacter();

		if (!x.isWildcard()) { // check if the first car is not a wildcard
			if (x.getChar() == y.getChar()) { // then check y

				if (x.getChar() != this.getGapCharacter())
					return new ExtendedCharacter(x.getChar(), 1, 1); // return this

				// x and
				// set
				// min and max to 1
			} else if (x.getChar() == this.getGapCharacter() || y.getChar() == this.getGapCharacter()) {
				return new ExtendedCharacter('*', 0, 1, true);
			}

			else if (!y.isWildcard() && y.getChar() != this.getGapCharacter()) { // otherwise, return '?' with 1 and 1
				// which means two different non
				// special characters
				return new ExtendedCharacter('?', 1, 1, true);
			}

			else if (y.getChar() == '?' && x.getChar() != this.getGapCharacter()) // when y is the special characters
																					// '?'
				return new ExtendedCharacter('?', 1, 1, true);
			else if (y.getChar() == getGapCharacter()) { // Now we define
				// different cases,
				// if
				// we have a character and the '-'
				result.setCar('*'); // 1. creates the special characters '*'
				result.setLowerBound(1);
				result.setUpperBound(0);
				result.setWildcard(true);
				return result;
			} else if (y.getChar() == '*')
				return new ExtendedCharacter('*', 0, 1, true);

		} else if (x.getChar() == y.getChar()) {
			if (x.equals(y)) {
				ExtendedCharacter c = new ExtendedCharacter(x);
				c.setWildcard(true);
				return c;
			} else
				return new ExtendedCharacter('µ', 0, 0, true);
		} else if (x.getChar() == '?') {
			if (y.getChar() == '*')
				return new ExtendedCharacter('*', 0, 1, true);

		}
		return new ExtendedCharacter('*', 0, 1, true);
	}

	/**
	 * Transform sequences to SRE.
	 * 
	 * @param alignedSequences
	 * @return the corresponding SRE for the sequences.
	 */
	public ExtendedCharSequence lookUpSequences(ExtendedCharSequence... alignedSequences) {
		ExtendedCharSequence sequence = new ExtendedCharSequence();
		ExtendedCharacter aChar = new ExtendedCharacter();

		for (int i = 0; i < alignedSequences[0].getACharacters().size(); i++) {

			for (int j = 1; j < alignedSequences.length; j++) {
				ExtendedCharacter x = alignedSequences[j - 1].getACharacters().get(i);
				ExtendedCharacter y = alignedSequences[j].getACharacters().get(i);

				aChar = lookUpCharCompTab(x, y);

				logger.trace("LookUp " + x.getChar() + " " + y.getChar() + " -> " + aChar.getChar());
			}

			if (Regex.getWildcards().contains(aChar.getChar()))
				aChar.setWildcard(true);
			sequence.getACharacters().add(aChar);
		}

		return sequence;
	}

	public void setFirstSequence(ExtendedCharSequence pSequence1) {
		getSequences()[0] = pSequence1;
	}

	public void setSecondSequence(ExtendedCharSequence pSequence2) {
		getSequences()[1] = pSequence2;
	}

	public void setSequences(ExtendedCharSequence[] alignmentSequences) {
		sequences = alignmentSequences;
	}

	@Override
	public List<String> align(String first, String second) {
		this.clearContiguityMatrix();
		this.setPairOfSequence(Pair.of(first, second));
		List<String> resutl = align();
		logger.trace(resutl);
		this.clearContiguityMatrix();
		return resutl;
	}

	private void clearContiguityMatrix() {
		this.contiguityMatrix = null;

	}

}
