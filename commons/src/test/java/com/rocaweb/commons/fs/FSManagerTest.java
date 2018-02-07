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
import java.net.URISyntaxException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.junit.Test;

/**
 * @author Djibrilla AMADOU KOUNTCHE
 *
 */
public class FSManagerTest {

	@Test
	public void test() throws FileSystemException, URISyntaxException {

		File file = new File("src/test/resources/application.properties");
		String pathToFile = file.getAbsolutePath();
		FileObject fo = FSManager.getManager().resolveFile(pathToFile);
		System.out.println(fo.getURL().toString());

		if (fo.isFolder()) {
			for (FileObject fobj : fo.getChildren()) {
				System.out.println(fobj.getURL().getFile());

			}
		}
	}

	@Test
	public void testRAMFS() throws FileSystemException, Exception {

		FileObject fo = FSManager.getManager().resolveFile("ram:/");

		for (FileObject fobj : fo.getChildren()) {
			System.out.println(fobj.getURL().toString());
		}
	}

}
