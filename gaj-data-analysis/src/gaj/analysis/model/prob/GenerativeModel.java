package gaj.analysis.model.prob;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataObject;
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
     * @param input
     *            - The input data, x.
     * @param info
     *            - An object either specifying auxiliary information for the
     *            processor, or requesting auxiliary information be provided.
     * @return The output object.
     */
    @Override
    GenerativeOutput process(DataObject input, AuxiliaryInfo info);

    /**
     * Obtains the prior probability p(y) for each y.
     * 
     * @return The prior probabilities.
     */
    DataVector getPriorProbabilities();

}
