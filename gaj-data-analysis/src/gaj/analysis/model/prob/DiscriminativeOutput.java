package gaj.analysis.model.prob;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Encapsulates the output of a {@link DiscriminativeModel}.
 */
public interface DiscriminativeOutput extends ProbDataOutput {

    /**
     * Obtains the posterior probabilities p(y|x) of the input data x for each
     * indexable label y.
     * 
     * @return The posterior probabilities.
     */
    DataVector getPosteriorProbabilities();

}
