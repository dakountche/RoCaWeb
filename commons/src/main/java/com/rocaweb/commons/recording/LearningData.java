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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.rocaweb.commons.fs.FSManager;
import com.rocaweb.commons.utils.Utils;

import static com.rocaweb.commons.configuration.CommonConfiguration.FILE_SEPARATOR;
import static org.junit.Assert.assertNotNull;;
/**
 * Representation of a sorted data structure : Url1 -GET -paramget1 -value1
 * -value2 ... -POST ... Url2 ...
 * 
 * @author Yacine TAMOUDI
 * @author Djibrilla AMADOU KOUNTCHE
 * 
 * @since 1.0.0
 */
public class LearningData {

	private static Logger logger = LogManager.getLogger(LearningData.class);
	
	private StringBuilder csvBuilder = null;
	

	/** Data structure to hold the content of the PDML */
	private HashMap<String, HashMap<String, HashMap<String, List<String>>>> hashmap;

	/**
	 * Default constructor
	 */
	public LearningData() {
		hashmap = Maps.newHashMap();
	}

	/**
	 * Get the node in the tree structure related to the URL
	 * 
	 * @param url
	 *            the name of the node
	 * @return another HashMap
	 */
	private HashMap<String, HashMap<String, List<String>>> getUrl(String url) {

		assertNotNull(url);
		return getHashMap().get(url);
	}

	/**
	 * Get a child node of the node name URL in the tree of type HTTP POST method
	 * 
	 * @param url
	 *            the parent node
	 * @return another HashMap structure
	 */
	private HashMap<String, List<String>> getUrlPosts(String url) {
		assertNotNull(url);

		return getUrl(url).get("POST");
	}

	/**
	 * Get a child node of the node name URL in the tree of type HTTP GET method
	 * 
	 * @param url
	 *            the parent node
	 * @return another HashMap structure
	 */
	private HashMap<String, List<String>> getUrlGets(String url) {
		assertNotNull(url);

		return getUrl(url).get("GET");
	}

	public List<String> getUrlGet(String url, String parameter) {
		assertNotNull(url);
		assertNotNull(parameter);

		return getUrlGets(url).get(parameter);
	}

	public List<String> getUrlPost(String url, String parameter) {
		assertNotNull(url);
		assertNotNull(parameter);
		
		return getUrlPosts(url).get(parameter);
	}

	public void putUrl(String url, HashMap<String, HashMap<String, List<String>>> urlmap) {

		getHashMap().put(url, urlmap);

	}

	public void putUrlPosts(String url, HashMap<String, List<String>> postsmap) {

		putHttpMethod(url, "POST", postsmap );

	}

	/**
	 * Insert the to the Map
	 * @param url
	 * @param postsmap
	 * @param httpMethod
	 */
	private void putHttpMethod(String url, String httpMethod , HashMap<String, List<String>> postsmap) {

		HashMap<String, HashMap<String, List<String>>> tempmap = null;
		HashMap<String, List<String>> parameters = null;
		
		if (!getHashMap().containsKey(url)) {
			tempmap = Maps.newHashMap();
			tempmap.put(httpMethod, postsmap);
		}
		else { 
			tempmap = getHashMap().get(url);
		    if (!tempmap.containsKey(httpMethod)) {
                 tempmap.put(httpMethod, postsmap);		    
		    }
		    else {
			  parameters = tempmap.get(httpMethod);
			  for (Entry<String, List<String>> entry: postsmap.entrySet()) {
				  if(!parameters.containsKey(entry.getKey())) {
					  parameters.put(entry.getKey(), entry.getValue());
				  }
				  else parameters.get(entry.getKey()).addAll(entry.getValue());
					  
			  }
		    }
		}
		
		putUrl(url, tempmap);

	}
	
	public void putUrlGets(String url, HashMap<String, List<String>> getsmap) {
		putHttpMethod(url, "GET" , getsmap);
	}

