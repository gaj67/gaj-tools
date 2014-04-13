package gaj.data.numeric;

import java.util.Iterator;

/**
 * Provides access to read-only numerical data objects.
 */
public abstract class NumericDataFactory {

	private NumericDataFactory() {}

	/**
	 * Creates a vector of the required length, full of zeroes.
	 * 
	 * @param length - The vector length.
	 * @return The zero-data vector.
	 */
	public static DataVector newZeroVector(int length) {
		return new ZeroDataVector(length);
	}

	/**
	 * Wraps the given data into a vector.
	 * <p/>Note: The data must not be modified!
	 * 
	 * @param data - The array of data.
	 * @return The data vector.
	 */
	public static DataVector newDenseVector(double... data) {
		return new DenseDataVectorImpl(data);
	}

	/**
	 * Creates a sparse vector from the given index/value pairs.
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
		return new SparseDataVectorImpl(length, indices, values);
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

	/**
	 * Computes the dot product of two vectors.
	 * 
	 * @param vec1 - The first vector.
	 * @param vec2 - The second vector.
	 * @return The dot product.
	 */
	public static double dot(DataVector vec1, DataVector vec2) {
		if (vec1 instanceof SparseDataVector)
			return ((SparseDataVector) vec1).dot(vec2);
		if (vec2 instanceof SparseDataVector)
			return ((SparseDataVector) vec2).dot(vec1);
		if (vec1 instanceof DenseDataVector)
			return ((DenseDataVector) vec1).dot(vec2);
		if (vec2 instanceof DenseDataVector)
			return ((DenseDataVector) vec2).dot(vec1);
		// Compound vectors - do it the hard way!
		double sum = 0;
		final Iterator<Double> iter1 = vec1.iterator();
		final Iterator<Double> iter2 = vec2.iterator();
		while (iter1.hasNext() && iter2.hasNext())
			sum += iter1.next() * iter2.next();
		return sum;
	}

}
