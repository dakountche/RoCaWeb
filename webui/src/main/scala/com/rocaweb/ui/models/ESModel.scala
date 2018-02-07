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
package com.rocaweb.ui.models

import scala.collection.JavaConversions.`deprecated asScalaBuffer`

import org.apache.commons.vfs2.FileObject

import com.rocaweb.commons.fs.FSManager
import com.rocaweb.commons.recording.ElasticSearchConnector
import com.rocaweb.ui.controllers.RocawebConfiguration

import javax.inject.Inject
import play.api.Logger
import scala.collection.JavaConverters._

/**
 * Manages the integration of Elasticsearch in RoCaWeb
 * @author Alexandre CLERICI
 * @author Djibrilla AMADOU KOUNTCHE
 */
class ESModel @Inject() (configuration: play.api.Configuration) extends RocawebConfiguration(configuration) {

  /**
   * The Elasticsearch index type
   */
  val elasticindextype: String = getConfiguration("rocaweb.elastic.index.type")

  /**
   * The Elasticsearch cluster
   */
  val elasticcluster: String = getConfiguration("rocaweb.elastic.cluster")

  /**
   * The search pattern for ModSecurity logs
   */
  val elasticpattern: String = getConfiguration("rocaweb.elastic.pattern")

  /**
   * Lists the available indexes in Elasticsearch
   */
  def listindex(): List[String] = {
    val esc: ElasticSearchConnector = new ElasticSearchConnector(elastichost, elasticport.toInt, elasticcluster, elasticindextype);
    val list: List[String] = esc.getIndexFromPattern(elasticpattern).toList
    esc.close()
    list
  }

  /**
   * Create a learning data set from the Elasticsearch index
   */
  def clusterizeESInputinVFS(index: String) = {

    val esc: ElasticSearchConnector = new ElasticSearchConnector(elastichost, elasticport.toInt, elasticcluster, elasticindextype);

    var newfold: String = "file:///" + rocaweb.getAbsolutepath(clusteringdir);

    Logger.debug(s"Starting clusterizing in folder $newfold")

    var fo: FileObject = FSManager.resolveFile(newfold);

    esc.parseByUrlBySiteFromES(index, fo);

    FSManager.close(fo)

    esc.close();

    Logger.debug(s"Finished clusterizing $index")

  }

}
