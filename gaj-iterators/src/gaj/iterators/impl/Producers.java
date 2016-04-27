/*
 * (c) Geoff Jarrad, 2015.
 */
package gaj.iterators.impl;

import gaj.iterators.core.Producer;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Enables a producer to be created from different types of sequences.
 */
public abstract class Producers {

    private static abstract class AbstractProducer<T> implements Producer<T> {

        protected T halt() {
        	throw new NoSuchElementException("End of supply");
        }

    }
    
    private Producers() {}

    /**
     * Provides a producer of an empty sequence.
     *
     * @return A producer of items that throws a {@link NoSuchElementException} when the sequence has finished.
     */
    public static <T> Producer<T> newProducer() {
        return new AbstractProducer<T>() {
            @Override
            public T get() {
            	return halt();
            }
        };
    }

    /**
     * Provides a producer of a singleton sequence.
     *
     * @param item - The singleton item.
     * @return A producer of items that throws a {@link NoSuchElementException} when the sequence has finished.
     */
    public static <T> Producer<T> newProducer(final T item) {
        return new AbstractProducer<T>() {
            private boolean hasNext = true;

            @Override
            public T get() {
                if (hasNext) {
                    hasNext = false;
                    return item;
                }
                return halt();
            }
        };
    }

    /**
     * Provides a producer of a non-null, repeated sequence.
     *
     * @param item - The item to be repeated.
     * @param count - The length of the sequence.
     * @return A producer of items that throws a {@link NoSuchElementException} when the sequence has finished.
     */
    public static <T> Producer<T> newProducer(final T item, final int count) {
        return new AbstractProducer<T>() {
            private int counter = count;

            @Override
            public T get() {
                if (counter > 0) {
                    counter--;
                    return item;
                }
                return halt();
            }
        };
    }

    /**
     * Provides a producer over an iterator sequence.
     *
     * @param iterator - An iterator over some arbitrary collection.
     * @return A producer of items that throws a {@link NoSuchElementException} when the sequence has finished.
     */
    public static <T> Producer<T> newProducer(final Iterator<? extends T> iterator) {
        return new AbstractProducer<T>() {
            @Override
            public T get() {
                return iterator.hasNext() ? iterator.next() : halt();
            }
        };
    }

    /**
     * Provides a producer over an iterable sequence.
     *
     * @param iterable - An iterable over some arbitrary collection.
     * @return A producer of items that throws a {@link NoSuchElementException} when the sequence has finished.
     */
    public static <T> Producer<T> newProducer(final Iterable<? extends T> iterable) {
        return new AbstractProducer<T>() {
            private Iterator<? extends T> iterator = null;

            @Override
            public T get() {
                if (iterator == null) {
                    iterator = iterable.iterator();
                }
                return iterator.hasNext() ? iterator.next() : halt();
            }
        };
    }

    /**
     * Provides a producer over an enumerator sequence.
     *
     * @param enumerator - An enumeration instance for some arbitrary collection.
     * @return A producer of items that throws a {@link NoSuchElementException} when the sequence has finished.
     */
    public static <T> Producer<T> newProducer(final Enumeration<? extends T> enumerator) {
        return new AbstractProducer<T>() {
            @Override
            public T get() {
                return enumerator.hasMoreElements() ? enumerator.nextElement() : halt();
            }
        };
    }

    /**
     * Provides a producer over an array of items.
     *
     * @param array - An array of elements to be iterated.
     * @return A producer of items that throws a {@link NoSuchElementException} when the sequence has finished.
     */
    public static <T> Producer<T> newProducer(final T[] array) {
        return new AbstractProducer<T>() {
            private int index = 0;

            @Override
            public T get() {
                return (index < array.length) ? array[index++] : halt();
            }
        };
    }

}
