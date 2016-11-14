package gaj.analysis.model.prob;

import gaj.analysis.model.DataInput;
import gaj.analysis.model.DataModel;
import gaj.analysis.model.ParameterisedModel;
import gaj.analysis.numeric.vector.DataVector;

/**
 * A model of the conditional probability p(x|y) of an observation x for known,
 * discrete values of y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 */
public interface GenerativeModel extends DataModel, ParameterisedModel {

    /**
     * Computes the conditional probabilities p(x|y) for each y.
     * 
     * @param x
     *            - The input data.
     * @return The output object.
     */
    @Override
    GenerativeOutput process(DataInput x);

    /**
     * Obtains the prior probability p(y) for each y.
     * 
     * @return The prior probabilities.
     */
    DataVector getPriorProbabilities();

}
