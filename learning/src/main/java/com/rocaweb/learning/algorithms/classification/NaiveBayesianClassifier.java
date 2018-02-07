/** 
* This file is part of RoCaWeb.
*
* RoCaWeb is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package com.rocaweb.learning.algorithms.classification;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;

/**
 * 
 * Application of the naive Bayesian classifier to the intrusion detection
 * problem.
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class NaiveBayesianClassifier extends AbstractWekaClassificationAlgorithm {

	private NaiveBayes bayes = null;

	
	@Override
	public Classifier getClassifier() {
		if (bayes == null) {
			bayes = new NaiveBayes();
		}
		return bayes;
	}
}
