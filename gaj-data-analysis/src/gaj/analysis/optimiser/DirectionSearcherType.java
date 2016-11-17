package gaj.analysis.optimiser;

/**
 * Specifies the type of algorithm to use for determining the parameter-update
 * search direction.
 */
public enum DirectionSearcherType {

    /**
     * Specifies a direct gradient ascent or descent search.
     */
    GRADIENT,

    /**
     * Specifies a limited-memory Broyden–Fletcher–Goldfarb–Shanno quasi-Newton
     * search.
     */
    LBFGS,

    ;

}
