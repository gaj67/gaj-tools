package gaj.analysis.model;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Encapsulates the output of a {@link GenerativeModel}.
 * <p/>
 * Note: Any additional information, such as a gradient matrix, will be computed
 * against the conditional probabilities, p(x|y).
 */
public interface GenerativeOutput extends DataOutput {

    /**
     * Obtains the prior probability p(y) for each y.
     * 
     * @return The prior probabilities.
     */
    DataVector getPriorProbabilities();

    /**
     * Obtains the marginal probability p(x) = sum_{y} p(y,x) of the input data
     * x.
     * 
     * @return The marginal probability.
     */
    double getMarginalProbability();

    /**
     * Obtains the joint probabilities p(y,x) of the input data x for each
     * indexable label y.
     * 
     * @return The joint probabilities.
     */
    DataVector getJointProbabilities();

    /**
     * Obtains the posterior probabilities p(y|x) of the input data x for each
     * indexable label y.
     * 
     * @return The posterior probabilities.
     */
    DataVector getPosteriorProbabilities();

}
