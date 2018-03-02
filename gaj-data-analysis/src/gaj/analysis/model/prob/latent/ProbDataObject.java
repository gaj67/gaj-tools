package gaj.analysis.model.prob.latent;

import gaj.analysis.data.DataObject;

/**
 * Encapsulates the general output from a probabilistic model.
 * 
 * <I> - The input data type.
 */
public interface ProbDataObject extends DataObject {

    /**
     * Obtains the type of the underlying probabilistic model used to obtain
     * this output.
     * 
     * @return The probabilistic model type.
     */
    ProbModelType getProbModelType();

}
