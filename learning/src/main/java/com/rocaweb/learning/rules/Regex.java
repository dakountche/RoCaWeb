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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.rocaweb.learning.algorithms.alignment.multipleSequence.AMAA;
import com.rocaweb.learning.algorithms.alignment.multipleSequence.AbstractMultipleAlignmentAlgorithm;
import com.rocaweb.learning.algorithms.alignment.pair.AbstractPairAlignmentAlgorithm;
import com.rocaweb.learning.data.alignment.ExtendedCharacter;
import com.rocaweb.learning.utils.LearningUtils;

/**
 * Generate regular expressions. It is the implementation of the algorithm
 * described as follow :
 * 
 * 
 * <p>
 * \begin{algorithm} <br>
 * \caption{Basic Regular Expression Learning Algorithm}<br>
 * \begin{algorithmic}[1]<br>
 * \STATE Input : a set $I =\{s_i, i=1,\dots, n\}$ with $len(s_i) = len(s_j) =
 * m, i \neq j, 1 <=i, j <= n$<br>
 * \STATE Output : The corresponding regular expression $RE$<br>
 * \FOR{every column $c_k, 0 <=k < m$}<br>
 * \STATE Determine the characters distribution for $c_k$<br>
 * \IF{$c_k$ contains only one type of character $\alpha$}<br>
 * \STATE $\alpha$ is an invariant character and append $\alpha$ in $RE$<br>
 * \ELSE<br>
 * \WHILE{$c_{k}$ is not a fixed character}<br>
 * \STATE Create a set ($V$) and insert the characters of $c_{k}$ in it<br>
 * \STATE Determine the $\min$ and $\max$ length (stop at the next fixed
 * character)<br>
 * \STATE $k \leftarrow k +1$<br>
 * \ENDWHILE<br>
 * \STATE Sort the characters of $V$ and append the character to $RE$ as
 * $[ordered(V)]\{\min, \max\}$<br>
 * \ENDIF<br>
 * \ENDFOR<br>
 * \end{algorithmic}<br>
 * \label{brela}<br>
 * \end{algorithm}<br>
 * </p>
 * 
 * <h3>Example</h3>
 * 
 * <p>
 * \texttt{\begin{tabular}{l|l|}<br>
 * \cline{2-2}<br>
 * &Result of AMAA \\ \cline{2-2}<br>
 * &GARFIELD THE VERY FAST CAT \\<br>
 * &+++++++++THE+++++ FAST CAT\\<br>
 * &GARFIELD THE+++++ FAST CAT\\<br>
 * &GARFIELD THE LAST FA+T CAT \\ \cline{2-2} <br>
 * Result of BRELA&[GARFIELD ]\{0,1\}THE[ LAST]\{0,1\} FA[S]\{0,1\}T CAT \\
 * \hline<br>
 * \end{tabular}}<br>
 * \caption{The final result of AMAA on the GARFIELD dataset.}<br>
 * \label{amaa:final:result}<br>
 * \end{table}<br>
 * </p>
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 * 
 */
public final class Regex extends Contract<Iterable<String>> {

	/** Special characters used in the final rule */
	protected static List<Character> wildcards = Lists.newArrayList('*', '?', 'µ', '+');

	private static Regex regex;

	/** Metacharacters used for the regular expression */
	private static Set<Character> metacharacters = Sets.newHashSet('(', ')', '?', '*', '|', '\\', '[', ']', '^', '<',
			'>', '$', '.', '{', '}', '+', '-');

	public Regex() {

	}

	public Regex(AbstractMultipleAlignmentAlgorithm<?> abstractMultipleAlignmentAlgorithm) {
		super(abstractMultipleAlignmentAlgorithm);
	}

