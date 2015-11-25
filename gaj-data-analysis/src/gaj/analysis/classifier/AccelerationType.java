package gaj.analysis.classifier;

/**
 * Indicates which curve-fitting acceleration scheme to apply to classifier training.
 */
public enum AccelerationType {

    /** No curve fitting. */
    Linear,
    /** Fit a quadratic curve. */
    Quadratic,
    /** Fit a cubic curve. */
    Cubic;
}
