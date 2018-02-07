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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.rocaweb.commons.utils.Utils;

/**
 *  Abstract  XPATH parser
 *  
 * @author Yacine Tamoudi
 * @author Djibrilla AMADOU KOUNTCHE
 *
 * @since 1.0.0
 */
public abstract class AbstractXPathParser {
    
	/** Header of the  CSV files */
	public static String PARSED_PDML_FILE_HEADER = "METHOD;URL;BODY";

	/** The PDML field containing the request method */
	public static String HTTP_REQUEST_METHOD = ".//field[@name='http.request.method']";

	/** The PDML field containing the request URI */
	public static String HTTP_REQUEST_URI = ".//field[@name='http.request.uri']";

	/** The PDML field containing the timestamp */
	public static String HTTP_TIMESTAMP = ".//field[@name='timestamp']";

	/** The PDML field containing the set of parameters and values sent in the request */
	public static String HTTP_DATA_LINES = ".//proto[@name='data-text-lines']";
	
	private static XPathFactory xPAF = XPathFactory.instance();
	
	protected  static Logger logger = LogManager.getLogger();

	/**
	 * @see {@link org.jdom2.xpath.XPathFactory.compile}
	 */
	public static XPathExpression<Element> createXPathExpression(String pattern) throws JDOMException {

		return xPAF.compile(pattern, Filters.element());
	}

	/**
	 * Return the first element of the Element packet
	 * @param pattern the type of PDML field
	 * @param packet the current node in the XML tree
	 * @return an instance of Element
	 * @throws JDOMException
	 */
	public static Element getFirst(String pattern, Element packet) throws JDOMException {
		return (Element) createXPathExpression(pattern).evaluateFirst(packet);
	}
    
	
	protected  static LearningData parseWithXPath(File file, LearningData sortedPostList) {
		Document document = PDMLReader.readDocument(file);
		logger.debug("Before getting the root element");
		Element racine = document.getRootElement();
		List<Element> packetlist = racine.getChildren("packet");
		
		return parseWithXPath( packetlist , sortedPostList);
	}
	
	
	protected static LearningData parseWithXPath(List<Element> packetlist , LearningData sortedPostList) {

		
        String url= null;
        String hexurl = null;
        String murl = null;
        
		
		logger.debug("Before iterator");
		for (Element packet : packetlist) {

			try {

				Element urifield = getFirst(HTTP_REQUEST_METHOD, packet);
				if (urifield != null) {

					String method = urifield.getAttributeValue("show");
					if (method.equals("POST")) {

						urifield = getFirst(HTTP_REQUEST_URI, packet);
						hexurl = urifield.getAttributeValue("value");
						url = Utils.decodeHexToString(hexurl);
						murl = Utils.moduloUrl(url);

						processXPath(sortedPostList, url, murl);
						//AbstractHandler.parse(sortedPostList, url, murl);

						Element postproto = getFirst(HTTP_DATA_LINES, packet);
						if (postproto != null) {
							String hexpostparameters = postproto.getChild("field").getAttribute("value").getValue();
							String postparameters = Utils.decodeHexToString(hexpostparameters);
							processXPath(sortedPostList, postparameters, murl);
							

							//AbstractHandler.parse(sortedPostList, postparameters, murl);
						}
						} else if (method.equals("GET")) {

							urifield = getFirst(HTTP_REQUEST_URI, packet);
							hexurl = urifield.getAttributeValue("value");
							url = Utils.decodeHexToString(hexurl);
							murl = Utils.moduloUrl(url);
							//AbstractHandler.parse(sortedPostList, url, murl);
							processXPath(sortedPostList, url, murl);
							

						
					}
				}

			} catch (JDOMException e) {
				logger.error(e.getMessage());
			}
		}
		return sortedPostList;
	}

	
	
	protected static void processXPath(LearningData sortedPostList, String url, String murl) {
		
	}

	
}
