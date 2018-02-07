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


package com.rocaweb.commons.handlers;

import org.apache.commons.vfs2.FileObject;

import com.rocaweb.commons.recording.PDMLConverter;

/**
 * Class used by XPath parser to parse specific field in
 * the XML file.
 * @author Yacine Tamoudi
 * @author Djibrila Amadou Kountche
 * 
 * @since 1.0.0
 */
public class PDMLHandler extends AbstractHandler {

	boolean is = false;
	FileObject fo = null;

	public PDMLHandler(FileObject fo, boolean is) {
		super();
		this.fo = fo;
		this.is = is;
	}


	@Override
	public void process() {
		PDMLConverter.write(timestamp, method, url, post, fo);		
	}

	@Override
	public boolean getCondition() {
		
		return (url.contains("?") || is || datatextlines);
	}

}
