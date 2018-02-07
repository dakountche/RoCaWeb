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

package com.rocaweb.learning.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;
import com.rocaweb.learning.parameters.WekaParameters;
import com.rocaweb.learning.rules.Contract;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * 
 * Represent all the Weka algorithms. In this class we implements the common
 * methods used by these algorithms for data processing (programmatically
 * creates the instances).
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @see http://www.cs.waikato.ac.nz/ml/weka/
 * 
 * @since 1.0.0
 *
 */
public abstract class AbstractWekaAlgorithm extends Algorithm<Classifier> {

	/**
	 * Parameters of a Weka algorithm
	 */
	protected WekaParameters parameters = null;

	/** The learning data set */
	protected DataSource dataSource = null;

	/** The training set */
	protected Instances trainingSet = null;

	/** Options of the algorithm */
	public String[] getOptions() {
		return getParameters().getOptions();
	}


	/**
	 * Create an <code>Instance</code> based on the the learning file. In this case
	 * the learning file should contain negative example if necessary. When these
	 * negative example are not available in the learning file, then this method
	 * will use the default negative base.
	 * 
	 * @return the instances for training the classifier
	 */
	public Instances getInstances() {
		if (trainingSet == null) {
			try {
				trainingSet = this.getDataSource().getDataSet();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
		return trainingSet;
	}

	/**
	 * Filter the inputs. By default, we are considering the inputs to be
	 * <code>String</code>. Therefore, this method transforms them into numerical
	 * value.
	 * 
	 * @param trainingSet
	 *            instances read form the training file
	 * @return filtered instances
	 * @throws Exception
	 */
	protected Instances filter(Instances trainingSet) throws Exception {
		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(trainingSet);
		Instances dataFiltered = Filter.useFilter(trainingSet, filter);

		if (dataFiltered.classIndex() == -1)
			dataFiltered.setClassIndex(0);
		return dataFiltered;
	}

	/**
	 * Create the test instances given the test data set.
	 * 
	 * @return Instances read from the test file.
	 * @throws Exception
	 */
	public Instances getTestSet() throws Exception {
		DataSource testDataSource = createDataSource(this.getParameters().getTestFile());
		Instances test = testDataSource.getDataSet();
		return filter(test);
	}

	/**
	 * Create a data source. This method can read as well as Weka ARFF files as CSV
	 * file.
	 * 
	 * @return a data source.
	 */
	public DataSource getDataSource() {
		if (dataSource == null) {
			try {
				dataSource = createDataSource(getParameters().getLearningFile());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dataSource;
	}

	private DataSource createDataSource(String dataSourceFile) throws Exception {
		return new DataSource(dataSourceFile);
	}

	/**
	 * Set the options of the algorithm.
	 * 
	 * @param options
	 */
	public void setOptions() {
		try {
			// getClassifier().setOptions(getOptions());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a<code>String</code> attribute
	 * 
	 * @param name
	 * @return
	 */
	private Attribute createAttribute(String name) {
		List<String> values = null;
		return new Attribute(name, values);
	}

	/**
	 * Create an attribute for the class
	 * 
	 * @return an attribute
	 */
	private Attribute createClassAttribute() {
		List<String> classAttributeValues = Lists.newArrayList();
		classAttributeValues.add(this.getParameters().getPositiveClassName());
		classAttributeValues.add(this.getParameters().getNegativeClassName());

		return new Attribute("class", classAttributeValues);
	}

	/**
	 * Create the attributes for the instance.
	 * 
	 * @param name
	 *            the name of the other attribute
	 * 
	 * @return a <code>List</code> of Attributes.
	 */
	public ArrayList<Attribute> createAttributes(String name) {

		ArrayList<Attribute> attributes = Lists.newArrayList();
		attributes.add(createAttribute(name));
		attributes.add(createClassAttribute());

		return attributes;
	}

	/**
	 * Fill the learning base with normal values. These classifier are mostly one
	 * class
	 * 
	 * @param positive
	 *            the List of positive inputs.
	 * 
	 * @param className
	 *            the name of the attribute which values are given in positive
	 * 
	 * @return the training set.
	 * 
	 * 
	 */
	public Instances createInstancesFrom(List<String> positive, String className) {
		Instances trainingSet = null;
		trainingSet = createInstances("Learning", positive.size(), 1);
		fillInstances(trainingSet, positive, className);
		logger.trace("\n" + trainingSet.toSummaryString());
		return trainingSet;
	}

	public Instances createInstancesAndLoadDefaultNegativeSet(List<String> positive, String className) {
		Instances instances = this.createInstancesFrom(positive, className);
		if (this.getParameters().useDefaultNegativeSet()) {
			try {
				Instances defaultnegative = createInstancesFromDefaultSet();
				instances.addAll(defaultnegative);
				instances.setClassIndex(instances.numAttributes() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instances;
	}

	/**
	 * Create Instances from the default negative set.
	 * 
	 * @return instances
	 * @throws Exception
	 */
	public Instances createInstancesFromDefaultSet() throws Exception {
		Instances instances = createDataSource(getParameters().getDeafaultNegativeBase()).getDataSet();
		return instances;
	}

	/**
	 * Fill the learning set given as parameter.
	 * 
	 * @param learningSet
	 * @param inputs
	 *            list of new values
	 * @param className
	 *            the type of class
	 */
	private void fillInstances(Instances learningSet, List<String> inputs, String className) {
		for (String sequence : inputs) {
			Instance iExample = new SparseInstance(2);
			iExample.setValue((Attribute) getAttributesVector().get(0), sequence);
			iExample.setValue((Attribute) this.getAttributesVector().get(1), className);
			learningSet.add(iExample);
		}
	}

	/**
	 * 
	 * Create a test set given a positive and negative values.
	 * @param positive
	 * @param negative
	 * @return instances
	 */
	public Instances createTestSet(List<String> positive, List<String> negative) {
		Instances positivetest = createInstancesFrom(positive, "normal");
		Instances ntest = createInstancesFrom(negative, "abnormal");
		ntest.addAll(positivetest);
		ntest.setClassIndex(ntest.numAttributes() - 1);
		return ntest;
	}

	/**
	 * @param string
	 * @param fv
	 * @param size
	 * @param classIndex
	 * @return
	 */
	private Instances createInstances(String string, int size, int classIndex) {
		Instances isTrainingSet = new Instances(string, this.getAttributesVector(), size);
		isTrainingSet.setClassIndex(classIndex);
		return isTrainingSet;
	}

	private ArrayList<Attribute> getAttributesVector() {
		return this.createAttributes(this.getParameters().getName());
	}
	
	public abstract void train() throws Exception;
	
	/**
	 * @param learningIns
	 */
	public void setTrainingSet(Instances learningIns) {
		this.trainingSet = learningIns;
	}


	
	@Override
	public WekaParameters getParameters() {
		if (parameters == null) {
			parameters = new WekaParameters();
		}
		return parameters;
	}

	
	@Override
	public void setDefaultParameters() {}

	@Override
	public char getFormatChar() {
		return 'w';
	}

	@Override
	public void setParameters(AbstractAlgorithmParameters parameters) {}

	@Override
	public Contract<Classifier> getContract() {
		return null;
	}
}
