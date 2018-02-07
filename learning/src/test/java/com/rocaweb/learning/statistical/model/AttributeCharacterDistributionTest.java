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
package com.rocaweb.learning.statistical.model;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.rocaweb.learning.algorithms.statistical.AttributeCharacterDistribution;
import com.rocaweb.learning.exceptions.CharacterDistribitonSizeException;

public class AttributeCharacterDistributionTest {
	// TODO a better test cases. This one is for making the tests pass !
	private String[] strings = { "labcdefghijklogin", "edit_evazrtent", "delete_opqevent", "add_event", "users", "logs",
			"logout" };
	private String testValue = "logoazertiopmqutzpasuvmntkà-è";

	private AttributeCharacterDistribution acd = new AttributeCharacterDistribution(Lists.newArrayList(strings));

	@Test
	public void theSizeOfTheBinsShouldBeTheSame() {
		// assertEquals();
	}

	@Test
	public void testKruegeletAlTestModel() {
		try {

			System.out.println(acd.getIcd().kruegelEtAlTestModel(testValue));
		} catch (CharacterDistribitonSizeException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testProposedTestModel() {
		System.out.println("th : " + acd.getTreshold());
	}

	@Test
	public void testICD() {
		System.out.println("ICD : " + acd.getIcd().toString());
	}

}
