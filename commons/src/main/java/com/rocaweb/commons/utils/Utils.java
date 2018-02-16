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
package com.rocaweb.commons.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rocaweb.commons.configuration.CommonConfiguration;
import com.rocaweb.commons.exceptions.MalformedUrlException;
import com.rocaweb.commons.exceptions.ParsingException;

/**
 * @author Yacine TAMOUDI
 */
public class Utils {

	private static Logger logger = LogManager.getLogger(Utils.class);

	/**
	 * Delete a folder
	 * 
	 * @param folder
	 */
	public static void deleteFolder(File folder) {

		try {
			FileUtils.deleteDirectory(folder);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Equivalence relation for url (remove get parameter, stranges paths)
	 * 
	 * @param url
	 * @return
	 */
	public static String moduloUrl(String url) {
		 String parseUrl = "";
		try {
			parseUrl =  tomoduloUrl(url);
		} catch (MalformedUrlException e) {
			e.printStackTrace();
		}
		return parseUrl;
	}
	
	private static String tomoduloUrl(String url) throws MalformedUrlException {

		String returnurl = null;
		try {
			returnurl = url.trim();
			int i = url.indexOf('?');
			if (i != -1) {
				returnurl = returnurl.substring(0, i);

			}

			i = returnurl.indexOf('#');
			if (i != -1) {
				logger.debug(url);
				returnurl = returnurl.substring(0, i);

			}

			// to do use the java Path or URL/URI type

			if (returnurl.contains("/./")) {
				returnurl = returnurl.replaceAll("/./", "/");
			}

			while (returnurl.endsWith("/")) {
				returnurl = returnurl.substring(0, returnurl.length() - 1);
			}

		} catch (Exception e) {
			throw new MalformedUrlException(e.getMessage());
		}
		return returnurl;
	}

	/**
	 * Parses a string with get parameters
	 * 
	 * @param parameters
	 * @return List<String[]> a list of pair(name:value)
	 */
	public static Map<String, List<String>> parseURI(String url) {

		Map<String, List<String>> entries = Maps.newHashMap();

		List<NameValuePair> values = Lists.newArrayList();
		try {
			values = URLEncodedUtils.parse(new URI(url), CommonConfiguration.DEFAULT_ENCODING);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		logger.debug("NameValuePair: " + values);

		for (NameValuePair value : values) {
			if (!entries.containsKey(value.getName())) {
				entries.put(value.getName(), new ArrayList<String>());

			}
			entries.get(value.getName()).add(value.getValue());
		}
		logger.debug(entries);

		return entries;

	}

	/**
	 * @param Regex
	 *            url
	 * @return String representing the fixed (not regex) part of an url
	 */
	public static String getFixedPart(String url) throws MalformedUrlException {
		int i;

		i = url.indexOf("(");
		if (i != -1) {
			url = url.substring(0, i);
		}

		i = url.indexOf("[");
		if (i != -1) {
			url = url.substring(0, i);
		}

		i = url.indexOf("{");
		if (i != -1) {
			url = url.substring(0, i);
		}

		i = url.indexOf("?");
		if (i > 1) {
			url = url.substring(0, i - 1);
		}
		i = url.indexOf("+");
		if (i >= 1) {
			url = url.substring(0, i - 1);
		}

		i = url.indexOf("*");
		if (i >= 1) {
			url = url.substring(0, i - 1);
		}

		url = moduloUrl(url);

		if (url.length() == 0) {
			url = "/";
		}

		return url;
	}

	/**
	 * 
	 * @param hex
	 * @return
	 * @throws ParsingException
	 */
	public static String decodeHexToString(String hex)  {
        String result = "";
        
        try {
			result  =  hexToString (hex);
		} catch (ParsingException e) {
			
			e.printStackTrace();
		}
        
        return result;
		
	}
	
	private static String hexToString (String hex) throws ParsingException {
	
		StringBuilder output = new StringBuilder();

		try {
			for (int i = 0; i < hex.length(); i += 2) {
				String str = hex.substring(i, i + 2);
				output.append((char) Integer.parseInt(str, 16));
			}
		} catch (StringIndexOutOfBoundsException e) {
			throw new ParsingException("Not a valid Hex");

		}

		return output.toString();

		
	}

	public static String listToString(List<String> elements, String separator) {
		return Joiner.on(separator).useForNull("").join(elements);
	}

    
	public static String listToString(List<String> elements) {
		return listToString(elements, System.getProperty("line.separator"));
	}
	
    /**
     * Determines the absolute Path of a file
     * @param path 
     * @return {@link File#getAbsoluteFile()}
     */
	public static String getAbsolutePath(String path) {
		
		return (new File(path)).getAbsolutePath();
	}

}
