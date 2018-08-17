package gaj.analysis.optimiser.searcher;

/**
 * Specifies the type of algorithm to use for searching along a given direction
 * to find the best model parameters update. The basic principle is to find an
 * optimum step-size rho such that x1 = x0 + rho * direction.
 */
public enum LineSearcherType {

    /**
     * Specifies a simple linear search.
     */
    LINEAR,

    /**
     * Specifies a search where the step-size is determined from a quadratic fit
     * through the gradients.
     */
    QUADRATIC,

    /**
     * Specifies a search where the step-size is determined from a cubic fit
     * through the gradients.
     */
    CUBIC,

    ;

}
