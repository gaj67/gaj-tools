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

	Iterable<GoldDatumScore> score(Classifier classifier);

}
