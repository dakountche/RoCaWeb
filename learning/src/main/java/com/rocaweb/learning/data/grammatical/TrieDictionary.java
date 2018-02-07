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
/**
 * 
 */
package com.rocaweb.learning.data.grammatical;

import org.trie4j.louds.TailLOUDSTrie;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */

public class TrieDictionary extends Dictionary<TailLOUDSTrie> {

	private TailLOUDSTrie model = null;

	/**
	 * @see com.rocaweb.learning.data.statistical.Validation#validate(java.lang.String)
	 */
	@Override
	public boolean validate(String sequence) {

		return this.getModel().contains(sequence);
	}

	/**
	 * @see com.rocaweb.learning.data.grammatical.Dictionary#getModel()
	 */
	@Override
	public TailLOUDSTrie getModel() {

		return model;
	}

	/**
	 * @see com.rocaweb.learning.data.grammatical.Dictionary#setModel(java.lang.Object)
	 */
	@Override
	public void setModel(TailLOUDSTrie model) {
		this.model = model;

	}

}
