package gaj.data.classifier;


/**
 * Specifies a probabilistic classifier
 * that can iteratively have its parameters updated
 * by measuring its accuracy against known classifications.
 */
public interface TrainableClassifier extends Classifier {

	/**
	 * Updates the classifier parameters based upon the given
	 * score from the training data, and optionally one or more scores from
	 * testing data.
	 * 
	 * @param params - The parameters controlling termination of iterative training.
	 * @param trainingScorer - The current score of the training data.
	 * @param testingScorers - The optional, current scores of testing data.
	 * @return A summary of the training iterations performed.
	 */
	TrainingSummary train(TrainingParams params, DataScorer trainingScorer, DataScorer... testingScorers);

}
