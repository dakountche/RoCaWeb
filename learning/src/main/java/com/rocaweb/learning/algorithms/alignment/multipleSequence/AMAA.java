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

package com.rocaweb.learning.algorithms.alignment.multipleSequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.rocaweb.learning.algorithms.alignment.pair.NeedlemanWunsch;
import com.rocaweb.learning.parameters.MultipleAlignmentParameters;
import com.rocaweb.learning.utils.LearningUtils;

/**
 * Implementation the AMAA algorithm. AMAA stands for Another multiple Alignment
 * Algorithm and has been developed for the RoCaWeb project.
 * 
 * The algorithm is described as follow :
 * 
 * <p>
 * \begin{algorithm} \caption{AMAA : Another Multiple Alignment Algorithm}<br>
 * \label{alg:amaa}<br>
 * \begin{algorithmic}[1] <br>
 * \STATE Input : $S = \{s_i, i=1,\dots, n\}$ set of sequences to be aligned.
 * <br>
 * \STATE $S' = \emptyset $<br>
 * \STATE And a pair alignment algorithm
 * (Needleman-Wunsch~\cite{bckenhauer2007algorithmic})<br>
 * \STATE Output : $S' = \{a(s_i), i = 1,\dots,n\}$ the aligned sequences (with
 * the same length). Where $a(s_i)$ is the alignment of $s_i$ produce by the
 * algorithm.<br>
 * \STATE Initialization : <br>
 * \STATE Determine $n\times(n-1)$ alignments of all sequences and choose the
 * best alignment :<br>
 * \STATE $A^{*}= (s'_k, s'_p)$ such that $score(s'_k, s'_p) > score(s'_i, s'_j)
 * $<br>
 * \STATE Insert the best pair ($A^{*}$) of sequences in $S'$ and delete $(s_k,
 * s_p)$ form $S$<br>
 * \WHILE{$S$ is not empty}<br>
 * \STATE $ s \leftarrow pop(S)$<br>
 * \STATE Align $s$ with all the sequences in $S'$ and choose the best sequences
 * <br>
 * \FOR{All the sequences $s' \in S'$}<br>
 * \IF{$s'$ has been modified by the alignment}<br>
 * \STATE Replace $s'$ in $S'$ by the new sequences $s''$<br>
 * \ELSIF{the length of the a sequence changed}<br>
 * \STATE Insert padding in the position where an insertion or a deletion took
 * place<br>
 * \ENDIF<br>
 * \ENDFOR<br>
 * \STATE Add the new best alignment for the sequence $s$ in $S'$<br>
 * \ENDWHILE<br>
 * \end{algorithmic}<br>
 * 
 * \end{algorithm}
 * </p>
 * 
 * <p>
 * <h3>Examples</h3>
 * <p>
 * To illustrate how AMAA works, the GARFIELD dataset~\cite{notredame2000t},
 * described in table~\ref{tab:garfield}, is used. The parameters of AMAA are
 * the alignment algorithm and it's parameters. The configuration used is : '+'
 * as gap character, $match=1.0$, $mismatch=0.0$ and the $penalty = -1.0$.
 * </p>
 * 
 * \begin{table}<br>
 * \centering<br>
 * \texttt{\begin{tabular}{l}<br>
 * GARFIELD THE LAST FAT CAT \\<br>
 * GARFIELD THE FAST CAT \\<br>
 * GARFIELD THE VERY FAST CAT \\<br>
 * THE FAST CAT \\<br>
 * \end{tabular}}<br>
 * \caption{The GARFIELD dataset.}<br>
 * \label{tab:garfield}<br>
 * \end{table}<br>
 * </p>
 * 
 * AMAA will produce the following result.
 * <p>
 * \texttt{\begin{tabular}{l|l|}<br>
 * \cline{2-2}<br>
 * &Result of AMAA \\ \cline{2-2}<br>
 * &GARFIELD THE VERY FAST CAT \\<br>
 * &+++++++++THE+++++ FAST CAT\\<br>
 * &GARFIELD THE+++++ FAST CAT\\<br>
 * &GARFIELD THE LAST FA+T CAT \\ \cline{2-2}<br>
 * Result of BRELA&[GARFIELD ]\{0,1\}THE[ LAST]\{0,1\} FA[S]\{0,1\}T CAT \\
 * \hline <br>
 * \end{tabular}}<br>
 * \caption{The final result of AMAA on the GARFIELD data set.}<br>
 * \label{amaa:final:result}<br>
 * \end{table}
 * 
 * <p>
 * 
 * 
 * This result is the used to generate a regular expression.
 * 
 * @author Djibrilla Amadou Kountche
 *
 * @since 1.0.0
 */