	public void putUrlGet(String url, String parameter, List<String> listvalue) {
		putUrlHttpMethod(url, "GET" , parameter, listvalue);
	}

	private void putUrlHttpMethod(String url, String httpMethod , String parameter, List<String> listvalue) {        
		HashMap<String, List<String>> content = createParamMap(parameter, listvalue); 
		putHttpMethod(url, httpMethod, content);
	}

	
	private HashMap<String, List<String>> createParamMap(String parameter, List<String> listvalue) {
		HashMap<String, List<String>> param = Maps.newHashMap();
		param.put(parameter, listvalue);
		return param;
	}

	public void putUrlPost(String url, String parameter, List<String> listvalue) {
		putUrlHttpMethod(url, "POST" , parameter, listvalue);
	}

	public void putUrlGetValue(String url, String parameter, String value) {
		putUrlHttpMethodValue(url, "GET", parameter, value);
	}

	
	private void putUrlHttpMethodValue(String url, String httpMethod, String parameter, String value) {
		putHttpMethod(url, httpMethod, createParamMap(parameter, Lists.newArrayList(value)));
	}


	public void putUrlPostValue(String url, String parameter, String value) {
		putUrlHttpMethodValue(url, "POST", parameter, value);
	}

	
	/**
	 * Build a file system matching the post data in the PDML
	 * @param filepath
	 * @param file
	 * @return
	 */
	public static FileObject buildFilePath(String filepath, File file) {

		FileObject returnFile = null;
		filepath = filepath.split("\\?")[0];
		if (file.isDirectory()) {
			returnFile = FSManager.createFile(file.getAbsolutePath() + FILE_SEPARATOR + filepath);
			logger.debug(FSManager.getURL(returnFile).toString());
		}
		return returnFile;
	}

	/**
	 * Write the content of the instance of this class
	 * 
	 * @param storage:
	 *            the path where the content is to be written
	 */
	public void buildPostParametersFileSystem(String storage, boolean isCSV) {
		File storageFile = new File(storage);
		for (Entry<String, HashMap<String, HashMap<String, List<String>>>> entry : getHashMap().entrySet()) {
			String url = entry.getKey();
			for (Entry<String, HashMap<String, List<String>>> f : entry.getValue().entrySet()) {
				String type = f.getKey();
				FileObject UrlFolder = buildFilePath(url + FILE_SEPARATOR + type + FILE_SEPARATOR, storageFile);
				if (UrlFolder != null) {
					for (Entry<String, List<String>> param : f.getValue().entrySet()) {
						FileObject paramFile = FSManager.resolveFromBase(UrlFolder,FILE_SEPARATOR + param.getKey());
						String content = Utils.listToString(param.getValue());
						FSManager.write(content, paramFile);
					}
				}
			}
		}
	}

	
	/**
	 * Print the sorted post parameters
	 * 
	 * @param hashmap
	 */
	public String toString() {
		String newline = System.getProperty("line.separator");
		StringBuilder sBuilder = new StringBuilder();
		for (Entry<String, HashMap<String, HashMap<String, List<String>>>> e : hashmap.entrySet()) {
			sBuilder.append(newline + "URL: " + e.getKey());

			for (Entry<String, HashMap<String, List<String>>> type : e.getValue().entrySet()) {
				sBuilder.append(newline + "\t" + type.getKey() + ": ");
				for (Entry<String, List<String>> param : type.getValue().entrySet()) {
					sBuilder.append(newline + "\t\t" + param.getKey() + " : " + param.getValue());
				}
			}
		}
		return sBuilder.toString();
	}
	
	
	
	/**
	 * Convert an instance of this class into a CSV
	 * @return a CSV String
	 */
	public String toCSV() {
		return getCSVBuilder().toString();
	}

	
	private StringBuilder getCSVBuilder() {
		if(csvBuilder == null) {
			csvBuilder = new StringBuilder();
		}
		return csvBuilder;
	}
	public HashMap<String, HashMap<String, HashMap<String, List<String>>>> getHashMap() {

		return hashmap;
	}

}
