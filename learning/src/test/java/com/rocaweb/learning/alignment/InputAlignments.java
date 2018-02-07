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
package com.rocaweb.learning.alignment;

public interface InputAlignments {
	char alignmentChar = '-';

	int[] weigths = { 88, 80, 100, 100, 100, 100 };

	String[] garfield = { "GARFIELD THE LAST FAST CAT", "GARFIELD THE FAST CAT", "GARFIELD THE VERY FAST CAT",
			"THE FAST CAT" };

	String[] dates = { "01/04/2015", "08/16/2013", "07/24/2014", "08/16/2013", "06/21/2015", "09/28/2013", "10/09/2013",
			"06/06/2015", "10/30/2014", "06/22/2015", "04/11/2014", "09/22/2013" };

	String[][] alignedGarfield = { { "GARFIELD THE LAST FAT CAT", "GARFIELD THE FAST CAT ---" },
			{ "GARFIELD THE LAST FA-T CAT", "GARFIELD THE VERY FAST CAT" },
			{ "GARFIELD THE LAST FAT CAT", "-------- THE ---- FAT CAT" },
			{ "GARFIELD THE ---- FAST CAT", "GARFIELD THE VERY FAST CAT" },
			{ "GARFIELD THE FAST CAT", "-------- THE FA-T CAT" },
			{ "GARFIELD THE VERY FAST CAT", "-------- THE ---- FA-T CAT" } };

}
