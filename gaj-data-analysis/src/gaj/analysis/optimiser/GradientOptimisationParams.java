package gaj.analysis.optimiser;

/**
 * Specifies the optimisation parameter settings for a gradient-aware optimiser.
 */
public interface GradientOptimisationParams extends OptimisationParams {

    /**
     * Specifies the smallest feasible norm of the score gradient, below which
     * training will cease. This will only be checked if gradient-based
     * optimisation is being performed.
     * 
     * @return The minimum score gradient tolerance, or a non-positive value if
     *         the gradient is not to be checked.
     */
    double gradientTolerance();

    // TODO Allow specification of the line search sub-algorithm.

}
