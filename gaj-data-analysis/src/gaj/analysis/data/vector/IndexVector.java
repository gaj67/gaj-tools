package gaj.analysis.data.vector;

import java.util.Iterator;

/**
 * Provides access to index data as a vector.
 */
public interface IndexVector extends Iterable<Integer> {

    /**
     * @return The number of data elements in the vector.
     */
    int size();

    /**
     * @param pos - The position of the desired element (counting from 0).
     * @return The element value.
     */
    int get(int pos);

    /**
     * Allows iteration over all elements of the index vector.
     * 
     * @return An element iterator.
     */
    Iterator<Integer> iterator();

}
