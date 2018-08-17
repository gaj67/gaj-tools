package gaj.analysis.model.prob.joint;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.prob.ProbModel;
import gaj.analysis.model.prob.ProbModelType;

/**
 * A model of the joint probability p(y,x) of an observation x for known,
 * discrete values of y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 * 
 * <I> - The input data type.
 */
public interface JointProbModel<I> extends ProbModel<I> {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.JOINT;
    }

    /**
     * Computes the joint probabilities p(y,x) for each discrete y.
     */
    @Override
    JointProbs process(I input, AuxiliaryInfo... info);

}
