package gaj.data.vector;


/**
 * Marks a vector as having a flat array representation,
 * which means it must also be dense.
 */
public interface ArrayVector extends DenseVector {

	/**
	 * @return The internal array of data.
	 */
	double[] getArray();

}
