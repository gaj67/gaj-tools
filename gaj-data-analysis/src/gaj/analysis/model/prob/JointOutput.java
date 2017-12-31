package gaj.analysis.model.prob;

import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.WritableVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;

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
    default double getDataProbability() {
        return getJointProbabilities().sum();
    }

    /**
     * Obtains the joint probabilities p(y,x) of the input data x for each
     * indexable label y.
     * 
     * @return The joint probabilities.
     */
    DataVector getJointProbabilities();

    /**
     * Obtains the posterior probabilities p(y|x) = p(y,x) / p(x) of the input
     * data x for each indexable label y.
     * 
     * @return The posterior probabilities.
     */
    @Override
    default DataVector getPosteriorProbabilities() {
        DataVector jointProbs = getJointProbabilities();
        double norm = jointProbs.sum();
        if (norm == 0.0) {
            return VectorFactory.newFixedVector(jointProbs.size(), 1.0 / jointProbs.size());
        } else {
            WritableVector probs = VectorFactory.copy(jointProbs);
            probs.multiply(1.0 / norm);
            return probs;
        }
    }

}
