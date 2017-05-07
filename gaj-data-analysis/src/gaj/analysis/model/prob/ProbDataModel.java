package gaj.analysis.model.prob;

import gaj.analysis.model.DataInput;
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

    /**
     * Computes some probability function of the input data x, possibly
     * conditioning or conditioned by a discrete response variable y.
     * 
     * @param x
     *            - The input data.
     * @param includeAuxiliary
     *            - A flag indicating whether (true) or not (false) to include
     *            auxiliary information (e.g. gradient, Hessian, etc.) in the
     *            output.
     * @return The output object containing the computed probability of the
     *         input.
     */
    @Override
    ProbDataOutput process(DataInput input, boolean includeAuxiliary);

}
