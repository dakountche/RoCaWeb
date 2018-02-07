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
package com.rocaweb.learning.parameters;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.rocaweb.commons.configuration.IConfiguration;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomSeedGenerator;

/**
 * This class summarize all the parameters of a pair alignment algorithm.
 * 
 * 
 * @author Djibrilla Amadou Kountche
 * @since 1.0.0
 *
 */
public class AlignmentAParameters extends AlgorithmParameters {

	/** The pair of sequences to align */
	private Pair<String, String> pairOfSequence = null;

	/** The penalty **/
	private double penalty = -1;

	/** The Mismatch **/
	private double mismatch = 0;

	/** The match **/
	private double match = 1;

	/** The alphabet is the set of characters observed in a given data file */
	private Set<Character> alphabet = new HashSet<Character>();

	/** The gap character to insert in a sequence */
	private char gapCharacter = '¤';

	/** The factor to multiply the match with */
	private transient double similarityFactor = 1;

	/**
	 * A string containing possible gap characters. The chars are tested against the
	 * alphabet and the first on which is not in the alphabet is chosen.
	 */
	private final char[] possibleGapCharacters = { '¤', '&', '£', '§' };

	/**
	 * A special char had a particular match value. This values corresponds to
	 * match*similatiyFactor
	 */
	private Set<Character> specialChars;

	/** The name of the fuction to use for the alignment */
	private String functionName = "Complete";

	private RandomSeedGenerator rsg = new RandomSeedGenerator();
	private int seed = rsg.nextSeed();
	private int seed1 = rsg.nextSeed();
	private int seed2 = rsg.nextSeed();

	private Uniform ma = new Uniform(1, 1, seed);
	private Uniform mi = new Uniform(-1, 1, seed1);
	private Uniform p = new Uniform(-2, 0, seed2);

	public AlignmentAParameters() {

	}

	public AlignmentAParameters(String defaultConfigFile) {
		this.loadConfig(defaultConfigFile);

	}

	public double getMatch() {
		return match;
	}

	public void setMatch(double match) {
		this.match = match;
	}

	public double getMismatch() {
		return mismatch;
	}

	public void setMismatch(double mismatch) {
		this.mismatch = mismatch;
	}

	public double getPenalty() {
		return penalty;
	}

	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Pair<String, String> getPairOfSequence() {

		return pairOfSequence;
	}

	/**
	 * Set the similarity factor
	 * 
	 */
	public void setSimilarityFactor(double similarityFactor) {
		this.similarityFactor = similarityFactor;
	}

	/**
	 * Set the first sequence
	 * 
	 */
	public void setTheFisrtSequence(String sequence) {

	}

	/**
	 * Set the second sequence
	 */
	public void setTheSecondSequence(String sequence) {

	}

	/**
	 * Set the pair of sequences
	 * 
	 */
	public void setPairOfSequence(Pair<String, String> pairOfSequence) {
		this.pairOfSequence = pairOfSequence;
	}

	/**
	 * Set the alphabet
	 * 
	 */
	public void setAlphabet(Set<Character> pAlphabet) {
		this.alphabet = pAlphabet;
	}

	/**
	 * Get the special characters
	 * 
	 * @return the special characters.
	 */
	public Set<Character> getSpecialChars() {
		if (specialChars == null) {
			specialChars = new HashSet<Character>();
		}
		return specialChars;
	}

	/**
	 * Get the similarity factor.
	 * 
	 */
	public double getSimilarityFactor() {
		return similarityFactor;
	}

	public char getGapCharacter() {

		return gapCharacter;
	}

	public Set<Character> getAlphabet() {

		return alphabet;
	}

	@Override
	public String printParameters() {

		String message = super.printParameters();
		message += specificParametersMessage();
		return message;
	}

	String specificParametersMessage() {
		StringBuilder message = new StringBuilder(String.format(format, "# Function Name :", this.getFunctionName()));
		message.append(String.format(format, "# penality", getPenalty()));
		message.append(String.format(format, "# match", getMatch()));
		message.append(String.format(format, "# mistmatch", getMismatch()));
		message.append(String.format(format, "# Gap Character", this.getGapCharacter()));

		return message.toString();
	}

	public void setGapCharacter(char c) {
		this.gapCharacter = c;
	}

	public String getAttributeName() {
		return "pair-alignment";
	}

	/**
	 * @return the possibleGapCharacters
	 */
	public char[] getPossibleGapCharacters() {
		return possibleGapCharacters;
	}

	public String toString() {

		return super.toString() + this.specificStringForJson();

	}

	@Override
	public String specificStringForJson() {

		return String.format(
				", gapCharacter: %s, functionName: %s, match: %s, mismatch: %s, penalty: %s, possibleGapCharacters: %s",
				this.getGapCharacter(), this.getFunctionName(), this.getMatch(), this.getMismatch(), this.getPenalty(),
				String.valueOf(this.getPossibleGapCharacters()));

	}

	@Override
	protected void specificConfig(IConfiguration config) {
		setGapCharacter(config.getString("gapCharacter").charAt(0));
		setFunctionName(config.getString("functionName"));
		setMatch(config.getDouble("match"));
		setMismatch(config.getDouble("mismatch"));
		setPenalty(config.getDouble("penalty"));

	}

	@Override
	public void randomlyGenerateParamValues() {
		this.setMatch(ma.nextDouble());
		this.setMismatch(mi.nextDouble());
		this.setPenalty(p.nextDouble());

	}

}
