package gaj.data.numeric;

/**
 * Marks a matrix as being sparse.
 */
public interface SparseMatrix extends DataMatrix {

	/**
	 * Post-multiplies the N x M sparse matrix by the given vector.
	 * 
	 * @param vector - A length-M vector.
	 * @return A length-N vector.
	 */
	DataVector rowMultiply(DataVector vector);

	/**
	 * Pre-multiplies the N x M sparse matrix by the given vector.
	 * 
	 * @param vector - A length-N vector.
	 * @return A length-M vector.
	 */
	DataVector columnMultiply(DataVector vector);

	/**
	 * Scales the sparse matrix by the given non-zero multiplier.
	 * 
	 * @param multiplier - The multiplier.
	 * @return The scaled sparse matrix.
	 */
	SparseMatrix scale(double multiplier);

}
