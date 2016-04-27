/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;

/**
 * Encapsulates the notion of iterating through a sequence of iterators.
 * This provides a flattened sequence of items obtain from a sequence of sequences.
 */
public abstract class MultiIterator<T> implements BaseIterator<T> {

    protected boolean hasNext = true;
    protected Iterator<? extends T> iterator = null;

    protected MultiIterator() {}

    /**
     * @return The next non-null iterator in the sequence,
     * or a value of null if there are no more iterators.
     */
    protected abstract /*@Nullable*/ Iterator<? extends T> nextIterator();

    @Override
    public boolean hasNext() {
        if (hasNext) {
            if (iterator == null) {
                iterator = nextIterator();
            }
            while (iterator != null && !iterator.hasNext()) {
                iterator = nextIterator();
            }
            if (iterator == null) {
                hasNext = false;
            }
        }
        return hasNext;
    }

    @Override
    /*@SuppressWarnings("nullness")*/
    public T next() {
        return hasNext() ? iterator.next() : halt("End of multi-sequence iteration");
    }

}
