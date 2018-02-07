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
package com.rocaweb.learning.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rocaweb.commons.fs.FSManager;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.parameters.LearningJobParameters;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.serialization.json.JobJSD;

/**
 * Create a clustering task described in a Json file. The file contains for each
 * algorithm the value of the parameters. The learning task create a Learning
 * control object and execute the list of algorithms.
 * 
 * 
 * @since 1.0.0
 * 
 * @author Djibrilla Amadou Kountché
 * 
 */
@SuppressWarnings("rawtypes")
public class Worker {

	private static Gson gson = null;
	private static Logger logger = LogManager.getLogger(Worker.class);
	private static Job lc = new Job();

	public static void executeLearningTask(String jsonFileContainingLearningTask) throws IOException {
		logger.info("Enter executeLearningTask");
		FileObject fo = FSManager.getManager().resolveFile(jsonFileContainingLearningTask);
		logger.info("Open " + fo.getURL().getPath());
		String config = IOUtils.toString(fo.getContent().getInputStream(), "UTF-8");
		logger.info(config);
		FSManager.close(fo);
		LearningJobParameters ljp = (LearningJobParameters) getGson().fromJson(config, LearningJobParameters.class);
		lc.setParameters(ljp);

		lc.execute();

		logger.trace("The end !");

	}

	private static Gson getGson() {

		if (gson == null) {
			GsonBuilder gsb = new GsonBuilder();
			gsb.registerTypeAdapter(LearningJobParameters.class, new JobJSD());
			gson = gsb.create();
		}

		return gson;
	}

	public static LearningJobParameters getLearningJobParameters(String path, String learningFile, String learningPath)
			throws IOException {
		String config = readFile(path, Charset.defaultCharset());

		LearningJobParameters ljp = (LearningJobParameters) getGson().fromJson(config, LearningJobParameters.class);

		List<Algorithm> lag = ljp.getAlgorithms();
		for (Algorithm<?> algorithm : lag) {
			algorithm.getParameters().setLearningFile(learningFile);
			algorithm.getParameters().setLearningPathName(learningPath);
			algorithm.setConfigFileName(path);
		}

		return ljp;
	}

	public static Set<Future<Contract>> specialWorker(String config, String learningFile, String learningPath) {

		LearningJobParameters ljp = (LearningJobParameters) getGson().fromJson(config, LearningJobParameters.class);

		Collection<Algorithm> lag = ljp.getAlgorithms();
		for (Algorithm<?> a : lag) {
			a.getParameters().setLearningFile(learningFile);
			a.getParameters().setLearningPathName(learningPath);
		}

		lc.setParameters(ljp);

		return lc.execute();

	}

	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
