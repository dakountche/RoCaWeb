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
import org.apache.commons.vfs2.FileTypeSelector
import org.apache.commons.vfs2.FileType
import com.rocaweb.ui.controllers.RocawebConfiguration
import com.rocaweb.ui.exceptions.MalformedParameterException

import play.api.Logger

/**
 * @author Alexandre CLERISSI
 * @author Djibrilla AMADOU KOUNTCHE
 */

class PreprocessedDataModel @Inject() (configuration: play.api.Configuration) extends RocawebConfiguration(configuration) {

  def list(): List[File] = {
    val d = new File(clusteringdir)
    d.listFiles.filter(_.isDirectory).toList

  }

  def get(file: String): File = {
    var f = Paths.get(file).getFileName.toString
    Logger.debug(s"Getting file $file")
    if (!f.equals(file)) {
      throw new MalformedParameterException(file + " is not a valid parameter")
    }
    var res = new File(clusteringdir + f)
    if (!(res.exists && res.isFile && res.canRead)) { throw new IOException(f + " does not exist") }

    res
  }

  def removePreprocessed(file: String) {

    var fo: FileObject = rocaweb.globalpersistentfolder.resolveFile("clusterized/" + file)
    fo.delete(new FileTypeSelector(FileType.FILE))
    fo.delete(new FileTypeSelector(FileType.FOLDER))
    fo.delete()
    Logger.info(s"Removing input data $file")
  }

}
