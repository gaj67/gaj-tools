package gaj.analysis.classifier;

import gaj.analysis.curves.CurveFactory;
import gaj.analysis.vector.VectorFactory;
import gaj.data.classifier.ClassifierScoreInfo;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.ParameterisedClassifier;
import gaj.data.classifier.TrainingControl;
import gaj.data.classifier.TrainingState;
import gaj.data.vector.DataVector;

/**
 * Implements a classifier trainer using
 * gradient ascent to maximise the score(s)
 * on one or more fixed data sets.
 */
public class GradientAscentTrainer extends BaseTrainer {

	/**
	 * Binds the training algorithm to the given classifier and scorers.
	 * 
	 * @param classifier - The classifier to be trained.
	 * @param scorers - The data scorers to measure classifier performance.
	 */
	protected GradientAscentTrainer(ParameterisedClassifier classifier,
			DataScorer[] scorers) {
		super(classifier, scorers);
		computeStepSizeAndDirection();
	}

	/** Maximum angle (in degrees) between vectors for projection. */
	private static final double MAX_ANGLE = 45;
	/** Helps measure the 'closeness' between two vectors. */
	private static final double CLOSENESS = Math.cos(Math.PI * MAX_ANGLE / 180);
	/** Current step-size for an upcoming gradient ascent. */
	private double stepSize;
	/** Current gradient or quasi-gradient direction for an upcoming gradient ascent. */
	private DataVector direction;

	/**
	 * Computes the direction for the next iteration, and the step-size 
	 * to take along that direction.
	 * <p/>Gradient information is available in {@link #getTrainingScore}().
	 */
	private void computeStepSizeAndDirection() {
		// TODO Normalise step-size by gradient size.
		stepSize = 0.5;
		direction = getTrainingScore().getGradient();
	}

	@Override
	protected TrainingState preTerminate() {
		TrainingState state = super.preTerminate();
		if (state != TrainingState.NOT_HALTED) return state;
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
		if (state != TrainingState.NOT_HALTED) return state;
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
	private TrainingState performLineSearch(ClassifierScoreInfo[] newInfo, double[] newScores) {
		double rho = stepSize;
		final TrainingControl control = getControl();
		int maxSubIterations = control.maxSubIterations();
		if (maxSubIterations <= 0) maxSubIterations = Integer.MAX_VALUE;
		int numSubIterations = 0;
		while (numSubIterations < maxSubIterations) {
			DataVector newParams = VectorFactory.add(
					classifier.getParameters(), 
					VectorFactory.scale(direction, rho));
			if (!classifier.setParameters(newParams)) return TrainingState.UPDATE_FAILED;
			ClassifierScoreInfo newTrainingScore = computeTrainingScore(newScores);
			if (newScores[0] > getScores()[0]) { // Score has improved.
				incIterations(numSubIterations); // Count minor iterations.
				newInfo[0] = newTrainingScore;
				return TrainingState.NOT_HALTED;
			}
			// Set up further line search.
			numSubIterations++;
			recomputeStepSize(newTrainingScore);
			rho = stepSize - Math.abs(rho);
		}
		return TrainingState.MAX_SUB_ITERATIONS_EXCEEDED;
	}

	/**
	 * Recomputes the step-size for a new step from
	 * the original point x0 along the same gradient g0. 
	 * Called due to a failure of
	 * the line search for the current step-size, rho,
	 * for which x1 = x0 + rho*g0.
	 * @param newTrainingScore 
	 */
	protected void recomputeStepSize(ClassifierScoreInfo newTrainingScore) {
		// TODO Use quadratic or cubic acceleration.
		//double rho = CurveFactory.quadraticOptimum(stepSize, gradient, newGradient);
		stepSize *= 0.5;
	}

	/**
	 * Computes the direction for the next iteration, and the step-size 
	 * to take along that direction.
	 * <p/>Gradient information is available in 
	 * {@link #getPrevTrainingScore}() and {@link #getTrainingScore}().
	 */
	private void recomputeStepSizeAndDirection() {
		// Keep previous step-size in both cases below.
		if (getControl().useAcceleration()) {
			// TODO Try cubic acceleration.
			final DataVector g0 = getPrevTrainingScore().getGradient();
			final DataVector g1 = getTrainingScore().getGradient();
			direction = CurveFactory.quadraticOptimumDisplacement(g0, g1, direction, CLOSENESS);
		} else {
			direction = getTrainingScore().getGradient();
		}
	}

}
