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
package com.rocaweb.learning.data.statistical;

import java.util.List;

import org.apache.commons.math3.stat.Frequency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public abstract class AbstractDistribution implements Validation {

	private List<String> sequences;
	private CharacterDistribution characterDistribution;
	protected Logger logger = LogManager.getLogger(getClass());
	protected double alpha = 0.05;

	public AbstractDistribution() {

	}

	public AbstractDistribution(List<String> sequences) {
		setSequences(sequences);
		initialize(sequences);

	}

	private void initialize(List<String> sequences) {
		if (sequences != null) {

			for (String chaine : sequences) {
				Frequency map = CharacterDistribution.createFrequency(chaine);
				logger.trace(map.toString());
				getCharacterDistribution().addToMap(map);
			}

		}
	}

	public void setSequences(List<String> sequences) {
		this.sequences = sequences;
	}

	public Frequency getFrequency() {

		return getCharacterDistribution().getFrequency();
	}

	public List<String> getSequences() {

		return sequences;
	}

	public CharacterDistribution getCharacterDistribution() {
		if (characterDistribution == null) {
			characterDistribution = new CharacterDistribution();
		}
		return characterDistribution;
	}

	/**
	 * @return
	 */
	public double getAlpha() {

		return alpha;
	}

}
