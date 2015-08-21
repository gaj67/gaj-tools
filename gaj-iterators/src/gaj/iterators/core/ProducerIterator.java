package gaj.iterators.core;

/**
 * Encapsulates a producer of a sequence of items.
 */
public abstract class ProducerIterator<T> extends IterableIterator<T> implements Producer<T> {

    private boolean hasNext = true; // Until proven otherwise.
    private T item = null;

    @Override
    public abstract /*@Nullable*/ T produce();

    @Override
    public boolean hasNext() {
        if (hasNext) {
            if (item == null) item = produce();
            if (item == null) hasNext = false;
            return hasNext;
        }
        return false;
    }

    @Override
    public T next() {
        if (hasNext()) {
            T _item = item;
            item = null;
            return _item;
        }
        return halt("End of sequence iteration");
    }

}
