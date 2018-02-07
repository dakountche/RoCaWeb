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

import java.io.File

import javax.inject.Inject
import java.io.IOException
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Calendar
import org.apache.commons.vfs2.VFS
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.FileSystemManager

import play.api.Logger

import org.apache.commons.vfs2.FileType
import play.api.libs.json.Json
import com.rocaweb.commons.fs.FSManager

import com.rocaweb.learning.rules.Contract

import com.rocaweb.ui.controllers.RocawebConfiguration
import com.google.common.collect.Maps

/**
 * @author Alexandre CLERISSI
 * @author Djibrilla AMADOU KOUNTCHE
 * @since 1.0.0
 */

object ProfileModel  {

  var profiles: Map[String, List[Contract[_]]] = Map()

  def addProfile(id: String, list: List[Contract[_]]) {
    Logger.info(s"Adding $id to profiles")

    profiles += (id -> list)

  }

  /**
   * @param id
   * @return
   */
  def getPrintedProfile(id: String): String = {
    var result: String = ""

    for (x <- profiles(id)) {
      var tree: FileObject = x.getFileObject.getChildren()(0)
      result = FSManager.printInputDataNodeToJson(tree)
    }

    result
  }

  def getGeneratedRule(id: String): String = {
    var result: String = ""

    for (x <- profiles(id)) {
      result += x.generateRule()

    }

    result
  }

  def getListProfile(): String = {

    val result = Json.toJson(profiles.keySet).toString()
    Logger.debug(result)

    result
  }

  def getRuleContent(id: String, path: String): String = {

    var answer = ""

    var fo: FileObject = null

    for (x <- profiles(id)) {
      fo = x.getFileObject.getChildren()(0)
    }

    Logger.debug(FSManager.getURL(fo).toString())
    var file: FileObject = FSManager.resolveFromBase(fo, path)
    Logger.debug(FSManager.getURL(file).toString())
    answer = FSManager.read(file)

    answer
  }

}

