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
package com.rocaweb.learning.algorithms.metalearning;

import java.util.List;

import com.rocaweb.learning.algorithms.Algorithm;
import com.rocaweb.learning.data.metalearning.Corpus;
import com.rocaweb.learning.data.metalearning.XStatistics;
import com.rocaweb.learning.parameters.AbstractAlgorithmParameters;
import com.rocaweb.learning.parameters.CrossValidationParam;
import com.rocaweb.learning.parameters.DefaultLearningAParameters;
import com.rocaweb.learning.rules.Contract;
import com.rocaweb.learning.rules.MetaContract;

/**
 * Implementation of the Cross Validation algorithms.
 * 
 * 
 * @author Yacine Tamoudi
 * @author Djibrilla Amadou Kountche
 *
 * @since 1.0.0
 * 
 * @see https://en.wikipedia.org/wiki/Cross-validation_%28statistics%29
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CrossValidation extends Algorithm<String> {

	private CrossValidationParam parameters = null;
	private Corpus<String> corpus = null;
	private XStatistics stats = new XStatistics();
	protected transient MetaContract contract = null;

	public CrossValidation() {}

	/**
	 * @param configFileName
	 */
	public CrossValidation(String configFileName) {
		super(configFileName);
	}

	public List<Algorithm> getAlgorithms() {
		return getParameters().getAlgorithms();
	}

	public String crossValidation(List<String> sequences) {
		createCorpus(sequences, getK(), isShuffle());
		return xvalidate();
	}

	private Object runChoosenAlgorithmAndUpdateContracts(int i, List<String> collection) {
		Algorithm algo = getAlgorithms().get(i);
		algo.getParameters().setLearningFile(getParameters().getLearningFile());
		algo.getParameters().setLearningPathName(getParameters().getLearningPath());

		Object rule = algo.process(collection);
		Contract contract = algo.getContract();
		contract.addToResultToFormat(getFileName(), rule);

		getContract().getContracts().add(contract);

		return rule;
	}

	/**
	 * Made a cross validation with the list of algorithms. One is chosen to
	 * generate the final rule.
	 * 
	 * @return a rule.
	 */
	private String xvalidate() {
		Object rule = "";
		if (getCorpus().hasCorrectCollectionSize()) {

			stats.updateTotalCount();

			if (getCorpus().collectionHasOneElement()) {
				rule = runChoosenAlgorithmAndUpdateContracts(0, this.getCorpus().getCollection());
				stats.updateOnlyOneCount();
			} else {
				int thisK = Math.min(getK(), getCorpus().getCollection().size());
				if (thisK < getK()) {
					stats.updateStatInfCount();
				}

				// If the the number of values is less than k then diminish k. 

				int[] countalgo = new int[getAlgorithms().size()];

				for (int i = 0; i < thisK; i++) {

					List<String> testlist = getCorpus().getTestSet();
					List<String> learninglist = getCorpus().getTrainingSet();

					int j = 0;
					logger.trace(learninglist);
					for (Algorithm<?> algo : getAlgorithms()) {
						logger.trace(testlist);
						logger.trace(learninglist);

						if (algo.validate(testlist, learninglist)) {
							countalgo[j]++;
						}

						j++;
					}

					getCorpus().putTheFirstAtTheEnd();
				}

				// End of the for loop for the leave one out

				boolean found = false;

				for (int j = 0; j < countalgo.length; j++) {

					double minvalid = thisK * getRatio();

					if (countalgo[j] >= minvalid) {

						rule = runChoosenAlgorithmAndUpdateContracts(j, getCorpus().getCollection());
						stats.updateRuleCount(getAlgorithms().get(j).getClass().getSimpleName());

						found = true;

						break;
					}

				}

				if (!found) {
					stats.updateNoRuleCount();

					int lastAlgoIndex = getAlgorithms().size() - 1;
					String simpleAlgoName = this.getAlgorithms().get(lastAlgoIndex).getClass().getSimpleName();
					rule = this.runChoosenAlgorithmAndUpdateContracts(lastAlgoIndex, this.getCorpus().getCollection());
					stats.updateRuleCount(simpleAlgoName);

				}

			} // End leaveone out

		}

		else {

			stats.updateNoRuleCount();
		}
		logger.trace(rule);
		logger.info("\n" + stats.printXStats());
		return rule.toString();

	}

	/**
	 * @param sequences
	 * @param k
	 * @param shuffle
	 */
	private void createCorpus(List<String> sequences, int k, boolean shuffle) {
		this.setCorpus(new Corpus(sequences, k, shuffle));

	}

	private double getRatio() {

		return this.getParameters().getRatio();
	}

	private boolean isShuffle() {
		return this.getParameters().isShuffle();
	}

	private int getK() {
		return this.getParameters().getK();
	}

	@Override
	public CrossValidationParam getParameters() {
		if (parameters == null) {
			parameters = new CrossValidationParam(getConfigFileName());
		}

		return parameters;
	}

	@Override
	public String process(List<String> sequences) {
		return crossValidation(sequences);
	}

	@Override
	public MetaContract getContract() {

		if (contract == null) {
			contract = new MetaContract(this);
		}

		return (MetaContract) contract;
	}

	@Override
	public void setDefaultParameters() {
		this.parameters = DefaultLearningAParameters.getDefaultCrossValidationParams();

	}

	/**
	 * @return the sequences
	 */
	public Corpus getCorpus() {
		return corpus;
	}

	/**
	 * @param garfield
	 *            the sequences to set
	 */
	public void setCorpus(Corpus corpus) {
		this.corpus = corpus;
	}

	@Override
	public char getFormatChar() {
		return 'c';
	}

	@Override
	public void setParameters(AbstractAlgorithmParameters parameters) {
		this.parameters = (CrossValidationParam) parameters;

	}

}
