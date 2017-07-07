package gaj.analysis.model.prob;

import gaj.analysis.model.GradientAware;
import gaj.analysis.numeric.vector.DataVector;

/**
 * Indicates output from a marginal model with a computed gradient.
 */
public interface MarginalGradientOuput extends MarginalOutput, GradientAware {

    /**
     * Computes the gradient, d_q/d_theta, of the marginal log-likelihood, q =
     * log p(x|theta), with respect to the model parameters, theta.
     * 
     * @return The length-P gradient vector, where P is the number of parameters
     *         in theta.
     */
    DataVector getDataGradient();

}
