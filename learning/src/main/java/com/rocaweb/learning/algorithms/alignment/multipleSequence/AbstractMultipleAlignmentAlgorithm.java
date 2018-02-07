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

package com.rocaweb.learning.algorithms.alignment.multipleSequence;

import java.util.List;

import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.algorithms.alignment.pair.NeedlemanWunsch;
import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;
import com.rocaweb.learning.parameters.DefaultLearningAParameters;
import com.rocaweb.learning.parameters.MultipleAlignmentParameters;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.rules.Regex;

/**
 * An <code>abstract</code> class representing all the Multiple alignment
 * algorithms.
 * 
 * @author Djibrilla Amadou Kountche
 *
 * @param <T>
 *            is the type of the sequence to align.
 * 
 * @since 1.0.0
 */
public abstract class AbstractMultipleAlignmentAlgorithm<T> extends Algorithm<Iterable<String>> {

	/** The sequences to be aligned */
	protected transient List<T> sequences;

	/** Parameters of the alignment algorithm */
	private MultipleAlignmentParameters parameters;

	/** The contract. An alignment algorithm produces regular expressions */
	private transient Regex contract = null;

	/**
	 * Default constructor
	 */
	public AbstractMultipleAlignmentAlgorithm() {
	}

	/**
	 * @param configFileName
	 */
	AbstractMultipleAlignmentAlgorithm(String configFileName) {
		super(configFileName);
	}

	public List<T> getSequences() {
		return sequences;
	}

	public void setSequences(List<T> library) {
		this.sequences = library;
	}

	public NeedlemanWunsch getPairAlignmentAlgorithm() {

		return getParameters().getPairAlignmentAlgorithm();
	}

	public void setPairAlignmentAlgorithm(NeedlemanWunsch pairAlignmentAlgorithm) {
		getParameters().setPairAlignmentAlgorithm(pairAlignmentAlgorithm);
	}

	public MultipleAlignmentParameters getParameters() {
		if (parameters == null)
			parameters = new MultipleAlignmentParameters(getConfigFileName());

		return parameters;
	}

	public Contract<Iterable<String>> getContract() {
		if (contract == null) {
			contract = new Regex(this);
		}
		return contract;
	}

	public void setDefaultParameters() {
		this.parameters = DefaultLearningAParameters.getDefaultMultipleAlignmentParameters();
	}

	public char getFormatChar() {
		return 'a';
	}

	@Override
	public void setParameters(AbstractAlgorithmParameters parameters) {
		this.parameters = (MultipleAlignmentParameters) parameters;

	}

}
