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

import scala.io.Source
import scala.util.Random
import play.api.Logger
import scala.language.dynamics
import com.rocaweb.commons.fs.FSManager
import org.apache.commons.vfs2.FileObject;
import java.io.File
import java.io.IOException
import play.api.Play

/**
 * @author Alexandre CLRERISSI
 */

class Rocaweb(workingdir: String) extends Dynamic {

  val globaltmpfolder: FileObject = FSManager.resolve("tmp://" + workingdir)

  val globalpersistentfolder: FileObject = FSManager.resolveFile(getAbsolutepath(workingdir))

   val urlMap = collection.immutable.HashMap[String, String ] (
    "getClusterized0" -> "/data/input/Donnees_Traitement.json",
    "getClusterized1" -> "/data/treeview/site/%s.json",
    "getClusterized2" -> "/data/input/Occurence.json",
    "getProfile0"     -> "/profile/Tableau_Dyna.json",
    "getProfile1" -> "/data/treeview/site/%s.json",
    "getProfile2" -> "/profile/edition/regles/%.0sEdition_%s.json"
  )

  val serv: String = "http://sein.rocaweb.com:8085"

  def applyDynamic(method: String)(args: Any*): String = {
    val meth = method + args.length.toString
    Logger.debug("method called: [" + method + args.length.toString + "]")

    val argString = if (args.length > 0) "(" + args.mkString(" ") + ")" else ""
    if (urlMap.contains(meth)) {
      Logger.debug("matching uri: [" + urlMap.get(meth).head.toString.format(args: _*) + "]")
      return get(urlMap.get(meth).head.toString.format(args: _*))
    } else
      throw new java.net.MalformedURLException("Unvalid path given")
  }

  def get(url: String) = invoke(serv + url).toString()

  def invoke(url: String): Seq[Char] = {
    //Make the http get
    Source.fromURL(url).mkString
  }

  /**
   * Determines the absolutes path of a File
   *
   */
  def getAbsolutepath(path: String): String = {
    var file: File = new File(path);

    file.getAbsolutePath()

  }

}
