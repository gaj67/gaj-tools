package gaj.data.numeric;

/**
 * Marks a vector as being sparse.
 */
/*package-private*/ interface SparseDataVector extends DataVector {

	/**
	 * Obtains a representation of the sparse indices.
	 * 
	 * @return An array of indices.
	 */
	int[] getIndices();

	/**
	 * Obtains a representation of the sparse values.
	 * 
	 * @return An array of values.
	 */
	double[] getValues();

	/**
	 * Calculates the dot-product of the sparse vector with
	 * the given vector.
	 * 
	 * @param vector - The second vector.
	 * @return The dot product.
	 */
	double dot(DataVector vector);

}
