package gaj.data.numeric;

import java.util.Iterator;

/**
 * Provides access to numerical data as an array of column vectors.
 */
public interface ColumnMatrix extends DataMatrix, Iterable<DataVector> {

	/**
	 * Allows iteration over all columns of the data matrix.
	 * 
	 * @return A column iterator.
	 */
	Iterator<DataVector> iterator();

}
