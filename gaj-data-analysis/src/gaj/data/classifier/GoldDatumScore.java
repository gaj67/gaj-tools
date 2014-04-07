package gaj.data.classifier;

/**
 * Specifies the classification accuracy score of a single data
 * point from a set of gold-standard data.
 */
public interface GoldDatumScore {

	/**
	 * Obtains the gold-standard data point used for scoring.
	 * 
	 * @return The gold standard.
	 */
	GoldDatum getGoldDatum();

	/**
	 * Obtains the unweighted score of the data point.
	 * <p/>The score of the entire data set is the 
	 * weight-normalised sum of the weighted data point scores.
	 * 
	 * @return The unweighted score.
	 */
	double getScore();

}
