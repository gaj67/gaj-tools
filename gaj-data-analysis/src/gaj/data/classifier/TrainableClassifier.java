package gaj.data.classifier;



/**
 * Specifies a trainer for a probabilistic classifier
 * that can iteratively have its parameters updated.
 */
public interface TrainableClassifier extends Classifier {

	/**
	 * Updates the classifier parameters based upon the given
	 * score from the training data, and optionally one or more scores from
	 * testing data.
	 * 
	 * @param params - The parameters controlling termination of iterative training.
	 * @param scorers - The data scorers used to test classifier performance.
	 * @return A summary of the training iterations performed.
	 */
	TrainingSummary train(TrainingParams params, DataScorer... scorers);

}
