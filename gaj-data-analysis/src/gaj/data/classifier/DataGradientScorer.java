package gaj.data.classifier;

/**
 * Extends the gold-standard scoring mechanism by additionally computing gradient information
 * at the level of individual data-points.
 */
public interface DataGradientScorer extends DataScorer {

	/**
	 * Computes the individual accuracy scores of the given classifier against each 
	 * data point in the gold standard data set.
	 * 
	 * @param classifier - A trained classifier.
	 * @return A sequence of individual scores with score gradients.
	 */
	Iterable<? extends DatumGradientScore> scores(Classifier classifier);

}
