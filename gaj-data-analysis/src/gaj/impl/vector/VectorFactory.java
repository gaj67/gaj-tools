package gaj.impl.vector;

import gaj.data.vector.ArrayVector;
import gaj.data.vector.DataVector;
import gaj.data.vector.IndexVector;
import gaj.data.vector.WritableVector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Provides access to numerical vectors.
 */
public abstract class VectorFactory {

    private VectorFactory() {}

    /**
     * Creates an immutable vector of the required length, full of zeroes.
     *
     * @param length - The vector length.
     * @return The zero-data vector.
     */
    public static DataVector newZeroVector(int length) {
	return new ZeroVector(length);
    }

    /**
     * Creates an initially zero-valued, modifiable vector.
     *
     * @return The data vector.
     */
    public static WritableVector newVector(int length) {
	return new WritableArrayVector(length);
    }

    /**
     * Wraps the given data into a modifiable vector.
     * <p/>Warning: The data must not be modified elsewhere!
     *
     * @param data - The array of data.
     * @return The data vector.
     */
    public static WritableVector newVector(double... data) {
	return new WritableArrayVector(data);
    }

    /**
     * Wraps the given integer data into an immutable vector.
     * <p/>Warning: The data must not be modified elsewhere!
     *
     * @param data - The array of data.
     * @return The data vector.
     */
    public static IndexVector newIndexVector(int[] data) {
	return new DenseIndexVector(data);
    }

