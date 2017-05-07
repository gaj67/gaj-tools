package gaj.analysis.model;

import gaj.analysis.numeric.DataObject;

/**
 * Indicates that a gradient can be computed upon request, typically from a
 * parameterised score or model.
 */
public interface GradientComputable extends GradientAware {

    /**
     * Computes the necessary gradient.
     * 
     * @return The gradient.
     */
    DataObject getGradient();

}