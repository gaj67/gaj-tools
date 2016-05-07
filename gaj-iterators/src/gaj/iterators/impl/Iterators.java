/*
 * (c) Geoff Jarrad, 2013. Updated 2015.
 */
package gaj.iterators.impl;

import gaj.iterators.core.Producer;
import gaj.iterators.core.StreamOp;
import gaj.iterators.core.StreamableIterator;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * A factory for building {@link StreamableIterator} instances.
 */
public abstract class Iterators {

    private Iterators() {}

    //===================================================================================
    // Iterators over a single sequence.

    /**
     * Provides an iterator over an empty sequence.
     *
     * @return The iterator.
     */
    public static <T> StreamableIterator<T> newIterator() {
        return new BaseIterator<T>() {
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
     * Provides an iterator over a singleton sequence.
     *
     * @param item - The singleton item.
     * @return The iterator.
     */
    public static <T> StreamableIterator<T> newIterator(final T item) {
        return new BaseIterator<T>() {
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
     * Provides an iterator over a repeated sequence.
     *
     * @param item - The item to be repeated.
     * @param count - The length of the sequence.
     * @return The iterator.
     */
    public static <T> StreamableIterator<T> newIterator(final T item, final int count) {
        return new BaseIterator<T>() {
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
     * Provides an iterator over an array of items.
     *
     * @param array - An array of elements.
     * @return An iterator bound to the given array.
     */
    public static <T> StreamableIterator<T> newIterator(final T[] array) {
        return new BaseIterator<T>() {
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
     * Provides an iterator over an enumerator.
     * 
     * @param enumerator - An enumerator of items.
     * @return An iterator bound to the given enumerator.
     */
    public static <T> StreamableIterator<T> newIterator(final Enumeration<? extends T> enumerator) {
        return new BaseIterator<T>() {
            @Override
            public boolean hasNext() {
                return enumerator.hasMoreElements();
            }

            @Override
            public T next() {
                return hasNext() ? enumerator.nextElement() : halt("End of sequence");
            }
        };
    }

    public static <T> StreamableIterator<T> newIterator(final Producer<? extends T> producer) {
        return new BaseIterator<T>() {
            private boolean hasNext = true; // Until proven otherwise.
        	private boolean haveItem = false;
            private T item = null;

            @Override
            public boolean hasNext() {
                if (hasNext) {
                    if (!haveItem) {
                    	try {
                    		item = producer.get();
                        	haveItem = true;
                    	} catch (NoSuchElementException e) {
                    		hasNext = false;
                    	}
                    }
                }
                return hasNext;
            }

            @Override
            public T next() {
                if (hasNext()) {
                	haveItem = false;
                    return item;
                }
                return halt("End of sequence iteration");
            }

        };
    }

    @SuppressWarnings("unchecked")
	public static <T> StreamableIterator<T> toStreamableIterator(Iterator<? extends T> iterator) {
    	if (iterator instanceof StreamableIterator) return (StreamableIterator<T>) iterator; 
    	return new BaseIterator<T>() {
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
     * Provides an iterator over a stream.
     * <p/>The stream must be explicitly closed by the caller.
     *
	 * @param stream - A stream of items.
     * @return An iterator bound to the given stream.
     */
    public static <T> StreamableIterator<T> newIterator(final Stream<? extends T> stream) {
        return new BaseIterator<T>() {
        	@SuppressWarnings("unchecked")
			private Stream<T> _stream = (Stream<T>) stream;
        	private StreamableIterator<T> iter = null;

			private StreamableIterator<T> iterator() {
				if (iter != null) return iter;
				if (_stream != null) {
					iter = toStreamableIterator(_stream.iterator());
					// Stream instance is now terminal and cannot be re-used.
					_stream = null;
					return iter;
				}
				throw new IllegalStateException("Have neither Stream nor StreambleIterator");
			}

			@Override
			public boolean hasNext() {
				return iterator().hasNext();
			}

			@Override
			public T next() {
				return iterator().next();
			}

			@Override
			public Stream<T> stream() {
				if (_stream != null) return _stream;
				_stream = iterator().stream();
				return _stream;
            }
        };
    }


    //===================================================================================
    // Iterator filtering and mapping.

	@SuppressWarnings("unchecked")
	public static <T> StreamableIterator<T> apply(Iterator<?> iterator, StreamOp<?, ?>... ops) {
		return (StreamableIterator<T>) _apply((Iterator<Object>) iterator, (StreamOp<Object, Object>) (Object) ops);
	}
	
	@SuppressWarnings("unchecked")
	public static StreamableIterator<Object> _apply(Iterator<Object> iterator, StreamOp<Object, Object>... ops) {
		Stream<Object> stream = toStreamableIterator(iterator).stream();
		for (StreamOp<Object, Object> op : ops) {
			stream = op.apply(stream);
		}
		return newIterator(stream);
	}

    //===================================================================================
    // Iterators over multiple sequences.

    @SuppressWarnings("unchecked")
	public static <T> StreamableIterator<T> newMultiIterator(final Iterator<? extends T>... iterators) {
		return new MultiIterator<T>() {
        	private int idx = 0;
        	
			@Override
			protected /*@Nullable*/ Iterator<? extends T> nextIterator() {
				return (idx >= iterators.length) ? null : iterators[idx++];
			}
		};
    }

    /**
     * Wraps an iterable of iterators into a single iterator.
     * 
     * @param iterable - An iterable of sub-sequences.
     * @return An iterator bound to the given sub-sequences.
     */
	public static <T> StreamableIterator<T> newMultiIterator(final Iterable<? extends Iterator<? extends T>> iterable) {
		return new MultiIterator<T>() {
        	private Iterator<? extends Iterator<? extends T>> iter = null;
        	
			@Override
			protected /*@Nullable*/ Iterator<? extends T> nextIterator() {
				if (iter == null) {
					iter = iterable.iterator();
				}
				return iter.hasNext() ? iter.next() : null;
			}
		};
    }

    @SuppressWarnings("unchecked")
	public static <T> StreamableIterator<T> newMultiIterator(final Iterable<? extends T>... iterables) {
		return new MultiIterator<T>() {
        	private int idx = 0;
        	
			@Override
			protected Iterator<? extends T> nextIterator() {
				return (idx >= iterables.length) ? null : iterables[idx++].iterator();
			}
		};
    }

    /**
     * Wraps an iterable of iterables into a single iterator.
     * 
     * @param iterable - An iterable of sub-sequences.
     * @return An iterator bound to the given sub-sequences.
     */
	public static <T> StreamableIterator<T> newIterablesIterator(final Iterable<? extends Iterable<? extends T>> iterable) {
		return new MultiIterator<T>() {
        	private Iterator<? extends Iterable<? extends T>> iter = null;
        	
			@Override
			protected /*@Nullable*/ Iterator<? extends T> nextIterator() {
				if (iter == null) {
					iter = iterable.iterator();
				}
				return iter.hasNext() ? iter.next().iterator() : null;
			}
		};
    }

    @SuppressWarnings("unchecked")
	public static <T> StreamableIterator<T> newMultiIterator(final Stream<? extends T>... streams) {
		return new MultiIterator<T>() {
        	private int idx = 0;
        	
			@Override
			protected Iterator<? extends T> nextIterator() {
				return (idx >= streams.length) ? null : streams[idx++].iterator();
			}
		};
    }

    /**
     * Wraps an iterable of streams into a single iterator.
     * 
     * @param iterable - An iterable of sub-sequences.
     * @return An iterator bound to the given sub-sequences.
     */
	public static <T> StreamableIterator<T> newStreamsIterator(final Iterable<? extends Stream<? extends T>> iterable) {
		return new MultiIterator<T>() {
        	private Iterator<? extends Stream<? extends T>> iter = null;
        	
			@Override
			protected /*@Nullable*/ Iterator<? extends T> nextIterator() {
				if (iter == null) {
					iter = iterable.iterator();
				}
				return iter.hasNext() ? iter.next().iterator() : null;
			}
		};
    }

    @SuppressWarnings("unchecked")
	public static <T> Stream<T> newMultiStream(final Stream<? extends T>... streams) {
    	List<Stream<? extends T>> list = Arrays.asList(streams);
    	return list.stream().flatMap(s -> s);
    }

    /**
     * Wraps an iterable of sub-sequences into a single iterator.
     * 
     * @param iterable - An iterable of sub-sequences.
     * @return An iterator bound to the given sub-sequences.
     */
	@SuppressWarnings("unchecked")
	public static <T> StreamableIterator<T> newGenericMultiIterator(final Iterable<?> iterable) {
		return new MultiIterator<T>() {
        	private Iterator<?> iter = null;
        	
			@Override
			protected /*@Nullable*/ Iterator<? extends T> nextIterator() {
				if (iter == null) {
					iter = iterable.iterator();
				}
				if (iter.hasNext()) {
					Object obj = iter.next();
					if (obj instanceof Iterator) {
						return (Iterator<? extends T>) obj;
					} else if (obj instanceof Iterable) {
						return ((Iterable<? extends T>) obj).iterator();
					} else if (obj instanceof Stream) {
						return ((Stream<? extends T>) obj).iterator();
					} else {
						throw new IllegalArgumentException("Unknown sub-sequence: " + obj);
					}
				} else {
					return null;
				}
			}
		};
    }


    //===================================================================================
    // Iterators over multiple Map sequences.

    /**
     * Provides an iterator over a flattened sequence of map key-sets.
     *
     * @param maps - An iterable of maps.
     * @return A multi-sequence iterator bound to the keys of the maps.
     */
    public static <K,V> StreamableIterator<K> newMapKeysMultiIterator(final Iterable<? extends Map<? extends K, V>> maps) {
    	return new MultiIterator<K>() {
    		private Iterator<? extends Map<? extends K, V>> iter = null;
    		
			@Override
			protected Iterator<? extends K> nextIterator() {
				if (iter == null) iter = maps.iterator();
				return iter.hasNext() ? iter.next().keySet().iterator() : null;
			}
		};
    }
    
    /**
     * Provides an iterator over a flattened sequence of map values.
     *
     * @param maps - An iterable of maps.
     * @return A multi-sequence iterative bound to the values of the maps.
     */
    public static <K,V> StreamableIterator<V> newMapValuesMultiIterator(final Iterable<? extends Map<K, ? extends V>> maps) {
    	return new MultiIterator<V>() {
    		private Iterator<? extends Map<K, ? extends V>> iter = null;
    		
			@Override
			protected Iterator<? extends V> nextIterator() {
				if (iter == null) iter = maps.iterator();
				return iter.hasNext() ? iter.next().values().iterator() : null;
			}
		};
    }
    
}
