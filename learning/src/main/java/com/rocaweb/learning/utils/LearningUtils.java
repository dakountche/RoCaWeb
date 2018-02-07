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
package com.rocaweb.learning.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */

public final class LearningUtils {

	public static String DEFAULT_CONFIG_FILE = "META-INF/algorithms-config.xml";

	private static Logger logger = LogManager.getLogger(LearningUtils.class);

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static List<HashMap<Character, Integer>> fillMap(final List<? extends String> list) {

		// First fill the list with the length of the StringBuffer
		// We suppose that the sequences are the same size, after a global
		// alignment
		List<HashMap<Character, Integer>> listCount = new ArrayList<HashMap<Character, Integer>>();
		int column = list.get(0).length();
		for (int i = 0; i < column; i++) {
			listCount.add(new HashMap<Character, Integer>());
		}

		// Then we start counting the for each column.
		// The goal is for a column i to determine the characters presents
		// which are different from the alignment char and to store the char and
		// it's number
		// of occurrences in a Map.

		for (int i = 0; i < column; i++) {
			for (int j = 0; j < list.size(); j++) {
				try {
					char c = list.get(j).charAt(i);

					if (listCount.get(i).containsKey(c)) {

						listCount.get(i).put(c, listCount.get(i).get(c) + 1);

					} else {
						listCount.get(i).put(c, 1);
					}

				} catch (IndexOutOfBoundsException iob) {
					logger.error("The sequences have not been aligned ! ");
				}
			}
		}

		return listCount;
	}

	public static List<String> stringBufferToString(List<StringBuffer> list) {
		List<String> list1 = Lists.newArrayList();
		for (StringBuffer sb : list)
			list1.add(sb.toString());
		return list1;
	}

	public static Set<String> removeEmptyColunms(Iterable<String> list, char gapChar) {
		logger.trace("Removing Empty columns in :" + list);

		List<String> copy = Lists.newArrayList(list);
		List<HashMap<Character, Integer>> maps = fillMap(copy);
		List<StringBuffer> sbFormCopy = stringsToSB(copy);

		for (int i = 0; i < maps.size(); i++) {
			if (maps.get(i).keySet().size() == 1 && maps.get(i).keySet().contains(gapChar)) {
				logger.trace(i + " " + maps.get(i));
				remove(sbFormCopy, i);
				copy = stringBufferToString(sbFormCopy);
				maps = fillMap(copy);
			}

		}

		return Sets.newHashSet(copy);
	}

	public static List<StringBuffer> stringsToSB(Iterable<String> set2) {
		List<StringBuffer> buffers = Lists.newArrayList();

		for (String stre : set2)
			buffers.add(toStringBuffer(stre));

		return buffers;
	}

	public static StringBuffer toStringBuffer(String string) {

		return new StringBuffer(string);
	}

	private static void remove(List<StringBuffer> alreadyAligned, int i) {
		for (StringBuffer sb : alreadyAligned) {
			logger.trace("Char about to be removed " + sb.charAt(i));
			sb.deleteCharAt(i);
		}

	}

	/**
	 * @param first
	 * @param second
	 * @return
	 */
	public static Pair<StringBuffer, StringBuffer> createPair(String first, String second) {

		return Pair.of(toStringBuffer(first), toStringBuffer(second));
	}

	/**
	 * @param alignedStrings
	 * @return
	 */
	public static List<String> stringBufferToString(Pair<StringBuffer, StringBuffer> alignedStrings) {
		List<String> result = Lists.newArrayList();
		result.add(alignedStrings.getLeft().toString());
		result.add(alignedStrings.getRight().toString());
		return result;
	}

}
