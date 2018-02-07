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
 * the XML file and convert it to a FileObject.
 * 
 * @author Yacine Tamoudi
 * @author Djibrila Amadou Kountche
 * 
 * @since 1.0.0
 */

package com.rocaweb.commons.handlers;

import org.apache.commons.vfs2.FileObject;

/**
 * A specific handler for FileObject
 * @author Yacine Tamoudi
 * @author Djibrilla Amadou Kountche
 *
 */
public class FileObjectHandler extends AbstractHandler {

	FileObject root = null;
	
	public FileObjectHandler(FileObject root) {
		this.root = root;
	}


	@Override
	public void process() {
		
		parse(url, getMurl(), root, method);
	}

}
