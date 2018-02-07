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
package com.rocaweb.learning.parameters;

/**
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public abstract class DefaultLearningAParameters {

	public static final String DEFAULT_CONFIG_FILE = "META-INF/algorithms-config.xml";

	private static AlgorithmParameters defaultAlgorithmParams = null;
	private static AlignmentAParameters defaultAlignmentParams = null;
	private static MultipleAlignmentParameters defaultMultipleAlignmentParams = null;
	private static TypingParameters defaultTypingParams = null;
	private static CrossValidationParam defaultCrossValidationParams = null;
	private static AttributeLengthParameters defaultAttributeLengthParams = null;

	public static MultipleAlignmentParameters getDefaultMultipleAlignmentParameters() {
		if (defaultMultipleAlignmentParams == null) {
			defaultMultipleAlignmentParams = new MultipleAlignmentParameters(DEFAULT_CONFIG_FILE);
		}
		return defaultMultipleAlignmentParams;
	}

	/**
	 * @return
	 */
	public static AlignmentAParameters getDefaultAlignmentParams() {
		if (defaultAlignmentParams == null) {
			defaultAlignmentParams = new AlignmentAParameters(DEFAULT_CONFIG_FILE);
		}
		return defaultAlignmentParams;
	}

	public static TypingParameters getDefaultTypingParameters() {
		if (defaultTypingParams == null) {
			defaultTypingParams = new TypingParameters(DEFAULT_CONFIG_FILE);
		}
		return defaultTypingParams;

	}

	/**
	 * @return the defaultAlgorithmParams
	 */
	public static AlgorithmParameters getDefaultAlgorithmParams() {

		if (defaultAlgorithmParams == null) {
			defaultAlgorithmParams = new AlgorithmParameters(DEFAULT_CONFIG_FILE);
		}
		return defaultAlgorithmParams;
	}

	/**
	 * @return the defaultCrossValidationParams
	 */
	public static CrossValidationParam getDefaultCrossValidationParams() {

		if (defaultCrossValidationParams == null) {
			defaultCrossValidationParams = new CrossValidationParam(DEFAULT_CONFIG_FILE);
		}
		return defaultCrossValidationParams;
	}

	/**
	 * @return the defaultAttributeLengthParams
	 */
	public static AttributeLengthParameters getDefaultAttributeLengthParams() {

		if (defaultAttributeLengthParams == null) {
			defaultAttributeLengthParams = new AttributeLengthParameters(DEFAULT_CONFIG_FILE);
		}
		return defaultAttributeLengthParams;
	}

}
