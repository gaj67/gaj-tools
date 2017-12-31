package gaj.analysis.model.prob;

import gaj.analysis.numeric.matrix.DataMatrix;
import gaj.analysis.numeric.matrix.impl.MatrixFactory;

/**
 * Indicates output from a generative model with a computed gradient.
 */
public interface GenerativeGradientOuput extends GenerativeOutput, JointGradientOuput {

    /**
     * Computes the gradient, d_q/d_theta', of the generative log-likelihoods, q
     * = log p(x|y, theta), with respect to the model parameters, theta.
     * 
     * @return The C x P gradient matrix, where C is the number of categories of
     *         y and P is the number of parameters in theta.
     */
    DataMatrix getConditionalGradients();

    /**
     * Computes the gradient, d_q/d_theta', of the generative prior
     * probabilities, q = log p(y|theta), with respect to the model parameters,
     * theta.
     * 
     * @return The C x P gradient matrix, where C is the number of categories of
     *         y and P is the number of parameters in theta.
     */
    DataMatrix getPriorGradients();

    @Override
    default DataMatrix getJointGradients() {
        return MatrixFactory.add(getPriorGradients(), getConditionalGradients());
    }

}
