package gaj.iterators.core;

/**
 * Specifies a producer of typed objects.
 */
public interface Producer<T> {

    /**
     * Produces the next item in a sequence.
     *
     * @return The item, or a value of null if the sequence has ended.
     */
    /*@Nullable*/ T produce();

}
