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
	 * Obtains the weighted average score of all data points up to
	 * and including the current one.
	 * 
	 * @return The weighted average score.
	 */
	double getAverageScore();

	/**
	 * Obtains the unweighted score of the data point.
	 * 
	 * @return The unweighted score.
	 */
	double getUnweightedScore();

	/**
	 * Obtains the weight of the data point.
	 * 
	 * @return The score weighting.
	 */
	double getWeight();

}
