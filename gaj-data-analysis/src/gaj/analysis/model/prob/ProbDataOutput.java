package gaj.analysis.model.prob;

import gaj.analysis.model.DataObject;
import gaj.analysis.numeric.vector.DataVector;

/**
 * Encapsulates the general output from a probabilistic model.
 */
public interface ProbDataOutput extends DataObject {

    /**
     * Obtains the type of the underlying probabilistic model used to obtain
     * this output.
     * 
     * @return The probabilistic model type.
     */
    ProbModelType getProbModelType();

    /**
     * Obtains the features that were input into the probabilistic model.
     * 
     * @return The feature vector.
     */
    DataVector getFeatures();

}
