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
package com.rocaweb.learning.rules;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.rocaweb.learning.algorithms.statistical.AttributeCharacterDistribution;
import com.rocaweb.learning.rules.format.ModSecurityFormat;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
@SuppressWarnings("rawtypes")
public class StatisticTest {

	String learningPathName = (new File("src/test/resources/rapport/countries/training")).getAbsolutePath();
	String learningFileName = "case1";

	@Test
	public void test() {

		AttributeCharacterDistribution al = new AttributeCharacterDistribution();
		al.setDefaultParameters();
		al.getParameters().setLearningPathName(learningPathName);
		al.getParameters().setLearningFile(learningFileName);
		al.getParameters().setRuleType("ModSecurity");
		try {

			al.call();
			// al.runAlgorithmAndGenerateContract(learningFileName);
			// Contract<LengthStatistics> stats = al.getContract();

			// al.runAlgorithmAndGenerateContract(learningFileName);
			Contract stats = al.getContract();

			ModSecurityFormat msf = new ModSecurityFormat();

			System.out.print(msf.format(stats));

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
