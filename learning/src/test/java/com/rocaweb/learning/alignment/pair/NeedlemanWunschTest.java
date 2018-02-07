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
package com.rocaweb.learning.alignment.pair;

import org.junit.Test;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class NeedlemanWunschTest extends GlobalAlignmentTest {

	@Test
	public void testBeginingOfFirstString() {
		this.getAlgorithm().setPairOfSequence(this.getPair());
		logger.info(getAlgorithm().beginningOfFirstString());
	}

	@Test
	public void testendOfFirstString() {
		this.getAlgorithm().setPairOfSequence(this.getPair());
		logger.info(getAlgorithm().endOfFirstString());
	}

	@Test
	public void testBegeginingOfSecondString() {
		this.getAlgorithm().setPairOfSequence(this.getPair());
		logger.info(getAlgorithm().beginningOfSecondSequence());
	}

	@Test
	public void testendOfSecondString() {
		this.getAlgorithm().setPairOfSequence(this.getPair());
		logger.info(getAlgorithm().endOfSecondSequence());
	}
}
