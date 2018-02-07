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
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;

import play.api.Logger
import org.apache.commons.vfs2.FileType;
import play.api.libs.json.Json

import com.rocaweb.ui.controllers.RocawebConfiguration

/**
 *  @author Alexandre  CLERISSI
 *  @author Djibrilla AMADOU KOUNTCHE
 */

class LearningModel @Inject() (configuration: play.api.Configuration) extends RocawebConfiguration(configuration) {

  var jobs: Map[String, Map[String, String]] = Map();

  var index: Int = 0

  /**
   * Creates a lists of the algorithms available
   */
  def listAlgorithms(): List[File] = {
    val algorithmsconfigdir = new File("public/json")
    return algorithmsconfigdir.listFiles.filter(_.isFile).toList

  }

  /**
   * Populates the jobs
   */
  def getListJob(): String = {

    return Json.toJson(jobs).toString()
  }

  /**
   * Add a new Job
   */
  def addNewJob(conf: String, input: String, node: String): String = {

    index += 1

    var hm: Map[String, String] = Map()

    hm += ("node" -> node)
    hm += ("input" -> input)
    hm += ("configuration" -> conf)
    hm += ("status" -> "running")

    jobs += (index.toString() -> hm)

    return index.toString()

  }

  def finishJob(id: String): Unit = {

    jobs.get(id) match {
      case Some(job: Map[String, String]) => { jobs += (id -> job.updated("status", "finish")) }
      case None => throw new Exception("Unknown Job")

    }

  }

  def deleteJob(id: String): Unit = {

    jobs -= id
  }

}

