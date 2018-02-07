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
import com.rocaweb.ui.models.Rocaweb
import com.rocaweb.ui.models.ProfileModel
import play.api.mvc._
import play.api.libs.json._
import play.mvc.Http.Request._
import play.api.Logger
import java.nio.file.{ Paths, Files }

/**
 * @author Alexandre CLERISSI
 * @author Djibrilla AMADOU KOUNTCHE
 */

class Profile @Inject() (cc: ControllerComponents, config: play.api.Configuration, env: play.api.Environment) extends RocawebController(cc, config, env) {

  def index(idwww: String, rule: String) = Action { request =>
    val path = request.path
    Logger.debug("Fetching: " + path + " with params id: [" + idwww + "] and rule: [" + rule.toString + "]")
    try {
      (idwww, rule) match {
        case (null, null) => Ok(Json.parse(rocaweb.getProfile())).as(JSON)
        case (s, null) => Ok(Json.parse(rocaweb.getProfile(idwww))).as(JSON)
        case (s, a) => Ok(Json.parse(rocaweb.getProfile(idwww, rule))).as(JSON)
      }
    } catch {
      case e @ (_: java.net.MalformedURLException | _: java.io.FileNotFoundException) =>
        Logger.error(e.toString + ":" + e.getMessage); NotFound(Json.toJson("Bad URI given")).as(JSON)
      case e: java.net.UnknownHostException =>
        Logger.error("Unable to call the RoCaWeb server"); InternalServerError(Json.toJson(e.getMessage + " not reachable")).as(JSON)
      case e: Throwable => Logger.error(e.toString + ":" + e.getMessage); InternalServerError(Json.toJson(e + ":" + e.getMessage)).as(JSON)
    }
  }

  def listProfiles = Action {

    Ok(ProfileModel.getListProfile()).as(JSON)
  }

  def getProfile(id: String) = Action {

    val tree = ProfileModel.getPrintedProfile(id)
    Logger.debug(s"Trying to serve: $tree")
    Ok(tree).as(JSON)

  }

  def getRuleFile(id: String) = Action {
    Ok(ProfileModel.getGeneratedRule(id).getBytes).as("application/octet-stream")
  }

  def getNode(id: String) = Action { request: Request[AnyContent] =>
    val path: String = getFromRequestBody(request, "node")

    Logger.info(s"Getting node $path")

    var result: String = ProfileModel.getRuleContent(id, path)
    Ok(Json.toJson(result)).as(JSON)
  }

}
