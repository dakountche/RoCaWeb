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

package com.rocaweb.ui.controllers

import com.rocaweb.ui.models.Rocaweb
import com.rocaweb.commons.fs.FSManager

/**
 * Manages the configuration variables of RoCaWeb
 * @author Djibrilla AMADOU KOUNTCHE
 *
 */
class RocawebConfiguration(configuration: play.api.Configuration) {

  /**
   * Gives access to the current configuration of the application
   * read from conf/application.conf
   */
  val config: play.api.Configuration = configuration

  /**
   * The Elasticsearch host
   */
  val elastichost: String = getConfiguration("rocaweb.elastic.host")

  /**
   * The listening port of the Elasticsearch server
   * Default is 9300
   */
  val elasticport: String = getConfiguration("rocaweb.elastic.port")

  /**
   * The RoCaWeb agent hostname
   */
  val agenthost: String = getConfiguration("rocaweb.agent.host")

  /**
   * The port on which the traffic is reversed by the agent
   */
  val agentport: String = getConfiguration("rocaweb.agent.port")

  /**
   * The hostname of the Kibana server
   */
  val kibanahost: String = getConfiguration("rocaweb.kibana.host")

  /**
   * The listening port of the Kibana server
   */
  val kibanaport: String = getConfiguration("rocaweb.kibana.port")

  /**
   * The working directory of RoCaWeb
   */
  val rocawebworkingdir: String = getConfiguration("rocaweb.ui.workingdir");

  /**
   * Directory to store the results of the PDML processor
   */
  val clusteringdir: String = rocawebworkingdir + "/clusterized/"

  /**
   * A backup folder of the processed PDML's
   */
  val pdmldir: String = rocawebworkingdir + "/pdml/"

  val profiledir: String = rocawebworkingdir + "/profile/"

  /**
   * Get the configuration from conf/application.conf
   * for the key configName
   * @param configName: the key to search for
   */
  def getConfiguration(configName: String): String = {
    return config.underlying.getString(configName)
  }

  def initFolders(): Boolean = {

    FSManager.createFolder(clusteringdir)
    FSManager.createFolder(pdmldir)
    FSManager.createFolder(profiledir)

    true
  }

  var rocaweb = new Rocaweb(rocawebworkingdir)
  val init = initFolders();
}

