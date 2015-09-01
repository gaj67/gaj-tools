/*
 * (c) Geoff Jarrad, 2013. Updated 2015.
 */
package gaj.iterators.utilities;

import gaj.iterators.core.Filter;
import gaj.iterators.core.IterableIterator;
import gaj.iterators.core.MultiSequenceIterator;
import gaj.iterators.core.Producer;
import gaj.iterators.core.ProducerIterator;
import gaj.iterators.core.SequenceIterator;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * Encapsulates both the concepts of Iterable and Iterator into a single IterableIterator.
 * Provides a number of methods for creating an IterableIterator from a variety of inputs.
 * 
 * <br/>Note: The key idea is to create <em>efficient</em> iterators that defer the real work
 * for as long as possible. In other words, the constructor shouldn't do all the work up-front
 * and build a simple collection that is iterated over. Instead, the very act of iteration should step
 * through the construction of elements.
 */
public abstract class IteratorFactory {

    private IteratorFactory() {}

    //===================================================================================
    // Iterators over a single sequence.

    /**
     * Provides an iterable-iterator over an empty sequence.
     *  
     * @return An iterable-iterator.
     */
    public static <T> IterableIterator<T> newIterator() {
        return new IterableIterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return halt("End of empty iteration");
            }
        };
    }

    /**
     * Provides an iterable-iterator over a non-null, singleton sequence.
     * 
     * @param item - The non-null, singleton item.
     * @return An iterable-iterator.
     */
    public static <T> IterableIterator<T> newIterator(final T item) {
        return new IterableIterator<T>() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public T next() {
                if (hasNext) {
                    hasNext = false;
                    return item;
                }
                return halt("End of singleton iteration");
            }
        };
    }

    /**
     * Provides an iterable-iterator over a non-null, repeated sequence.
     *
     * @param item - The non-null item to be repeated.
     * @param count - The length of the sequence.
     * @return An iterable-iterator.
     */
    public static <T> IterableIterator<T> newIterator(final T item, final int count) {
        return new IterableIterator<T>() {
            private int counter = count;
            
            @Override
            public boolean hasNext() {
                return counter > 0;
            }

            @Override
            public T next() {
                if (counter > 0) {
                    counter--;
                    return item;
                }
                return halt("End of repeated token iteration");
            }
        };
    }

    /**
     * Provides an iterable-iterator over an iterator sequence.
     *  
     * @param iterator - An iterator over some arbitrary sequence.
     * @return An iterable-iterator.
     */
    public static <T> IterableIterator<T> newIterator(final Iterator<? extends T> iterator) {
        return new IterableIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }
        };
    }

    /**
     * Provides an iterable-iterator over an iterable sequence.
     * 
     * @param iterable - An iterable over some arbitrary sequence.
     * @return An iterable-iterator.
     */
    public static <T> IterableIterator<T> newIterator(final Iterable<? extends T> iterable) {
        return new SequenceIterator<T>() {
            @Override
            protected Iterator<? extends T> getIterator() {
                return iterable.iterator();
            }
        };
    }

    /**
     * Provides an iterable-iterator over an enumerated sequence.
     * 
     * @param enumerator - An enumeration instance for some arbitrary sequence.
     * @return An iterable-iterator.
     */
    public static <T> IterableIterator<T> newIterator(final Enumeration<? extends T> enumerator) {
        return new IterableIterator<T>() {
            @Override
            public boolean hasNext() {
                return enumerator.hasMoreElements();
            }

            @Override
            public T next() {
                return enumerator.nextElement();
            }
        };
    }

    /**
     * Provides an iterable-iterator over an array of items. 
     * 
     * @param array - An array of elements.
     * @return An iterable-enumerator.
     */
    public static <T> IterableIterator<T> newIterator(final T[] array) {
        return new IterableIterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public T next() {
                return hasNext() ? array[index++] : halt("End of array");
            }
        };
    }

    /**
     * Provides an iterable-iterator over a sequence producer.
     * 
     * @param producer - A producer of items.
     * @return An iterable-enumerator bound.
     */
    public static <T> IterableIterator<T> newIterator(final Producer<T> producer) {
        return new ProducerIterator<T>() {
			@Override
			public T produce() {
				return producer.produce();
			}
        };
    }

    //===================================================================================
    // Filtered iterators over a single sequence.

    /**
     * Creates a filtered iterable-iterator.
     * 
     * @param iterable - An iterable over elements of the input type.
     * @param filter - A filter that transforms elements to the output type, and/or causes elements
     * to be skipped during iteration.
     * @return A filtered iterable-iterator bound to the given filter and input iterator.
     */
    public static <T,V> IterableIterator<V> newIterator(final Iterable<? extends T> iterable, final Filter<T,? extends V> filter) {
        return new IterableIterator<V>() {
            private /*@LazyNonNull*/ Iterator<? extends T> iterator = null;
            private /*@Nullable*/ V element = null;

            @Override
            public boolean hasNext() {
                if (iterator == null) {
                    iterator = iterable.iterator(); // Deferred construction.
                }
                while (element == null && iterator.hasNext()) {
                    element = filter.filter(iterator.next());
                }
                return element != null;
            }

            @Override
            public V next() {
                if (hasNext()) {
                    /*@SuppressWarnings("nullness")*/ V elem = element;
                    element = null;
                    return elem;
                }
                return halt("End of filtered iteration");
            }
        };
    }

    //===================================================================================
    // Iterators over multiple sequences.

    /**
     * Provides an iterable-iterator over an array of sequences.
     * 
     * @param iterators - An array of iterator instances.
     * @return A multi-sequence iterable-iterator bound to the given array elements.
     */
    public static <T> IterableIterator<T> newMultiIterator(@SuppressWarnings("unchecked") final Iterator<? extends T>... iterators) {
        return new MultiSequenceIterator<T>() {
            private int index = -1;

            @Override
            protected /*@Nullable*/ Iterator<? extends T> getNextIterator() {
                Iterator<? extends T> iterator = null;
                while (iterator == null && ++index < iterators.length)
                    iterator = iterators[index];
                return iterator;
            }
        };
    }

    /**
     * Provides an iterable-iterator over an array of sequences.
     * 
     * @param iterables - An array of iterable instances.
     * @return A multi-sequence iterable-iterator bound to the given array elements.
     */
    public static <T> IterableIterator<T> newMultiIterator(@SuppressWarnings("unchecked") final Iterable<? extends T>... iterables) {
        return new MultiSequenceIterator<T>() {
            private int index = -1;

            @Override
            protected /*@Nullable*/ Iterator<? extends T> getNextIterator() {
                Iterable<? extends T> iterable = null;
                while (iterable == null && ++index < iterables.length)
                    iterable = iterables[index];
                return (iterable != null) ? iterable.iterator() : null;
            }
        };
    }

    /**
     * Provides an iterable-iterator over a sequence of sequences.
     *   
     * @param iterables - An iterable of iterable instances.
     * @return A multi-sequence iterable-iterator bound to the given iterables.
     */
    public static <T> IterableIterator<T> newMultiIterator(final Iterable<? extends Iterable<? extends T>> iterables) {
        return new MultiSequenceIterator<T>() {
            private /*@LazyNonNull*/ Iterator<? extends Iterable<? extends T>> iterator = null;

            @Override
            protected /*@Nullable*/ Iterator<? extends T> getNextIterator() {
                if (iterator == null) {
                    iterator = iterables.iterator(); // Deferred construction.
                }
                return iterator.hasNext() ? iterator.next().iterator() : null;
            }
        };
    }

    //===================================================================================
    // Filtered iterators over multiple sequences.

    /**
     * Creates a filtered iterable-iterator.
     * @param iterable - An iterable over elements of the input type.
     * @param filter - A filter that transforms elements to the output type, and/or causes elements
     * to be skipped during iteration.
     * @return A filtered iterable-iterator bound to the given filter and input iterable.
     */
    public static <T,V> IterableIterator<V> newMultiIterator(final Iterable<? extends T> iterable, final Filter<T,Iterable<? extends V>> filter) {
    	return newMultiIterator(
    			new IterableIterator<Iterable<? extends V>>() {
    				private /*@LazyNonNull*/ Iterator<? extends T> iterator = null;
    				private /*@Nullable*/ Iterable<? extends V> element = null;

    				@Override
    				public boolean hasNext() {
    					if (iterator == null) {
    						iterator = iterable.iterator(); // Deferred construction.
    					}
    					while (element == null && iterator.hasNext()) {
    						element = filter.filter(iterator.next());
    					}
    					return element != null;
    				}

    				@Override
    				/*@SuppressWarnings("nullness")*/
    				public Iterable<? extends V>  next() {
    					if (hasNext()) {
    						Iterable<? extends V>  elem = element;
    						element = null;
    						return elem;
    					}
    					return halt("End of filtered iteration");
    				}
    			});
    }

    /**
     * Provides an iterable-iterator over a sequence of maps.
     * 
     * @param maps - An iterable of maps.
     * @return A iterable-iterator bound to the keys of the maps.
     */
    public static <K,V> IterableIterator<K> newMapKeysMultiIterator(final Iterable<? extends Map<? extends K,V>> maps) {
        return new MultiSequenceIterator<K>() {
            private /*@LazyNonNull*/ Iterator<? extends Map<? extends K, V>> iterator = null;

            @Override
            protected /*@Nullable*/ Iterator<? extends K> getNextIterator() {
                if (iterator == null) {
                    iterator = maps.iterator(); // Deferred construction.
                }
                return iterator.hasNext() ? (Iterator<? extends K>)iterator.next().keySet().iterator() : null;
            }
        };
    }

    /**
     * Provides an iterable-iterator over a sequence of maps.
     *  
     * @param maps - An iterable of maps.
     * @return A iterable-iterator bound to the values of the maps.
     */
    public static <K,V> IterableIterator<V> newMapValuesMultiIterator(final Iterable<? extends Map<K,? extends V>> maps) {
        return new MultiSequenceIterator<V>() {
            private /*@LazyNonNull*/ Iterator<? extends Map<K,? extends V>> iterator = null;

            @Override
            protected /*@Nullable*/ Iterator<? extends V> getNextIterator() {
                if (iterator == null) {
                    iterator = maps.iterator(); // Deferred construction.
                }
                return iterator.hasNext() ? iterator.next().values().iterator() : null;
            }
        };
    }

}
