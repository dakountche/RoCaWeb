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

import org.junit.Test;

import com.google.common.collect.Lists;
import com.rocaweb.learning.algorithms.statistical.AttributeLength;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class AttributeLengthTest {

	private String[] strings = { "labcdefghijklogin", "edit_evazrtent", "delete_opqevent", "add_event", "users", "logs",
			"logout" };

	@Test
	public void test() {
		AttributeLength al = new AttributeLength();
		al.setSequences(Lists.newArrayList(strings));

		System.out.println(al.determineTreshold());
	}

}
