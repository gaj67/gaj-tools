package gaj.analysis.data.vector.impl;

import gaj.analysis.data.object.RepresentationType;
import gaj.analysis.data.vector.DataVector;

/**
 * Methods in this factory wrap the underlying vector(s) into a compound vector.
 * The intention is that results for this compound vector will be computed later in a once-off fashion.
 * Hence, these methods are suitable for computing temporary, throw-away results.
 */
public abstract class CompoundVectorFactory {

    private CompoundVectorFactory() {}

    /**
     * Forces the necessary computations on the vector and produces a dense result.
     * 
     * @param vector - The vector to be computed.
     * @return A dense vector with all elements computed. This might be the original vector if it is already dense.
     */
    public static DataVector compute(DataVector vector) {
        if (vector.representationType() == RepresentationType.DENSE) return vector;
        final int N = vector.size();
        double[] data = new double[N];
        for (int i = 0; i < N; i++) {
            data[i] = vector.get(i);
        }
        return new WritableArrayVector(data);
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
     * <p/>
     * Note: The underlying vector is NOT copied, merely wrapped with a multiplier. Hence, each element will be scaled by the multiplier each time that element is accessed. Useful
     * for scaling a large vector for a one-shot use.
     *
     * @param vector - The data vector to be scaled.
     * @param multiplier - The scaling factor.
     * @return The scaled data vector.
     */
    public static DataVector multiply(DataVector vector, double multiplier) {
        if (vector instanceof ZeroVector) {
            return vector;
        }
        if (multiplier == 0) {
            return new ZeroVector(vector.size());
        }
        return new ScaledVector(vector, multiplier);
    }

    /**
     * Divides the vector elements by a non-zero divisor.
     * <p/>
     * Note: The underlying vector is copied, and the elements are scaled once.
     * <p/>
     * Note: If the divisor is zero, then the returned vector is zero-valued.
     *
     * @param vector - The data vector to be scaled.
     * @param divisor - The inverse scaling factor.
     * @return The scaled data vector.
     */
    public static DataVector divide(DataVector vector, double divisor) {
        if (vector instanceof ZeroVector) {
            return vector;
        }
        if (divisor == 0.0) {
            return new ZeroVector(vector.size());
        }
        return new ScaledVector(vector, 1.0 / divisor);
    }

}