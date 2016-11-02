package gaj.analysis.classifier.impl;

import gaj.analysis.classifier.ClassifierScoreInfo;
import gaj.analysis.classifier.DataScorer;
import gaj.analysis.classifier.ParameterisedClassifier;
import gaj.analysis.classifier.TrainingControl;
import gaj.analysis.classifier.TrainingState;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;

/**
 * Implements a classifier trainer using
 * linear gradient ascent to maximise the score(s)
 * on one or more fixed data sets.
 */
public class GradientAscentTrainer extends BaseTrainer {

    /**
     * Binds the training algorithm to the given classifier and scorers.
     * 
     * @param classifier - The classifier to be trained.
     * @param scorers - The data scorers to measure classifier performance.
     */
    protected GradientAscentTrainer(ParameterisedClassifier classifier, DataScorer[] scorers) {
        super(classifier, scorers);
        computeStepSizeAndDirection();
    }

    /** Current step-size for an upcoming gradient ascent. */
    protected double stepSize;
    /** Current gradient or quasi-gradient direction for an upcoming gradient ascent. */
    protected DataVector direction;

    /**
     * Computes the direction for the next iteration, and the step-size
     * to take along that direction.
     * <p/>
     * Gradient information is available in {@link #getTrainingScore}().
     */
    protected void computeStepSizeAndDirection() {
        // TODO Normalise step-size by gradient size.
        stepSize = 0.5;
        direction = getTrainingScore().getGradient();
    }

    @Override
    protected TrainingState preTerminate() {
        TrainingState state = super.preTerminate();
        if (state != TrainingState.NOT_HALTED)
            return state;
        final TrainingControl control = getControl();
        return (control.gradientTolerance() > 0
        && direction.norm() < control.gradientTolerance())
                ? TrainingState.GRADIENT_TOO_SMALL : TrainingState.NOT_HALTED;
    }

    @Override
    protected TrainingState update() {
        double[] newScores = new double[numScores()];
        ClassifierScoreInfo[] newTrainingScore = new ClassifierScoreInfo[1];
        TrainingState state = performLineSearch(newTrainingScore, newScores);
        if (state != TrainingState.NOT_HALTED)
            return state;
        computeTestingScores(newScores);
        updateScores(newTrainingScore[0], newScores);
        recomputeStepSizeAndDirection();
        return TrainingState.NOT_HALTED;
    }

    /**
     * Finds a step-size rho for direction d such that
     * x1 = x0 + rho*d and score(x1) > score(x0).
     * 
     * @param newInfo - Place-holder for the training score gradient, f'(x1).
     * @param newScores - Place-holder for the new training score, f(x1).
     * @return The state of the trainer.
     */
    protected TrainingState performLineSearch(ClassifierScoreInfo[] newInfo, double[] newScores) {
        TrainingState state = TrainingState.MAX_SUB_ITERATIONS_EXCEEDED;
        double prevStepSize = 0;
        final TrainingControl control = getControl();
        int maxSubIterations = control.maxSubIterations();
        if (maxSubIterations <= 0)
            maxSubIterations = Integer.MAX_VALUE;
        int numSubIterations = 0;
        while (numSubIterations < maxSubIterations) {
            DataVector newParams = VectorFactory.add(
                    classifier.getParameters(),
                    VectorFactory.scale(direction, stepSize - prevStepSize));
            if (!classifier.setParameters(newParams)) {
                state = TrainingState.UPDATE_FAILED;
                break;
            }
            ClassifierScoreInfo newTrainingScore = computeTrainingScore(newScores);
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
     * Recomputes the step-size for a new step from
     * the original point x0 along the same gradient g0.
     * Called due to a failure of
     * the line search for the current step-size, rho,
     * for which x1 = x0 + rho*g0.
     * 
     * @param newTrainingScore
     */
    protected void recomputeStepSize(ClassifierScoreInfo newTrainingScore) {
        stepSize *= 0.5;
    }

    /**
     * Computes the direction for the next iteration, and the step-size
     * to take along that direction.
     * <p/>
     * Gradient information is available in {@link #getPrevTrainingScore}() and {@link #getTrainingScore}().
     */
    protected void recomputeStepSizeAndDirection() {
        // Best guess is previous step-size.
        direction = getTrainingScore().getGradient();
    }

}
