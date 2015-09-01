package gaj.iterators.core;

/**
 * Specifies a combined consumer and producer of typed objects.
 */
public interface ConsumerProducer<T> extends Consumer<T>, Producer<T> {

    /**
     * Consumes the next item in a sequence, for subsequent production. 
     * This method may block if necessary until {@link #produce}() is called.
     * 
     * @param The next (non-null) item in a sequence, or a value of null
     * denoting the end of the sequence.
     * @return A value of true (or false) if the item has (or has not) been consumed.
     */
    boolean consume(/*@Nullable*/ T item);

    /**
     * Produces the next item in a sequence. This method may block if 
     * necessary until {@link #consume}() is called.
     *
     * @return The item, or a value of null if the sequence has ended.
     */
    /*@Nullable*/ T produce();

}
