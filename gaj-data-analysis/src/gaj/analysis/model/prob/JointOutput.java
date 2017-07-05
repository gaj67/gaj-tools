package gaj.analysis.model.prob;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Encapsulates the output of a {@link JointModel}.
 * <p/>
 * Note: Any additional information, such as a gradient matrix, will be computed
 * against the joint probabilities.
 */
public interface JointOutput extends MarginalOutput, DiscriminativeOutput {

    /**
     * Obtains the marginal probability p(x) = sum_{y} p(y,x) of the input data
     * x.
     * 
     * @return The marginal probability.
     */
    @Override
    double getDataProbability();

    /**
     * Obtains the joint probabilities p(y,x) of the input data x for each
     * indexable label y.
     * 
     * @return The joint probabilities.
     */
    DataVector getJointProbabilities();

    /**
     * Obtains the posterior probabilities p(y|x) = p(y,x)/p(x) of the input
     * data x for each indexable label y.
     * 
     * @return The posterior probabilities.
     */
    @Override
    DataVector getPosteriorProbabilities();

}