    /**
     * Creates a sparse vector from the given index/value pairs.
     *
     * @param data - A sequence of index/value pairs, with ascending indices.
     * @return The data vector.
     */
    public static DataVector newSparseVector(int length, double... data) {
	if (data.length % 2 != 0) {
	    throw new IllegalArgumentException("Odd length - expected index/value pairs");
	}
	int[] indices = new int[data.length / 2];
	double[] values = new double[data.length / 2];
	int maxIndex = Integer.MIN_VALUE;
	for (int j = 0, i = 0; i < data.length;) {
	    int index = indices[j] = (int) data[i++];
	    if (index < 0 || index >= length) {
		throw new IndexOutOfBoundsException("Invalid index: " + index);
	    }
	    if (index <= maxIndex) {
		throw new IndexOutOfBoundsException("Invalid non-ascending index: " + index);
	    }
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
     * <p/>Note: The underlying vector is NOT copied, merely wrapped with a multiplier.
     * Hence, each element will be scaled by the multiplier each time that element is accessed.
     * Useful for scaling a large vector for a one-shot use.
     *
     * @param vector - The data vector to be scaled.
     * @param multiplier - The scaling factor.
     * @return The scaled data vector.
     */
    public static DataVector scale(DataVector vector, double multiplier) {
	if (vector instanceof ZeroVector) {
	    return vector;
	}
	if (multiplier == 0) {
	    return new ZeroVector(vector.size());
	}
	return new ScaledVector(vector, multiplier);
    }

    /**
     * Scales the vector by a multiplicative factor.
     * <p/>Note: The underlying vector is copied, and the elements are scaled once.
     *
     * @param vector - The data vector to be scaled.
     * @param multiplier - The scaling factor.
     * @return The scaled data vector.
     */
    public static WritableVector multiply(DataVector vector, double multiplier) {
	WritableVector newVec = VectorFactory.newVector(vector);
	newVec.multiply(multiplier);
	return newVec;
    }

    /**
     * Divides the vector elements by a non-zero divisor.
     * <p/>Note: The underlying vector is copied, and the elements are scaled once.
     * <p/>Note: If the divisor is zero, then the returned vector is zero-valued.
     *
     * @param vector - The data vector to be scaled.
     * @param divisor - The inverse scaling factor.
     * @return The scaled data vector.
     */
    public static WritableVector divide(DataVector vector, double divisor) {
	if (divisor == 0.0) {
	    return VectorFactory.newVector(vector.size());
	}
	WritableVector newVec = VectorFactory.newVector(vector);
	newVec.multiply(1.0 / divisor);
	return newVec;
    }

    /**
     * Computes the dot product of two vectors.
     *
     * @param vec1 - The first vector.
     * @param vec2 - The second vector.
     * @return The dot product.
     */
    public static double dot(DataVector vec1, DataVector vec2) {
	if (vec1.isSparse()) {
	    return vec1.dot(vec2);
	}
	if (vec2.isSparse()) {
	    return vec2.dot(vec1);
	}
	if (vec1.isCompound()) {
	    return vec1.dot(vec2);
	}
	if (vec2.isCompound()) {
	    return vec2.dot(vec1);
	}
	if (vec1.isDense()) {
	    return vec1.dot(vec2);
	}
	if (vec2.isDense()) {
	    return vec2.dot(vec1);
	}
	double sum = 0;
	final Iterator<Double> iter1 = vec1.iterator();
	final Iterator<Double> iter2 = vec2.iterator();
	while (iter1.hasNext() && iter2.hasNext()) {
	    sum += iter1.next() * iter2.next();
	}
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
	WritableVector summedVector = newVector(length);
	for (DataVector vector : vectors) {
	    if (vector instanceof AbstractVector) {
		((AbstractVector) vector).addTo(summedVector);
	    } else {
		summedVector.add(vector);
	    }
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
	for (double value : vector) {
	    System.out.printf(" %f", value);
	}
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
	WritableVector vw = newVector(v1);
	vw.multiply(v2);
	return vw;
    }

    /**
     * Obtains a writable vector copy of the given vector.
     *
     * @param vec - The vector.
     * @return A writable vector.
     */
    public static WritableVector newVector(DataVector vec) {
	return newVector(toArray(vec));
    }

    /**
     * Obtains an array copy of the vector data.
     *
     * @param vec - The vector.
     * @return The copied array data.
     */
    public static double[] toArray(DataVector vec) {
	int length = vec.size();
	if (vec instanceof ArrayVector) {
	    return Arrays.copyOf(((ArrayVector) vec).getArray(), length);
	} else {
	    double[] data = new double[length];
	    int pos = 0;
	    for (double value : vec) {
		data[pos++] = value;
	    }
	    return data;
	}
    }

    /**
     * Determines whether or not two vectors have equal values to the
     * given order of accuracy.
     *
     * @param v1 - The first vector.
     * @param v2 - The second vector.
     * @param accuracy - The largest allowable difference.
     * @return A value of true (or false) if the two vectors do (or do not) agree
     * on dimensions and values.
     */
    public static boolean equals(DataVector v1, DataVector v2, double accuracy) {
	final int length = v1.size();
	if (v2.size() != length) {
	    return false;
	}
	for (int i = 0; i < length; i++) {
	    if (Math.abs(v1.get(i) - v2.get(i)) > accuracy) {
		return false;
	    }
	}
	return true;
    }

    /**
     * Computes the lowest index of a maximal element of the vector.
     *
     * @param vec - The vector.
     * @return The index of a maximal element.
     */
    public static int argMax(DataVector vec) {
	double maxValue = Double.NEGATIVE_INFINITY;
	int maxIndex = -1;
	int index = -1;
	for (double value : vec) {
	    index++;
	    if (value > maxValue) {
		maxValue = value;
		maxIndex = index;
	    }
	}
	return maxIndex;
    }

    /**
     * Computes the indices of the maximal elements of the vector.
     *
     * @param vec - The vector.
     * @return The list of indices of maximal elements.
     */
    public static List<Integer> argMaxes(DataVector vec) {
	List<Integer> maxIndices = new ArrayList<>(1);
	double maxValue = Double.NEGATIVE_INFINITY;
	int index = -1;
	for (double value : vec) {
	    index++;
	    if (value == maxValue) {
		maxIndices.add(index);
	    } else if (value > maxValue) {
		maxValue = value;
		maxIndices.clear();
		maxIndices.add(index);
	    }
	}
	return maxIndices;
    }

}