public class AMAA extends AbstractMultipleAlignmentAlgorithm<String> {

	private static final int FIRST = 0;

	/** Store the already aligned sequences. */
	private transient ArrayList<StringBuffer> alreadyAligned = null;

	/** Source sequence */
	private transient String source = null;

	/** Target sequence */
	private transient String target = null;

	/** Result of the alignment for the source sequence */
	private transient StringBuffer alignedSource = null;

	/** Result of the alignment for the target sequence */
	private transient StringBuffer alignedTarget = null;

	/** Utility set used to filter the sequences */
	private transient Set<String> filteringSet = null;

	/**
	 * Candidate sequence to pad. If a padding is necessary. It can be source or
	 * target
	 */
	private transient String sequenceToPadd = null;

	/** The original sequence. Can be source of target */
	private transient String originalalignedSequence = null;

	/**
	 * Constructor
	 */
	public AMAA() {}

	/**
	 * Constructor
	 * 
	 * @param nw
	 *            An instance of <code>NeedlemanWunsch</code>
	 * 
	 * @param aParameters
	 *            the parameters of the AMAA
	 */
	public AMAA(NeedlemanWunsch nw, MultipleAlignmentParameters aParameters) {
		this.setPairAlignmentAlgorithm(nw);
		this.setParameters(aParameters);
	}

	/**
	 * Constructor
	 * 
	 * @param configFile
	 *            the configuration file.
	 */
	public AMAA(String configFile) {
		super(configFile);
	}

	/**
	 * Align all of the sequences according to the algorithm described above.
	 * 
	 * @param inputsequences
	 *            the list of all the sequences.
	 * @return a set of the aligned sequences.
	 */
	public Set<String> amaa(final List<String> inputsequences) {
		this.setSequences(Lists.newArrayList(inputsequences));
		if (sequences.size() == 1) {
			return Sets.newHashSet(sequences);
		}
		initialize(sequences);
		while (!getSequences().isEmpty()) {
			String sequence = getSequences().remove(FIRST);
			determineTheBestAlignmentFromTheAlreadyAlignedSequencesFor(sequence);
			paddTheOtherSequences();
			filterTheAlignedSequences();
		}
		return removeEmptyColunms(filteringSet);
	}

	/**
	 * Remove duplicated elements
	 */
	private void filterTheAlignedSequences() {
		filteringSet.clear();
		for (StringBuffer rchaine : alreadyAligned) {
			filteringSet.add(rchaine.toString());
		}
	}

