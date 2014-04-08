package gaj.data.numeric;

/**
 * Provides access to read-only numerical data objects.
 */
public abstract class NumericDataFactory {

	private NumericDataFactory() {}

	/**
	 * Concatenates multiple data vectors together into a single vector.
	 * 
	 * @param vectors - The data vectors to be concatenated.
	 * @return The concatenated data vector.
	 */
	public static DataVector concatenate(DataVector... vectors) {
		return new MultiDataVectorImpl(vectors);
	}

	/**
	 * Scales the vector by a multiplicative factor.
	 * 
	 * @param vector - The data vector to be scaled.
	 * @param multiplier - The scaling factor.
	 * @return The scaled data vector.
	 */
	public static DataVector scale(DataVector vector, double multiplier) {
		return new ScaledDataVectorImpl(vector, multiplier);
	}

}
