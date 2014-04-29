/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.core;

/**
 * Defines a filter for: (1) transforming iterated elements from one type into another;
 * (2) skipping over unwanted elements.
 */
public interface Filter<T,V> {

    /**
     * Transforms an element from the input type to the output type.
     * If the output object is null, then the element is skipped over
     * during iteration.
     * @param element - The input element.
     * @return The output element, or a value of null to skip the element.
     */
    public /*@Nullable*/ V filter(T element);

}