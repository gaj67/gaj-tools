package gaj.analysis.optimiser.impl;

import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;
import gaj.analysis.optimiser.LineSearchParams;
import gaj.analysis.optimiser.LineSearchStatus;
import gaj.analysis.optimiser.LineSearcher;
import gaj.analysis.optimiser.LineSearcherType;

/**
 * Implements {@link LineSearcherType#LINEAR}.
 */
public class LinearLineSearcher implements LineSearcher {

    private final UpdatableOptimser optimiser;

    /**
     * Binds the line search to the updatable optimiser.
     * 
     * @param optimiser
     *            - The optimiser to be updated.
     */
    public LinearLineSearcher(UpdatableOptimser optimiser) {
        this.optimiser = optimiser;
    }

    @Override
    public LineSearchStatus search(DataVector direction, LineSearchParams params) {
        double prevStepSize = 0;
        double stepSize = 1;
        if (params.getMinDirectionTolerance() > 0) {
            final double norm = direction.norm();
            if (params.getMinDirectionTolerance() > 0 && norm < params.getMinDirectionTolerance()) {
                return LineSearchStatus.DIRECTION_TOO_SMALL;
            }
        }
        // TODO Control step-size according to direction norm and tolerances.
        final double prevScore = optimiser.getScores()[0];
        final int maxSubIterations = (params.getMaxLineSearchIterations() > 0) 
                ? params.getMaxLineSearchIterations()
                : Integer.MAX_VALUE;
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
            if (scoreImproved(params, prevScore)) {
                return LineSearchStatus.SUCCESSFUL;
            }
            // Set up further line search.
            prevStepSize = stepSize;
            stepSize *= 0.5;
        }
        return LineSearchStatus.MAX_ITERATIONS_EXCEEDED;
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
    protected boolean scoreImproved(LineSearchParams params, double prevScore) {
        // TODO Implement Wolfe conditions.
        return (params.getOptimisationDirection() * (optimiser.getScores()[0] - prevScore) > 0);
    }

}
