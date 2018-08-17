package gaj.analysis.model.prob.discriminative;

import gaj.analysis.model.prob.Probs;
import gaj.analysis.model.prob.ProbModelType;
import gaj.analysis.numeric.vector.DataVector;

/**
 * Encapsulates the output of a {@link DiscriminativeProbModel}.
 */
public interface DiscriminativeProbs extends Probs {

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
    static DiscriminativeProbs newDataObject(DataVector probs) {
        return new DiscriminativeProbs() {
            @Override
            public DataVector getPosteriorProbabilities() {
                return probs;
            }
        };
    }

}
