package gaj.analysis.model.prob.discriminative;

import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.model.prob.ProbDataObject;
import gaj.analysis.model.prob.ProbModelType;

/**
 * Encapsulates the output of a {@link DiscriminativeDataModel}.
 */
public interface DiscriminativeDataObject extends ProbDataObject {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.DISCRIMINATIVE;
    }

    /**
     * Obtains the posterior probabilities p(y|x) of the input data x for each
     * indexable label y.
     * 
     * @return The posterior probabilities.
     */
    DataVector getPosteriorProbabilities();

    /**
     * Constructs discriminative probability information.
     * 
     * @param probs
     *            - The posterior probabilities, p(y|x).
     * @return The discriminative information.
     */
    static DiscriminativeDataObject newDataObject(DataVector probs) {
        return new DiscriminativeDataObject() {
            @Override
            public DataVector getPosteriorProbabilities() {
                return probs;
            }
        };
    }

}
