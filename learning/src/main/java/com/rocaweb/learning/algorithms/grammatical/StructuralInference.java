/** 
* This file is part of RoCaWeb.
*
* RoCaWeb is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package com.rocaweb.learning.algorithms.grammatical;

/**
 * 
 * This method determines the regular grammar which was used to generated the
 * values of the parameters.
 * 
 * <h3>Learning phase</h3>
 * 
 * <p>
 * A probabilistic grammar is assumed which is transformed as a non
 * deterministic finite automaton. This grammar assigns probabilities to each of
 * its productions. Some words are more likely to be produce than others.
 * </p>
 * 
 * <p>
 * $p(w) = p(o_1, o_2, .., o_n) = \sum \pi p_{s_i}(o_i) \times p(t_i)$
 * </p>
 * 
 * <p>
 * The approach use is to generalize the grammar as long as it seems to be
 * "reasonable" and stop before too much information is lost. The notion of
 * "reasonable" is capture by Markov models and probabilistic grammar.
 * </p>
 * 
 * <p>
 * The goal of the structural inference is to find an NFA that has the highest
 * likelihood for the given training elements which is done by using the method
 * of Stolcke and Omohundro
 * </p>
 * 
 * 
 * <h3>Detection phase</h3>
 * <p>
 * Given the model determined previously, calculate the likelihood of a given
 * word by the equation 1.
 * 
 * If the word is part of the learning words, return 1 Otherwise return the
 * value.
 * 
 * </p>
 * 
 * <h3>Advantages</h3>
 * <p>
 * Useful for detecting all kind of attacks that require the parameter string to
 * have a different structure than regular query arguments.
 * 
 * It can also identify attack that exploit errors in the program logic.
 * </p>
 * 
 * 
 * <h3>Limitations</h3> The marginalization</br>
 * Prior probabilities</br>
 * TODO Compare to AMAA.
 * 
 * 
 * <h3>Examples</h3> TODO
 * 
 * 
 * 
 * @author Djibrilla Amadou Kountche
 * 
 * @since 1.0.0
 *
 * 
 * @see Kruegel, C., Vigna, G., & Robertson, W. (2005). A multi-model approach
 *      to the detection of web-based attacks. Computer Networks, 48(5),
 *      717-738.}.
 *      
 * @see Stolcke, A., & Omohundro, S. (1994). Inducing probabilistic grammars by
 *      Bayesian model merging. In Grammatical inference and applications (pp.
 * 106-118). Springer Berlin Heidelberg.
 * 
 * 
 * TODO 
 */

public class StructuralInference {

    
}
