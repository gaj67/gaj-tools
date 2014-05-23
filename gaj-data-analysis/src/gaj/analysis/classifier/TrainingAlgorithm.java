package gaj.analysis.classifier;

import gaj.data.classifier.ClassifierScore;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.ParameterisedClassifier;
import gaj.data.classifier.TrainingControl;
import gaj.data.classifier.TrainingSummary;

/**
 * Specifies the training algorithm used train a parameterised classifier on gold-standard data.
 */
public abstract class TrainingAlgorithm {

	protected ParameterisedClassifier classifier;
	protected DataScorer[] scorers;
	protected TrainingControl control;
	private ClassifierScore state;
	private double[] scores;

	/**
	 * Instantiates the algorithm, to be later initialised via {@link #bindArguments}().
	 */
	public TrainingAlgorithm() {}

	/*package-private*/ void bindArguments(ParameterisedClassifier classifier, DataScorer[] scorers, TrainingControl control) {
		this.classifier = classifier;
		this.scorers = scorers;
		this.control = control;		
	}

	/**
	 * Performs classifier training.
	 * 
	 * @return A summary of the training iterations performed.
	 */
	protected abstract TrainingSummary train();

	/**
	 * Specifies the classifier training score information.
	 * 
	 * @param state - The training score information.
	 */
	protected void setState(ClassifierScore state) {
		this.state = state;
	}

	/**
	 * Obtains the current classifier training score information.
	 * 
	 * @return The training score information.
	 */
	protected ClassifierScore getState() {
		return state;
	}

	/**
	 * Specifies the classifier training and testing scores.
	 * 
	 * @param scores - The array of scores.
	 */
	protected void setScores(double[] scores) {
		this.scores = scores;
	}

	/**
	 * Obtains the current classifier training and testing scores.
	 * 
	 * @return The array of scores.
	 */
	protected double[] getScores() {
		return scores;
	}

}
