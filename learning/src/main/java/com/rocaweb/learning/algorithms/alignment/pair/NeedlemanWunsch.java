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

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.rocaweb.learning.data.alignment.PairAlignment;
import com.rocaweb.learning.utils.LearningUtils;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

/**
 * Implementation of the Needleman-Wunsch algorithm.
 * 
 * This algorithm makes a global alignment of two sequences.
 * 
 * It finds the one of the optimal solutions to alignment problem.
 * 
 * This implementation can be modified to print the other solutions.
 * 
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * 
 * @see http://www.sciencedirect.com/science/article/pii/0022283670900574
 * 
 * @since 1.0.0
 */

public class NeedlemanWunsch extends AbstractPairAlignmentAlgorithm {

	/**
	 * The default constructor
	 */

	public NeedlemanWunsch() {

	}

	/**
	 * Constructor
	 * 
	 * @param source
	 * @param target
	 */
	public NeedlemanWunsch(String source, String target) {
		setPairOfSequence(Pair.of(source, target));
	}

	/**
	 * Constructor
	 * 
	 * @param configFileName
	 */
	public NeedlemanWunsch(String configFileName) {
		super(configFileName);
	}

	/**
	 * Implements Needleman and Wunsch algorithm described in
	 * 
	 * @see http://www.sciencedirect.com/science/article/pii/0022283670900574
	 * 
	 * @return a list of the align sequences
	 */
	List<String> nw(int sizeFirst, int sizeSecond) {
		Pair<StringBuffer, StringBuffer> alignedStrings = initSequences(sizeFirst, sizeSecond);

		/* Variables indicating the directions to follow in the score matrix */
		BigDecimal currentScore, diagonalScore, leftScore, upScore;
		currentScore = diagonalScore = leftScore = upScore = BigDecimal.ZERO;
		boolean decision;
		while (sizeFirst > 0 && sizeSecond > 0) {

			currentScore = valueOf(getScoreMatrix().get(sizeFirst, sizeSecond));
			diagonalScore = valueOf(getScoreMatrix().get(sizeFirst - 1, sizeSecond - 1));
			upScore = valueOf(getScoreMatrix().get(sizeFirst, sizeSecond - 1));
			leftScore = valueOf(getScoreMatrix().get(sizeFirst - 1, sizeSecond));

			// The two characters matches or mismatches
			decision = false;

			if (currentScore.equals(diagonalScore.add(valueOf(this.getSimilarity(sizeFirst - 1, sizeSecond - 1))))) {

				alignedStrings.getLeft().insert(0, getTheFirst().charAt(sizeFirst - 1));
				alignedStrings.getRight().insert(0, getTheSecond().charAt(sizeSecond - 1));
				sizeFirst--;
				sizeSecond--;
				decision = true;
			}

			// Insert the alignment character in the second sequence
			else if (currentScore.equals(leftScore.add(valueOf(getPenalty())))) {

				alignedStrings.getLeft().insert(0, getTheFirst().charAt(sizeFirst - 1));
				alignedStrings.getRight().insert(0, getGapCharacter());
				sizeFirst--;
				decision = true;
			}

			// Insert the alignment character in the first sequence
			else if (currentScore.equals(upScore.add(BigDecimal.valueOf(getPenalty())))) {

				alignedStrings.getLeft().insert(0, getGapCharacter());
				alignedStrings.getRight().insert(0, "" + getTheSecond().charAt(sizeSecond - 1));
				sizeSecond--;
				decision = true;
			}
			if (!decision)
				break;

		}

		this.paddSequences(alignedStrings, sizeFirst, sizeSecond);
		clearScoreMatrix();

		return LearningUtils.stringBufferToString(alignedStrings);
	}

	/**
	 * Pad the sequences if necessary.
	 * 
	 * @param alignedStrings
	 * @param sizeFirst
	 * @param sizeSecond
	 */
	protected void paddSequences(Pair<StringBuffer, StringBuffer> alignedStrings, int sizeFirst, int sizeSecond) {
		while (sizeFirst > 0) {
			alignedStrings.getLeft().insert(0, getTheFirst().charAt(sizeFirst - 1));
			alignedStrings.getRight().insert(0, getGapCharacter());
			sizeFirst--;
		}

		while (sizeSecond > 0) {
			alignedStrings.getLeft().insert(0, getGapCharacter());
			alignedStrings.getRight().insert(0, "" + getTheSecond().charAt(sizeSecond - 1));
			sizeSecond--;
		}

	}

