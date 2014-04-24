package gaj.data.vector;

import gaj.data.numeric.DataObject;

import java.util.Iterator;


/**
 * Provides access to numerical data as a vector.
 */
public interface DataVector extends DataObject, Iterable<Double> {

	//***************************************
	// Intrinsic properties of the vector.
	
	/**
	 * @return The length of the vector.
	 */
	int length();

	/**
	 * Calculates the Euclidean norm of the vector.
	 * 
	 * @return The vector norm.
	 */
	double norm();

	/**
	 * Obtains the value of a vector element.
	 * 
	 * @param pos - The position of the element (counting from 0).
	 * @return The element value.
	 */
	double get(int pos);

	/**
	 * Allows iteration over all elements of the data vector.
	 * 
	 * @return An element iterator.
	 */
	Iterator<Double> iterator();

	//***************************************
	// Extrinsic properties of the vector.
	// Usually called by factory methods.

	/**
	 * Calculates the dot-product of the vector with
	 * another vector.
	 * <p/>Note: This is not guaranteed to give the most
	 * efficient result - use a factory method.
	 * 
	 * @param vector - The second vector.
	 * @return The dot product.
	 */
	double dot(DataVector vector);

	/**
	 * Adds the current vector to the given vector.
	 * <p/>Note: This is not guaranteed to give the most
	 * efficient result - use a factory method.
	 * 
	 * @param vector - A mutable vector.
	 */
	void addTo(WritableVector vector);

}