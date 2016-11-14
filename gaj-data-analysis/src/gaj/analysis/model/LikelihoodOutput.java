package gaj.analysis.model;

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
