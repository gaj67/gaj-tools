package gaj.analysis.model.prob;

import org.eclipse.jdt.annotation.Nullable;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.numeric.vector.DataVector;

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
     * @param features
     *            - The input feature vector, x.
     * @param info
     *            - Optional object either specifying auxiliary information for
     *            the processor, or requesting auxiliary information be
     *            provided.
     * @return The output object.
     */
    @Override
    JointOutput process(DataVector features, @Nullable AuxiliaryInfo info);

}
