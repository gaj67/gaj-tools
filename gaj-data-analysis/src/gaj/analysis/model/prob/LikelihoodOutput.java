package gaj.analysis.model.prob;

/**
 * Encapsulates the output of a {@link LikelihoodModel}.
 */
public interface LikelihoodOutput extends ProbDataOutput {

    /**
     * Obtains the probability p(x) of the input data x.
     * 
     * @return The observation likelihood.
     */
    double getDataProbability();

}
