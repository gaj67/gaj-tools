package gaj.data.object;

/**
 * Indicates dimensionalised numerical data, e.g. a vector or matrix or tensor, etc., or a
 * collection of such data objects.
 */
public interface DataObject {

	/**
	 * Indicates the total number of elements in the data structure.
	 * 
	 * @return The size of the data.
	 */
	int size();

	/**
	 * Indicates whether or not the data structure has an explicit representation of all elements.
	 * 
	 * @return A value of true (or false) if each element is (or is not) represented.
	 */
	boolean isDense();

	/**
	 * Indicates whether or not the data structure has an explicit representation of only non-zero elements.
	 * 
	 * @return A value of true (or false) if only non-zero elements are (or are not) represented.
	 */
	boolean isSparse();

	/**
	 * Indicates whether or not the data structure has an unknown representation of elements.
	 * 
	 * @return A value of true (or false) if the data object is (or is not) composed of one or more sub-objects.
	 */
	boolean isCompound();

	/**
	 * Indicates the size of the data structure along each dimension.
	 * 
	 * @return An array of dimensions.
	 */
	//int[] dimensions();

}
