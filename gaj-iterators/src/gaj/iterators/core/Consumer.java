package gaj.iterators.core;

/**
 * Specifies a consumer of typed objects.
 */
public interface Consumer<T> {

    /**
     * Consumes a sequence of items.
     * 
     * @param The next (non-null) item in a sequence, or a value of null
     * denoting the end of the sequence.
     * @return A value of true (or false) if the item has (or has not) been consumed.
     */
    boolean consume(/*@Nullable*/ T item);

}
