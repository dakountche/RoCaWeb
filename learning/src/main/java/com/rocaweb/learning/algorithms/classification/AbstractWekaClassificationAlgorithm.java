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

import java.util.List;

import com.rocaweb.learning.algorithms.AbstractWekaAlgorithm;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * Represent all the WEKA classification algorithm
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public abstract class AbstractWekaClassificationAlgorithm extends AbstractWekaAlgorithm {

	/**
	 * Train the classifier
	 * 
	 * @throws Exception
	 */
	public void train() throws Exception {
		Instances instances = this.getInstances();
		if (this.getParameters().isFiltered())
			instances = this.filter(instances);

		this.getClassifier().buildClassifier(instances);
	}


	/**
	 * Evaluate the classifier
	 */
	public void eval() {
		try {
			train();
			Evaluation eval = new Evaluation(getInstances());
			eval.evaluateModel(getClassifier(), getTestSet());
			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			System.out.println(eval.toClassDetailsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public Classifier process(List<String> sequences) {
		Instances learningIns = createInstancesAndLoadDefaultNegativeSet(sequences, "normal");
		try {
			this.setTrainingSet(learningIns);
			train();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return getClassifier();
	}
	
	public abstract Classifier getClassifier();

}
