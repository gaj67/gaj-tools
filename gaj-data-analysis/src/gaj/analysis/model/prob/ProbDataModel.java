package gaj.analysis.model.prob;

import gaj.analysis.model.DataModel;
import gaj.analysis.model.ParameterisedModel;

/**
 * An arbitrary probabilistic model of an observation x, possibly for a discrete
 * response variable y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 */
public interface ProbDataModel extends DataModel, ParameterisedModel {

    /**
     * Specifies the type of probability distribution encapsulated by the model.
     * 
     * @return The probabilistic model type.
     */
    ProbModelType getProbModelType();

}
