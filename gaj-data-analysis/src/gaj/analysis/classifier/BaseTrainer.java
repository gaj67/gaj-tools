package gaj.analysis.classifier;

import gaj.data.classifier.ClassifierScoreInfo;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.ParameterisedClassifier;
import gaj.data.classifier.TrainingControl;
import gaj.data.classifier.TrainingSummary;

import java.util.Arrays;

/**
 * A base class for implementing a classifier training algorithm.
 * </p>The same instance may be reused multiple times.
 */
public abstract class BaseTrainer extends TrainingAlgorithm {

	/**
	 * Binds the training algorithm to the given classifier and scorers.
	 * 
	 * @param classifier - The classifier to be trained.
	 * @param scorers - The data scorers to measure classifier performance.
	 */
	protected BaseTrainer(ParameterisedClassifier classifier,
			DataScorer[] scorers) {
		super(classifier, scorers);
	}

	/** The training controls set by {@link #train}(). */
	private TrainingControl control;
	/** Copy of training and testing scores just prior to training. */
	private double[] initialScores;
	/** Holds the previous training score of the classifier. */
	private ClassifierScoreInfo prevTrainingScore;
	/** Holds the previous training and testing scores of the classifier. */
	private double[] prevScores;
	
	@Override
	public TrainingSummary train(TrainingControl control) {
		this.control = control;
		start();
		while (iterate());
		return end();
	}

	/**
	 * Starts the training run by resetting the number of iterations.
	 */
	protected void start() {
		initialScores = Arrays.copyOf(getScores(), numScores());
		resetIterations();
	}

	/**
	 * Ends the training run, and summarises the training pocess.
	 * 
	 * @return A summary of the training process.
	 */
	protected TrainingSummary end() {
		final int _numIterations = numIterations();
		final double[] _initialScores = initialScores;
		initialScores = null;
		final double[] _finalScores = Arrays.copyOf(getScores(), numScores());
		return new TrainingSummary() {
			@Override
			public int numIterations() {
				return _numIterations;
			}

			@Override
			public double[] initalScores() {
				return _initialScores;
			}

			@Override
			public double[] finalScores() {
				return _finalScores;
			}
		};
	}

	/**
	 * Performs at most one major iteration
	 * of the training process. 
	 * <p/>This method is responsible for setting
	 * the iteration count and computing the classifier scores, 
	 * and then testing these values against the training control parameters.
	 * 
	 * @return A value of true (or false) if 
	 * further training is (or is not) permitted.
	 */
	protected boolean iterate() {
		if (preTerminate()) return false;
		if (!update()) return false;
		incIterations();
		return !postTerminate();
	}

	/**
	 * Performs an update of the classifier parameters and training state.
	 * <p/>If successful, this method is responsible for calling
	 * {@link #updateScores}().
	 * 
	 * @return A value of true (or false) if a training
	 * update did (or did not) occur.
	 */
	protected abstract boolean update();

	/**
	 * Checks whether or not training should cease
	 * prior to an update iteration.
	 * 
	 * @return A value of true (or false) if training
	 * should (or should not) cease.
	 */
	protected boolean preTerminate() {
		return (control.maxIterations() > 0 && numIterations() >= control.maxIterations());
	}

	/**
	 * Checks whether or not iterative training should cease
	 * given the change in scores due to an update iteration. 
	 * For example, testing scores could be
	 * used to control over-training.
	 * 
	 * @return A value of true (or false) if training
	 * should (or should not) cease.
	 */
	protected boolean postTerminate() {
		final double prevScore = prevScores[0];
		final double diffScore = getScores()[0] - prevScore;
		if (control.scoreTolerance() > 0
				&& diffScore < control.scoreTolerance())
			return true;
		return (control.relativeScoreTolerance() > 0
				&& diffScore < Math.abs(prevScore) * control.relativeScoreTolerance());
	}

	/**
	 * Obtains the training score information from the previous iteration.
	 * 
	 * @return The previous training score information.
	 */
	protected ClassifierScoreInfo getPrevTrainingScore() {
		return prevTrainingScore;
	}

	/**
	 * Obtains the training and testing scores from the previous iteration.
	 * 
	 * @return The previous training and testing scores.
	 */
	protected double[] getPrevScores() {
		return prevScores;
	}

	/**
	 * Updates the previous training and testing scores from the current scores, 
	 * and the current scores from the new scores.
	 * 
	 * @param trainingScore - The classifier training score information.
	 * @param scores - The classifier training and testing scores.
	 */
	protected void updateScores(ClassifierScoreInfo trainingScore, double[] scores) {
		this.prevTrainingScore = getTrainingScore();
		this.prevScores = getScores();
		setScores(trainingScore, scores);
	}

	/**
	 * Obtains the settings that control this training run.
	 * 
	 * @return The training control.
	 */
	protected TrainingControl getControl() {
		return control;
	}

}
