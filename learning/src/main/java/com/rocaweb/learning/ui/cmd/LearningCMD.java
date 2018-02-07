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

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rocaweb.commons.fs.FSManager;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.algorithms.Algorithms;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.utils.LearningUtils;
import com.rocaweb.learning.utils.Worker;

/**
 * Implements a command line interface for the learning module. If the user did
 * not specifies a configuration file, the default configuration is used.
 * 
 * 
 * This cmd can be used as :
 * <p>
 * <ul>
 * <li>LearningCMD -a algorithmName -f learningFileName</li>
 * <li>LearningCMD -a algorithmName -f learningFileName -c configurationFileName
 * </li>
 * <li>LearningCMD -a algorithmName -c configurationFileName</li>
 * <li>LearningCMD -b jsonJobFileName.json</li>
 * </ul>
 * </p>
 * 
 * 
 * @author Djibrilla AMADOU KOUNTCHE
 * @since 1.0.0
 * 
 */

public class LearningCMD {

	private Options options = null;

	private CommandLineParser parser = null;
	private CommandLine cmd;

	private static Logger logger = LogManager.getLogger(LearningCMD.class);

	/* Boolean options */
	private Option help = new Option("h", "print this message");
	private Option version = new Option("v", "3");
	private HelpFormatter formatter = new HelpFormatter();

	public LearningCMD() {
		initialiseOptions();
	}

	private static final String[][] optionNames = {
			{ "a", "algo", "The name of the algorithm to use for the learning." },
			{ "f", "file", "The file or directrory containing the training set." },
			{ "c", "config", "A xml file containing the parameters of the algorithm." },
			{ "b", "batch", "A json file describing a learning task wich corresponds to many sub-tasks." } };

	private void configArgumentOptions() {
		Option option = null;
		for (int i = 0; i < optionNames.length; i++) {
			String name = optionNames[i][0];
			String longopt = optionNames[i][1];
			String description = optionNames[i][2];

			option = createArgumentOption(name, description, longopt);
			getOptions().addOption(option);
		}
	}

	private Option createArgumentOption(String name, String description, String longopt) {

		Option option = Option.builder(name).argName("arg-name").desc(description).longOpt(longopt).hasArg().build();

		return option;
	}

	private void initialiseOptions() {

		getOptions().addOption(help);
		getOptions().addOption(version);
		configArgumentOptions();

	}

	private Options getOptions() {
		if (options == null) {
			options = new Options();
		}

		return options;
	}

	public void usage() {
		formatter.printHelp("RoCaweb Learning module", getOptions());
	}

	private CommandLineParser getParser() {
		if (parser == null) {
			parser = new DefaultParser();
		}
		return parser;
	}

	public static void main(String[] args) {
		LearningCMD lcmd = new LearningCMD();
		lcmd.parse(args);

	}

	public void parse(String[] args) {

		initializeCommandLine(args);

		if (help() || version())
			return;

		else if (batch())
			return;

		else if (getAlgoAndRun())
			return;
		else if ((hasOption('f', "file") || hasOption('c', "config")) && (!hasOption('a', "algo"))) {
			usage();
			return;
		}

	}

	public void initializeCommandLine(String[] args) {

		try {
			cmd = getParser().parse(getOptions(), args);
		} catch (ParseException e) {
			usage();
		}
	}

	/**
	 * Execute a learning task described in a JSON File
	 */
	public boolean batch() {
		boolean seen = false;
		if (hasOption('b', "batch")) {
			seen = true;
			String jsonFile = getCmd().getOptionValue('b');
			try {
				Worker.executeLearningTask(jsonFile);
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}

		}
		return seen;
	}

	public boolean help() {
		boolean seen = getCmd().hasOption('h');
		if (seen) {
			usage();
		}
		return seen;
	}

	public boolean version() {

		boolean seen = getCmd().hasOption('v');
		;
		if (seen) {
			System.out.println("RoCaweb Learning module : " + 3);
			seen = true;
		}
		return seen;

	}

	public boolean getAlgoAndRun() {
		boolean seen = hasOption('a', "algo");
		if (seen) {
			String name = getCmd().getOptionValue("algo");
			String learningFile = file();
			String configFile = config();

			if (!FSManager.fileExists(learningFile)) {
				logger.error("Please specifiy an existing file ! ");
				System.exit(-1);
			}

			if (!FSManager.fileExists(configFile)) {

				configFile = LearningUtils.DEFAULT_CONFIG_FILE;
				logger.warn("Using the default configuration file : " + configFile);
			}

			runAlgo(name, learningFile, configFile);
		}
		return seen;
	}

	public String file() {
		String filename = "";
		if (hasOption('f', "file")) {
			filename = getCmd().getOptionValue('f');
		}
		return filename;
	}

	public String config() {
		String filename = null;
		if (hasOption('c', "config")) {
			filename = this.getCmd().getOptionValue('c');

		}
		return filename;
	}

	@SuppressWarnings("rawtypes")
	private Contract runAlgo(String name, String file, String config) {

		Contract contract = null;
		try {
			contract = createAlgorithm(name, file, config).call();
			logger.info(contract.generateRule());

		} catch (IOException e) {
			logger.error(e.getMessage());

		}
		return contract;
	}

	public CommandLine getCmd() {
		return cmd;
	}

	private boolean hasOption(char a, String b) {
		return getCmd().hasOption(a) || this.getCmd().hasOption(b);
	}

	private static Algorithm<?> createAlgorithm(String algorithmName, String learningFile, String configFile) {

		Algorithm<?> algo = Algorithms.createAlgorithm(algorithmName, configFile);

		if (FSManager.fileExists(learningFile)) {
			algo.getParameters().setLearningFile(learningFile);
		} else {
			String anotherFile = algo.getParameters().getLearningFile();
			logger.debug(anotherFile);

			if (!FSManager.fileExists(anotherFile)) {
				logger.error("No valid learning file found ! ");
				System.exit(-1);
			}

		}

		return algo;
	}

}
