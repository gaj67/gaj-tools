package gaj.analysis.optimiser.impl;

import gaj.analysis.model.ScoreInfo;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;
import gaj.analysis.optimiser.LineSearchParams;
import gaj.analysis.optimiser.LineSearchStatus;
import gaj.analysis.optimiser.LineSearcher;

/**
 * Implements the underlying line searcher algorithm.
 */
public abstract class BaseLineSearcher implements LineSearcher {

    private final UpdatableOptimser optimiser;

    /**
     * Binds the line search to the updatable optimiser.
     * 
     * @param optimiser
     *            - The optimiser to be updated.
     */
    protected BaseLineSearcher(UpdatableOptimser optimiser) {
        this.optimiser = optimiser;
    }

    /**
     * Obtains the underlying optimiser for this algorithm.
     * 
     * @return The updatable optimiser.
     */
    protected UpdatableOptimser getOptimiser() {
        return optimiser;
    }

    @Override
    public LineSearchStatus search(DataVector direction, LineSearchParams params) {
        final ScoreInfo prevScore = optimiser.getScoreInfo();
        double prevStepSize = 0;
        double stepSize = computeStepSize(direction, prevScore, params);
        LineSearchStatus status = checkStepSize(stepSize, direction, prevScore, params);
        if (status != LineSearchStatus.SUCCESSFUL) return status;
        final int maxSubIterations = (params.getMaxLineSearchIterations() > 0) 
                ? params.getMaxLineSearchIterations() : Integer.MAX_VALUE;
        int numSubIterations = 0;
        while (numSubIterations < maxSubIterations) {
            DataVector newParams = VectorFactory.add(
                    optimiser.getModelParameters(),
                    VectorFactory.scale(direction, params.getOptimisationDirection() * (stepSize - prevStepSize)));
            if (!optimiser.setModelParameters(newParams)) {
                return LineSearchStatus.PARAMETER_UPDATE_FAILED;
            }
            optimiser.incNumSubIterations();
            optimiser.computeOptimisationScore();
            if (scoreImproved(params, prevScore)) return LineSearchStatus.SUCCESSFUL;
            // Set up further line search.
            prevStepSize = stepSize;
            stepSize = recomputeStepSize(stepSize, direction, prevScore, params);
            status = checkStepSize(stepSize, direction, prevScore, params);
            if (status != LineSearchStatus.SUCCESSFUL) return status;
        }
        return LineSearchStatus.MAX_ITERATIONS_EXCEEDED;
    }

    /**
     * Computes the initial step-size for the first direction step.
     * 
     * @param direction
     *            - The search direction.
     * @param prevScore
     *            - The initial optimisation score information.
     * @param params
     *            - The line search parameters.
     * @return The initial step-size.
     */
    protected double computeStepSize(DataVector direction, ScoreInfo prevScore, LineSearchParams params) {
        // TODO Control step-size according to direction norm and tolerances.
        return 1;
    }

    /**
     * Recomputes a new step-size for the next direction step.
     * 
     * @param prevStepSize
     *            - The previous step-size used.
     * @param direction
     *            - The search direction.
     * @param prevScore
     *            - The initial optimisation score information.
     * @param params
     *            - The line search parameters.
     * @return The new step-size.
     */
    abstract protected double recomputeStepSize(double prevStepSize, DataVector direction, ScoreInfo prevScore,
            LineSearchParams params);

    /**
     * Checks that the magnitude of the update of the model parameters is within
     * limits.
     * 
     * @param stepSize
     *            - The current step-size to use.
     * @param direction
     *            - The search direction.
     * @param prevScore
     *            - The initial optimisation score information.
     * @param params
     *            - The line search parameters.
     * @return A status of {@link LineSearchStatus#SUCCESSFUL} if the direction
     *         step is acceptable, or an error status if it is not.
     */
    protected LineSearchStatus checkStepSize(double stepSize, DataVector direction, ScoreInfo prevScore,
            LineSearchParams params) 
    {
        if (stepSize <= 0) return LineSearchStatus.DIRECTION_STEP_TOO_SMALL;
        if (params.getMinDirectionStep() > 0 || params.getMaxDirectionStep() > 0) {
            double directionStep = stepSize * direction.norm();
            if (params.getMinDirectionStep() > 0 && directionStep < params.getMinDirectionStep())
                return LineSearchStatus.DIRECTION_STEP_TOO_SMALL;
            if (params.getMaxDirectionStep() > 0 && directionStep > params.getMaxDirectionStep())
                return LineSearchStatus.DIRECTION_STEP_TOO_LARGE;
        }
        return LineSearchStatus.SUCCESSFUL;
    }

    /**
     * Determines whether or not the optimisation score has been improved.
     * 
     * @param params
     *            - The line search parameters.
     * @param prevScore
     *            - The initial optimisation score.
     * @return A value of true (or false) if the score has (or has not)
     *         improved.
     */
    protected boolean scoreImproved(LineSearchParams params, ScoreInfo prevScore) {
        // TODO Implement Wolfe conditions.
        return (params.getOptimisationDirection() * (optimiser.getScores()[0] - prevScore.getScore()) > 0);
    }

}
