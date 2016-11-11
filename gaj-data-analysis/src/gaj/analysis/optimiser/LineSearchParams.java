package gaj.analysis.optimiser;

/**
 * Specifies the parameters for a directional line search.
 */
public interface LineSearchParams {

    /**
     * Indicates the direction of score optimisation.
     * 
     * @return A positive (or negative) value corresponding to score
     *         maximisation (or minimisation).
     */
    int getOptimisationDirection();

    /**
     * Specifies the maximum number of iterations to perform during a line
     * search.
     * 
     * @return The maximum number of search iterations, or a non-positive value
     *         if there is no maximum.
     */
    default int getMaxLineSearchIterations() {
        return 0;
    }

    /**
     * Specifies the minimum acceptable size of the direction vector in which to
     * search.
     * 
     * @return The minimum direction tolerance, or a non-positive value if the
     *         tolerance is not to be checked.
     */
    default double getMinDirectionTolerance() {
        return 0;
    }

}
