/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.core;

import java.util.Iterator;

/**
 * Encapsulates the notion of iterating through a sequence of iterators.
 */
public abstract class MultiSequenceIterator<T> extends IterableIterator<T> {

    private boolean hasNext = true;
    private /*@Nullable*/ Iterator<? extends T> iterator = null;

    /**
     * @return The next non-null iterator in the sequence,
     * or a value of null if there are no more iterators.
     */
    protected abstract /*@Nullable*/ Iterator<? extends T> getNextIterator();

    @Override
    public boolean hasNext() {
        if (hasNext) {
            if (iterator == null) {
                iterator = getNextIterator();
            }
            while (iterator != null && !iterator.hasNext()) {
                iterator = getNextIterator();
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
        return hasNext() ? iterator.next() : halt("End of sequence iteration");
    }

}
