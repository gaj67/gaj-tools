package gaj.data.classifier;

/**
 * Provides the means for scoring the accuracy of a classifier
 * against a known gold-standard data set.
 */
public interface GoldDataScorer {

	/**
	 * Obtains the gold-standard data set used for scoring.
	 * 
	 * @return The gold standard.
	 */
	GoldData getGoldData();

	/**
	 * Computes the partial, unweighted accuracy scores of the given 
	 * classifier against each
	 * data point in the gold standard data set.
	 * 
	 * @param classifier - A trained classifier.
	 * @return The weighted score.
	 */
	Iterable<GoldDatumScore> scores(Classifier classifier);

	/**
	 * Computes the overall, weighted accuracy score of the given 
	 * classifier against the gold standard data set.
	 * <p/>This should only be called if the individual data point scores are
	 * not required, e.g. for testing data.
	 * 
	 * @param classifier - A trained classifier.
	 * @return The weighted score.
	 */
	double score(Classifier classifier);

}
