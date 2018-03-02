package gaj.analysis.model.prob.latent;

import gaj.analysis.data.numeric.vector.DataVector;

/**
 * Encapsulates the output of a {@link DiscriminativeDataModel}.
 */
public interface DiscriminativeDataObject extends ProbDataObject {

    /**
     * Obtains the posterior probabilities p(y|x) of the input data x for each
     * indexable label y.
     * 
     * @return The posterior probabilities.
     */
    DataVector getPosteriorProbabilities();

}
