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

import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.junit.Test;

import com.rocaweb.commons.fs.FSManager;

/**
 * Class used to test the Elasticsearch connector
 * 
 * @author Djibrilla AMADOU KOUNTCHE
 *
 */
public class ElasticSearchConnectorTest {

	static final String ROOTPATH = "file:///tmp/rocawebdevtest/";
	final String PATTERN = "logstash_mod_security.*";
	final String HOST = "localhost";
	final String CLUSTER = "rocaweb-elk";
	final String TYPE = "mod_security_alert";
	final int PORT = 9300;

	ElasticSearchConnector esc = new ElasticSearchConnector(HOST, PORT, CLUSTER, TYPE);

	List<String> currentindices = esc.getIndexFromPattern(PATTERN);

	static FileObject root = FSManager.resolveFile(ROOTPATH);

	static {
		FSManager.createFolder(ROOTPATH);
	}

	@Test
	public void testInit() throws Exception {

		for (String index : currentindices) {
			esc.parseByUrlBySiteFromES(index, root);
		}

		FSManager.printInputDataVFS(root);

	}

	@Test
	public void testParseByScroll() throws Exception {

		for (String index : currentindices) {
			esc.parseResponsesByUrlBySiteFromESScroll(index, root);
		}
		System.out.println("parsing done");

		FSManager.printInputDataVFS(root);

	}

	@Test
	public void testGetAllIncidices() {
		for (String index : esc.getAllIndices())
			System.out.println(index);
	}

	@Test
	public void testGetIndexFromPattern() {
		System.out.println(esc.getIndexFromPattern(PATTERN));
	}

	@Test
	public void testParseUrlSiteFromES() {
		if (!currentindices.isEmpty())
			esc.parseByUrlBySiteFromES(currentindices.get(0), root);
	}
}
