package gaj.data.numeric;

/**
 * Provides access to read-only numerical data objects.
 */
public abstract class NumericDataFactory {

	private NumericDataFactory() {}

	/**
	 * Wraps the given data into a vector.
	 * <p/>Note: The data must not be modified!
	 * 
	 * @param data - The array of data.
	 * @return The data vector.
	 */
	public static DataVector newDenseVector(double... data) {
		return new DenseDataVector(data);
	}
	
	/**
	 * Wraps the given data into a vector.
	 * <p/>Note: The data must not be modified!
	 * 
	 * @param data - A sequence of index/value pairs, with ascending indices.
	 * @return The data vector.
	 */
	public static DataVector newSparseVector(int length, double... data) {
		if (data.length % 2 != 0)
			throw new IllegalArgumentException("Odd length - expected index/value pairs");
		int[] indices = new int[data.length / 2];
		double[] values = new double[data.length / 2];
		int maxIndex = Integer.MIN_VALUE;
		for (int j = 0, i = 0; i < data.length;) {
			int index = indices[j] = (int) data[i++];
			if (index < 0 || index >= length)
				throw new IndexOutOfBoundsException("Invalid index: " + index);
			if (index <= maxIndex)
				throw new IndexOutOfBoundsException("Invalid non-ascending index: " + index);
			maxIndex = index;
			values[j++] = data[i++];
		}
		return new SparseDataVector(length, indices, values);
	}
	
	/**
	 * Concatenates multiple data vectors together into a single vector.
	 * 
	 * @param vectors - The data vectors to be concatenated.
	 * @return The concatenated data vector.
	 */
	public static DataVector concatenate(DataVector... vectors) {
		return new ConcatenatedDataVector(vectors);
	}

	/**
	 * Scales the vector by a multiplicative factor.
	 * 
	 * @param vector - The data vector to be scaled.
	 * @param multiplier - The scaling factor.
	 * @return The scaled data vector.
	 */
	public static DataVector scale(DataVector vector, double multiplier) {
		return new ScaledDataVector(vector, multiplier);
	}

}
