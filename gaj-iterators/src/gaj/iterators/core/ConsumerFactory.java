/*
 * (c) Geoff Jarrad, 2015.
 */
package gaj.iterators.core;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Enables the consumption of various types of sequences.
 */
public abstract class ConsumerFactory {

    private ConsumerFactory() {}

    /**
     * Provides a consumer that accepts any item.
     * 
     * @return A consumer of items.
     */
    public static <T> Consumer<T> newConsumer() {
    	return new Consumer<T>() {
			@Override
			public boolean consume(T item) {
				return true;
			}
    	};
    }
    
    /**
     * Consumes a singleton sequence.
     * 
     * @param consumer - A consumer of items.
     * @param item - The non-null, singleton item.
     */
    public static <T> void consume(final Consumer<? super T> consumer, final T item) {
    	consumer.consume(item);
    	consumer.consume(null);
    }

    /**
     * Consumes a repetitive sequence of given length.
     * 
     * @param consumer - A consumer of items.
     * @param item - The non-null item to be repeated.
     * @param count - The length of the sequence.
     */
    public static <T> void consume(final Consumer<? super T> consumer, final T item, final int count) {
    	for (int i = 0; i < count; i++) {
    		consumer.consume(item);
    	}
    	consumer.consume(null);
    }

    /**
     * Consumes an iterator of items.
     *  
     * @param consumer - A consumer of items.
     * @param iterator - An iterator of non-null items.
     */
    public static <T> void consume(Consumer<? super T> consumer, Iterator<? extends T> iterator) {
    	while (iterator.hasNext()) {
    		consumer.consume(iterator.next());
    	}
    	consumer.consume(null);
    } 

    /**
     * Consumes an iterable of items.
     *  
     * @param consumer - A consumer of items.
     * @param iterable - An iterable of non-null items.
     */
    public static <T> void consume(Consumer<? super T> consumer, Iterable<? extends T> iterable) {
    	for (T item : iterable) {
    		consumer.consume(item);
    	}
    	consumer.consume(null);
    } 

    /**
     * Consumes an enumerator of items.
     *  
     * @param consumer - A consumer of items.
     * @param enumerator - An enumerator of non-null items.
     */
    public static <T> void consume(Consumer<? super T> consumer, final Enumeration<? extends T> enumerator) {
    	while (enumerator.hasMoreElements()) {
    		consumer.consume(enumerator.nextElement());
    	}
    	consumer.consume(null);
    }

    /**
     * Consumes an array of items.
     *  
     * @param consumer - A consumer of items.
     * @param array - An array of non-null elements to be consumed.
     */
    public static <T> void consume(Consumer<? super T> consumer, final T[] array) {
    	for (int i = 0; i < array.length; i++) {
    		consumer.consume(array[i]);
    	}
    	consumer.consume(null);
    }

    /**
     * Consumes an producer of items.
     *  
     * @param consumer - A consumer of items.
     * @param producer - An producer of non-null items.
     */
    public static <T> void consume(Consumer<? super T> consumer, final Producer<? extends T> producer) {
    	while (true) {
    		T item = producer.produce();
    		consumer.consume(item);
    		if (item == null) break;
    	}
    }

}
