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


/**
 * Class use by XPath parser to parse specific field in
 * the XML file.
 * @author Yacine Tamoudi
 * @author Djibrila Amadou Kountche
 * 
 * @since 1.0.0
 */
package com.rocaweb.commons.handlers;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rocaweb.commons.fs.FSManager;
import com.rocaweb.commons.recording.LearningData;
import com.rocaweb.commons.utils.Utils;

/**
 * 
 * 
 * @author Yacine Tamoudi
 * @author Djibrilla AMADOU KOUNTCHE
 * 
 * @since 1.0.0
 *
 */
public abstract class AbstractHandler extends DefaultHandler {

	protected Logger logger = LogManager.getLogger(getClass());

	protected boolean http = false;
	protected boolean geninfo = false;
	protected String timestamp = "";
	protected String method = "";
	protected String url = "";
	protected String post = "";
	protected String host = "";

	protected boolean datatextlines = false;

	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (toLowerCase(qName)) {

		case "packet":
			break;
		case "proto":
			initAttributes(attributes);
			break;
		case "field":
			getTheTimestamp(attributes);
		    processHTTP(attributes);
		    getDataTextlines(attributes);
			break;
		}
	}


	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		switch (toLowerCase(qName)) {

		case "packet":
			if (getCondition())
				process();
			resetAttibutes();
			break;
		case "proto":
			if (http)
				http = false;
			break;
		case "geninfo":
			if (geninfo)
				geninfo = false;
		}

	}
	
	private void processHTTP(Attributes attributes) {
		if (http) {

			if (has(attributes, "http.request.method"))
				method = attributes.getValue("show");

			if (has(attributes, "http.request.uri")) {
				String hexurl = attributes.getValue("value");
				url = Utils.decodeHexToString(hexurl);
			}
		}
		
	}


	private void getDataTextlines(Attributes attributes) {
		if (datatextlines) {
			String hexpost = attributes.getValue("value");
			post = Utils.decodeHexToString(hexpost);
		}
		
	}


	private void getTheTimestamp(Attributes attributes) {
		if (geninfo && has(attributes, "timestamp"))
		timestamp = attributes.getValue("value");
		
	}


	private void initAttributes(Attributes attributes) {
		geninfo = has(attributes, "geninfo");
		http = has(attributes, "http");
		datatextlines = has(attributes, "data-text-lines");
	}

	protected boolean has(Attributes attr, String valName) {
		return equalsAttrValueTo(attr, "name", valName);
	}

	protected void resetAttibutes() {
		http = false;
		datatextlines = false;
		url = "";
		post = "";

	}

	protected String getValue(Attributes attr, String atName) {
		return attr.getValue(atName);
	}

	protected String toLowerCase(String other) {
		return other.toLowerCase();
	}

	protected boolean equalsAttrValueTo(Attributes attr, String name, String toCompare) {
		return toLowerCase(getValue(attr, name)).equals(toCompare);
	}
	
	
	public String getMurl() {
		return Utils.moduloUrl(url);
	}

	public abstract void process();

	public boolean getCondition() {
		
		return (!url.equals("")) && (url.contains("?") || datatextlines);
	}
	
	/**
	 * Parse the PDML and write the content to OrganisedLearningData instance
	 * @param sortedPostList the destination of the parsed content
	 * @param url the URL
	 * @param murl the modified URL
	 */
	public static void parse(LearningData sortedPostList, String url, String murl) {
		Map<String, List<String>> listget = Utils.parseURI(url);
		for (Entry<String, List<String>> entry : listget.entrySet()) {
			sortedPostList.putUrlGetValue(murl, entry.getKey(), Utils.listToString(entry.getValue()));
		}
	}
	
	
	/**
	 *  Parse the PDML and write the content to a FileObject
	 * @param requestedUri the URI
	 * @param host the current host name
	 * @param root the destination of the parsed content
	 * @param method the HTTP method
	 */
	public static void parse(String requestedUri, String host, FileObject root, String method) {
		Map<String, List<String>> listget = Utils.parseURI(requestedUri);
		for (Entry<String, List<String>> entry : listget.entrySet()) {
			String murl = Utils.moduloUrl(requestedUri);
			String content = Utils.listToString(entry.getValue());
			FSManager.putUrlMethodValue(host + "/" + murl, entry.getKey(), content, root, method);
		}
	}

}
