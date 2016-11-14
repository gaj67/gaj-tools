package gaj.analysis.model;

/**
 * A model of the probability (likelihood) p(x) of an observation x.
 */
public interface LikelihoodModel extends DataModel, ParameterisedModel {

    /**
     * Computes the data likelihood p(x).
     * 
     * @param x
     *            - The input data.
     * @return The output object.
     */
    @Override
    LikelihoodOutput process(DataInput x);

}
