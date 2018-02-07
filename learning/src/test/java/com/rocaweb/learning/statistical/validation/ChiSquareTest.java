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
package com.rocaweb.learning.statistical.validation;

import org.junit.Test;

import com.rocaweb.learning.validation.statistical.ChiSquare;

public class ChiSquareTest {

	double[] expected = { 10, 10, 10, 10, 10, 10 };
	double[] observed = { 5, 8, 9, 8, 10, 20 };

	@Test
	public void test() {
		System.out.println("p-value " + ChiSquare.chiSquareTest(expected, observed));
		System.out.println("khi2 : " + ChiSquare.chiSquare(expected, observed));
	}

}
