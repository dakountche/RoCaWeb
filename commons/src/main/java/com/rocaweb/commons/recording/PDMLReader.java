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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.Lists;
import com.rocaweb.commons.handlers.AbstractHandler;
import com.rocaweb.commons.handlers.FileObjectHandler;
import com.rocaweb.commons.handlers.GenericHandler;

/**
 * @author Yacine TAMOUDI
 * @author Djibrilla AMADOU KOUNTCHE
 * 
 * @since 1.0.0
 */

public class PDMLReader extends AbstractXPathParser {

	static Logger logger = LogManager.getLogger(PDMLReader.class);
	private static SAXBuilder saxbuilder = null;

	public static void preparePDML(File file, String outfile) {
		Document document = readDocument(file);
		Element racine = document.getRootElement();
		List<Element> packetlist = racine.getChildren("packet");
		List<Element> packetlisttodelete = getElementsToDelete(packetlist);
		removeElements(packetlisttodelete, racine);
		writeToFile(document, outfile);
	}

	private static List<Element> getElementsToDelete(List<Element> packetlist) {
		List<Element> packetToBeRemoved = Lists.newArrayList();
		for (Element packet : packetlist) {
			try {
				Element postproto = getFirst(HTTP_DATA_LINES, packet);
				if (postproto != null) {
					String postparameters = postproto.getChild("field").getAttribute("show").getValue();
					if (postparameters.contains("pw=")) {
						packetToBeRemoved.add(packet);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return packetToBeRemoved;
	}

	
	private static void writeToFile(Document document, String outfile) {
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());

		try {
			FileWriter filewriter = new FileWriter(outfile);
			xmlOutput.output(document, filewriter);
			filewriter.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	private static void removeElements(List<Element> packetlisttodelete, Element racine) {
		Iterator<Element> it = packetlisttodelete.iterator();
		while (it.hasNext()) {
			racine.removeContent(it.next());
			it.remove();
		}
	}

	/**
	 * Read a PDML file and cast it into a list of element, each element represent a
	 * packet
	 * 
	 * @param filename
	 * @return
	 */
	public static List<Element> readPDML(File file) {

		List<Element> packetlist = Lists.newArrayList();
		Document document = readDocument(file);
		Element racine = document.getRootElement();
		packetlist.addAll(racine.getChildren("packet"));

		return packetlist;
	}

	public static List<Element> readPDML(String file) {

		return readPDML(new File(file));
	}

	/**
	 * @param packetlist
	 * @return
	 */
	public static LearningData parsePostsandGetsByUrlByXPath(List<Element> packetlist, LearningData sortedPostList) {
		return parseWithXPath(packetlist, sortedPostList);
	}

	private static void addPosts(LearningData sortedPostList, String postparameters, String murl, String httpmethod) {
		AbstractHandler.parse(sortedPostList, postparameters, murl);
	}

	
	public static LearningData parsePostsandGetsByUrlByXPath(List<Element> packetlist) {
		return parsePostsandGetsByUrlByXPath(packetlist, new LearningData());
	}

	public static LearningData parsePostsandGetsByUrlByXPathWithSax(File file) {
		return PDMLReader.parsePostsandGetsByUrlByXPathWithSax(file, new LearningData());
	}

	private static SAXBuilder getSaxBuilder() {

		if (saxbuilder == null) {
			saxbuilder = new SAXBuilder();
		}
		return saxbuilder;
	}

	public static Document readDocument(File file) {
		Document document = null;
		try {
			document = getSaxBuilder().build(file);
		} catch (Exception e) {
			logger.error(e.getMessage());
	}
	return document;
	}

	public static void processXPath(LearningData sortedPostList, String url, String murl) {
		addPosts(sortedPostList, url, murl, "");
	}

	
	/**
	 * @param packetlist
	 * @return
	 */
	public static LearningData parsePostsandGetsByUrlByXPathWithSaxFromZip(File file, LearningData sortedPostList) {
		final LearningData spl = sortedPostList;
		try {
			DefaultHandler handler = new GenericHandler(spl);
			ZipFile zipFile = new ZipFile(file);
			ZipEntry entry = zipFile.entries().nextElement();
			InputStream stream = zipFile.getInputStream(entry);
			getParser().parse(stream, handler);
			zipFile.close();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error(e.getMessage());
		}

		return sortedPostList;
	}

	/**
	 * @param packetlist
	 * @return
	 */
	public static LearningData parsePostsandGetsByUrlByXPathWithSax(File file, LearningData sortedPostList) {
		final LearningData spl = sortedPostList;
		try {
			DefaultHandler handler = new GenericHandler(spl);
			getParser().parse(file, handler);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error(e.getMessage());
		}

		return sortedPostList;
	}

	/**
	 * 
	 * @param packetlist
	 */
	public static void printPDML(List<Element> packetlist) {

		Iterator<Element> i = packetlist.iterator();
		while (i.hasNext()) {
			Element pckcourant = (Element) i.next();
			// Print the name of the current element
			List<Element> protolist = pckcourant.getChildren("proto");
			for (Element proto : protolist) {
				List<Element> fieldlist = proto.getChildren("field");
				for (Element field : fieldlist) {
					logger.debug(field.getAttributeValue("name") + " : " + field.getAttributeValue("show"));
				}
			}
		}
	}

	public static LearningData parsePostsandGetsByUrlByXPathWithSaxFromZip(File file) {
		return parsePostsandGetsByUrlByXPathWithSaxFromZip(file, new LearningData());
	}

	/**
	 * @param packetlist
	 * @return
	 */
	public static void parsePostsandGetsByUrlBySiteByXPathWithSax(File file, final FileObject root) {
		try {
			DefaultHandler handler = new FileObjectHandler(root);
			getParser().parse(file, handler);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	 static SAXParser getParser() throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		return factory.newSAXParser();
	}

}
