package gaj.iterators.core;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * Specifies a supplier of items that throws the  NoSuchElementException when the sequence of items has been exhausted.
 */
public interface Producer<T> extends Supplier<T> {

    /**
     * @return An item.
     * @throws NoSuchElementException if the sequence has finished.
     */
    @Override
    public abstract T get();

    default T halt(String message) {
        throw new NoSuchElementException(message);
    }

}
