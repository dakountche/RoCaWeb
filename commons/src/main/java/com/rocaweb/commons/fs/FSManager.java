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

package com.rocaweb.commons.fs;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.collections.iterators.ArrayIterator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.FileTypeSelector;
import org.apache.commons.vfs2.VFS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rocaweb.commons.configuration.CommonConfiguration;

/**
 * Implements Singleton virtual file system manager. This VFS manager can handle
 * various file systems :
 * 
 * <ul>
 * <li>Local </><li>Ram</li>
 * <li>Remote : FTP, SFTP</li>
 * <li>Compressed : ZIP, TAR, GZ</li>
 * <li>Combined : ZIP:FTP,GZ:TAR:FTP, etc</li>
 * </ul>
 * 
 * @author Djibrilla AMADOU KOUNTCHE
 * @author Yacine Tamoudi
 * 
 * @see https://commons.apache.org/proper/commons-vfs/
 * 
 * @since 1.0.0
 */
public final class FSManager {

	private static Logger logger = LogManager.getLogger(FSManager.class);

	private static String SEP = "/";

	/** Default FileSystem manager */
	private static FileSystemManager manager = null;

	/**
	 * Getter of the default VFS manager.
	 * 
	 * @return the VFS manager.
	 */
	public static synchronized FileSystemManager getManager() {
		if (manager == null) {
			try {
				manager = VFS.getManager();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return manager;
	}

	/**
	 * Determines whether the file already exists
	 * 
	 * @param fileName
	 *            the URL of the file
	 * 
	 * @return a boolean
	 */
	public static synchronized boolean fileExists(String fileName) {
		boolean exists = false;
		if (fileName != null) {
		try {
			exists = getManager().resolveFile(fileName).exists();
		} catch (FileSystemException | NullPointerException e) {
			logger.error(e.getMessage(), e);
		}
		}
		return exists;
	}

	/**
	 * Close the fileObject instance.
	 * 
	 * @param fileobject
	 *            a FileObject.
	 */
	public static synchronized void close(FileObject fileobject) {

		if (fileobject != null) {
		try {
			fileobject.close();
		} catch (FileSystemException e) {
			logger.error(e.getMessage(), e);
		}
		}
	}

	/**
	 * Creates a FileObject instance for the URL fileName.
	 * 
	 * @param fileName
	 *            URL to the file
	 * 
	 * @return an instance of FileObject
	 */
	public static synchronized FileObject resolve(String fileName) {
		FileObject fileobject = null;
		try {
			fileobject = getManager().resolveFile(fileName);
		} catch (FileSystemException e) {
			logger.error(e.getMessage(), e);
		}
		return fileobject;
	}

	/**
	 * Writes the content to a FileObject.
	 * 
	 * @param content
	 *            the content to write in a file
	 * @param fileToAdd
	 *            the FileOject instance
	 */
	public static synchronized void write(String content, FileObject fileToAdd) {

		if(fileToAdd != null && content != null) {
		try {
			/* This OutputStream accepts appending content */
			OutputStream os = fileToAdd.getContent().getOutputStream(true);
			IOUtils.write(content + CommonConfiguration.LINE_SEPARATOR, os, CommonConfiguration.DEFAULT_ENCODING);
			close(fileToAdd);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		}
	}

	/**
	 * Creates a file on the Virtual FileSystem.
	 * 
	 * @param filename
	 *            the URL of the file to create
	 * 
	 * @return a FileObject instance.
	 */
	public static synchronized FileObject createFile(String filename) {
		FileObject fileToAdd = resolveFile(filename);
		try {
			if (!fileToAdd.exists()) fileToAdd.createFile();
		} catch (FileSystemException e) {
			logger.error(e.getMessage(), e);
		}
		return fileToAdd;
	}

	/**
	 * Determines the URL of a FileObject.
	 * 
	 * @param fileObject
	 *            the FileObject instance
	 * 
	 * @return a URL
	 */
	public static synchronized URL getURL(FileObject fileObject) {
		URL url = null;
		if (fileObject == null) return url;
		try {
			url = fileObject.getURL();
		} catch (FileSystemException e) {
			logger.debug("", e);
		}
		return url;
	}

	/**
	 * Determines the URL from the file name.
	 * 
	 * @param fileName:
	 *            the file name
	 * @return the URL
	 */
	public static synchronized URL getURL(String fileName) {
		return getURL(resolve(fileName));
	}

	/**
	 * Creates a folder on the VFS.
	 * 
	 * @param string
	 *            the URL of the folder
	 * 
	 * @return an instance of a FileObject
	 */
	public static synchronized FileObject createFolder(String string) {
		FileObject fobject = resolve(string);
		try {
			if (!fobject.exists()) fobject.createFolder();
		} catch (FileSystemException e) {
			logger.error(e.getMessage(), e);
		}
		return fobject;
	}

	/**
	 * Creates a FileObject instance from a URL.
	 * 
	 * @param fileName
	 *            the URL
	 * 
	 * @return and instance of FileObject.
	 */
	public static synchronized FileObject resolveFile(String fileName) {
		FileObject fo = null;
		try {
			File file = new File(fileName);
			fo = FSManager.getManager().resolveFile(file.getAbsolutePath());
		} catch (FileSystemException e) {
			logger.error(e.getMessage(), e);
		}
		return fo;
	}

	/**
	 * Get the number of children of an FileObject
	 * 
	 * @param fo:
	 *            the FileObject instances
	 * 
	 * @return the length
	 */
	public static synchronized int getChildrenLength(FileObject fo) {
		int count = 0;
		try {
			count = fo.getChildren().length;
		} catch (FileSystemException e) {
			logger.error(e.getMessage(), e);
		}
		return count;
	}

	public static synchronized void putUrlMethodValue(String murl, String paramname, String paramvalue, FileObject base,
			String method) {
		FileObject fo = resolveFromBase(base, murl + SEP + method + SEP + paramname);
		createFile(getURL(fo).toString());
		write(paramvalue, fo);
	}

	public static synchronized void putUrlMethodResponseValue(String murl, String response, FileObject base,
			String method) {

		String path = SEP + murl + SEP + method + SEP;
		FileObject fo = resolveFromBase(base, path);
		createFolder(getURL(fo).toString());
		int i = getChildrenLength(fo);
		FileObject fof = resolveFromBase(fo, SEP + i);
		write(response, fof);
	}

	/**
	 * Resolve a file path from a base FileObject
	 * 
	 * @param base:
	 *            the base FileObject
	 * @param path:
	 *            the path of the file
	 * @return an instance of FileObject
	 */
	public static synchronized FileObject resolveFromBase(FileObject base, String path) {
		return resolveFile(getURL(base).toString() + "/" + path);
	}

	/**
	 * Read the content of a FileObject
	 * 
	 * @param fo:
	 *            the FileObject to read
	 * @return the content of the FileObject as a String
	 */
	public static synchronized String read(FileObject fo) {
		String content = "";
		try {
			content = IOUtils.toString(fo.getContent().getInputStream(), CommonConfiguration.DEFAULT_ENCODING);
			close(fo);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return content;
	}

	/**
	 * Read the content of a file
	 * 
	 * @param filePath:
	 *            the path to the file
	 * @return the content of the file
	 */
	public static synchronized String read(String filePath) {
		FileObject fo = resolveFile(filePath);
		return read(fo);
	}

	/**
	 * Print the content of the files in a given FileObject
	 * 
	 * @param root
	 * @throws IOException
	 */
	public static synchronized void printInputDataVFS(FileObject root) throws IOException {
		FileSelector selector = new FileTypeSelector(FileType.FILE);
		FileObject[] files = root.findFiles(selector);
		for (FileObject file : files) {
			logger.debug(file.getURL().getPath());
			logger.debug(read(file));
		}

	}

	 /**
	  * Print a FileObject tree structure as a JSON file whithout including the root
	  * @param root: the root of the tree
	  * @return a String representing a JSON object
	  */
	public static String printInputDataNodeToJsonNoHost(FileObject root) {
		return fileObjectToJsonHelper(root, getURL(root).getPath());
	}

	
	/**
	 *  Print a FileObject tree structure as a JSON file
	  * @param root: the root of the tree
	  * @return a String representing a JSON object
	 */
	public static String printInputDataNodeToJson(FileObject root) {
		return fileObjectToJsonHelper(root, null);
	}

	/**
	 * Print a FileObject tree structure as a JSON file
	 * @param root: the root of the tree
	 * @param base: the pathname o
	 * @return a String representing a JSON object
	 */
	private static String fileObjectToJsonHelper(FileObject root, String base) {

		StringBuilder builder = new StringBuilder();
		String content = null;

		if (base == null) {
			base = getURL(root).getPath();
			content = root.getName().getBaseName();
		} else {
			int len = base.length();
			content = getURL(root).getPath().substring(len);
		}

		builder.append("{\n");
		builder.append("\"text\": \"" + content + "\"");
		recurseOnRoot(root, base, builder);
		builder.append("}");

		return builder.toString();
	}

	/**
	 * Convert recursively a tree structure into JSON element
	 * @param root: the root of the tree
	 * @param base:the base name
	 * @param builder: a StringBuilder
	 */
	private static void recurseOnRoot(FileObject root, String base, StringBuilder builder) {
		try {

			if (root.isFolder()) {
				FileObject[] children = root.getChildren();

				if (children.length > 0) {

					builder.append(",\n\"nodes\": [\n");

					ArrayIterator ite = new ArrayIterator(children);

					FileObject fo = (FileObject) ite.next();
					builder.append("\n" + fileObjectToJsonHelper(fo, base));

					while (ite.hasNext()) {

						builder.append(",\n" + fileObjectToJsonHelper((FileObject) ite.next(), base));
					}
					builder.append("]");
				}
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Reformat the lines of a String into JSON elements
	 * @param string: the String to reformat
	 * @return a formated JSON string
	 */
	public static String printListValueToJson(String string) {
		StringBuffer answer = new StringBuffer("{");
		String[] lines = string.split(System.getProperty("line.separator"));
		int i = 0;
		for (String line : lines) {
			answer.append(System.getProperty("line.separator") + "\t\"" + i + "\":\"" + line + "\"");
			i++;
		}
		answer.append("}");
		return answer.toString();
	}

	/**
	 * Format the content of a FileObject into a JSON element
	 * @param fo: the FileObject
	 * @return a formated String
	 * @throws IOException
	 */
	public static String printInputDataValueToJson(FileObject fo) throws IOException {
		return printListValueToJson(read(fo));
	}

}
