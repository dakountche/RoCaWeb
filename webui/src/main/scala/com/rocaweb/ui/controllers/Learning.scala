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
import java.io.File
import play.api._
import play.api.mvc._

import com.rocaweb.ui.models._
import play.api.mvc._
import play.api.libs.json._
import play.mvc.Http.Request._

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.parameters.LearningJobParameters;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.serialization.json.JobJSD;
import com.rocaweb.learning.utils.Worker
import com.rocaweb.learning.utils.Job
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.Set
import play.api.Logger
import org.apache.commons.vfs2.FileObject;
import scala.collection.JavaConverters._
import com.rocaweb.commons.utils.Utils

class Learning @Inject() (cc: ControllerComponents, config: play.api.Configuration, env: play.api.Environment) extends RocawebController(cc, config, env) {

  val inputKey: String = getConfiguration("rocaweb.ui.html.input")
  val algorithmskey: String = getConfiguration("rocaweb.ui.html.algorithms")
  val nodekey: String = getConfiguration("rocaweb.ui.html.node")
  val encoding: String = getConfiguration("rocaweb.ui.encoding")

  val algoconfpathkey: String = getConfiguration("rocaweb.conf.algorithms")
  /**
   * Creates an Action to display all the available algorithms
   */
  def listAlgorithms = Action {
    val files = learningmodel.listAlgorithms().map(_.getName)

    Ok(Json.toJson((0 until files.length).map(_.toString).zip(files).toMap)).as(JSON)
  }

  /**
   * Creates an Action to display all the current Jobs
   */
  def listJobs = Action {

    Ok(learningmodel.getListJob).as(JSON)
  }

  /**
   * Launches a Job given the configuration selected by the user
   */
  def launchNewJob = Action { request: Request[AnyContent] =>

    val input: String = getFromRequestBody(request, inputKey)

    val algorithm: String = getFromRequestBody(request, algorithmskey)

    val node: String = getFromRequestBody(request, nodekey)

    val id: String = learningmodel.addNewJob(algorithm, input, node)

    val algoconfpath: String = Utils.getAbsolutePath(algoconfpathkey + "/" + algorithm)

    var algoconfig = scala.io.Source.fromFile(algoconfpath, encoding).mkString

    launchBackEndJob(id, algoconfig, node, input)

    Ok(Json.toJson(true)).as(JSON)
  }

  /**
   * Launches a Job given the configuration selected by the user
   */
  def launchBackEndJob(id: String, config: String, node: String, input: String): Unit = {

    Logger.debug(s"Executing the algorithm $node : $input : $config")

    val learningPath: String = rocaweb.getAbsolutepath(getClusterignDir() + input) + System.getProperty("file.separator")

    Logger.debug(s"Learning path : $learningPath, Learning node : $node")
    var jresults: Set[Future[Contract[_]]] = Worker.specialWorker(config, node, learningPath)

    var results = jresults.asScala

    var contract_result: List[Contract[_]] = Nil

    for (fc <- results) {

      var c: Contract[_] = fc.get();

      contract_result = c :: contract_result

    }

    ProfileModel.addProfile(id, contract_result)

    learningmodel.finishJob(id)

  }

  /**
   * Deletes a Job from the Learning View
   * @param id: the Identifier of the Job
   */
  def deleteJobResult(id: String) = Action {

    learningmodel.deleteJob(id)

    Ok(Json.toJson(true)).as(JSON)
  }

}
