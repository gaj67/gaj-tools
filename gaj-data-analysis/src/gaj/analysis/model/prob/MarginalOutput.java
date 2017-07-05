package gaj.analysis.model.prob;

/**
 * Encapsulates the output of a {@link MarginalModel}.
 */
public interface MarginalOutput extends ProbDataOutput {

    /**
     * Obtains the probability p(x) of the input data x.
     * 
     * @return The observation likelihood.
     */
    double getDataProbability();

}
