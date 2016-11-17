package gaj.analysis.model.prob;

import gaj.analysis.model.DataInput;

/**
 * A model of the probability (likelihood) p(x) of an observation x.
 */
public interface LikelihoodModel extends ProbDataModel {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.LIKELIHOOD;
    }

    /**
     * Computes the data likelihood p(x).
     * 
     * @param x
     *            - The input data.
     * @param includeAuxiliary
     *            - A flag indicating whether (true) or not (false) to include
     *            auxiliary information (e.g. gradient, Hessian, etc.) in the
     *            output.
     * @return The output object.
     */
    @Override
    LikelihoodOutput process(DataInput x, boolean includeAuxiliary);

}
