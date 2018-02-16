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

package com.rocaweb.learning.algorithms.grammatical;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.rules.Tokens;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;

/**
 * Implementation of the Token finder algorithm
 *  
 * @author Djibrilla Amadou Kountche
 * @since 1.0.0
 * 
 * @see Kruegel, C., Vigna, G., & Robertson, W. (2005). A multi-model approach
 *      to the detection of web-based attacks. Computer Networks, 48(5),
 *      717-738.}.
 *
 */
public class TokenFinder extends AbstractGrammaticalAlgorithm<List<String>> {

	private List<String> sequences;
	private DoubleArrayList fFunstion = new DoubleArrayList();
	private DoubleArrayList gFunction = new DoubleArrayList();

	protected transient Tokens contract = null;

	public TokenFinder(List<String> sequences) {
		setSequences(sequences);
	}

	/**
	 * Constructor
	 */
	public TokenFinder() {}

	
	public void setSequences(List<String> sequences) {
		this.sequences = sequences;
	}

	
	private void initialize() {
		for (int i = 0; i < getSequences().size(); i++) {
			fFunstion.add(i);
			calculateG(i);
		}
		logger.trace(print(fFunstion));
		logger.trace(print(gFunction));
	}

	/**
	 * @return
	 */
	public List<String> getSequences() {
		if (sequences == null) {
			sequences = new ArrayList<String>();
		}
		return sequences;
	}

	/**
	 * Fill the function G
	 * 
	 * @param i
	 */
	private void calculateG(int i) {
		if (i == 0) {
			gFunction.add(i);
		} else if (getSequences().subList(0, i - 1).contains(getSequences().get(i))) {
			gFunction.add(gFunction.get(i - 1) - 1);
		} else {
			gFunction.add(gFunction.get(i - 1) + 1);
		}
	}

	public double correlate() {
		this.initialize();
		double stdF = standardDeviation(fFunstion);
		double stdG = standardDeviation(gFunction);
		return Descriptive.correlation(fFunstion, stdF, gFunction, stdG);
	}

	public double standardDeviation(DoubleArrayList f) {
		int size = f.size();
		double sum = Descriptive.sum(f);
		double squarreSum = Descriptive.sumOfSquares(f);
		return Descriptive.standardDeviation(Descriptive.variance(size, sum, squarreSum));
	}

	private String print(DoubleArrayList f) {
		StringBuilder msg = new StringBuilder();
		for (int i = 0; i < f.size(); i++) {
			msg.append("  " + f.get(i));
		}
		return msg.toString();
	}

	public Set<String> getEnumeration() {
		HashSet<String> enumeration = new HashSet<>();
		if (correlate() < 0.0) {
			enumeration.addAll(getSequences());
		}
		return enumeration;
	}

	@Override
	public List<String> process(List<String> sequences) {
		setSequences(sequences);
		logger.trace(sequences);
		this.initialize();
		return Lists.newArrayList(this.getEnumeration());
	}

	@Override
	public Contract<List<String>> getContract() {
		if (contract == null) {
			contract = new Tokens(this);
		}
		return contract;
	}
	
	public char getFormatChar() {
		return 's';
	}
}
