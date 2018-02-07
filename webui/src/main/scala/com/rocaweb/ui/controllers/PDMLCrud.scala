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
import java.io.{ File, FileInputStream, FileOutputStream }
import java.nio.file.{ Paths, Files }

import com.rocaweb.ui.exceptions.NotPDMLException

import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logger

import java.io.File

import sun.misc.IOUtils

import scala.collection._

@Singleton
class PDMLCrud @Inject() (cc: ControllerComponents, config: play.api.Configuration, env: play.api.Environment) extends RocawebController(cc, config, env) {

  /**
   * Creates an Action to allow a PDML file upload
   */
  def index = Action {
    Logger.info("On Get")
    Ok(views.html.upload())
  }

  /**
   * Creates an Action to allow to save a PDML file
   */
  def up = Action(parse.multipartFormData) { implicit request =>
    try {
      Logger.debug("Receiving a file")
      request.body.file("files[]").map { request =>
        val filename = request.filename

        pdmlcrudmodel.save(request.ref.path.toFile(), request.filename)
      }
      Ok(Json.toJson(true)).as(JSON)
    } catch {

      case e: NotPDMLException =>
        Logger.error(e.toString + ":" + e.getMessage); BadRequest(Json.toJson(e + ":" + e.getMessage)).as(JSON)
      case e: Throwable => Logger.error(e.toString + ":" + e.getMessage); InternalServerError(Json.toJson(e + ":" + e.getMessage)).as(JSON)
    }
  }

  /**
   * Creates an Action to Allow listing Files
   */
  def listfiles = Action {

    var i = 0
    val files = pdmlcrudmodel.list().map(_.getName)

    Ok(Json.toJson((i until files.length).map(_.toString).zip(files).toMap)).as(JSON)
  }

  /**
   * Creates an Action to list the content of the Elasticsearch index
   */
  def listES = Action {

    Ok(Json.toJson(esmodel.listindex()))
  }

  /**
   * Creates an Action to allow downloading a file
   */
  def get(name: String) = Action {
    try {
      Ok(Files.readAllBytes(Paths.get(pdmlcrudmodel.get(name).getAbsolutePath))).as("application/octet-stream")
    } catch {
      case e: Throwable => Logger.error(e.getMessage); NotFound(e.getMessage)
    }
  }

  def output(id: Long, params: String) = Action {
    Logger.info("Uploading a file")
    Ok(views.html.output(id))
  }

  /**
   * Creates an Action to allow parsing a PDML file
   */
  def clusterizeInputFile = Action { implicit request: Request[AnyContent] =>

    val filename = getFromRequestBody(request, "file")

    Logger.info(s"Clusterizing input data $filename")

    pdmlcrudmodel.clusterizeFileInputinVFS(filename)

    Ok(Json.toJson(true)).as(JSON)

  }

  /**
   * Creates an action to allow parsing the content of Elasticsearch index
   */
  def clusterizeInputES = Action { implicit request: Request[AnyContent] =>

    val body = request.body
    val indexname: String = getFromRequestBody(request, "file")

    esmodel.clusterizeESInputinVFS(indexname)

    Logger.info(s"Clusterizing input data $indexname")

    Ok(Json.toJson(true)).as(JSON)

  }

  /**
   * Creates an Action for removing a file
   */
  def removeInputData = Action { implicit request =>

    val file = getFromRequestBody(request, "file")

    Logger.info(s"Removing input data $file")
    pdmlcrudmodel.removeInput(file)
    Ok(Json.toJson("OK")).as(JSON)

  }

}
