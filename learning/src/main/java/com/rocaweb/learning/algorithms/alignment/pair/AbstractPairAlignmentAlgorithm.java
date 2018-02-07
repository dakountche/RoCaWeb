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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.data.alignment.PairAlignment;
import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;
import com.rocaweb.learning.parameters.AlignmentAParameters;
import com.rocaweb.learning.parameters.DefaultLearningAParameters;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.rules.RawContract;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 * The <code> AbstractPairAlignmentAlgorithm </code> class summaries all the
 * functionalities present in a pair alignment algorithm.
 * 
 * Pair alignment is the process of inserting a gap characters in the first or
 * the second character in order that commons characters holds in the same
 * column.
 * 
 * <p>
 * Pair sequences alignment algorithms can be categorised in :
 * <ul>
 * <li>Global : sequences of similar length
 * <li>Local : sub-sequences and
 * <li>Multi-sequences : alignment algorithms usually aligns two sequences.
 * These algorithms can align \f[ $N$ \f] ( with \f[ $N >= 2$\f]) sequences.
 * </ul>
 * </p>
 * 
 * 
 * 
 * @see COULSON, A. (1998). Algorithms on Strings, Trees and Sequences by 
 * Dan Gusfield, Cambridge University Press 1997, ISBN 0 521 58519 8, 534 xviii pages. -. Genetical Research, 71(1), 91-95.

 * 
 *
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 */

public abstract class AbstractPairAlignmentAlgorithm extends Algorithm<Iterable<PairAlignment>> {

	/** The score matrix **/
	private transient DoubleMatrix2D scoreMatrix = null;

	/** the parameters of the algorithm */
	protected AlignmentAParameters parameters;

	/** This contract is for algorithms which does not have a special formating */
	private transient RawContract contract = null;

	/**
	 * Constructor
	 */
	public AbstractPairAlignmentAlgorithm() {
	}

	/**
	 * Constructor
	 * 
	 * @param configFileName
	 *            the configuration file.
	 */
	AbstractPairAlignmentAlgorithm(String configFileName) {
		super(configFileName);
	}

	/**
	 * Align two sequences.
	 * 
	 * @param source
	 * 
	 * @param target
	 * 
	 * @return a list of aligned sequences.
	 */
	public abstract List<String> align(String source, String target);

	/**
	 * Determine the n*(n-1) possible alignments pairs.
	 * 
	 * @param sequences
	 *            the sequences to be aligned.
	 * @return aligned sequences with duplicated pairs removed.
	 */
	public List<PairAlignment> alignAllPairOfSequences(final List<String> sequences) {

		List<PairAlignment> alignedSequences = Lists.newArrayList();

		for (int i = 0; i < sequences.size() - 1; i++) {
			for (int j = i + 1; j < sequences.size(); j++) {
				alignedSequences.add((createPair(sequences.get(i), sequences.get(j))));
			}
		}

		return alignedSequences;
	}

	/**
	 * Align all the sequences to the longest sequence.
	 * 
	 * @param sequences the list of sequences
	 * @return the aligned sequences
	 */
	public List<PairAlignment> alignAllTheSequencesToTheLongest(final List<String> sequences) {
		List<PairAlignment> alignedSequences = Lists.newArrayList();
		String longestSequence;
		int maxIndex = indexOfLongestSequence(sequences);
		longestSequence = sequences.remove(maxIndex);
		for (String sequence : sequences) {
			alignedSequences.add(createPair(longestSequence, sequence));
		}

		return alignedSequences;
	}

	/**
	 * Align randomly all the sequences. The behavior is the same as the preceding
	 * <code> alignAllPairOfSequence </code> as it produces (n-1)*n pairs of
	 * sequences. However, the pair of sequences is taken randomly.
	 * 
	 * This method used the default Java Collections.shuffle() function.
	 * 
	 * @param sequences the list of sequences
	 * @return the aligned sequences
	 */

	public List<PairAlignment> alignRandomlyAllThePairOfSequence(final List<String> sequences) {
		List<String> alignedSequences = Lists.newArrayList(sequences);
		Collections.shuffle(alignedSequences);
		return alignAllPairOfSequences(alignedSequences);

	}

	public char getGapCharacter() {
		return this.getParameters().getGapCharacter();
	}

	public Set<Character> getAlphabet() {
		return this.getParameters().getAlphabet();
	}

	public double getMatch() {
		return this.getParameters().getMatch();
	}

