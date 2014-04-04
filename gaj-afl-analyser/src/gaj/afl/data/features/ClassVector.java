package gaj.afl.data.features;

import java.util.Iterator;

/**
 * Describes a vector of class indices, with each index 
 * indicating the known class of some corresponding feature vector.
 */
public interface ClassVector extends Iterable<Integer> {

	/**
	 * Indicates the maximum number C of classes. 
	 * <br/>The largest possible index is C-1, although this value might not
	 * necessarily occur in this vector.
	 * 
	 * @return The number of classes.
	 */
	int numClasses();

	/**
	 * @return The full length of the class vector.
	 */
	int length();

	/**
	 * @param pos - The desired position of the particular class index.
	 * @return The class index, ranging from 0 to C-1.
	 */
	int get(int pos);

	/**
	 * Allows iteration over all elements of the class vector.
	 * 
	 * @return A class-index iterator.
	 */
	Iterator<Integer> iterator();

}
