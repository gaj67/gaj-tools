package gaj.data.classifier;

/**
 * Specifies the classification accuracy score of a single data
 * point from a set of gold-standard data.
 */
public interface DatumScore {

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
	 * Obtains the unnormalised, weighted score of the data point.
	 * 
	 * @return The weighted score.
	 */
	double getScore();

	/**
	 * Obtains the weight of the data point.
	 * 
	 * @return The score weighting.
	 */
	double getWeight();

}
