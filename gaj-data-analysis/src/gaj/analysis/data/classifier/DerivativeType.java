package gaj.analysis.data.classifier;

/**
 * Specifies the highest type of parameter optimisation that may be used by a parameterised classifier.
 */
public enum DerivativeType {

    /**
     * Indicates that no derivative information is available.
     */
    NONE,
    /**
     * Indicates that first derivative information is available.
     */
    FIRST,
    /**
     * Indicates that both first and second derivative information is available.
     */
    SECOND;
}
