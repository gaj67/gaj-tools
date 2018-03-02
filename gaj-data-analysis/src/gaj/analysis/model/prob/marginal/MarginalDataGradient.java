package gaj.analysis.model.prob.marginal;

import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.model.GradientAware;

/**
 * Indicates output from a marginal model with a computed gradient.
 */
public interface MarginalDataGradient extends MarginalDataObject, GradientAware {

    /**
     * Computes the gradient, d_q/d_theta, of the marginal log-likelihood, q =
     * log p(x|theta), with respect to the model parameters, theta.
     * 
     * @return The length-P gradient vector, where P is the number of parameters
     *         in theta.
     */
    DataVector getMarginalGradient();

}
