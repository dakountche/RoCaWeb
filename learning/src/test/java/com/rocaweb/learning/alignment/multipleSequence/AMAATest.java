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
package com.rocaweb.learning.alignment.multipleSequence;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.rocaweb.learning.algorithms.alignment.multipleSequence.AMAA;
import com.rocaweb.learning.alignment.InputAlignments;
import com.rocaweb.learning.parameters.DefaultLearningAParameters;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class AMAATest {

	AMAA amaa = new AMAA("src/test/resources/json/amaa.json");

	@Test
	public void testamaa() {

		List<String> sequences = Lists.newArrayList(InputAlignments.garfield);
		Set<String> alignedSequences = amaa.process(sequences);
		assertEquals(sequences.size(), alignedSequences.size());
		System.out.println(alignedSequences);
	}

	@Test
	public void testDifferentParameters() {
		AMAA other = new AMAA(DefaultLearningAParameters.DEFAULT_CONFIG_FILE);
		List<String> sequences = Lists.newArrayList(InputAlignments.garfield);
		Set<String> source = amaa.process(sequences);
		Set<String> target = other.align(sequences);

		assertEquals(source, target);
		System.out.println(source);
		System.out.println(target);

	}

}
