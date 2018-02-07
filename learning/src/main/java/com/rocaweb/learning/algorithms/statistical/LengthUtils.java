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
package com.rocaweb.learning.algorithms.statistical;

import java.util.List;

import com.rocaweb.learning.data.statistical.AbstractLengthStatistics;
import com.rocaweb.learning.validation.statistical.Chebychev;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class LengthUtils {

	private static AttributeLength al = new AttributeLength();

	public static double determineBestThreshold(List<String> sequences) {
		double min = Double.MAX_VALUE;

		al.setSequences(sequences);
		AbstractLengthStatistics ls = al.calculateMeanAndStd(al.getSequences());
		for (String sequence : al.getSequences()) {
			double current = sequence.length();
			double value = Chebychev.getAnomalyProbability(ls.getMean(), ls.getStandarddeviation(), current);
			min = Double.min(min, value);
		}

		return min;
	}

}
