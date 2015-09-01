/*
 * (c) Geoff Jarrad, 2015.
 */
package gaj.iterators.utilities;

import gaj.iterators.core.Producer;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Enables a producer to be created from different types of sequences.
 */
public abstract class ProducerFactory {

    private ProducerFactory() {}

    /**
     * Provides a producer of an empty sequence.
     *
     * @return A sequence producer.
     */
    public static <T> Producer<T> newProducer() {
        return new Producer<T>() {
            @Override
            public T produce() {
                return null;
            }
        };
    }

    /**
     * Provides a producer of a non-null, singleton sequence
     *
     * @param item - The non-null, singleton item.
     * @return A sequence producer.
     */
    public static <T> Producer<T> newProducer(final T item) {
        return new Producer<T>() {
            private boolean hasNext = true;

            @Override
            public T produce() {
                if (hasNext) {
                    hasNext = false;
                    return item;
                }
                return null;
            }
        };
    }

    /**
     * Provides a producer of a non-null, repeated sequence.
     *
     * @param item - The item to be repeated.
     * @param count - The length of the sequence.
     * @return A sequence producer.
     */
    public static <T> Producer<T> newProducer(final T item, final int count) {
        return new Producer<T>() {
            private int counter = count;

            @Override
            public T produce() {
                if (counter > 0) {
                    counter--;
                    return item;
                }
                return null;
            }
        };
    }

    /**
     * Provides a producer over an iterator sequence.
     *
     * @param iterator - An iterator over some arbitrary collection.
     * @return A producer bound to the given iterator.
     */
    public static <T> Producer<T> newIterator(final Iterator<? extends T> iterator) {
        return new Producer<T>() {
            @Override
            public T produce() {
                return iterator.hasNext() ? iterator.next() : null;
            }
        };
    }

    /**
     * Provides a producer over an iterable sequence.
     *
     * @param iterable - An iterable over some arbitrary collection.
     * @return A producer bound to the given iterable.
     */
    public static <T> Producer<T> newIterator(final Iterable<? extends T> iterable) {
        return new Producer<T>() {
            private Iterator<? extends T> iterator = null;

            @Override
            public T produce() {
                if (iterator == null) {
                    iterator = iterable.iterator();
                }
                return iterator.hasNext() ? iterator.next() : null;
            }
        };
    }

    /**
     * Provides a producer over an enumerator sequence.
     *
     * @param enumerator - An enumeration instance for some arbitrary collection.
     * @return A producer bound to the given enumerator.
     */
    public static <T> Producer<T> newIterator(final Enumeration<? extends T> enumerator) {
        return new Producer<T>() {
            @Override
            public T produce() {
                return enumerator.hasMoreElements() ? enumerator.nextElement() : null;
            }
        };
    }

    /**
     * Provides a producer over an array of items.
     *
     * @param array - An array of elements to be iterated.
     * @return A producer bound to the given array.
     */
    public static <T> Producer<T> newIterator(final T[] array) {
        return new Producer<T>() {
            private int index = 0;

            @Override
            public T produce() {
                return (index < array.length) ? array[index++] : null;
            }
        };
    }

}
