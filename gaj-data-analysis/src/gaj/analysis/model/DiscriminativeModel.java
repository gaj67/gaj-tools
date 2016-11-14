package gaj.analysis.model;

/**
 * A model of the posterior probability p(y|x) of an observation x for known,
 * discrete values of y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 */
public interface DiscriminativeModel extends DataModel, ParameterisedModel {

    /**
     * Computes the posterior probabilities p(y|x) for each y.
     * 
     * @param x
     *            - The input data.
     * @return The output object.
     */
    @Override
    DiscriminativeOutput process(DataInput x);

}
