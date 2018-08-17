package gaj.analysis.model.prob.discriminative;

import gaj.analysis.model.GradientAware;
import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.vector.DataVector;

/**
 * Indicates output from a discriminative model with a computed gradient.
 */
public interface DiscriminativeProbsGradient extends DiscriminativeProbs, GradientAware {

    /**
     * Computes the gradient, d_q/d_theta', of the discriminative
     * log-likelihoods, q = log p(y|x, theta), with respect to the model
     * parameters, theta.
     * 
     * @return The C x P gradient matrix, where C is the number of categories of
     *         y and P is the number of parameters in theta.
     */
    DataMatrix getDiscriminativeGradients();

    /**
     * Constructs discriminative probability gradient information.
     * 
     * @param probs
     *            - The posterior probabilities, p(y|x).
     * @param gradient
     *            - The discriminative likelihoods gradient matrix.
     * @return The discriminative gradient information.
     */
    static DiscriminativeProbsGradient newDataObject(DataVector probs, DataMatrix gradient) {
        return new DiscriminativeProbsGradient() {
            @Override
            public DataVector getPosteriorProbabilities() {
                return probs;
            }

            @Override
            public DataMatrix getDiscriminativeGradients() {
                return gradient;
            }
        };
    }

}
