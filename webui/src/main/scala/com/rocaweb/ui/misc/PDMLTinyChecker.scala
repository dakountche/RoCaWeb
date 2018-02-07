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
package com.rocaweb.ui.misc

import java.io.ByteArrayInputStream
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.{ Validator => JValidator }
import org.xml.sax.SAXException
import org.apache.commons
import java.nio.charset._

import play.api.Logger

import scala.util.Try

import com.rocaweb.commons.fs.FSManager
import com.rocaweb.commons.utils.Utils

/**
 * @author Alexandre CLERISSI
 * @since 1.0.0
 */

object PDMLTinyChecker {

  //val schemaLang = getConfiguration("rocaweb.pdml.xmlschema")
  //val xsdfile = getConfiguration("rocaweb.pdml.xsd")

  val xsdFile = FSManager.read(Utils.getAbsolutePath("conf/xsd/pdml-schema.xsd"))

  def isPDML(xmlFile: String): Boolean = {

    Try({
      val schemaLang = "http://www.w3.org/2001/XMLSchema"
      val factory = SchemaFactory.newInstance(schemaLang)
      val schema = factory.newSchema(new StreamSource(new java.io.StringReader(xsdFile)))
      val validator = schema.newValidator()
      validator.validate(new StreamSource(xmlFile))
      return true
    }).getOrElse(return true)

  }

}