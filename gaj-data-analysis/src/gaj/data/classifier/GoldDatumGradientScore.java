package gaj.data.classifier;

/**
 * Specifies the classification accuracy score of a single data
 * point <em>x</em> from a set of gold-standard data, 
 * along with the
 * gradient of the score with respect to the posterior
 * probability <em>P(c|x)</em> of each class c.
 */
public interface GoldDatumGradientScore extends GoldDatumScore {

	/**
	 * Obtains the gradient of the score with respect to the 
	 * posterior probability <em>P(c|x)</em> of each class c.
	 * 
	 * @return
	 */
	DataVector getGradient();
	
}
