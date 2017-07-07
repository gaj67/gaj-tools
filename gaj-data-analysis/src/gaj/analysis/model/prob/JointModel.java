package gaj.analysis.model.prob;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataObject;

/**
 * A model of the joint probability p(y,x) of an observation x for known,
 * discrete values of y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 */
public interface JointModel extends ProbDataModel {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.JOINT;
    }

    /**
     * Computes the joint probabilities p(y,x) for each discrete y.
     * 
     * @param input
     *            - The input data, x.
     * @param info
     *            - An object either specifying auxiliary information for the
     *            processor, or requesting auxiliary information be provided.
     * @return The output object.
     */
    @Override
    JointOutput process(DataObject input, AuxiliaryInfo info);

}
