package gaj.analysis.model.prob;

import org.eclipse.jdt.annotation.Nullable;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.numeric.vector.DataVector;

/**
 * A model of the conditional probability p(x|y) of an observation x for known,
 * discrete values of y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 */
public interface GenerativeModel extends ProbDataModel {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.GENERATIVE;
    }

    /**
     * Computes the conditional probabilities p(x|y) for each discrete y.
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
    GenerativeOutput process(DataVector features, @Nullable AuxiliaryInfo info);

    /**
     * Obtains the prior probability p(y) for each y.
     * 
     * @return The prior probabilities.
     */
    DataVector getPriorProbabilities();

}
