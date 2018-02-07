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
package com.rocaweb.learning.parameters;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class WekaParameters extends AlgorithmParameters {

	private String[] options = { "-U" };

	/** Default name of the attributes in the generated ARFF file */
	protected String name = "sequence";

	/** The label of the positive class */
	private String positiveClassName = "normal";

	/** The label of the negative class */
	private String negativeClassName = "abnormal";

	/** indicates whether to use the negative set */
	private boolean usedDefaultNegativeSet = true;

	/**
	 * Use this data base to set the negative values. As we are doing an
	 * specification based intrusion detection, only the positive value are
	 * available at learning phase. However to train certain weka algorithm, we need
	 * to specify negative values.
	 */
	private String deafaultNegativeBase = "src/test/resources/dataset/negative.csv";

	/** Indicates if the data set has to be filtered. true by default. */
	private boolean filtered = true;

	public WekaParameters() {

	}

	public String[] getOptions() {
		return this.options;
	}

	public boolean isFiltered() {
		return filtered;
	}

	public String getLearningFile() {
		return "src/test/resources/dataset/comment.arff";

	}

	public String getTestFile() {
		return "src/test/resources/dataset/test.arff";
	}

	public boolean useDefaultNegativeSet() {

		return usedDefaultNegativeSet;
	}

	/**
	 * @return the deafaultNegativeBase
	 */
	public String getDeafaultNegativeBase() {
		return deafaultNegativeBase;
	}

	/**
	 * @param deafaultNegativeBase
	 *            the deafaultNegativeBase to set
	 */
	public void setDeafaultNegativeBase(String deafaultNegativeBase) {
		this.deafaultNegativeBase = deafaultNegativeBase;
	}

	/**
	 * @return the positiveClassName
	 */
	public String getPositiveClassName() {
		return positiveClassName;
	}

	/**
	 * @param positiveClassName
	 *            the positiveClassName to set
	 */
	public void setPositiveClassName(String positiveClassName) {
		this.positiveClassName = positiveClassName;
	}

	/**
	 * @return the negativeClassName
	 */
	public String getNegativeClassName() {
		return negativeClassName;
	}

	/**
	 * @param negativeClassName
	 *            the negativeClassName to set
	 */
	public void setNegativeClassName(String negativeClassName) {
		this.negativeClassName = negativeClassName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
