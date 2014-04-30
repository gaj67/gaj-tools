package gaj.analysis.classifier;

import gaj.analysis.numeric.NumericFactory;
import gaj.data.classifier.DatumScore;
import gaj.data.classifier.TrainingParams;
import gaj.data.numeric.DataObject;

/**
 * Implements a classifier trainer using
 * gradient ascent to maximise the score(s)
 * on one or more fixed data sets.
 */
public class GradientAscentTrainer extends ClassifierTrainer {

	/**
	 * Current gradient of the training data.
	 */
	protected DataObject gradient;
	/**
	 * Current step-size for an upcoming gradient ascent.
	 */
	protected double stepSize;

	@Override
	protected void initialise() {
		if (!classifier.hasGradient())
			throw new IllegalArgumentException("A gradient-enabled classifier is required");
		if (scorers.length == 0 || !scorers[0].hasGradient())
			throw new IllegalArgumentException("A training-data gradient scorer is required");
		gradient = computeTrainingScoreAndGradient(scores);
		computeTestingScores(scores);
		stepSize = 0.5;
	}

	/**
	 * Finds a step-size rho such that
	 * x1 = x0 + rho*g0 and score(x1) > score(x0).
	 * 
	 * @param control - The control parameters.
	 * @param newScores - Place-holder for the new training score, f(x1).
	 * @return The new slope, f'(x1), or a value of null
	 * if no better-scoring point was found.
	 */
	private DataObject performLineSearch(TrainingParams control, double[] newScores) {
		double rho = stepSize;
		while (numIterations < control.maxIterations()) {
			NumericFactory.display("Gradient=", gradient);
			System.out.printf("Step-size=%5.3f%n", rho);
			DataObject increment = NumericFactory.scale(gradient, rho);
			if (!updateParams(increment)) 
				return null;
			DataObject newGradient = computeTrainingScoreAndGradient(newScores);
			if (newScores[0] > scores[0]) return newGradient;
			numIterations++; // XXX Include minor iterations here.
			recomputeStepSize(newScores, newGradient);
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
	 * 
	 * @param newScores - The scores at x1.
	 * @param newGradient - The slope at x1.
	 */
	protected void recomputeStepSize(double[] newScores, DataObject newGradient) {
		// TODO Use quadratic or cubic acceleration.
		//stepSize = curveFit(stepSize, scores[0], gradient, newScores[0], newGradient);
		stepSize *= 0.5;
	}

	/**
	 * Recomputes the step-size for a new step from
	 * the new point x1 along a new gradient.
	 * 
	 * @param newScores - The scores at x1.
	 * @param newGradient - The slope at x1.
	 */
	protected void recomputeStepSizeAndGradient(double[] newScores, DataObject newGradient) {
		// TODO Use quadratic or cubic acceleration.
		//double rho = curveFit(stepSize, scores[0], gradient, newScores[0], newGradient);
		//stepSize = Maths.abs(stepSize - rho);
		stepSize = 0.5;
		gradient = newGradient;
	}

	private DataObject computeTrainingScoreAndGradient(double[] scores) {
		scores[0] = Double.NEGATIVE_INFINITY;
		double sumWeights = 0;
		DataObject sumGradient = null;
		for (DatumScore datumScore : scorers[0].getScores(classifier)) {
			scores[0] = datumScore.getAverageScore();
			final double weight = datumScore.getWeight();
			sumWeights += weight;
			DataObject datumGradient = NumericFactory.scale(
					classifier.getGradient(datumScore),
					weight);
			sumGradient = (sumGradient == null) 
					? datumGradient
							: NumericFactory.add(sumGradient, datumGradient);
		}
		sumGradient = NumericFactory.scale(sumGradient, 1 / sumWeights);
		return sumGradient;
	}

	private void computeTestingScores(double[] scores) {
		for (int i = 1; i < scorers.length; i++)
			scores[i] = scorers[i].getScore(classifier);
	}

	private boolean updateParams(DataObject increment) {
		NumericFactory.display("Increment=", increment);
		DataObject newParams = NumericFactory.add(classifier.getParameters(), increment);
		NumericFactory.display("Parameters=", newParams);
		return classifier.setParameters(newParams);
	}

	@Override
	protected boolean preTerminate(TrainingParams control) {
		if (super.preTerminate(control))
			return true;
		return (control.gradientTolerance() > 0 
				&& NumericFactory.norm(gradient) < control.gradientTolerance());
	}

	@Override
	protected double[] update(TrainingParams control) {
		double[] newScores = new double[scorers.length];
		DataObject newGradient = performLineSearch(control, newScores);
		if (newGradient == null) return scores;
		NumericFactory.display("New gradient=", newGradient);
		computeTestingScores(newScores);
		recomputeStepSizeAndGradient(newScores, newGradient);
		return newScores;
	}

}
