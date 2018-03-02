package gaj.analysis.model.prob.latent;

import gaj.analysis.data.DataObject;
import gaj.analysis.model.AuxiliaryInfo;

/**
 * A model of the joint probability p(y,x) of an observation x for known,
 * discrete values of y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 */
public interface JointDataModel<I extends DataObject> extends ProbDataModel<I, JointDataObject> {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.JOINT;
    }

    /**
     * Computes the joint probabilities p(y,x) for each discrete y.
     */
    @Override
    JointDataObject process(I input, AuxiliaryInfo... info);

}
