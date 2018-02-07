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

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represent an alignment Character.
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */

public class ExtendedCharacter extends SimpleCharacter {

	/** The minimum value of the interval */
	private int lowerBound = 0;

	/** The maximum value of the interval */
	private int upperBound = 0;

	private boolean isWildcard = false;

	public ExtendedCharacter() {

	}

	public ExtendedCharacter(ExtendedCharacter aChar) {
		this.setCar(aChar.getChar());
		this.setLowerBound(aChar.getLowerBound());
		this.setUpperBound(aChar.getUpperBound());

	}

	public ExtendedCharacter(char aChar, int lowerBound, int upperBound) {
		setCar(aChar);
		swapAndSet(lowerBound, upperBound);
	}

	public ExtendedCharacter(char aChar, int lowerBound, int upperBound, boolean isWildcard) {
		this(aChar, lowerBound, upperBound);
		this.setWildcard(isWildcard);
	}

	ExtendedCharacter(char a, int index, int lowerBound, int upperBound, boolean isWildcard) {
		this(a, lowerBound, upperBound, isWildcard);
		this.setIndex(index);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ExtendedCharacter))
			return false;
		else {
			ExtendedCharacter aCharacter = (ExtendedCharacter) obj;
			return (super.equals(obj) && getChar() == aCharacter.getChar()
					&& getLowerBound() == aCharacter.getLowerBound() && getUpperBound() == aCharacter.getUpperBound());
		}
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public boolean isWildcard() {
		return isWildcard;
	}

	public void setLowerBound(int min) {
		this.lowerBound = min;
	}

	/* Getters and Setters */
	public void setUpperBound(int max) {
		this.upperBound = max;

	}

	public void setWildcard(boolean isWildcard) {
		this.isWildcard = isWildcard;
	}

	/**
	 * This method swaps the two values when the max is inferior to the min
	 * 
	 * @param min
	 *            : the given minimum value
	 * @param max
	 *            : the given maximum value
	 */
	private void swapAndSet(int min, int max) {
		if (max < min) {
			min += max;
			max = min - max;
			min -= max;
		}
		setLowerBound(min);
		setUpperBound(max);
	}

}
