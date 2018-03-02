package gaj.analysis.model.prob.generative;

import gaj.analysis.data.DataObject;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.prob.ProbDataModel;
import gaj.analysis.model.prob.ProbModelType;

/**
 * A model of the conditional probability p(x|y) of an observation x for known,
 * discrete values of y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 */
public interface GenerativeDataModel<I extends DataObject> extends ProbDataModel<I, GenerativeDataObject> {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.GENERATIVE;
    }

    /**
     * Computes the conditional probabilities p(x|y) for each discrete y.
     */
    @Override
    GenerativeDataObject process(I input, AuxiliaryInfo... info);

    /**
     * Obtains the prior probability p(y) for each y.
     * 
     * @return The prior probabilities.
     */
    DataVector getPriorProbabilities();

}
