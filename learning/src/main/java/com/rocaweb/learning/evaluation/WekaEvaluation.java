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
package com.rocaweb.learning.evaluation;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * Evaluates a Weka algorithm.
 * 
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */

public class WekaEvaluation extends AbstractEvaluation<Classifier> {

	private Evaluation evaluation = null;

	private Instances testIns = null;

	private Instances learningtIns = null;

	private Classifier model = null;

	/**
	 * Default constructor.
	 */
	public WekaEvaluation() {

	}

	/**
	 * Default constructor.
	 */
	public WekaEvaluation(Instances learnIns, Instances testIns) {

		this.learningtIns = learnIns;
		this.testIns = testIns;
	}

	public Evaluation getEvaluation() {
		try {
			evaluation = new Evaluation(learningtIns);
			evaluation.evaluateModel(getPattern(), this.getTestIns());

		} catch (Exception e) {

			e.printStackTrace();
		}

		return evaluation;
	}

	/**
	 * @return
	 */
	private Instances getTestIns() {

		return testIns;
	}

	@Override
	public double getAccuracy() {

		return this.getEvaluation().correct();
	}

	@Override
	public double getFPRate() {

		return this.getEvaluation().weightedFalsePositiveRate();
	}

	@Override
	public double getFNRate() {

		return this.getEvaluation().weightedFalseNegativeRate();
	}

	@Override
	public double getTPRate() {

		return this.getEvaluation().weightedTruePositiveRate();
	}

	@Override
	public double getFMeasure() {

		return this.getEvaluation().weightedFMeasure();
	}

	@Override
	public double getTNRate() {

		return this.getEvaluation().weightedTrueNegativeRate();
	}

	@Override
	public void setPattern(Classifier model) {

		this.model = model;
	}

	public Classifier getPattern() {
		return model;
	}

	/**
	 * @see com.rocaweb.learning.evaluation.AbstractEvaluation#getClassification()
	 */
	@Override
	public String getClassification() {
		// TODO Auto-generated method stub
		return null;
	}

}
