package gaj.analysis.optimiser;

/**
 * Specifies the type of algorithm to use for searching along a given direction
 * to find the best model parameters update.
 */
public enum LineSearcherType {

    /**
     * Specifies a simple linear search of the form x1 = x0 + rho * direction.
     */
    LINEAR;

}