	public double getMismatch() {
		return this.getParameters().getMismatch();
	}

	public Pair<String, String> getPairOfSequence() {
		return this.getParameters().getPairOfSequence();
	}

	public double getPenalty() {
		return this.getParameters().getPenalty();
	}

	/**
	 * This abstract method will be implemented to adapt the scoreMatrix to the
	 * particular alignment algorithm.
	 */
	public abstract DoubleMatrix2D getScoreMatrix();

	/**
	 * Determine the score matrix based of the common step between all the sequence
	 * alignment methods. However, in the case of Smith and Waterman algorithm the
	 * values can not be negative. Therefore the return value is is the maximum
	 * between the current, diagonal, left values and 0. This method also deals with
	 * the case of semi-global initialization cases.
	 * 
	 * @param initRow
	 *            the value used to initialize the first row.
	 * @param initColumn
	 *            the value used to initialize the first column.
	 * @param penalty
	 *            the penalty.
	 * @param isSmithWaterman
	 *            indicates if the method is SW based.
	 * @return the score matrix.
	 */
	protected DoubleMatrix2D getScoreMatrix(double initRow, double initColumn, double penalty) {

		if (scoreMatrix == null) {

			scoreMatrix = new DenseDoubleMatrix2D(getTheFirst().length() + 1, getTheSecond().length() + 1);
			// Initialize the first row and the first column.
			for (int i = 0; i < scoreMatrix.rows(); i++)
				scoreMatrix.set(i, 0, initRow * i);
			for (int j = 0; j < scoreMatrix.columns(); j++)
				scoreMatrix.set(0, j, initColumn * j);

			double match, delete, insert;

			// Fill the score matrix.
			for (int i = 1; i < scoreMatrix.rows(); i++) {
				for (int j = 1; j < scoreMatrix.columns(); j++) {
					match = scoreMatrix.get(i - 1, j - 1) + this.getSimilarity(i - 1, j - 1);
					insert = scoreMatrix.get(i, j - 1) + penalty;
					delete = scoreMatrix.get(i - 1, j) + penalty;
					double max = getMax(match, delete, insert);
					scoreMatrix.set(i, j, max);
				}
			}
		}
		return scoreMatrix;
	}

	/**
	 * Determine the max between the parameters.
	 * 
	 * @param match
	 * @param delete
	 * @param insert
	 * @return a maximum.
	 */
	public abstract double getMax(double match, double delete, double insert);

	/**
	 * Abstract method used to adapt the similarity function to the particular case.
	 */
	public abstract double getSimilarity(int firstChar, int secondChar);

	/**
	 * Determine the similarity between two characters.
	 * 
	 * @param firstChar
	 *            the index of the first character.
	 * @param secondChar
	 *            the index of the second character.
	 * @param match
	 *            the match
	 * @param mismatch
	 *            the mismatch
	 * @param specialChars
	 *            The set of special characters which will be given match*factor
	 *            match value.
	 * @param factor
	 *            the factor for special characters.
	 * @return the match, penalty or match*factor.
	 */

	protected double getSimilarity(int firstChar, int secondChar, double match, double mismatch, double penalty,
			Set<Character> specialChars, double factor) {
		if (this.isMatch(firstChar, secondChar)) {
			return match(firstChar, secondChar, match, factor, specialChars);
		} else if (this.isMismatch(firstChar, secondChar))
			return mismatch;
		else
			return penalty;
	}

	private boolean isMatch(int i, int j) {
		return getTheFirst().charAt(i) == getTheSecond().charAt(j) && getTheFirst().charAt(i) != this.getGapCharacter();
	}

	private boolean isMismatch(int i, int j) {
		return getTheFirst().charAt(i) != getTheSecond().charAt(j) && getTheFirst().charAt(i) != this.getGapCharacter()
				&& getTheSecond().charAt(j) != this.getGapCharacter();
	}

	/**
	 * Get the similarity factor.
	 * 
	 */
	public double getSimilarityFactor() {
		return this.getParameters().getSimilarityFactor();
	}

	/**
	 * Get the special characters. A special char had a particular match value. This
	 * values corresponds to match*similatiyFactor.
	 * 
	 * @return the special characters.
	 */
	public Set<Character> getSpecialChars() {
		return this.getParameters().getSpecialChars();
	}

	/**
	 * Get the first sequence
	 * 
	 * @return theFirstSequence
	 */
	public String getTheFirst() {

		return getPairOfSequence().getLeft();
	}

