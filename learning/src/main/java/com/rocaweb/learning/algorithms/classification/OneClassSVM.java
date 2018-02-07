/** 
* This file is part of RoCaWeb.
*
* RoCaWeb is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package com.rocaweb.learning.algorithms.classification;

import com.rocaweb.learning.algorithms.AbstractELKIAlgorithm;

import de.lmu.ifi.dbs.elki.algorithm.outlier.svm.LibSVMOneClassOutlierDetection;
import de.lmu.ifi.dbs.elki.data.DoubleVector;

/**
 * 
 * Implement the one class SVM algorithm for outlier detection.
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */
public class OneClassSVM extends AbstractELKIAlgorithm {

	private LibSVMOneClassOutlierDetection<DoubleVector> oneClassSVM = null;

	/**
	 * @return the oneClassSVM
	 */
	public LibSVMOneClassOutlierDetection<DoubleVector> getOneClassSVM() {
		return oneClassSVM;
	}

	/**
	 * @param oneClassSVM
	 *            the oneClassSVM to set
	 */
	public void setOneClassSVM(LibSVMOneClassOutlierDetection<DoubleVector> oneClassSVM) {
		this.oneClassSVM = oneClassSVM;
	}

}
