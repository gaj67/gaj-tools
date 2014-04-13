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
	 * Indicates the total number C of classes in the classification scheme.
	 * 
	 * @return The number of classes.
	 */
	int numClasses();

	/**
	 * Indicates the number F of numerical features in any feature vector
	 * to be classified.
	 * 
	 * @return The number of feature elements.
	 */
	int numFeatures();

	/**
	 * Indicates whether or not the scorer computes gradient
	 * information.
	 * 
	 * @return A value of true (or false) if it is (or is not)
	 * safe to call the DatumScore.getGradient() method.
	 */
	boolean hasGradient();
	
	/**
	 * Computes the individual accuracy scores 
	 * (and any other required information) 
	 * of the given classifier against each 
	 * data point in the gold-standard data set.
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
