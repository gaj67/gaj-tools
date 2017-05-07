package gaj.analysis.model.prob;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Encapsulates the output of a {@link GenerativeModel}.
 * <p/>
 * Note: Any additional information, such as a gradient matrix, will be computed
 * against the conditional probabilities, p(x|y).
 */
public interface GenerativeOutput extends JointOutput {

    /**
     * Obtains the prior probability p(y) for each y.
     * 
     * @return The prior probabilities.
     */
    DataVector getPriorProbabilities();

    /**
     * Obtains the conditional probabilities p(x|y) of the input data x for each
     * indexable label y.
     * 
     * @return The conditional probabilities.
     */
    DataVector getConditionalProbabilities();

    /**
     * Obtains the joint probabilities p(y,x) = p(x|y)*p(y) of the input data x
     * for each indexable label y.
     * 
     * @return The joint probabilities.
     */
    @Override
    DataVector getJointProbabilities();

    /**
     * Obtains the marginal probability p(x) = sum_{y} p(x|y)*p(y) of the input
     * data x.
     * 
     * @return The marginal probability.
     */
    @Override
    double getDataProbability();

    /**
     * Obtains the posterior probabilities p(y|x) = p(x|y)*p(y)/p(x) of the
     * input data x for each indexable label y.
     * 
     * @return The posterior probabilities.
     */
    @Override
    DataVector getPosteriorProbabilities();

}