	/**
	 * Pad the sequences if necessary. This method is used when certain aligned
	 * sequences does not have the same size as the longest aligned sequence.
	 */
	private void paddTheOtherSequences() {
		if (!sequenceToPadd.isEmpty() && !sequenceToPadd.equals(originalalignedSequence.toString())) {
			int numberOfPads = 0;
			for (StringBuffer alignedSequence : alreadyAligned) {
				numberOfPads = Math.abs(sequenceToPadd.length() - originalalignedSequence.length());
				if (alignedSequence.length() < sequenceToPadd.length()) {
					for (int i = 0; i < sequenceToPadd.length(); i++) {
						if (i < originalalignedSequence.length()) {
							if (originalalignedSequence.charAt(i) != sequenceToPadd.charAt(i)
									&& sequenceToPadd.charAt(i) == getPairAlignmentAlgorithm().getGapCharacter()
									&& numberOfPads > 0) {
								alignedSequence.insert(i, getPairAlignmentAlgorithm().getGapCharacter());
								numberOfPads--;

							}
							// Insert the padding at the end.
						} else {
							if (numberOfPads > 0) {
								alignedSequence.insert(i, getPairAlignmentAlgorithm().getGapCharacter());
								numberOfPads--;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Remove columns where there is only gap characters.
	 * 
	 * @param filteredSequences the aligned sequences.
	 * @return filter clean aligned sequences.
	 */
	private Set<String> removeEmptyColunms(Set<String> filteredSequences) {

		List<String> copyOfFS = Lists.newArrayList(filteredSequences);
		List<HashMap<Character, Integer>> maps = LearningUtils.fillMap(copyOfFS);
		List<StringBuffer> stringBuffers = Lists.newArrayList();
		for (String s : copyOfFS)
			stringBuffers.add(new StringBuffer(s));
		for (int i = 0; i < maps.size(); i++) {
			if (maps.get(i).keySet().size() == 1
					&& maps.get(i).keySet().contains(getPairAlignmentAlgorithm().getGapCharacter())) {

				remove(stringBuffers, i);
				copyOfFS = stringBufferToString(stringBuffers);
				maps = LearningUtils.fillMap(copyOfFS);
			}
		}
		return Sets.newHashSet(copyOfFS);
	}

	private List<String> stringBufferToString(List<StringBuffer> source) {
		List<String> target = Lists.newArrayList();
		for (StringBuffer sb : source)
			target.add(sb.toString());
		return target;
	}

	
	private void remove(List<StringBuffer> alreadyAligned, int i) {
		for (StringBuffer sb : alreadyAligned) {
			logger.trace("Char about to be removed " + sb.charAt(i));
			sb.deleteCharAt(i);
		}
	}

	/**
	 * Compute the best alignment for a given sequence.
	 * 
	 * @param sequence the candidate sequence
	 */
	private void determineTheBestAlignmentFromTheAlreadyAlignedSequencesFor(String sequence) {
		double max = Integer.MIN_VALUE;

		for (int k = 0; k < alreadyAligned.size(); k++) {
			// String element = alreadyAligned.get(k).toString();
			List<String> alignment = getPairAlignmentAlgorithm().align(sequence, alreadyAligned.get(k).toString());
			// TODO think about the case < or <=
			if ((!alignment.get(0).toString().equals(alignment.get(1).toString()))
					&& max <= getPairAlignmentAlgorithm().getScore(alignment.get(0), alignment.get(1))) {
				max = getPairAlignmentAlgorithm().getScore(alignment.get(0), alignment.get(1));
				originalalignedSequence = alreadyAligned.get(k).toString();
				alignedSource = new StringBuffer(alignment.get(0));
				alignedTarget = new StringBuffer(alignment.get(1));
			}
		}

		filteringSet.remove(originalalignedSequence);
		filteringSet.add(alignedSource.toString());
		filteringSet.add(alignedTarget.toString());
		alreadyAligned.clear();
		for (String stre : filteringSet)
			alreadyAligned.add(new StringBuffer(stre));

		if (filteringSet.contains(alignedTarget.toString()))
			sequenceToPadd = alignedTarget.toString();
		else if (filteringSet.contains(alignedSource.toString()))
			sequenceToPadd = alignedSource.toString();

	}

	private void initialize(List<String> sequences) {
		alreadyAligned = new ArrayList<StringBuffer>();
		source = null;
		target = null;
		alignedSource = null;
		alignedTarget = null;
		filteringSet = new HashSet<String>();
		sequenceToPadd = "";
		originalalignedSequence = "";

		this.setSequences(sequences);
		alignAllTheSequencesAndSearchTheMaxScore(getSequences());
		removeAlignedSequences(getSequences());

		alreadyAligned.add(alignedSource);
		alreadyAligned.add(alignedTarget);
		filteringSet.add(alignedSource.toString());
		filteringSet.add(alignedTarget.toString());
	}

	/**
	 * Determine the best alignment for initialized <code>alignedSource</code>.
	 * 
	 * @param sequences
	 *            the sequences to be aligned.
	 */
	private void alignAllTheSequencesAndSearchTheMaxScore(List<String> sequences) {
		double max = Integer.MIN_VALUE;
		for (int i = 0; i < sequences.size() - 1; i++) {
			for (int j = i + 1; j < sequences.size(); j++) {

				List<String> result = getPairAlignmentAlgorithm().align(sequences.get(i), sequences.get(j));

				// Search the max alignment
				if (max <= getPairAlignmentAlgorithm().getScore(result.get(0), result.get(1))) {
					source = sequences.get(i);
					target = sequences.get(j);
					max = getPairAlignmentAlgorithm().getScore(result.get(0), result.get(1));
					alignedSource = new StringBuffer(result.get(0));
					alignedTarget = new StringBuffer(result.get(1));

				}
			}
		}

	}

	/**
	 * Remove the two sequences which were selected for the alignment.
	 * 
	 * @param sequences
	 *            the original list of sequences.
	 */
	private void removeAlignedSequences(List<String> sequences) {
		if (source != null && target != null) {
			sequences.remove(source);
			sequences.remove(target);
		}
	}

	@Override
	public Set<String> process(List<String> sequences) {
		return amaa(sequences);
	}

	public Set<String> align(List<String> sequences) {
		this.setSequences(sequences);
		return amaa(sequences);
	}

}
