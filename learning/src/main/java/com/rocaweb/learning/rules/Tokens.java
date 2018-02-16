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
package com.rocaweb.learning.rules;

import java.util.List;

import com.rocaweb.learning.algorithms.grammatical.TokenFinder;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public final class Tokens extends Contract<List<String>> {

	/**
	 * @param tokenFinder
	 */
	public Tokens(TokenFinder tokenFinder) {
		super(tokenFinder);
	}

	public String createRule(List<String> tokens) {
		if(tokens == null || tokens.isEmpty())
			return "";
		int i = 0;
		StringBuilder result = new StringBuilder();
		for (String token: tokens)
			result.append(String.format("token%d=%s,", i++, token));
		return result.toString();
	}
	
	

}
