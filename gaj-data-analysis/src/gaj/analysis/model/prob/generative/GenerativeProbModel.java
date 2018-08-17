package gaj.analysis.model.prob.generative;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.prob.ProbModel;
import gaj.analysis.model.prob.ProbModelType;
import gaj.analysis.numeric.vector.DataVector;

/**
 * A model of the conditional probability p(x|y) of an observation x for known,
 * discrete values of y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 * 
 * <I> - The input data type.
 */
public interface GenerativeProbModel<I> extends ProbModel<I> {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.GENERATIVE;
    }

    /**
     * Computes the conditional probabilities p(x|y) for each discrete y.
     */
    @Override
    GenerativeProbs process(I input, AuxiliaryInfo... info);

    /**
     * Obtains the prior probability p(y) for each y.
     * 
     * @return The prior probabilities.
     */
    DataVector getPriorProbabilities();

}
