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

import javax.inject._
import play.api._
import play.api.mvc._
import com.rocaweb.ui.models._
import play.mvc.Http.Request._

/**
 * A Generic Template used by all RoCaWeb Controllers
 *
 */
class RocawebController @Inject() (var cc: ControllerComponents, var configuration: play.api.Configuration, var environment: play.api.Environment) extends AbstractController(cc) {

  /**
   * Gives access to the current configuration of the application
   * read from conf/application.conf
   */
  val config: play.api.Configuration = configuration

  /**
   * Gives access to the environment
   */
  val env: play.api.Environment = environment

  val rocawebConfigInstance: RocawebConfiguration = new RocawebConfiguration(config)

  var learningmodel = new LearningModel(config)

  var esmodel = new ESModel(config)

  var pdmlcrudmodel = new PDMLCrudModel(config)

  var preprocesseddatamodel = new PreprocessedDataModel(config)

  var treeviews = new TreeViews(config)

  /**
   * Get the configuration from conf/application.conf
   * for the key configName
   * @param configName: the key to search for
   */
  def getConfiguration(configName: String): String = {
    config.underlying.getString(configName)
  }

  val rocaweb = new Rocaweb(getWorkingConfigDir())

  /**
   * Getter of the Rocaweb working directory
   */
  def getWorkingConfigDir(): String = {

    rocawebConfigInstance.rocawebworkingdir
  }

  /**
   * Get the clustering Directory
   */
  def getClusterignDir(): String = {

    rocawebConfigInstance.clusteringdir
  }

  def getLearningModel(): LearningModel = {

    learningmodel
  }

  def getKibanaHost(): String = {

    rocawebConfigInstance.kibanahost
  }

  def getKibanaPort(): String = {
    rocawebConfigInstance.kibanaport
  }

  def getElasticHost(): String = {
    rocawebConfigInstance.elastichost
  }

  def getElasticPort(): String = {
    rocawebConfigInstance.elasticport
  }

  def getAgentHost(): String = {
    rocawebConfigInstance.agenthost
  }

  def getAgentPort(): String = {
    rocawebConfigInstance.agentport
  }

  /**
   * Determines the content of the body of the request
   * given a key
   * @param key: the value to search for
   */
  def getFromRequestBody(request: Request[AnyContent], key: String): String = {

    Logger.debug(request.body.asFormUrlEncoded.toString())
    request.body.asFormUrlEncoded.get(key).apply(0)
  }

}
