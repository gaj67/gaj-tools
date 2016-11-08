package gaj.analysis.optimiser;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Indicates that a gradient vector can be computed upon request.
 */
public interface GradientEnabled {

    /**
     * Computes the necessary gradient.
     * 
     * @return The gradient vector.
     */
    DataVector getGradient();

}
