package gaj.iterators.core;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Specifies a combined consumer and producer of typed objects.
 */
public interface ConsumerProducer<T> extends Consumer<T>, Producer<T> {

    /**
     * Consumes the next item in a sequence, for subsequent production. 
     * This method may block if necessary until {@link #get}() is called.
     * 
     * @param The next item in a sequence.
     */
    @Override
    void accept(/*@Nullable*/ T item);

    /**
     * Produces the next item in a sequence. This method may block if 
     * necessary until {@link #accept}() is called.
     *
     * @return The next item in a sequence.
     * @throws NoSuchElementException if the sequence has finished.
     */
    @Override
    /*@Nullable*/ T get();

}
