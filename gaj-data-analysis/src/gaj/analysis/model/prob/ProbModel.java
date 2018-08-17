package gaj.analysis.model.prob;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.ParameterisedModel;

/**
 * An arbitrary probabilistic model of an observation x, possibly for an 
 * additional discrete response variable y. Typically, y is a label for 
 * either a class (for a supervised model) or a cluster (for an unsupervised model).
 * 
 * <I> - The input data type.
 */
public interface ProbModel<I> extends ParameterisedModel<I, Probs> {

    /**
     * Specifies the type of probability distribution encapsulated by the model.
     * 
     * @return The probabilistic model type.
     */
    ProbModelType getProbModelType();

    /**
     * Computes the necessary probabilities as a function of the input, x.
     */
    @Override
    Probs process(I input, AuxiliaryInfo... info);

}
