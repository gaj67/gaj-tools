package gaj.impl.vector;

import gaj.data.vector.CompoundVector;
import gaj.data.vector.DataVector;
import gaj.data.vector.DenseVector;
import gaj.data.vector.IndexVector;
import gaj.data.vector.SparseVector;
import gaj.data.vector.WritableVector;

import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Provides access to numerical vectors.
 */
public abstract class VectorFactory {

	private VectorFactory() {}

	/**
	 * Creates a vector of the required length, full of zeroes.
	 * 
	 * @param length - The vector length.
	 * @return The zero-data vector.
	 */
	public static DataVector newVector(int length) {
		return new ZeroVector(length);
	}

	/**
	 * Wraps the given data into a vector.
	 * <p/>Warning: The data must not be modified!
	 * 
	 * @param data - The array of data.
	 * @return The data vector.
	 */
	public static DataVector newVector(double... data) {
		return new WritableArrayVector(data);
	}

	/**
	 * Wraps the given integer data into a vector.
	 * <p/>Warning: The data must not be modified!
	 * 
	 * @param data - The array of data.
	 * @return The data vector.
	 */
	public static IndexVector newIndexVector(int... data) {
		return new DenseIndexVector(data);
	}

	/**
	 * Creates an initially zero-valued, modifiable vector.
	 * 
	 * @return The data vector.
	 */
	public static WritableVector newWritableVector(int length) {
		return new WritableArrayVector(length);
	}

	/**
	 * Wraps the given data into a modifiable vector.
	 * 
	 * @param data - The array of data.
	 * @return The data vector.
	 */
	public static WritableVector newWritableVector(double... data) {
		return new WritableArrayVector(data);
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
		return new SparseVectorImpl(length, indices, values);
	}

	/**
	 * Concatenates multiple data vectors together into a single pseudo-vector.
	 * 
	 * @param vectors - The data vectors to be concatenated.
	 * @return The concatenated data vector.
	 */
	public static DataVector concatenate(DataVector... vectors) {
		return new ConcatenatedVector(vectors);
	}

	/**
	 * Scales the vector by a multiplicative factor.
	 * 
	 * @param vector - The data vector to be scaled.
	 * @param multiplier - The scaling factor.
	 * @return The scaled data vector.
	 */
	public static DataVector scale(DataVector vector, double multiplier) {
		if (vector instanceof ZeroVector) return vector;
		if (multiplier == 0) return new ZeroVector(vector.size());
		if (vector instanceof SparseVector)
			return new ScaledSparseVector((SparseVector) vector, multiplier);
		if (vector instanceof DenseVector)
			return new ScaledDenseVector((DenseVector) vector, multiplier);
		if (vector instanceof CompoundVector)
			return new ScaledCompoundVector((CompoundVector) vector, multiplier);
		return new ScaledVector(vector, multiplier);
	}

	/**
	 * Computes the dot product of two vectors.
	 * 
	 * @param vec1 - The first vector.
	 * @param vec2 - The second vector.
	 * @return The dot product.
	 */
	public static double dot(DataVector vec1, DataVector vec2) {
		if (vec1 instanceof SparseVector)
			return ((SparseVector) vec1).dot(vec2);
		if (vec2 instanceof SparseVector)
			return ((SparseVector) vec2).dot(vec1);
		if (vec1 instanceof CompoundVector)
			return ((CompoundVector) vec1).dot(vec2);
		if (vec2 instanceof CompoundVector)
			return ((CompoundVector) vec2).dot(vec1);
		if (vec1 instanceof DenseVector)
			return ((DenseVector) vec1).dot(vec2);
		if (vec2 instanceof DenseVector)
			return ((DenseVector) vec2).dot(vec1);
		double sum = 0;
		final Iterator<Double> iter1 = vec1.iterator();
		final Iterator<Double> iter2 = vec2.iterator();
		while (iter1.hasNext() && iter2.hasNext())
			sum += iter1.next() * iter2.next();
		return sum;
	}

	/**
	 * Sums multiple data vectors.
	 * 
	 * @param vectors - An array of vectors.
	 * @return The summed vector.
	 */
	public static WritableVector add(DataVector... vectors) {
		final int length = vectors[0].size();
		WritableVector summedVector = newWritableVector(length);
		for (DataVector vector : vectors) {
			//if (vector instanceof AbstractVector)
				((AbstractVector) vector).addTo(summedVector);
			//else
			//	summedVector.add(vector);
		}
		return summedVector;
	}

	public static void display(String prefix, DataVector vector, String suffix) {
		System.out.print(prefix);
		display(vector);
		System.out.print(suffix);
	}

	public static void display(DataVector vector) {
		System.out.print("[");
		for (double value : vector)
			System.out.printf(" %f", value);
		System.out.print(" ]");
	}

	/**
	 * Computes the element-wise product of two vectors.
	 * 
	 * @param v1 - The first vector.
	 * @param v2 - The second vector.
	 * @return The product vector.
	 */
	public static DataVector multiply(DataVector v1, DataVector v2) {
		throw new NotImplementedException();
	}

}
