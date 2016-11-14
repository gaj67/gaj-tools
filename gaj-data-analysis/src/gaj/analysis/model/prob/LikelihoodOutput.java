package gaj.analysis.model.prob;

import gaj.analysis.model.DataOutput;

/**
 * Encapsulates the output of a {@link LikelihoodModel}.
 */
public interface LikelihoodOutput extends DataOutput {

    /**
     * Obtains the probability p(x) of the input data x.
     * 
     * @return The observation likelihood.
     */
    double getProbability();

}
