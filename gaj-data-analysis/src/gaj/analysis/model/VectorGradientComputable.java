package gaj.analysis.model;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Indicates that a gradient vector can be computed upon request, typically from
 * a parameterised score or model.
 */
public interface VectorGradientComputable extends GradientComputable {

    /**
     * Computes the necessary gradient.
     * 
     * @return The gradient vector.
     */
    @Override
    DataVector getGradient();

}
