package gaj.data.classifier;

/**
 * Provides the means for scoring the accuracy of a classifier
 * against a known gold-standard data set.
 */
public interface DataScorer {

	/**
	 * Obtains the gold-standard data set used for scoring.
	 * 
	 * @return The gold standard.
	 */
	GoldData getGoldData();

	/**
	 * Computes the individual accuracy scores of the given classifier against each 
	 * data point in the gold standard data set.
	 * 
	 * @param classifier - A trained classifier.
	 * @return A sequence of individual scores.
	 */
	Iterable<? extends DatumScore> scores(Classifier classifier);

	/**
	 * Computes the overall, weighted average score of the given 
	 * classifier against the gold standard data set.
	 * <p/>This should only be called if the individual data point scores are
	 * not required, e.g. for testing data.
	 * 
	 * @param classifier - A trained classifier.
	 * @return The weighted average score.
	 */
	double score(Classifier classifier);

}