	/**
	 * Another algorithm for generating regular expressions based on the multiple
	 * sequence alignment.
	 * 
	 * @param the
	 *            list of the aligned sequences
	 * @return the basic regular expression generated
	 */
	public String generateBRE(final List<? extends String> list, final char gapCharacter) {

		// If the list contains only one sequence
		if (list.size() == 1) {

			return escape(list.get(0).toString());
		}

		StringBuilder rule = new StringBuilder();

		// The set of all the chars observed between the first and the second
		// fixed characters
		Set<Character> observedCharBetweenfs = new HashSet<Character>();

		// Store here the seen characters
		List<Integer> seenPositions = new ArrayList<Integer>();
		// Counters of the length of the substrings.
		int min = 0;
		int max = 0;
		int k = 0;

		List<HashMap<Character, Integer>> map = LearningUtils.fillMap(list);
		List<Character> sortedList = new ArrayList<Character>();
		logger.trace("The size of list of maps " + map.size());

		for (int i = 0; i < map.size();) {

			int j = i;

			while (j < map.size() && map.get(j).size() > 1) {

				observedCharBetweenfs.addAll(map.get(j).keySet());

				j++;

			}

			if (!seenPositions.contains(j) && j < map.size() && map.get(j).size() == 1) {
				seenPositions.add(j);
			}

			String spechar = "";

			if (j < map.size() && map.get(j).size() == 1 && !map.get(j).keySet().contains(gapCharacter)) {

				spechar = map.get(j).keySet().toArray()[0].toString();
				if (this.getMetacharacters().contains(spechar.charAt(0)))
					spechar = "\\" + spechar;
			}

			max = Integer.MIN_VALUE;
			min = Integer.MAX_VALUE;
			for (String string : list) {

				String substring = "";

				if (k == 0) {

					if (seenPositions.contains(k))
						if (k == j)
							continue;
						else
							substring = string.substring(1, j);
					else
						substring = string.substring(0, j);
				} else {

					substring = string.substring(k + 1, j);

				}
				if (substring.contains("" + gapCharacter))
					substring = substring.replace("" + gapCharacter, "");

				if (substring.length() < min) {
					min = substring.length();
				}

				if (substring.length() > max) {
					max = substring.length();
				}

			}
			if (!observedCharBetweenfs.isEmpty()) {
				rule.append("[");
				sortedList.clear();
				sortedList.addAll(observedCharBetweenfs);
				Collections.sort(sortedList);
				StringBuilder part = new StringBuilder("");
				String echap = "";
				for (char cara : sortedList) {
					if (cara != gapCharacter) {
						if (getMetacharacters().contains(cara))
							echap = "\\";
						else
							echap = "";
						part.append(echap + cara);
					}
				}
				String text = "";
				if (min == max)
					text = "{" + max + "}";
				else
					text = "{" + min + "," + max + "}";
				rule.append(part.toString() + "]" + text + spechar);
			} else {

				rule.append(spechar);
			}
			logger.trace(rule);
			if (seenPositions.size() > 0) {
				k = seenPositions.get(seenPositions.size() - 1);

			}
			i = j + 1;

			observedCharBetweenfs.clear();

		}

		return rule.toString();

	}

	/**
	 * Generates a simplified regular expression from a given list of characters.
	 * The method searches the occurrences of wildcards, and depending of special
	 * characters (?, +, *, µ), calculates the lower and the upper bound.
	 * 
	 * @param alignedSequences
	 *            the result of the alignment.
	 * @return a simplified regular expression.
	 */
	public String generateSRE(List<ExtendedCharacter> alignedSequences) {
		// The resulting SRe generated from result.
		StringBuffer generatedSRE = new StringBuffer("");
		ExtendedCharacter aCharacter;
		int upperBound = 0;
		int lowerBound = 0;
		for (int i = 0, j = 0; i < alignedSequences.size();) {
			aCharacter = alignedSequences.get(i);
			// If the character is not a wildcard the include it in the final
			// SRE.
			if (!getWildcards().contains(aCharacter.getChar())) {
				generatedSRE.append(alignedSequences.get(i).getChar());
				i++;
			} else { // Otherwise, start a new interval.
				generatedSRE.append("[");
				j = i; // Starting from the current position count the numbers
						// of special characters
				while (j < alignedSequences.size() && getWildcards().contains(alignedSequences.get(j).getChar())) {
					aCharacter = alignedSequences.get(j);
					lowerBound += aCharacter.getLowerBound();
					upperBound += aCharacter.getUpperBound();
					j++;
				}
				if (lowerBound > upperBound) {
					lowerBound += upperBound;
					upperBound = lowerBound - upperBound;
					lowerBound -= upperBound;
				}
				// in case of identity, just keep one
				if (upperBound == lowerBound) {
					generatedSRE.append(upperBound + "]");
				} else { // otherwise put both
					generatedSRE.append(lowerBound + "," + upperBound + "]");
				}
				i = j; // then we starts again at the last position we found a
						// special character
				lowerBound = 0;
				upperBound = 0;
			}
		}

		return generatedSRE.toString();
	}

	/**
	 * Here we defined the wildcards with their meanings.
	 * 
	 * @return the list of available wildcards.
	 */
	public static List<Character> getWildcards() {

		return wildcards;
	}

	public static synchronized Regex getInstance() {
		if (regex == null)
			regex = new Regex();
		return regex;
	}

	@Override
	public String createRule(Iterable<String> iterable) {
		List<String> results = Lists.newArrayList(iterable);
		String rule = "";
		AbstractPairAlignmentAlgorithm aaa = null;

		if (getAlgorithm() instanceof AMAA) {
			aaa = ((AMAA) this.getAlgorithm()).getPairAlignmentAlgorithm();
		}

		if (aaa != null)
			rule = generateBRE(results, aaa.getGapCharacter());

		return rule;
	}

	/**
	 * A meta-character is used in the regular expression to mark the beginning or
	 * the end of sub part of the regex.
	 * 
	 * @return the set of meta-characters
	 */
	private Set<Character> getMetacharacters() {

		return metacharacters;
	}

	private String escape(String chaine) {
		StringBuilder escapedString = new StringBuilder();
		for (char cara : chaine.toCharArray()) {
			if (this.getMetacharacters().contains(cara)) {
				escapedString.append("\\" + cara);
			} else
				escapedString.append("" + cara);
		}
		return escapedString.toString();
	}

	@Override
	public boolean validate(String sequence, Iterable<String> alignedSequences) {
		Pattern pat = Pattern.compile(createRule(alignedSequences));
		return pat.matcher(sequence).matches();
	}

}
