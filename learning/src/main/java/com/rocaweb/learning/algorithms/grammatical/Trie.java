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

package com.rocaweb.learning.algorithms.grammatical;

import java.util.List;

import org.trie4j.louds.TailLOUDSTrie;
import org.trie4j.patricia.PatriciaTrie;

import com.rocaweb.learning.data.grammatical.TrieDictionary;
import com.rocaweb.learning.rules.Contract;

/**
 * Application of the Trie data structure to the intrusion detection problem.
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 * 
 * @see https://en.wikipedia.org/wiki/Trie
 */
public class Trie extends AbstractGrammaticalAlgorithm<TrieDictionary> {

	private PatriciaTrie patTrie = null;

	private List<String> sequences = null;

	private Contract<TrieDictionary> contract = null;

	public Trie() {}

	private PatriciaTrie getPatTrie() {
		// if(patTrie == null) {
		patTrie = new PatriciaTrie();
		// }
		return patTrie;
	}

	private TailLOUDSTrie createTailTrie(PatriciaTrie pat) {
		return new TailLOUDSTrie(pat);
	}

	@Override
	public TrieDictionary process(List<String> sequences) {
		this.setSequences(sequences);
		for (String string : this.getSequences()) {
			this.getPatTrie().insert(string);
		}

		return this.getTrieDictionnaire();

	}

	/**
	 * @return
	 */
	private TrieDictionary getTrieDictionnaire() {
		TrieDictionary td = new TrieDictionary();
		td.setModel(this.createTailTrie(getPatTrie()));
		return td;
	}

	@Override
	public Contract<TrieDictionary> getContract() {
		if(contract == null) {
		
		}

		return contract;
	}

	/**
	 * @return the sequences
	 */
	public List<String> getSequences() {
		return sequences;
	}

	/**
	 * @param sequences
	 *            the sequences to set
	 */
	public void setSequences(List<String> sequences) {
		this.sequences = sequences;
	}

}
