/*
 * (c) Geoff Jarrad, 2013. Updated 2015.
 */
package gaj.iterators.impl;

import gaj.iterators.core.Iterative;
import gaj.iterators.core.Producer;
import gaj.iterators.core.SingleUseIterative;
import gaj.iterators.core.StreamOp;
import gaj.iterators.core.StreamableIterator;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A factory for building single-use {@link Iterative} instances.
 */
public abstract class Iteratives {

    private Iteratives() {}

    //===================================================================================
    // Iteratives over a single sequence.

    /**
     * Provides an iterative over an empty sequence.
     *
     * @return The iterative.
     */
    public static <T> Iterative<T> newIterative() {
        return new Iterative<T>() {
			@Override
			public StreamableIterator<T> iterator() {
				return Iterators.newIterator();
			}
        };
    }

    /**
     * Provides an iterative over a singleton sequence.
     *
     * @param item - The singleton item.
     * @return The iterative.
     */
    public static <T> Iterative<T> newIterative(final T item) {
        return new Iterative<T>() {
			@Override
			public StreamableIterator<T> iterator() {
				return Iterators.newIterator(item);
			}
        };
    }

    /**
     * Provides an iterative over a repeated sequence.
     *
     * @param item - The item to be repeated.
     * @param count - The length of the sequence.
     * @return The iterative.
     */
    public static <T> Iterative<T> newIterative(final T item, final int count) {
        return new Iterative<T>() {
			@Override
			public StreamableIterator<T> iterator() {
				return Iterators.newIterator(item, count);
			}
        };
    }

    /**
     * Provides a <b>single-use</b> iterative over an iterator sequence.
     *
     * @param iterator - An iterator over some arbitrary sequence.
     * @return An iterative bound to the given iterator.
     */
    public static <T> SingleUseIterative<T> newIterative(final Iterator<? extends T> iterator) {
        return new SingleUseIterative<T>() {
			@Override
			public StreamableIterator<T> iterator() {
				return Iterators.toStreamableIterator(iterator);
			}
        };
    }

    /**
     * Provides an iterative over an iterable sequence.
     *
     * @param iterable - An iterable over some arbitrary sequence.
     * @return An iterative bound to the given iterable.
     */
    @SuppressWarnings("unchecked")
	public static <T> Iterative<T> toIterative(final Iterable<? extends T> iterable) {
    	if (iterable instanceof Iterative) {
    		return (Iterative<T>) iterable;
    	}
        return new Iterative<T>() {
            @Override
			public StreamableIterator<T> iterator() {
                return Iterators.toStreamableIterator(iterable.iterator());
            }
        };
    }

    /**
     * Provides a <b>single-use</b> iterative over an enumerated sequence.
     *
     * @param enumerator - An enumeration instance for some arbitrary sequence.
     * @return An iterative bound to the given enumerator.
     */
    public static <T> SingleUseIterative<T> newIterative(final Enumeration<? extends T> enumerator) {
        return new SingleUseIterative<T>() {
            @Override
			public StreamableIterator<T> iterator() {
                return Iterators.newIterator(enumerator);
            }
        };
    }

    /**
     * Provides an iterative over an array of items.
     *
     * @param array - An array of elements.
     * @return An iterative bound to the given array.
     */
    public static <T> Iterative<T> newIterative(final T[] array) {
        return new Iterative<T>() {
            @Override
			public StreamableIterator<T> iterator() {
                return Iterators.newIterator(array);
            }
        };
    }

    /**
     * Provides a <b>single-use</b> iterative over a sequence producer.
     *
	 * @param producer - A producer of items.
     * @return An iterative bound to the given producer.
     */
    public static <T> SingleUseIterative<T> newIterative(final Producer<? extends T> producer) {
        return new SingleUseIterative<T>() {
            @Override
			public StreamableIterator<T> iterator() {
                return Iterators.newIterator(producer);
            }
        };
    }

