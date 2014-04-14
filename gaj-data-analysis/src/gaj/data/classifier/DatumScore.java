package gaj.data.classifier;

import gaj.data.numeric.DataVector;

/**
 * Specifies the classification accuracy score of a single data
 * point from a set of gold-standard data.
 */
public interface DatumScore extends GoldDatum {

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

	/**
	 * Indicates whether or not score gradient
	 * information is available.
	 * 
	 * @return A value of true (or false) if it is (or is not)
	 * safe to call the {@link #getGradient}() method.
	 */
	boolean hasGradient();

	/**
	 * If {@link #hasGradient}() is true, 
	 * obtains the gradient of the score with respect to the 
	 * posterior probability <em>P(c|x)</em> of each class c.
	 * 
	 * @return The class-posterior gradient vector.
	 * @throws RuntimeException If gradient information is 
	 * not computed.
	 */
	DataVector getGradient() throws RuntimeException;

}
