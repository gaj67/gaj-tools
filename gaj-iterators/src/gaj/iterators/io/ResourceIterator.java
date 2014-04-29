/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.io;

import gaj.iterators.core.IterableIterator;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

/**
 * Encapsulates the generic use of an openable/closeable resource that may be iterated over.

 * <p/><b>Note:</b> The use of the resource is automatically deferred until the first use of the iterator,
 * whereupon {@link #openResource}() is called. Similarly, at the end of the iteration the resource is
 * automatically released via {@link #closeResource}(). If the iteration is terminated abruptly, then the public
 * {@link #close}() method should be called explicitly by the consumer.
 */
public abstract class ResourceIterator<T> extends IterableIterator<T> implements Closeable {

    private boolean hasNext = true;
    private /*@Nullable*/ Iterator<? extends T> iterator = null;
    private boolean closed = false;

    /**
     * Opens the resource for iteration.
     * <br/>This method is implicitly called when {@link #hasNext()} or {@link #next}() is first called.
     * @return A non-null iterator over the resource.
     */
    protected abstract Iterator<? extends T> openResource();

    /**
     * Closes the resource.
     * <br/>This method is implicitly called when {@link #close}() is called.
     * 
     * @throws IOException If the underlying resource fails to be closed.
     */
    protected abstract void closeResource() throws IOException;

    /**
     * Idempotently closes the resource.
     * <br/>This method is called implicitly when {@link #hasNext()} first becomes false.
     * <br/>Note: It should be called explicitly if the iteration is abandoned prematurely.
     */
    @Override
    public void close() {
        if (!closed) {
            closed = true; // XXX: Preserve idempotency.
            try {
                closeResource();
            } catch (IOException e) {
                throw failure(e);
            }
        }
    }

    @Override
    public boolean hasNext() {
        if (hasNext) {
            if (iterator == null) {
                iterator = openResource();
            }
            if (hasNext = iterator.hasNext()) {
                return true;
            }
            // End of iteration.
            iterator = null;
            close();
        }
        return false;
    }

    @Override
    /*@SuppressWarnings("nullness")*/
    public T next() {
        return hasNext() ? iterator.next() : halt("End of resource iteration");
    }

    /**
     * Wraps a checked exception, typically an IOException, as an unchecked IO exception.
     * 
     * @param e - The underlying exception.
     * @return UncheckedIOException The wrapped exception.
     */
    protected RuntimeException failure(Throwable e) {
    	return new UncheckedIOException(e);
    }
    
    /**
     * Creates an unchecked IO exception.
     * 
     * @param message - A description of the cause of the exception.
     * @return UncheckedIOException The exception.
     */
    protected RuntimeException failure(String message) {
    	return new UncheckedIOException(message);
    }
}
