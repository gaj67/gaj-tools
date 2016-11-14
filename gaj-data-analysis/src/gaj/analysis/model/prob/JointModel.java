package gaj.analysis.model.prob;

import gaj.analysis.model.DataInput;
import gaj.analysis.model.DataModel;
import gaj.analysis.model.ParameterisedModel;

/**
 * A model of the joint probability p(y,x) of an observation x for known,
 * discrete values of y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 */
public interface JointModel extends DataModel, ParameterisedModel {

    /**
     * Computes the joint probabilities p(y,x) for each y.
     * 
     * @param x
     *            - The input data.
     * @return The output object.
     */
    @Override
    JointOutput process(DataInput x);

}
