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
package com.rocaweb.learning.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.algorithms.metalearning.CrossValidation;
import com.rocaweb.learning.parameters.CrossValidationParam;
import com.rocaweb.learning.parameters.LearningJobParameters;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.serialization.json.CrossValidationJSD;
import com.rocaweb.learning.serialization.json.JobJSD;

/**
 * 
 * Represent a leaning Job. A job is a set of algorithms to execute on certain
 * folders.
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 */

@SuppressWarnings(value = { "rawtypes", "unchecked" })

public class Job {

	private LearningJobParameters parameters = null;

	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	private static Logger logger = LogManager.getLogger(Job.class);

	public Job() {

	}

	public Set<Future<Contract>> execute() {

		Set<Future<Contract>> results = Sets.newHashSet();
		try {

			results = executeInThreadPool(getAlgorithms());

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		getAlgorithms().clear();

		return results;
	}

	private Set<Future<Contract>> executeInThreadPool(Collection<Algorithm> algorithms) throws Exception {

		Set<Future<Contract>> futureContract = new HashSet<Future<Contract>>();
		for (Algorithm algorithm : algorithms) {
			futureContract.add(cachedThreadPool.submit(algorithm));

		}

		return futureContract;
	}

	/**
	 * @return
	 */
	public List<Algorithm> getAlgorithms() {

		return getParameters().getAlgorithms();
	}

	/**
	 * @return
	 */
	public LearningJobParameters getParameters() {
		if (parameters == null) {
			parameters = new LearningJobParameters();
		}
		return parameters;
	}

	/**
	 * @param algorithms
	 */
	public void setAlgorithms(List<Algorithm> algorithms) {

		getParameters().setAlgorithms(algorithms);
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

	public String toJSon() {
		GsonBuilder gsb = new GsonBuilder();
		gsb.registerTypeAdapter(this.getParameters().getClass(), new JobJSD());
		gsb.registerTypeAdapter(CrossValidationParam.class, new CrossValidationJSD());
		gsb.registerTypeAdapter(CrossValidation.class, new CrossValidationJSD());

		Gson gson = gsb.create();

		return gson.toJson(this.getParameters());
	}

	/**
	 * @param ljp
	 */
	public void setParameters(LearningJobParameters ljp) {
		this.parameters = ljp;

	}

}
