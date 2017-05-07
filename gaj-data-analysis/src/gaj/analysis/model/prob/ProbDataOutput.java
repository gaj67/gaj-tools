package gaj.analysis.model.prob;

import gaj.analysis.model.DataOutput;

/**
 * Encapsulates the general output from a probabilistic model.
 */
public interface ProbDataOutput extends DataOutput {

    /**
     * Obtains the type of the underlying probabilistic model.
     * 
     * @return The probabilistic model type.
     */
    ProbModelType getProbModelType();

}
