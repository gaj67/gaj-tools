package gaj.analysis.model;

import gaj.analysis.numeric.DataNumeric;

/**
 * Indicates that a gradient can be computed upon request, typically from a
 * parameterised score or model.
 */
public interface GradientComputable<T extends DataNumeric> extends GradientAware {

    /**
     * Computes the necessary gradient.
     * 
     * @return The gradient.
     */
    T getGradient();

}
