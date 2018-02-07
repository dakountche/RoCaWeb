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
package com.rocaweb.learning.algorithms.grammatical;

import org.trie4j.doublearray.DoubleArray;
import org.trie4j.louds.TailLOUDSTrie;
import org.trie4j.patricia.PatriciaTrie;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class TrieTest {

	public static void main(String[] args) throws Exception {
		PatriciaTrie pat = new PatriciaTrie();
		pat.insert("Hello world");
		pat.insert("World");
		pat.insert("Wonder");
		pat.insert("Wonderful!");
		pat.contains("Hello"); // -> true
		pat.predictiveSearch("Wo"); // -> {"Wonder", "Wonderful!", "World"} as Iterable<String>

		DoubleArray da = new DoubleArray(pat); // construct DoubleArray from existing Trie
		System.out.println(da.contains("Hello world")); // -> true

		TailLOUDSTrie lt = new TailLOUDSTrie(pat); // construct LOUDS succinct Trie with ConcatTailBuilder(default)
		System.out.println(lt.contains("Wonderful!")); // -> true
		System.out.println(lt.commonPrefixSearch("Wonderful!").toString()); // -> {"Wonder", "Wonderful!"} as
																			// Iterable<String>
	}

}
