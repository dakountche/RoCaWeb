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

package com.rocaweb.learning.algorithms.clustering;

import com.rocaweb.learning.algorithms.AbstractELKIAlgorithm;

import de.lmu.ifi.dbs.elki.algorithm.clustering.DBSCAN;

/**
 * 
 * @author Djibrilla Amadou Kountche
 *
 */
public class DBSCANClusterer extends AbstractELKIAlgorithm {

	private DBSCAN<String> dbscan = null;

	/**
	 * @return the dbscan
	 */
	public DBSCAN<String> getDbscan() {
		return dbscan;
	}

	/**
	 * @param dbscan
	 *            the dbscan to set
	 */
	public void setDbscan(DBSCAN<String> dbscan) {
		this.dbscan = dbscan;
	}

}
