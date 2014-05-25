package gaj.analysis.classifier;

import gaj.analysis.curves.CurveFactory;
import gaj.analysis.vector.VectorFactory;
import gaj.data.classifier.ClassifierScoreInfo;
import gaj.data.classifier.ParameterisedClassifier;
import gaj.data.classifier.TrainingControl;
import gaj.data.vector.DataVector;

/**
 * Implements a classifier trainer using
 * gradient ascent to maximise the score(s)
 * on one or more fixed data sets.
 */
public class GradientAscentTrainer extends BaseTrainer {

	/** Maximum angle (in degrees) between vectors for projection. */
	private static final double MAX_ANGLE = 45;
	/** Helps measure the 'closeness' between two vectors. */
	private static final double CLOSENESS_SCALE = Math.cos(Math.PI * MAX_ANGLE / 180);
	/** Current step-size for an upcoming gradient ascent. */
	private double stepSize;
	/** Current gradient or quasi-gradient direction for an upcoming gradient ascent. */
	private DataVector direction;

	@Override
	protected void start() {
		super.start();
		computeStepSizeAndDirection();
	}
	
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
	protected boolean preTerminate() {
		if (super.preTerminate()) return true;
		final TrainingControl control = getControl();
		return (control.gradientTolerance() > 0 
				&& direction.norm() < control.gradientTolerance());
	}

	@Override
	protected boolean update() {
		double[] newScores = new double[numScores()];
		ClassifierScoreInfo newTrainingScore = performLineSearch(newScores);
		if (newTrainingScore == null) return false;
		computeTestingScores(newScores);
		updateScores(newTrainingScore, newScores);
		recomputeStepSizeAndDirection();
		return true;
	}

	/**
	 * Finds a step-size rho for direction d such that
	 * x1 = x0 + rho*d and score(x1) > score(x0).
	 * 
	 * @param newScores - Place-holder for the new training score, f(x1).
	 * @return The new training score information, containing f'(x1), 
	 * or a value of null if no better-scoring point was found.
	 */
	private ClassifierScoreInfo performLineSearch(double[] newScores) {
		double rho = stepSize;
		final ParameterisedClassifier classifier = getClassifier();
		final TrainingControl control = getControl();
		while (control.maxIterations() <= 0 || numIterations() < control.maxIterations()) {
			DataVector newParams = VectorFactory.add(
					classifier.getParameters(), 
					VectorFactory.scale(direction, rho));
			if (!classifier.setParameters(newParams)) break;
			ClassifierScoreInfo newTrainingScore = computeTrainingScore(newScores);
			if (newScores[0] > getScores()[0]) return newTrainingScore;
			// Set up further line search.
			incIterations(); // Count minor iterations.
			recomputeStepSize(newTrainingScore);
			rho = stepSize - Math.abs(rho);
		}
		return null;
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
		if (getControl().useAcceleration()) {
			// TODO Try cubic acceleration.
			final DataVector g0 = getPrevTrainingScore().getGradient();
			final DataVector g1 = getTrainingScore().getGradient();
			double rho = CurveFactory.quadraticOptimum(g0, g1, direction); // Not scaled by step-size.
			stepSize *= (rho - 1); // Allow for being at x1 = x0+stepSize*d.
			// Check if it is safe to update direction to projection onto g1.
			final double d_dot_g1 = VectorFactory.dot(direction, g1);
			final double g1_norm = g1.norm();
			if (Math.abs(d_dot_g1) > CLOSENESS_SCALE * direction.norm() * g1_norm) {
				// < MAX_ANGLE; project onto g1.
				stepSize *= d_dot_g1 / (g1_norm * g1_norm);
				direction = g1;
			} else {
				// >= MAX_ANGLE; keep same direction.
			}
		} else {
			direction = getTrainingScore().getGradient();
			// Keep previous step-size.
		}
	}

}