    /**
     * Provides a <b>single-use</b> iterative over a stream.
     * <br/>Note that the underlying stream cannot be reused once it has been converted into an iterator.
     *
	 * @param stream - A stream of items.
     * @return An iterative bound to the given stream.
     */
    public static <T> SingleUseIterative<T> newIterative(final Stream<? extends T> stream) {
        return new SingleUseIterative<T>() {
			private StreamableIterator<T> iter = null;

            @SuppressWarnings("unchecked")
			@Override
			public StreamableIterator<T> iterator() {
				if (iter == null) iter = (StreamableIterator<T>) Iterators.newIterator(stream);
				return iter;
            }
        };
    }


    //===================================================================================
    // Iterative filtering and mapping.

	public static <T> Iterative<T> apply(Iterable<?> iterable, StreamOp<?,?>... ops) {
        return new Iterative<T>() {
            @Override
			public StreamableIterator<T> iterator() {
                return Iterators.apply(iterable.iterator(), ops);
            }
        };
	}


    
    //===================================================================================
    // Iteratives over multiple sequences.

    /**
     * Provides a <b>single-use</b> iterative over an array of sequences.
     *
     * @param iterators - An array of iterator instances.
     * @return A multi-sequence iterative bound to the given array elements.
     */
    @SuppressWarnings("unchecked")
	public static <T> SingleUseIterative<T> newMultiIterative(final Iterator<? extends T>... iterators) {
        return new SingleUseIterative<T>() {
			@Override
			public StreamableIterator<T> iterator() {
				return Iterators.newMultiIterator(iterators);
			}
        };
    }

    /**
     * Provides an iterative over an array of sequences.
     *
     * @param iterables - An array of iterable instances.
     * @return A multi-sequence iterative bound to the given iterables.
     */
    @SuppressWarnings("unchecked") 
    public static <T> Iterative<T> newMultiIterative(final Iterable<? extends T>... iterables) {
        return new Iterative<T>() {
			@Override
			public StreamableIterator<T> iterator() {
				return Iterators.newMultiIterator(iterables);
			}
        };
    }

    @SuppressWarnings("unchecked")
	public static <T> SingleUseIterative<T> newMultiIterative(final Stream<? extends T>... streams) {
    	return newIterative(Iterators.newMultiStream(streams));
    }

    /**
     * Provides an iterative over a sequence of sub-sequences. Whether or not the iterative is reusable or
     * single-use depends upon the inputs.
     *
     * @param iterable - An iterable of sub-sequences.
     * @return A multi-sequence iterative bound to the given sub-sequences.
     */
    public static <T> Iterative<T> newGenericMultiIterative(final Iterable<?> iterable) {
        return new Iterative<T>() {
			@Override
			public StreamableIterator<T> iterator() {
				return Iterators.newGenericMultiIterator(iterable);
			}
        };
    }

    //===================================================================================
    // Iteratives over multiple Map sequences.

    /**
     * Provides an iterative over a flattened sequence of map key-sets.
     *
     * @param maps - An iterable of maps.
     * @return A multi-sequence iterative bound to the keys of the maps.
     */
    public static <K,V> Iterative<K> newMapKeysMultiIterative(final Iterable<? extends Map<? extends K, V>> maps) {
    	return new Iterative<K>() {
			@Override
			public StreamableIterator<K> iterator() {
				return Iterators.newMapKeysMultiIterator(maps);
			}
    	};
    }

    /**
     * Provides an iterative over a flattened sequence of map values.
     *
     * @param maps - An iterable of maps.
     * @return A multi-sequence iterative bound to the values of the maps.
     */
    public static <K,V> Iterative<V> newMapValuesMultiIterative(final Iterable<? extends Map<K, ? extends V>> maps) {
    	return new Iterative<V>() {
			@Override
			public StreamableIterator<V> iterator() {
				return Iterators.newMapValuesMultiIterator(maps);
			}
    	};
    }

}
