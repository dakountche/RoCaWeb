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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rocaweb.learning.algorithms.alignment.multipleSequence.AMAA;

/**
 * Implementation of the library used in the TCOFFEE algorithm
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class Library {

	private static final char SPECIALCHAR = '¤';
	private List<PairAlignment> alignedSequences;
	private Logger logger = LogManager.getLogger(Library.class);

	private AMAA progressiveMultiAlignmentAlgorithm;

	/**
	 * Combine this library with another library
	 * 
	 * @param libraries
	 */
	public void combine(List<String> libraries) {

	}

	/**
	 * Extends the library by using the Library Extension Heuristic.
	 * 
	 */
	public void extend() {
		List<PairAlignment> copyOfAlignedSequences = Collections.unmodifiableList(getAlignedSequences());
		for (PairAlignment target : getAlignedSequences()) {
			extendPair(target, copyOfAlignedSequences);
		}
	}

	/**
	 * Extend an alignment with the information included the others alignments.
	 * 
	 * @param alignment
	 * @param othersAlignments
	 */
	private void extendPair(PairAlignment alignment, List<PairAlignment> othersAlignments) {
		Set<Set<String>> alreadySeen = Sets.newHashSet();
		Set<Set<String>> alreadySeenResults = Sets.newHashSet();

		Set<String> sequences = Sets.newHashSet();
		Set<String> result = Sets.newHashSet();

		for (PairAlignment other : othersAlignments) {

			sequences.clear();

			sequences.addAll(createList(alignment));

			for (String sequence : createList(other)) {
				sequences.add(sequence);
				logger.trace(sequences);
				if (sequences.size() == 3) {
					if (!alreadySeen.contains(sequences)) {
						alreadySeen.add(sequences);
						AMAA amaa = new AMAA();
						result = amaa.align(Lists.newArrayList(sequences));
						logger.trace(result);
						if (result.size() == 3 && !alreadySeenResults.contains(result)) {
							alreadySeenResults.add(result);
							logger.trace(Joiner.on(",").join(result) + '\n');

							Map<Pair<SimpleCharacter, SimpleCharacter>, Integer> map = determineMap(
									Lists.newArrayList(result));
							logger.trace(map);
							alignment.addMap(map);

						}
					}
				}

			}
		}

	}

	private static List<String> createList(PairAlignment alignment) {
		List<String> list = Lists.newArrayList();
		list.add(alignment.getSequences().getLeft());
		list.add(alignment.getSequences().getRight());

		return list;
	}

	private Map<Pair<SimpleCharacter, SimpleCharacter>, Integer> determineMap(List<String> alignedSequences) {
		Map<Pair<SimpleCharacter, SimpleCharacter>, Integer> map = Maps.newHashMap();

		List<SimpleCharSequence> simpleCharSequences = Lists.newArrayList();
		int minimumWeigth = Integer.MAX_VALUE;
		for (int i = 0; i < alignedSequences.size() - 1; i++) {
			for (int j = i + 1; j < alignedSequences.size(); j++) {
				PairAlignment alignment = new PairAlignment(
						this.getProgressiveMultipleAlignmentAlgorithm().getPairAlignmentAlgorithm(),
						alignedSequences.get(i), alignedSequences.get(j));

				simpleCharSequences.add(alignment.getSimpleCharSequences().getLeft());
				simpleCharSequences.add(alignment.getSimpleCharSequences().getRight());
				minimumWeigth = Math.min(alignment.getWeight(), minimumWeigth);
			}

		}

		for (int i = 0; i < simpleCharSequences.get(0).getACharacters().size(); i++) {
			SimpleCharacter char1 = simpleCharSequences.get(0).getACharacters().get(i);
			SimpleCharacter char2 = simpleCharSequences.get(1).getACharacters().get(i);
			SimpleCharacter char3 = simpleCharSequences.get(2).getACharacters().get(i);

			if (equals(char1, char2, char3)) {
				updateMap(map, char1, char2, char3, minimumWeigth);
			}

		}

		return map;
	}

	private void updateMap(Map<Pair<SimpleCharacter, SimpleCharacter>, Integer> map, SimpleCharacter char1,
			SimpleCharacter char2, SimpleCharacter char3, int minimumWeigth) {
		map.put(Pair.of(char1, char2), minimumWeigth);
		map.put(Pair.of(char1, char3), minimumWeigth);
		map.put(Pair.of(char2, char3), minimumWeigth);

	}

	private boolean equals(SimpleCharacter char1, SimpleCharacter char2, SimpleCharacter char3) {
		return (char1.getChar() != SPECIALCHAR && char2.getChar() != SPECIALCHAR  && char3.getChar() != SPECIALCHAR ) &&

				(char1.getChar() == char2.getChar() && char1.getChar() == char3.getChar());

	}

	public List<PairAlignment> getAlignedSequences() {
		if (alignedSequences == null) {
			alignedSequences = new ArrayList<PairAlignment>();
		}
		return alignedSequences;
	}

	public AMAA getProgressiveMultipleAlignmentAlgorithm() {
		return progressiveMultiAlignmentAlgorithm;
	}

	public void setAlignmentAlgorithm(AMAA amaa) {
		this.progressiveMultiAlignmentAlgorithm = amaa;
	}

	/**
	 * @param alignedSequences
	 *            the alignedSequences to set
	 */
	public void setAlignedSequences(List<PairAlignment> alignedSequences) {
		this.alignedSequences = alignedSequences;
	}

}
