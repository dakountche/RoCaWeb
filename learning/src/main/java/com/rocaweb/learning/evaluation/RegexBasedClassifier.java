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
package com.rocaweb.learning.evaluation;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class RegexBasedClassifier extends AbstractContractBasedClassifier<String, String> {

	private Pattern p = null;

	public RegexBasedClassifier(Set<String> inputs, Set<String> attacks, String pattern) {
		super(inputs, attacks, pattern);

	}

	public RegexBasedClassifier(Set<String> inputs, Set<String> attacks) {
		super(inputs, attacks);
	}

	@Override
	protected boolean matches(String t) {

		return this.matching(t);
	}

	private boolean matching(String t) {
		Matcher m = getP().matcher(t);
		return m.matches();
	}

	private Pattern getP() {
		p = Pattern.compile(getPattern());
		return p;
	}

	public void setPattern(String pattern) {
		this.model = pattern;
	}

	public String getPattern() {
		return model;
	}

}
