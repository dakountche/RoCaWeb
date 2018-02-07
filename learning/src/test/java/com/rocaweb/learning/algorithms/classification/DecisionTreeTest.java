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
package com.rocaweb.learning.algorithms.classification;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.rocaweb.learning.alignment.InputAlignments;

import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class DecisionTreeTest {

	@Test
	public void test() throws Exception {
		DecisionTree dt = new DecisionTree();
		// dt.eval();
		System.out.println(dt.createInstancesFromDefaultSet().toSummaryString());
	}

	@Test
	public void testInststancesSize() {
		List<String> sequences = Lists.newArrayList(InputAlignments.garfield);
		DecisionTree dt = new DecisionTree();
		Instances instances = dt.createInstancesAndLoadDefaultNegativeSet(sequences, "normal");
		// 5 instances were in the negative data set
		// TODO read the the default test file and add the correct size
		assertEquals(instances.size(), sequences.size() + 5);
	}

	@Test
	public void testProcess() {
		List<String> sequences = Lists.newArrayList(InputAlignments.garfield);
		DecisionTree dt = new DecisionTree();
		Classifier classifier = dt.process(sequences);
		System.out.println(classifier.getCapabilities());
	}

	@Test
	public void testCreateTestSet() {

	}

}
