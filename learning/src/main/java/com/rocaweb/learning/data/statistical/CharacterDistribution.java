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
package com.rocaweb.learning.data.statistical;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.Frequency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

/**
 * Represents a character distribution.
 * 
 * @author Djibrila Amadou Kountche
 * 
 * @since 1.0.0
 */

public class CharacterDistribution {

	private Frequency frequency;

	private String sequence;

	private static Logger logger = LogManager.getLogger(CharacterDistribution.class);

	public CharacterDistribution() {

	}

	public CharacterDistribution(String sequence) {
		setSequence(sequence);
		initialize();
	}

	public Frequency getFrequency() {
		if (frequency == null) {
			frequency = new Frequency();
		}
		return frequency;
	}

	private void initialize() {
		setFrequency(createFrequency());
	}

	public void setFrequency(Frequency frequency) {

		this.frequency = frequency;

	}

	public static Frequency createFrequency(String chaine) {
		Frequency frequency = new Frequency();
		for (char a : chaine.toCharArray())
			frequency.addValue(a);

		return frequency;
	}

	public Frequency createFrequency() {
		return createFrequency(getSequence());
	}

	public void setSequence(String chaine) {
		sequence = chaine;

	}

	public String getSequence() {
		return sequence;
	}

	public void addToMap(Frequency map) {
		getFrequency().merge(map);
	}

	public static List<Double> getValues(Frequency frequency, int k) {

		List<Entry<Comparable<?>, Long>> entries = getEntries(frequency);
		List<Double> values = Lists.newArrayList();
		logger.trace("K value " + k);
		for (Entry<Comparable<?>, Long> entry : entries) {
			values.add(frequency.getPct(entry.getKey()) / k);
		}
		Collections.sort(values);
		Collections.reverse(values);
		logger.trace(values);

		return values;
	}

	private static List<Entry<Comparable<?>, Long>> getEntries(Frequency frequency) {
		return Lists.newArrayList(frequency.entrySetIterator());
	}

	public List<Double> getValues() {
		return getValues(this.getFrequency(), 1);
	}

	public String toString() {
		StringBuilder chaine = new StringBuilder();
		for (Double value : this.getValues()) {
			if (chaine.toString().isEmpty())
				chaine.append("" + value);
			else
				chaine.append("," + value);
		}

		return chaine.toString();
	}

	public static double[] getArraytValues(Frequency frequency) {

		List<Entry<Comparable<?>, Long>> entries = getEntries(frequency);

		double[] values = new double[entries.size()];
		int i = 0;
		for (Entry<Comparable<?>, Long> entry : entries) {
			values[i++] = entry.getValue();
		}

		return values;
	}

}