	/**
	 * Initialize the sequences. Depending on the start cell the method will put
	 * remaining characters in the resulting sequences.
	 * 
	 * @param startI
	 * @param startJ
	 * @return
	 */
	private Pair<StringBuffer, StringBuffer> initSequences(int startI, int startJ) {

		return Pair.of(new StringBuffer(), new StringBuffer());
	}

	public List<String> align() {
		return nw(getScoreMatrix().rows() - 1, getScoreMatrix().columns() - 1);
	}

	/**
	 * Align two sequences
	 * 
	 * @param theFristSequence
	 *            : the first sequence
	 * @param theSecondSequence
	 *            : the second sequence
	 * @return a list of two aligned sequences
	 */
	@Override
	public List<String> align(String theFristSequence, String theSecondSequence) {
		clearScoreMatrix();
		setPairOfSequence(Pair.of(theFristSequence, theSecondSequence));
		return this.align();
	}

	@Override
	public DoubleMatrix2D getScoreMatrix() {

		return getScoreMatrix(getPenalty(), getPenalty(), getPenalty());
	}

	@Override
	public double getSimilarity(int firstChar, int secondChar) {

		return getSimilarity(firstChar, secondChar, getMatch(), getMismatch(), getPenalty(), getSpecialChars(),
				getSimilarityFactor());
	}

	@Override
	public List<PairAlignment> process(List<String> sequences) {
		return runByFunctionName(sequences);
	}

	private void clearScoreMatrix() {
		this.setScoreMatrix(null);

	}

	private BigDecimal valueOf(double value) {
		return BigDecimal.valueOf(value);
	}

	@Override
	public double getMax(double match, double delete, double insert) {

		return Math.max(match, Math.max(delete, insert));
	}

	public List<String> beginningOfFirstString() {
		clearScoreMatrix();
		setScoreMatrix(getScoreMatrix(0, getPenalty(), getPenalty()));
		return nw(getScoreMatrix().rows() - 1, getScoreMatrix().columns() - 1);
	}

	public List<String> beginningOfSecondSequence() {
		clearScoreMatrix();
		setScoreMatrix(getScoreMatrix(getPenalty(), 0, getPenalty()));
		return nw(getScoreMatrix().rows() - 1, getScoreMatrix().columns() - 1);
	}

	public List<String> endOfFirstString() {
		clearScoreMatrix();
		DoubleMatrix1D lastRow = getScoreMatrix().viewRow(getScoreMatrix().rows() - 1);
		int position = getThePositionOfTheMaxValue(lastRow);
		return nw(getTheFirst().length(), position);
	}

	public List<String> endOfSecondSequence() {
		clearScoreMatrix();
		DoubleMatrix1D lastColumn = getScoreMatrix().viewColumn(getScoreMatrix().columns() - 1);
		int posi = getThePositionOfTheMaxValue(lastColumn);
		return nw(lastColumn.size() - 1, posi);
	}

	/**
	 * Calculate the score of the alignment.
	 * 
	 * @param source
	 *            the first sequence
	 * @param target
	 *            the second sequence
	 * 
	 * @return the score
	 */
	public double getScore(String source, String target) {
		double score = 0;

		if (source.length() != target.length()) {
			logger.error("The alignment is not yet done ! ");

		} else {

			logger.trace(this.getPairOfSequence());
			this.setPairOfSequence(Pair.of(source, target));
			for (int i = 0; i < this.getTheFirst().length(); i++) {
				double value = getSimilarity(i, i);
				logger.trace(value);

				score += value;
			}
		}

		return score;
	}

	/**
	 * Determine the index of the maximum of a vector
	 * 
	 * @param values
	 *            the vector to search in
	 * @return the index of the maximum value.
	 */
	private int getThePositionOfTheMaxValue(DoubleMatrix1D values) {
		double max = values.get(0);
		int iPosmax = 0;
		for (int i = 1; i < values.size(); i++) {
			if (max <= values.get(i)) {
				max = values.get(i);
				iPosmax = i;
			}
		}
		return iPosmax;
	}

}
