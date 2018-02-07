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

import java.util.Locale;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public abstract class AbstractEvaluation<E> {

	private String csvFormat = "%s,%s,%s,%s";

	/**
	 * Determines the accuracy.
	 * 
	 * @return the accuracy.
	 */
	public abstract double getAccuracy();

	/**
	 * Determines the false positive rate.
	 * 
	 * @return
	 */
	public abstract double getFPRate();

	/**
	 * Determines the false negative rate.
	 * 
	 * @return
	 */
	public abstract double getFNRate();

	/**
	 * Determines the true positive rate.
	 * 
	 * @return
	 */
	public abstract double getTPRate();

	/**
	 * Determines the value of the F-Measure.
	 * 
	 * @return F1 measure.
	 */
	public abstract double getFMeasure();

	/**
	 * Determines the true negative rate.
	 * 
	 * @return
	 */
	public abstract double getTNRate();

	/**
	 * Creates a ROC curve.
	 */
	public void getROC() {

	}

	/**
	 * Write the evaluation to a CSV file.
	 */
	public void toCSV() {

	}

	/**
	 * Assign the learned model
	 * 
	 * @param model
	 */
	public abstract void setPattern(E model);

	@Override
	public String toString() {

		return String.format(csvFormat, format(this.getFPRate()), format(this.getFNRate()), format(this.getTNRate()),
				format(this.getTPRate()));
	}

	/**
	 * Format a double value for the CSV file
	 * 
	 * @param value
	 * @return
	 */
	private String format(double value) {
		return String.format(Locale.US, "%.2f", value);
	}

	/**
	 * @return
	 */
	public abstract String getClassification();

}
