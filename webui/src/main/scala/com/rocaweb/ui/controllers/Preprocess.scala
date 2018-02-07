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
import com.rocaweb.ui.models.PreprocessedDataModel
import com.rocaweb.ui.models.TreeViews
import play.api.mvc._
import play.api.libs.json._
import play.mvc.Http.Request._
import play.api.Logger
import com.rocaweb.ui.models.PreprocessedDataModel

/**
 * @author Alexandre CLERISSI
 * @author Djibrilla AMADOU KOUNTCHE
 */
class Preprocess @Inject() (cc: ControllerComponents, config: play.api.Configuration, env: play.api.Environment) extends RocawebController(cc, config, env) {

  def index(idwww: String, rule: String) = Action { request =>
    val path = request.path
    // Logger.info("Trying to fetch: " + path + " with params id: [" + idwww + "] and rule: [" + rule.toString + "]")

    Logger.info("Clusterization in progress:" + idwww + ":" + rule);
    try {
      (idwww, rule) match {
        case (null, null) => Ok(Json.parse(rocaweb.getClusterized())).as(JSON)
        case (s, null) => Ok(Json.parse(rocaweb.getClusterized(idwww))).as(JSON)
        case (s, a) => Ok(Json.parse(rocaweb.getClusterized(idwww, rule))).as(JSON)

      }
    } catch {
      case e @ (_: java.net.MalformedURLException | _: java.io.FileNotFoundException) =>
        Logger.error(e.toString + ":" + e.getMessage); NotFound(Json.toJson("Bad URI given")).as(JSON)
      case e: java.net.UnknownHostException =>
        Logger.error("Unable to call ares"); InternalServerError(Json.toJson(e.getMessage + " not reachable")).as(JSON)
      case e: Throwable => Logger.error(e.toString + ":" + e.getMessage); InternalServerError(Json.toJson(e + ":" + e.getMessage)).as(JSON)
    }
  }

  def list = Action {

    val files = preprocesseddatamodel.list().map(_.getName)

    Ok(Json.toJson((0 until files.length).map(_.toString).zip(files).toMap)).as(JSON)
  }

  def get(name: String, rule: String) = Action {
    try {

      Ok(treeviews.getInputDataTreeView("test")).as("application/octet-stream")
    } catch {
      case e: Throwable => Logger.error(e.getMessage); NotFound(e.getMessage)
    }
  }

  def setTree = Action { request: Request[AnyContent] =>

    val filename: String = getFromRequestBody(request, "file")

    Logger.info(s"Clusterizing input data $filename")

    treeviews.importNewTreeView(filename)
    Ok(Json.toJson(true)).as(JSON)

  }

  def getNode = Action { request: Request[AnyContent] =>

    val path: String = getFromRequestBody(request, "node")

    Logger.info(s"Getting node $path")

    var result: String = treeviews.getNodeContent(path)
    Ok(Json.toJson(result)).as(JSON)

  }

  def saveNode = Action { request: Request[AnyContent] =>
    val body = request.body
    // val path :String = body.get("node").get(0)

    Logger.info("Saving Node")

    //var result :String= TreeViews.getNodeContent(path)
    Ok(Json.toJson("OK")).as(JSON)

  }

  def removePreprocessed = Action { request: Request[AnyContent] =>

    val file: String = getFromRequestBody(request, "file")

    Logger.info("Removing preprocessed data")
    preprocesseddatamodel.removePreprocessed(file)
    Ok(Json.toJson("OK")).as(JSON)

  }

}
