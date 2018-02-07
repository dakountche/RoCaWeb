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


import javax.inject._
import scala.io.Source
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.VFS
import org.apache.commons.vfs2.FileSystemManager;

import com.rocaweb.commons.fs.FSManager
import play.api.Logger
import com.rocaweb.ui.controllers.RocawebConfiguration

/**
 *
 * @author Yacine TAMOUDI
 * @since 1.0.0
 *
 */

class TreeViews @Inject() (configuration: play.api.Configuration) extends RocawebConfiguration(configuration) {

  var fo: FileObject = null

  def importNewTreeView(file: String) = {

    if (fo != null) { fo.close() }
    var path: String = rocaweb.getAbsolutepath(clusteringdir + file)

    fo = FSManager.resolveFile(path)
  }

  def getClusterizedNode(base: String, node: String): String = {

    val message: String = "Not yet defined"

    message
  }

  def getInputDataTreeView(url: String): String = {
    //Make the http get
    var json = ""
    if (fo != null) {

      json = FSManager.printInputDataNodeToJson(fo);
    }

    json
  }

  def getfakeInputDataTreeView(url: String): String = {
    //Make the http get

    return "{}"
  }

  def getNodeContent(path: String): String = {
    var answer = ""

    Logger.debug(FSManager.getURL(fo).toString())
    var file: FileObject = FSManager.resolveFromBase(fo, "." + path)
    Logger.debug(FSManager.getURL(file).getPath)

    answer = FSManager.read(file)

    answer
  }

}

