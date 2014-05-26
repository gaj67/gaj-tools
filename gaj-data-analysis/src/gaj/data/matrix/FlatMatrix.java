package gaj.data.matrix;


/**
 * Indicates that the matrix data is stored as a flat array.
 */
public interface FlatMatrix extends DenseMatrix {

	/**
	 * Obtains the internal array of matrix data.
	 * 
	 * @return The flat data array.
	 */
	double[] getFlatData();

}
