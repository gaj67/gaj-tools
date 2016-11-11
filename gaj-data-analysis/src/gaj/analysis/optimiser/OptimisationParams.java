package gaj.analysis.optimiser;

import com.sun.istack.internal.Nullable;

public interface OptimisationParams {

    /**
     * Indicates the direction of score optimisation.
     * 
     * @return A positive (or negative) value corresponding to score
     *         maximisation (or minimisation).
     */
    int getOptimisationDirection();

    /**
     * Specifies the maximum number of update iterations to perform during
     * optimisation.
     * 
     * @return The maximum number of iterations, or a non-positive value if
     *         there is no maximum.
     */
    default int getMaxIterations() {
        return 0;
    }

    /**
     * Specifies the smallest difference in accuracy scores between update
     * iterations, below which optimisation will cease.
     * 
     * @return The minimum score tolerance, or a non-positive value if tolerance
     *         is not to be checked.
     */
    default double getScoreTolerance() {
        return 0;
    }

    /**
     * Specifies the smallest relative difference in accuracy scores between
     * update iterations, below which optimisation will cease.
     * 
     * @return The minimum relative score tolerance, or a non-positive value if
     *         tolerance is not to be checked.
     */
    default double getRelativeScoreTolerance() {
        return 0;
    }

    /**
     * Specifies the type of algorithm to use for searching for a direction in
     * which to update the model parameters.
     * 
     * @return The direction search algorithm type, or a value of null to use
     *         the default algorithm.
     */
    default @Nullable DirectionSearcherType getDirectionSearchType() {
        return null;
    }
    
    /**
     * Specifies the type of algorithm to use for searching along a given
     * direction in order to update the model parameters.
     * 
     * @return The line search algorithm type, or a value of null to use the
     *         default algorithm.
     */
    default @Nullable LineSearcherType getLineSearchType() {
        return null;
    }
    
}
