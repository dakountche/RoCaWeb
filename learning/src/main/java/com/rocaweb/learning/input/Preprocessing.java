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
package com.rocaweb.learning.input;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.rocaweb.commons.fs.FSManager;

import weka.core.converters.ConverterUtils.DataSource;

/**
 * This class contains certain utilities for file analysis. The aim is to
 * determine statistics of characters, words, and strings presents in a given
 * file. For sequence alignment algorithms, it permit to determines the alphabet
 * and the importance to give for certain characters.
 * 
 * In this class, we also put some clustering algorithms. The goal of clustering
 * is to break in input stream down , into the "natural" clusters and determine
 * regular expressions or rules appropriate for this input stream.
 * 
 * @author Djibrilla Amadou Kountche
 * @since 1.0.0
 * 
 */
// TODO for the alignment replace a separator if many type are present in the
// data
public final class Preprocessing {

	private static final String STARTLINE = "#";

	private Logger logger = LogManager.getLogger(Preprocessing.class);

	/** The inputSet */
	private Set<String> inputSet = null;

	/** The alphabet is the characters observed during the file scanning */
	private Set<Character> alphabet = null;

	/**
	 * Maps containing the characters and their occurrences observed before altering
	 * the file.
	 */
	private Map<Character, Integer> characterCount = null;

	public Preprocessing() {

	}

	/**
	 * reads from a given file and return
	 * 
	 * @param fileName
	 *            the name of the file.
	 * @return
	 * @throws IOException
	 */
	public List<String> readFile(final String fileName, boolean decode) throws IOException {
		logger.trace("ReadFile: " + fileName);
		logger.trace("ReadFile decode: " + decode);
		FileObject fo = FSManager.resolveFile(fileName);
		return readFile(fo, false, decode, "UTF-8", false);
	}

	/**
	 * Create a data source from a URL.
	 * 
	 * @param dataFile
	 *            containing the learning file.
	 * @return a data source
	 * @throws Exception
	 */
	public DataSource readDataSource(final String dataFile) throws Exception {
		return new DataSource(dataFile);
	}

	public Set<Character> getAlphabet() {
		if (alphabet == null) {
			alphabet = new HashSet<Character>();
		}
		return alphabet;
	}

	public void setInputSet(List<String> listOfSequence) {
		if (listOfSequence != null) {
			for (String string : listOfSequence) {
				getInputSet().add(string);
			}
		}
	}

	public Set<String> getInputSet() {
		if (inputSet == null) {
			inputSet = new HashSet<String>();
		}
		return inputSet;
	}

	public List<String> readFile(FileObject directory, boolean hasCleanning, boolean hasDecode, String base,
			boolean hasCasse) throws IOException {
		ArrayList<String> sequences = new ArrayList<String>();
		LineIterator lineIter = null;

		if (!hasDecode)
			base = "UTF-8"; // DEFAULT-Charset
		lineIter = IOUtils.lineIterator(directory.getContent().getInputStream(), base);

		while (lineIter.hasNext()) {
			String line = lineIter.nextLine();
			if (!line.startsWith(STARTLINE)) {

				if (hasCasse)
					line = line.toLowerCase();

				for (Character car : line.toCharArray()) {
					getAlphabet().add(car);
				}

				if (hasCleanning && sequences.contains(line))
					continue;
				else
					sequences.add(line);

			}
		}

		FSManager.close(directory);

		return sequences;
	}

	/**
	 * Detect if know attacks are not in the learning data set.
	 */
	public void cleanKnownAttacks() {}

	/**
	 * @return the characterCount
	 */
	public Map<Character, Integer> getCharacterCount() {
		return characterCount;
	}

	/**
	 * @param characterCount
	 *            the characterCount to set
	 */
	public void setCharacterCount(Map<Character, Integer> characterCount) {
		this.characterCount = characterCount;
	}

	/**
	 * TODO Change this methods to a take into account the diverse algorithm.
	 * 
	 * @param inputs
	 * @return
	 */
	public Set<Set<String>> createCluster(List<String> inputs) {

		return Sets.newHashSet();
	}

	public Set<Set<String>> createCluster(String inputFile) {
		return Sets.newHashSet();
	}

	/**
	 * @param sequences
	 * @param hasCleanning
	 * @param hasDecode
	 * @param base
	 * @param hasCasse
	 * @return
	 */
	public List<String> filter(final List<String> sequences, boolean hasCleanning, boolean hasDecode, String base,
			boolean hasCasse) {
		List<String> filterSequences = Lists.newArrayList();

		for (String string : sequences) {
			if (hasCleanning) {
				if (filterSequences.contains(string))
					continue;
			}

			if (hasDecode) {
				string = Charset.forName(base).encode(string).toString();
			}

			if (hasCasse) {
				string = string.toLowerCase();
			}
			filterSequences.add(string);
		}

		return filterSequences;
	}

}
