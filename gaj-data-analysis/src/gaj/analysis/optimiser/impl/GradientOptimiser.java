package gaj.analysis.optimiser.impl;

import gaj.analysis.model.ModelScorer;
import gaj.analysis.model.OptimisableModel;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;
import gaj.analysis.optimiser.GradientEnabled;
import gaj.analysis.optimiser.GradientOptimisationParams;
import gaj.analysis.optimiser.OptimisationParams;
import gaj.analysis.optimiser.OptimiserStatus;

/**
 * Implements an optimiser using linear gradient ascent/descent to
 * maximise/minimise the model score.
 */
public class GradientOptimiser extends IterativeOptimiser {

    /** Current step-size for an upcoming gradient ascent. */
    private double stepSize;

    /**
     * Current gradient or quasi-gradient direction for an upcoming gradient
     * ascent.
     */
    private DataVector direction;

    /**
     * Binds the optimiser to the given model and scorers.
     * 
     * @param model
     *            - The model to be optimised.
     * @param scorers
     *            - The scorers to measure model performance.
     */
    protected GradientOptimiser(OptimisableModel model, ModelScorer[] scorers) {
        super(model, scorers);
        if (!(getOptimisationScore() instanceof GradientEnabled))
            throw new IllegalArgumentException("The optimisation scorer must be able to compute gradients");
        computeStepSizeAndDirection();
    }

    /**
     * Computes the direction for the next iteration, and the step-size to take
     * along that direction.
     * <p/>
     * Gradient information is available in {@link #getOptimisationScore}().
     */
    protected void computeStepSizeAndDirection() {
        // TODO Normalise step-size by gradient size.
        stepSize = 0.5;
        direction = ((GradientEnabled) getOptimisationScore()).getGradient();
    }

    /**
     * Recomputes the step-size for a new step from the original point x0 along
     * the same gradient g0. Called due to a failure of the line search for the
     * current step-size, rho, for which x1 = x0 + dir*rho*g0.
     */
    protected void recomputeStepSize() {
        stepSize *= 0.5;
    }

    /**
     * Computes the direction for the next iteration, and the step-size to take
     * along that direction.
     * <p/>
     * Gradient information is available in {@link #getPrevTrainingScore}() and
     * {@link #getTrainingScore}().
     */
    protected void recomputeStepSizeAndDirection() {
        // Best guess for step-size is previous step-size.
        direction = ((GradientEnabled) getOptimisationScore()).getGradient();
    }

    @Override
    protected OptimiserStatus preUpdate(OptimisationParams params) {
        OptimiserStatus status = super.preUpdate(params);
        if (status != OptimiserStatus.RUNNING)
            return status;
        final double gradientTolerance = (params instanceof GradientOptimisationParams) 
                ? ((GradientOptimisationParams) params).gradientTolerance() : -1;
        if (gradientTolerance > 0 && direction.norm() < gradientTolerance)
            return OptimiserStatus.GRADIENT_TOO_SMALL;
        return OptimiserStatus.RUNNING;
    }

    @Override
    protected OptimiserStatus update(OptimisationParams params) {
        OptimiserStatus status = performLineSearch(params);
        if (status != OptimiserStatus.RUNNING)
            return status;
        recomputeStepSizeAndDirection();
        return OptimiserStatus.RUNNING;
    }

    /**
     * Finds a step-size rho for directed distance d such that x1 = x0 +
     * dir*rho*d and dir*[score(x1) - score(x0)] > 0, where dir = +1/-1 for
     * maximisation/minimisation.
     * 
     * @param params
     *            - The optimisation parameters.
     * 
     * @return The state of the optimiser after line search has been performed.
     */
    protected OptimiserStatus performLineSearch(OptimisationParams params) {
        double prevScore = getScores()[0];
        OptimiserStatus status = OptimiserStatus.MAX_SUB_ITERATIONS_EXCEEDED;
        double prevStepSize = 0;
        int maxSubIterations = params.maxSubIterations();
        if (maxSubIterations <= 0)
            maxSubIterations = Integer.MAX_VALUE;
        int numSubIterations = 0;
        while (numSubIterations < maxSubIterations) {
            DataVector newParams = VectorFactory.add(
                    getModel().getParameters(),
                    VectorFactory.scale(direction, params.optimisationDirection() * (stepSize - prevStepSize)));
            if (!getModel().setParameters(newParams)) {
                status = OptimiserStatus.UPDATE_FAILED;
                break;
            }
            incSubIterations();
            computeOptimisationScore();
            if (params.optimisationDirection() * (getScores()[0] - prevScore) > 0) {
                // Score has improved.
                status = OptimiserStatus.RUNNING;
                break;
            }
            // Set up further line search.
            numSubIterations++;
            prevStepSize = stepSize;
            recomputeStepSize();
        }
        return status;
    }

}
