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

import com.rocaweb.ui.misc.PDMLTinyChecker
import play.api.{ Play, Logger }
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.FileObject;
import com.rocaweb.commons.recording.PDMLReader
import com.rocaweb.commons.fs.FSManager
import com.rocaweb.ui.controllers.RocawebConfiguration
import com.rocaweb.ui.exceptions._

/**
 * @author Alexandre CLERISSI
 * @author Djibrilla AMADOU KOUNTCHE
 */

class PDMLCrudModel @Inject() (configuration: play.api.Configuration) extends RocawebConfiguration(configuration) {

  def save(content: File, filename: String) = {
    if (PDMLTinyChecker.isPDML(content.getAbsolutePath) == false) {
      Logger.warn(s"Uploaded file $filename does not match pdml schema, removing file")
      content.delete()
      throw new NotPDMLException("Invalid pdml file")
    }

    val newFileName = (new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss.S_").format(Calendar.getInstance().getTime())) + filename

    val bresult = content.renameTo(new File(s"" + pdmldir + "/" + newFileName))
    if (!bresult) throw new Error("other filesystem exceptions")

    Logger.info(s"moved $filename to " + pdmldir + "/" + newFileName)
  }

  def list(): List[File] = {
    val d = new File(pdmldir)
    return d.listFiles.filter(_.isFile).toList
  }

  def get(file: String): File = {
    var f = Paths.get(file).getFileName.toString
    Logger.debug(s"Getting file $file")
    if (!f.equals(file)) {
      throw new MalformedParameterException(file + " is not a valid parameter")
    }
    var res = new File(pdmldir + f)
    if (!res.exists || !res.isFile || !res.canRead) { throw new IOException(f + " does not exist") }

    return res
  }

  def clusterizeFileInputinVFS(filename: String) = {

    var newfold: String = "file:///" + rocaweb.getAbsolutepath(clusteringdir);

    Logger.debug(s"Starting clusterizing in folder $newfold")

    var filein = get(filename)

    var fo: FileObject = FSManager.resolveFile(newfold);

    PDMLReader.parsePostsandGetsByUrlBySiteByXPathWithSax(filein, fo);

    FSManager.close(fo)

    Logger.debug(s"Finished clusterizing $filein")

  }

  def removeInput(file: String) {

    var fo: FileObject = rocaweb.globalpersistentfolder.resolveFile("pdml/" + file)
    fo.delete()
    Logger.info(s"removing input data $file")
  }

}
