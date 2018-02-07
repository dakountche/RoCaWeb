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

package com.rocaweb.commons.recording;

import static com.rocaweb.commons.configuration.CommonConfiguration.LINE_SEPARATOR;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rocaweb.commons.fs.FSManager;
import com.rocaweb.commons.handlers.PDMLHandler;

/**
 * Converts a given PDML file into different format.
 * 
 * @author Yacine Tamoudi
 * @author Djibrilla AMADOU KOUNTCHE
 *
 * @since 1.0.0
 */
public class PDMLConverter extends AbstractXPathParser {

	private static Logger logger = LogManager.getLogger(PDMLConverter.class);

	public static String format = "%s;%s;%s;%s" + LINE_SEPARATOR;

	/**
	 * Converts a PDML file to CSV
	 * 
	 * @param source:
	 *            the PDML file
	 * @param destination:
	 *            the CSV file
	 * @param issounddata
	 */

	public static void pdmlToCsv(File source, File destination, boolean includestatic) {
        
		pdmlToCsvWithSax(source, destination,includestatic);
	}


	public static void write(String timestamp, String method, String murl, String postparameters, FileObject fo) {
		String content = String.format(format, timestamp, method, murl, postparameters);
		FSManager.write(content, fo);
	}

	
	public static void pdmlToCsv(File source, File destination) {
		pdmlToCsv(source, destination, false);
	}

	
	public static void pdmlToCsvWithSax(File source, File destination) {
		pdmlToCsvWithSax(source, destination, false);
	}

	
	public static void pdmlToCsvWithSax(File source, File destination, boolean includestatic) {

		FileObject fo = FSManager.resolve(destination.getAbsolutePath());
		FSManager.write(PARSED_PDML_FILE_HEADER, fo);
		final boolean is = includestatic;

		try {

			DefaultHandler handler = new PDMLHandler(fo, is);
			PDMLReader.getParser().parse(source, handler);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error(e.getMessage());
			logger.debug(e.getStackTrace());
		}
	}

}
