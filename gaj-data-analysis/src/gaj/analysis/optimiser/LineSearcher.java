package gaj.analysis.optimiser;

import gaj.analysis.numeric.vector.DataVector;

/**
 * Specifies a line search algorithm to be used in conjunction with a bound
 * optimiser. Its purpose is to search in a given direction for an update to the
 * model parameters that improves the optimisation score.
 */
public interface LineSearcher {

    /**
     * Searches in the given direction d for a feasible step-size rho that
     * improves the optimisation score, giving rise to new model parameters x1 =
     * x0 + rho*d.
     * 
     * @param direction
     *            - The direction, d, in which to search.
     * @return The status of the optimisation after the search.
     */
    LineSearchStatus search(DataVector direction);

}
