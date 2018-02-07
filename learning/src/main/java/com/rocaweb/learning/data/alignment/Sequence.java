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

import java.util.ArrayList;
import java.util.List;

/**
 * This class resumes functions and details concerning a "sequecne"
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
abstract class Sequence<T> {

    private List<T> aCharacters = null;
    private int sequenceId;

    public Sequence() {

    }

    Sequence(String sequence) {
	T aCharacter;
	for (int i = 0; i < sequence.length(); i++) {

	    aCharacter = create(sequence.charAt(i), i);
	    getACharacters().add(aCharacter);
	}

    }

    

    public List<T> getACharacters() {
	if (aCharacters == null) {
	    aCharacters = new ArrayList<T>();
	}
	return aCharacters;
    }

    public String toString() {
	String sequence = "";
	for (T csr : getACharacters()) {
	    sequence += csr.toString();
	}
	return sequence;

    }

    public void setACharacters(List<T> aCharacters) {
	this.aCharacters = aCharacters;
    }

    public abstract T create(char a, int index);

    /**
     * @return the sequenceId
     */
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * @param sequenceId
     *            the sequenceId to set
     */
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

}
