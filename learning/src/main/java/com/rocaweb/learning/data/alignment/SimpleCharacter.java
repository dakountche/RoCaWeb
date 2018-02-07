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
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */

class SimpleCharacter {

	/** A character from the alphabet */
	private char car;

	/** The index of this character in the sequence */
	private int indexOfTheChar;

	private int sequenceID;

	public SimpleCharacter() {

	}

	SimpleCharacter(char charAt, int i) {
		setCar(charAt);
		setIndex(i);
	}

	public char getChar() {
		return car;
	}

	public void setCar(char car) {
		this.car = car;
	}

	public int getIndex() {

		return indexOfTheChar;
	}

	public void setIndex(int pIndex) {
		indexOfTheChar = pIndex;
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof SimpleCharacter)) {
			return false;
		} else {
			SimpleCharacter sc = (SimpleCharacter) obj;
			return (getChar() == sc.getChar()) && (this.getIndex() == sc.getIndex());
		}
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(car).append(indexOfTheChar).toHashCode();
	}

	public String toString() {
		return "" + getChar();
	}

	public void setSequenceID(int sequenceID) {
		this.sequenceID = sequenceID;
	}

	public int getSequenceId() {
		return this.sequenceID;
	}

}
