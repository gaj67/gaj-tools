package gaj.data.numeric;

/**
 * Marks a vector as being dense.
 */
public interface DenseDataVector extends DataVector {
	
	/**
	 * Obtains a representation of the dense values.
	 * 
	 * @return An array of values.
	 */
	double[] getValues();

	/**
	 * Calculates the dot-product of the dense vector with
	 * the given vector.
	 * 
	 * @param vector - The second vector.
	 * @return The dot product.
	 */
	double dot(DataVector vector);

	/**
	 * Calculates the Euclidean norm of the vector.
	 * 
	 * @return The vector norm.
	 */
	double norm();

	/**
	 * Scales the dense vector by the given non-zero multiplier.
	 * 
	 * @param multiplier - The multiplier.
	 * @return The scaled dense vector.
	 */
	DataVector scale(double multiplier);

}