	/**
	 * Get the second sequence
	 * 
	 * @return theSecondSequence
	 */
	public String getTheSecond() {

		return getPairOfSequence().getRight();
	}

	/**
	 * Search the longest sequence
	 * 
	 * @param sequeces
	 *            the list of sequences
	 * @return the index of the longest sequence
	 */
	private int indexOfLongestSequence(List<String> sequeces) {
		int indexOfTheLongestSequence = 0;
		for (int j = 0; j < sequeces.size(); j++) {
			if (sequeces.get(indexOfTheLongestSequence).length() < sequeces.get(j).length()) {
				indexOfTheLongestSequence = j;
			}
		}
		return indexOfTheLongestSequence;
	}

	/**
	 * Determines if a characters is special or not.
	 */
	private boolean isSpecialChar(char theChar, Set<Character> specialChars) {
		if (specialChars == null) return false;
		return specialChars.contains(theChar);
	}

	private double match(int first, int second, double match, double factor, Set<Character> specialChars) {
		if (isSpecialChar(getTheFirst().charAt(first), specialChars))
			return factor * match;
		else return match;
	}

	/**
	 * Set the alphabet
	 * 
	 */
	public void setAlphabet(Set<Character> pAlphabet) {
		this.getParameters().setAlphabet(pAlphabet);
	}

	/**
	 * Set the match
	 * 
	 */
	public void setMatch(double match) {
		this.getParameters().setMatch(match);
	}

	/**
	 * Set the mismatch
	 * 
	 */
	public void setMismatch(double mismatch) {
		this.getParameters().setMismatch(mismatch);
	}

	/**
	 * Set the pair of sequences
	 * 
	 */
	public void setPairOfSequence(Pair<String, String> pairOfSequence) {
		this.getParameters().setPairOfSequence(pairOfSequence);
	}

	/**
	 * Set the penalty
	 * 
	 */
	public void setPenalty(double penalty) {
		this.getParameters().setPenalty(penalty);
	}

	/**
	 * Set the scoreMatrix
	 * 
	 */
	public void setScoreMatrix(DoubleMatrix2D scoreMatrix) {
		this.scoreMatrix = scoreMatrix;
	}

	/**
	 * Set the similarity factor
	 * 
	 */
	public void setSimilarityFactor(double similarityFactor) {
		this.getParameters().setSimilarityFactor(similarityFactor);
	}

	/**
	 * Run an algorithm based on the chosen function.
	 * 
	 * <p>
	 * Three of such functions are defined :
	 * <ol>
	 * <li>Align all the sequence</li>
	 * <li>Align to the longest</li>
	 * <li>Align randomly.</li>
	 * </ol>
	 * </p>
	 * 
	 * @param sequences
	 *            the list of sequence to be aligned
	 * @return a list of PairAlignment objects.
	 */
	protected List<PairAlignment> runByFunctionName(List<String> sequences) {
		List<PairAlignment> result = null;
		String functionName = getParameters().getFunctionName();
		switch (functionName.toLowerCase()) {
		case "complete":
			result = alignAllPairOfSequences(sequences);
			break;
		case "longest":
			result = alignAllTheSequencesToTheLongest(sequences);
			break;
		case "random":
			result = alignRandomlyAllThePairOfSequence(sequences);
			break;
		default:
			logger.error("This case is not implemented !");
		}

		return result;
	}

	@Override
	public AlignmentAParameters getParameters() {
		if (parameters == null) {
			parameters = new AlignmentAParameters(this.getConfigFileName());
		}
		return parameters;
	}

	public void setParameters(AlignmentAParameters parameters) {
		this.parameters = parameters;
	}

	public void setGapCharacter(char c) {
		this.getParameters().setGapCharacter(c);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Contract<Iterable<PairAlignment>> getContract() {
		if (this.contract == null) {
			contract = new RawContract(this);
		}
		return contract;
	}

	@Override
	public void setDefaultParameters() {
		this.setParameters(DefaultLearningAParameters.getDefaultAlignmentParams());
	}

	public char getFormatChar() {
		return 'a';
	}

	public PairAlignment createPair(String source, String target) {
		return new PairAlignment(this, source, target);

	}

	@Override
	public void setParameters(AbstractAlgorithmParameters parameters) {
		this.parameters = (AlignmentAParameters) parameters;

	}

}
