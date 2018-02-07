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
package com.rocaweb.learning.statistical.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.rocaweb.learning.algorithms.grammatical.TokenFinder;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class TokenFinderTest {

	String[] pays = { "France", "Allemagne", "Autriche", "Allemagne", "Niger", "France", "France", "Chili", "Allemagne",
			"Niger", "France" };
	TokenFinder tf = new TokenFinder(Lists.newArrayList(pays));

	@Test
	public void test() {
		System.out.println(tf.correlate());
	}

	@Test
	public void testTokenSize() {
		assertEquals(tf.getEnumeration().size(), 5);
		System.out.println(tf.getEnumeration());
	}

}
