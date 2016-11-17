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
    private final LineSearchParams params;

    /**
     * Binds the line search to the updatable optimiser and the search
     * parameters.
     * 
     * @param optimiser
     *            - The optimiser to be updated.
     * @param params
     *            - The parameters controlling the termination of the line
     *            search.
     */
    protected BaseLineSearcher(UpdatableOptimser optimiser, LineSearchParams params) {
        this.optimiser = optimiser;
        this.params = params;
    }

    /**
     * Obtains the underlying optimiser for this algorithm.
     * 
     * @return The updatable optimiser.
     */
    protected UpdatableOptimser getOptimiser() {
        return optimiser;
    }

    /**
     * Obtains the parameters controlling the line search.
     * 
     * @return The line search parameters.
     */
    protected LineSearchParams getParams() {
        return params;
    }

    @Override
    public LineSearchStatus search(DataVector direction) {
        final ScoreInfo prevScore = optimiser.getScoreInfo();
        double prevStepSize = 0;
        double stepSize = computeStepSize(direction, prevScore);
        LineSearchStatus status = checkStepSize(stepSize, direction, prevScore);
        if (status != LineSearchStatus.SUCCESSFUL) return status;
        final int maxSubIterations = (params.getMaxLineSearchIterations() > 0) 
                ? params.getMaxLineSearchIterations() : Integer.MAX_VALUE;
        int numSubIterations = 0;
        while (numSubIterations < maxSubIterations) {
            DataVector newParams = VectorFactory.add(
                    optimiser.getModelParameters(),
                    VectorFactory.scale(direction, params.getDirectionSign() * (stepSize - prevStepSize)));
            if (!optimiser.setModelParameters(newParams)) {
                return LineSearchStatus.PARAMETER_UPDATE_FAILED;
            }
            optimiser.incNumSubIterations();
            optimiser.computeOptimisationScore();
            if (scoreImproved(params, prevScore)) return LineSearchStatus.SUCCESSFUL;
            // Set up further line search.
            prevStepSize = stepSize;
            stepSize = recomputeStepSize(stepSize, direction, prevScore);
            status = checkStepSize(stepSize, direction, prevScore);
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
     * @return The initial step-size.
     */
    protected double computeStepSize(DataVector direction, ScoreInfo prevScore) {
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
     * @return The new step-size.
     */
    abstract protected double recomputeStepSize(double prevStepSize, DataVector direction, ScoreInfo prevScore);

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
     * @return A status of {@link LineSearchStatus#SUCCESSFUL} if the direction
     *         step is acceptable, or an error status if it is not.
     */
    protected LineSearchStatus checkStepSize(double stepSize, DataVector direction, ScoreInfo prevScore) 
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
        return (params.getDirectionSign() * (optimiser.getScores()[0] - prevScore.getScore()) > 0);
    }

}
