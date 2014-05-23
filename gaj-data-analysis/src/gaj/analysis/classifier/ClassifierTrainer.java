package gaj.analysis.classifier;

import gaj.data.classifier.ClassifierScore;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.ScoredTrainer;
import gaj.data.classifier.TrainingControl;
import gaj.data.classifier.TrainingSummary;
import gaj.data.classifier.ParameterisedClassifier;

import java.util.Arrays;

/**
 * A base class for implementing a trainer for
 * a trainable classifier. The trainer is initialised
 * once, but may be repeatedly used (on the same data) by instantiating
 * the specified training algorithm class.
 */
public class ClassifierTrainer implements ScoredTrainer {

	private final ParameterisedClassifier classifier;
	private final DataScorer[] scorers;
	private final Class<? extends TrainingAlgorithm> algo;
	/**
	 * Current, total number of training iterations performed.
	 */
	private int numIterations = 0;
	/**
	 * Current training and testing scores.
	 */
	private final double[] scores;
	/**
	 * Current training score and any other information.
	 */
	private ClassifierScore state;

	/**
	 * Binds the classifier and scorers to the  training algorithm.
	 * 
	 * @param classifier - The classifier to be trained.
	 * @param scorers - The scorers to be evaluated. The first scorer represents the training data,
	 * and subsequent scorers, if any, represent testing data.
	 * @param algo - The training algorithm.
	 */
	protected ClassifierTrainer(ParameterisedClassifier classifier, DataScorer[] scorers, Class<? extends TrainingAlgorithm> algo) {
		this.classifier = classifier;
		this.scorers = scorers;
		this.algo = algo;
		scores = new double[scorers.length];
		state = scorers[0].getClassifierScoreInfo(classifier);
		scores[0] = state.getScore();
		for (int i = 1; i < scorers.length; i++)
			scores[i] = scorers[i].getClassifierScore(classifier);
	}

	@Override
	public int numIterations() {
		return numIterations;
	}

	@Override
	public double[] getScores() {
		return Arrays.copyOf(scores, scores.length);
	}

	@Override
	public TrainingSummary train(TrainingControl control) {
		try {
			TrainingAlgorithm trainer = algo.newInstance();
			trainer.bindArguments(classifier, scorers, control);
			trainer.setState(state);
			trainer.setScores(getScores());
			TrainingSummary summary = trainer.train();
			numIterations += summary.numIterations();
			state = trainer.getState();
			System.arraycopy(trainer.getScores(), 0, scores, 0, scores.length);
			return summary;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}
