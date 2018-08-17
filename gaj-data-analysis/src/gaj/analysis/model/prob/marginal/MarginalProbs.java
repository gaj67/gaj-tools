package gaj.analysis.model.prob.marginal;

import gaj.analysis.model.prob.Probs;

/**
 * Encapsulates the output of a {@link MarginalProbModel}.
 */
public interface MarginalProbs extends Probs {

    /**
     * Obtains the probability p(x) of the input data x.
     * 
     * @return The observation likelihood.
     */
    double getMarginalProbability();

}
