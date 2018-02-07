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
 * 
 */
package com.rocaweb.learning.ui.cmd;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Amadou Kountché Djibrilla
 *
 */
public class LearningCMDTest {

	LearningCMD cmd = null;
	String[] args = { "", "" };

	@Test
	public void testUsage() {
		cmd = new LearningCMD();
		cmd.usage();
	}

	@Test
	public void testHelp() {
		cmd = new LearningCMD();
		args[0] = "-h";
		cmd.initializeCommandLine(args);
		assertEquals(cmd.getCmd().hasOption('h'), true);

	}

	@Test
	public void testVersion() {
		cmd = new LearningCMD();
		args[0] = "-v";
		cmd.initializeCommandLine(args);
		assertEquals(cmd.version(), true);
	}

	@Test
	public void testAlgoCase1() {
		cmd = new LearningCMD();
		String[] args = { "-a", "amaa", "-f", "/tmp" };
		cmd.initializeCommandLine(args);
		assertEquals(cmd.file(), "/tmp");
		assertEquals(cmd.getCmd().hasOption("a"), true);
		assertEquals(cmd.getCmd().getOptionValue("a"), "amaa");

	}

	@Test
	public void testAlgoCase2() {
		cmd = new LearningCMD();
		String[] args = { "-a", "amaa", "-c", "config" };
		cmd.initializeCommandLine(args);
		assertEquals(cmd.getCmd().hasOption("a"), true);
		assertEquals(cmd.getCmd().hasOption("c"), true);
		assertEquals(cmd.getCmd().getOptionValue("c"), "config");
	}

	@Test
	public void testAlgoCase3() {
		cmd = new LearningCMD();
		String[] args = { "-a", "typage", "-c", "config.xml", "-f", "/tmp" };
		cmd.initializeCommandLine(args);
		// cmd.parse(args);

	}

	@Test
	public void testConfig() {
		cmd = new LearningCMD();
		args[0] = "-c";
		args[1] = "config.xml";
		cmd.initializeCommandLine(args);
		assertEquals(cmd.getCmd().hasOption('c'), true);
		assertEquals(cmd.getCmd().getOptionValue('c'), "config.xml");
	}

	@Test
	public void testBatch() {
		cmd = new LearningCMD();
		args[0] = "-b";
		args[1] = "job.json";

		cmd.initializeCommandLine(args);
		assertEquals(cmd.getCmd().hasOption('b'), true);
		assertEquals(cmd.getCmd().getOptionValue('b'), "job.json");

	}

}
