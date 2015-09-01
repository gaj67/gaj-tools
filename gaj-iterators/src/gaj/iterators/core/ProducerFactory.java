/*
 * (c) Geoff Jarrad, 2015.
 */
package gaj.iterators.core;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Enables a producer to be created from different types of sequences.
 */
public abstract class ProducerFactory {

    private ProducerFactory() {}

    //===================================================================================
    // Producers over a single sequence.

    /**
     * 
     * @return A producer over an empty sequence.
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
     * 
     * @return A producer over a non-null singleton sequence.
     */
    public static <T> Producer<T> newProducer(final T singleton) {
        return new Producer<T>() {
            private boolean hasNext = true;

            @Override
            public T produce() {
                if (hasNext) {
                    hasNext = false;
                    return singleton;
                }
                return null;
            }
        };
    }

    /**
     * 
     * @return A producer over a non-null repeated sequence.
     */
    public static <T> Producer<T> newProducer(final T token, final int count) {
        return new Producer<T>() {
            private int counter = count;
            
            @Override
            public T produce() {
                if (counter > 0) {
                    counter--;
                    return token;
                }
                return null;
            }
        };
    }

    /**
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
     * 
     * @param iterable - An iterable over some arbitrary collection.
     * @return A producer bound to the given iterable.
     */
    public static <T> Producer<T> newIterator(final Iterable<? extends T> iterable) {
        return new Producer<T>() {
        	private Iterator<? extends T> iterator = null;
        	
			@Override
			public T produce() {
				if (iterator == null) iterator = iterable.iterator();
                return iterator.hasNext() ? iterator.next() : null;
			}
        };
    }

    /**
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
