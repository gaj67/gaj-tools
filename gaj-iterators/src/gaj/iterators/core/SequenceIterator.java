/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.core;

import java.util.Iterator;

public abstract class SequenceIterator<T> extends IterableIterator<T> {

    private boolean hasNext = true;
    private /*@Nullable*/ Iterator<? extends T> iterator = null;

    /**
     * Obtains the underlying iterator.
     * <br/>This method is implicitly called when {@link #hasNext()} or {@link #next}() is first called.
     * @return A non-null iterator over the resource.
     */
    protected abstract Iterator<? extends T> getIterator();

    @Override
    public boolean hasNext() {
        if (hasNext) {
            if (iterator == null) {
                iterator = getIterator();
            }
            if (hasNext = iterator.hasNext()) {
                return true;
            }
            // End of iteration.
            iterator = null;
        }
        return false;
    }

    @Override
    /*@SuppressWarnings("nullness")*/
    public T next() {
        return hasNext() ? iterator.next() : halt("End of sequence iteration");
    }
}
