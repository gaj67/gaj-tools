package gaj.analysis.model.prob.marginal;

import gaj.analysis.model.prob.ProbDataObject;

/**
 * Encapsulates the output of a {@link MarginalDataModel}.
 */
public interface MarginalDataObject extends ProbDataObject {

    /**
     * Obtains the probability p(x) of the input data x.
     * 
     * @return The observation likelihood.
     */
    double getMarginalProbability();

}
