package gaj.analysis.model.prob;

/**
 * Encapsulates the general output from a probabilistic model.
 */
public interface Probs {

    /**
     * Obtains the type of the underlying probabilistic model used to obtain
     * this output.
     * 
     * @return The probabilistic model type.
     */
    ProbModelType getProbModelType();

}
