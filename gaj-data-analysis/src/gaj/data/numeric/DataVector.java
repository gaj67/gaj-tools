package gaj.data.numeric;

import java.util.Iterator;


/**
 * Provides access to numerical data as a vector.
 */
public interface DataVector extends Iterable<Double> {

	/**
	 * @return The length of the vector.
	 */
	int length();

	/**
	 * @param pos - The position of the desired element (counting from 0).
	 * @return The element value.
	 */
	double get(int pos);

	/**
	 * Allows iteration over all elements of the data vector.
	 * 
	 * @return An element iterator.
	 */
	Iterator<Double> iterator();

}
