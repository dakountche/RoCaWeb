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
package com.rocaweb.learning.rules;

import java.util.regex.Pattern;

import com.rocaweb.learning.algorithms.typing.AbstractTypingAlgorithm;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public final class Type extends Contract<String> {

	private int maxlength;
	private int minlength;
	private boolean learned;
	private String naivepattern;
	private int naivepatternweight;
	private String uri;
	private String name;
	private String parametertype;

	public Type() {
		setLearned(false);
		setNaivepatternweight(0);
	}

	public Type(AbstractTypingAlgorithm algorithm) {
		super(algorithm);
	}

	public String toString() {

		return getNaivepattern() + "{" + getMinlength() + "," + getMaxlength() + "}";

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getParametertype() {
		return parametertype;
	}

	public void setParametertype(String parametertype) {
		this.parametertype = parametertype;
	}

	public int getMinlength() {
		return minlength;
	}

	public void setMinlength(int minlength) {
		this.minlength = minlength;
	}

	public int getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}

	public int getNaivepatternweight() {
		return naivepatternweight;
	}

	public void setNaivepatternweight(int naivepatternweight) {
		this.naivepatternweight = naivepatternweight;
	}

	public boolean isLearned() {
		return learned;
	}

	public void setLearned(boolean learned) {
		this.learned = learned;
	}

	public String getNaivepattern() {
		return naivepattern;
	}

	public void setNaivepattern(String naivepattern) {
		this.naivepattern = naivepattern;
	}

	@Override
	public boolean validate(String sequence, String t) {
		Pattern pat = Pattern.compile(t);
		return pat.matcher(sequence).matches();
	}

}
