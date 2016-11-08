package gaj.analysis.optimiser.impl;

import gaj.analysis.classifier.TrainingControl;
import gaj.analysis.classifier.TrainingState;
import gaj.analysis.model.ModelScorer;
import gaj.analysis.model.OptimisableModel;
import gaj.analysis.model.ScoreInfo;
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

    @Override
    protected OptimiserStatus preUpdate(OptimisationParams params) {
        OptimiserStatus status = super.preUpdate(params);
        if (status != OptimiserStatus.NOT_HALTED)
            return status;
        final double gradientTolerance = (params instanceof GradientOptimisationParams) 
                ? ((GradientOptimisationParams) params).gradientTolerance() : -1;
        if (gradientTolerance > 0 && direction.norm() < gradientTolerance)
            return OptimiserStatus.GRADIENT_TOO_SMALL;
        return OptimiserStatus.NOT_HALTED;
    }

    @Override
    protected OptimiserStatus update(OptimisationParams params) {
        double[] newScores = new double[numScores()];
        ScoreInfo[] newOptimisationScore = new ScoreInfo[1];
        OptimiserStatus status = performLineSearch(newOptimisationScore);
        if (status != OptimiserStatus.NOT_HALTED)
            return status;
        newScores[0] = newOptimisationScore[0].getScore();
        computeValidationScores(newScores);
        setScores(newOptimisationScore[0], newScores);
        recomputeStepSizeAndDirection();
        return OptimiserStatus.NOT_HALTED;
    }

    /**
     * Finds a step-size rho for direction d such that
     * x1 = x0 + rho*d and score(x1) > score(x0).
     * 
     * @param newInfo - Place-holder for the training score gradient, f'(x1).
     * @param newScores - Place-holder for the new training score, f(x1).
     * @return The state of the trainer.
     */
    protected TrainingState performLineSearch(ScoreInfo[] newInfo, double[] newScores) {
        TrainingState state = TrainingState.MAX_SUB_ITERATIONS_EXCEEDED;
        double prevStepSize = 0;
        final TrainingControl control = getControl();
        int maxSubIterations = control.maxSubIterations();
        if (maxSubIterations <= 0)
            maxSubIterations = Integer.MAX_VALUE;
        int numSubIterations = 0;
        while (numSubIterations < maxSubIterations) {
            DataVector newParams = VectorFactory.add(
                    model.getParameters(),
                    VectorFactory.scale(direction, stepSize - prevStepSize));
            if (!model.setParameters(newParams)) {
                state = TrainingState.UPDATE_FAILED;
                break;
            }
            ScoreInfo newTrainingScore = computeOptimisationScore(newScores);
            if (newScores[0] > getScores()[0]) { // Score has improved.
                newInfo[0] = newTrainingScore;
                state = TrainingState.NOT_HALTED;
                break;
            }
            // Set up further line search.
            numSubIterations++;
            prevStepSize = stepSize;
            recomputeStepSize(newTrainingScore);
        }
        incIterations(numSubIterations); // Count minor iterations.
        return state;
    }

    /**
     * Recomputes the step-size for a new step from the original point x0 along
     * the same gradient g0. Called due to a failure of the line search for the
     * current step-size, rho, for which x1 = x0 + rho*g0.
     * 
     * @param newScore
     */
    protected void recomputeStepSize(ScoreInfo newScore) {
        stepSize *= 0.5;
    }

    /**
     * Computes the direction for the next iteration, and the step-size
     * to take along that direction.
     * <p/>
     * Gradient information is available in {@link #getPrevTrainingScore}() and {@link #getTrainingScore}().
     */
    protected void recomputeStepSizeAndDirection() {
        // Best guess for step-size is previous step-size.
        direction = ((GradientEnabled) getOptimisationScore()).getGradient();
    }

}
