package gaj.analysis.model.prob.latent;

import gaj.analysis.data.numeric.matrix.DataMatrix;
import gaj.analysis.model.GradientAware;

/**
 * Indicates output from a discriminative model with a computed gradient.
 */
public interface DiscriminativeDataGradient extends DiscriminativeDataObject, GradientAware {

    /**
     * Computes the gradient, d_q/d_theta', of the discriminative
     * log-likelihoods, q = log p(y|x, theta), with respect to the model
     * parameters, theta.
     * 
     * @return The C x P gradient matrix, where C is the number of categories of
     *         y and P is the number of parameters in theta.
     */
    DataMatrix getDiscriminativeGradients();

}
