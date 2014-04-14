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

}
