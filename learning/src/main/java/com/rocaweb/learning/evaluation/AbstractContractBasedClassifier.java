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
package com.rocaweb.learning.evaluation;

import java.util.Set;

import com.aliasi.classify.PrecisionRecallEvaluation;
import com.aliasi.cluster.ClusterScore;
import com.google.common.collect.Sets;

/**
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 *
 *
 * @param <T>
 *            The type used for the learning process.
 * @param <E>
 *            The generated type of the algorithm.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractContractBasedClassifier<T, E> extends AbstractEvaluation<E> {

	/** The test set */
	private Set<Set<T>> referenceDataSet = null;

	/** The data set holding the partitioning of the data */
	private Set<Set<T>> learnedDataSet = null;

	/** The model learned by the algorithm */
	protected E model = null;

	/** Holds the statistics of the learning */
	private ClusterScore clusterScore = null;

	private String classification = "";

	/**
	 * Creates an instance of this class using :
	 * 
	 * @param normalDataSet
	 *            for the normal data set.
	 * @param abnormal
	 *            for the abnormal data set.
	 */
	public AbstractContractBasedClassifier(Set<T> normalDataSet, Set<T> abnormal) {
		this.referenceDataSet = Sets.newHashSet();
		this.referenceDataSet.add(normalDataSet);
		this.referenceDataSet.add(abnormal);
	}

	public AbstractContractBasedClassifier(Set<T> normal, Set<T> abnormal, E model) {
		this(normal, abnormal);
		this.model = model;
	}

	/**
	 * Apply the model on the learning on the test data set.
	 * 
	 * @param t
	 *            the model
	 * 
	 */
	protected abstract boolean matches(T t);

	@Override
	public double getFMeasure() {

		return this.getSC().equivalenceEvaluation().fMeasure();
	}

	@Override
	public double getAccuracy() {
		return this.getSC().equivalenceEvaluation().accuracy();
	}

	@Override
	public double getFPRate() {
		return getPRE().falsePositive() / (1.0 * this.getPRE().negativeReference());
	}

	@Override
	public double getTPRate() {
		return getPRE().truePositive() / (1.0 * this.getPRE().positiveReference());
	}

	@Override
	public double getFNRate() {
		return 1.0 - this.getTPRate();
	}

	@Override
	public double getTNRate() {
		return 1.0 - this.getFPRate();
	}

	private PrecisionRecallEvaluation getPRE() {
		return this.getSC().equivalenceEvaluation();
	}

	/**
	 * Create a data set based on the model.
	 * 
	 * @return learnedDataSet.
	 */
	private Set<Set<T>> getLearnedDataSet() {
		Set<T> normal = Sets.newHashSet();
		Set<T> abNormal = Sets.newHashSet();
		learnedDataSet = Sets.newHashSet();
		for (Set<T> set : this.getReferenceData()) {
			for (T t : set) {
				if (this.matches(t))
					normal.add(t);
				else
					abNormal.add(t);
			}
		}

		learnedDataSet.add(normal);

		learnedDataSet.add(abNormal);
		System.out.println("\t\tClassification results\t\t");
		System.out.println("Normal :  " + normal);
		System.out.println("Abnormal : " + abNormal);
		classification += toCSV(normal, "normal");
		classification += toCSV(abNormal, "abnormal");
		return learnedDataSet;
	}

	/**
	 * @param normal
	 * @param string
	 * @return
	 */
	private String toCSV(Set<T> normal, String string) {
		String result = "";
		for (T t : normal)
			result += t.toString() + "," + string + "\n";
		return result;
	}

	public void setPattern(E pattern) {

		this.model = pattern;
	}

	private Set<Set<T>> getReferenceData() {
		if (referenceDataSet == null) {
			referenceDataSet = Sets.newHashSet();
		}

		return referenceDataSet;
	}

	private ClusterScore getSC() {
		if (clusterScore == null) {
			clusterScore = new ClusterScore(this.getReferenceData(), this.getLearnedDataSet());
		}
		return clusterScore;
	}

	/**
	 * @see com.rocaweb.learning.evaluation.AbstractEvaluation#getClassification()
	 */
	@Override
	public String getClassification() {

		return classification;
	}

}
