package gaj.data.numeric;

import java.util.Iterator;

/**
 * Provides access to numerical data as an array of row vectors.
 */
public interface RowMatrix extends DataMatrix, Iterable<DataVector> {

	/**
	 * Allows iteration over all rows of the data matrix.
	 * 
	 * @return A row iterator.
	 */
	Iterator<DataVector> iterator();

}
