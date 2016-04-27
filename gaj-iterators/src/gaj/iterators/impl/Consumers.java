/*
 * (c) Geoff Jarrad, 2015.
 */
package gaj.iterators.impl;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Enables the consumption of various types of sequences.
 */
public abstract class Consumers {

	private Consumers() {}

	/**
	 * Provides a consumer that discards all items.
	 *
	 * @return A consumer of items.
	 */
	public static <T> Consumer<T> newConsumer() {
		return new Consumer<T>() {
			@Override
			public void accept(T item) {}
		};
	}

	/**
	 * Provides a consumer that adds all items to a collection.
	 *
	 * @param collection - A collection of items.
	 * @return A consumer of items.
	 */
	public static <T> Consumer<T> newConsumer(final Collection<? super T> collection) {
		return new Consumer<T>() {
			@Override
			public void accept(T item) {
				collection.add(item);
			}
		};
	}

	/**
	 * Consumes a single item.
	 *
	 * @param item - The item.
	 * @param consumer - A consumer of items.
	 */
	public static <T> void consume(final T item, final Consumer<? super T> consumer) {
		consumer.accept(item);
	}

	/**
	 * Consumes a single item multiple times.
	 *
	 * @param item - The item to be repeated.
	 * @param count - The length of the sequence.
	 * @param consumer - A consumer of items.
	 */
	public static <T> void consume(final T item, final int count, final Consumer<? super T> consumer) {
		for (int i = 0; i < count; i++) {
			consumer.accept(item);
		}
	}

	/**
	 * Consumes a sequence of items.
	 *
	 * @param iterator - An iterator of items.
	 * @param consumer - A consumer of items.
	 */
	public static <T> void consume(Iterator<? extends T> iterator, Consumer<? super T> consumer) {
		while (iterator.hasNext()) {
			consumer.accept(iterator.next());
		}
	}

	/**
	 * Consumes a sequence of items.
	 *
	 * @param iterable - An iterable of items.
	 * @param consumer - A consumer of items.
	 */
	public static <T> void consume(Iterable<? extends T> iterable, Consumer<? super T> consumer) {
		for (T item : iterable) {
			consumer.accept(item);
		}
	}

	/**
	 * Consumes a sequence of items.
	 *
	 * @param enumerator - An enumerator of items.
	 * @param consumer - A consumer of items.
	 */
	public static <T> void consume(final Enumeration<? extends T> enumerator, Consumer<? super T> consumer) {
		while (enumerator.hasMoreElements()) {
			consumer.accept(enumerator.nextElement());
		}
	}

	/**
	 * Consumes a sequence of items.
	 *
	 * @param array - An array of elements.
	 * @param consumer - A consumer of items.
	 */
	public static <T> void consume(final T[] array, Consumer<? super T> consumer) {
		for (T element : array) {
			consumer.accept(element);
		}
	}

	/**
	 * Consumes a sequence of items.
	 *
	 * @param producer - A producer of items that throws {@link NoSuchElementException} when the sequence has finished.
	 * @param consumer - A consumer of items.
	 */
	public static <T> void consume(final Supplier<? extends T> producer, Consumer<? super T> consumer) {
		while (true) {
			try {
				consumer.accept(producer.get());
			} catch (NoSuchElementException e) {
				break;
			}
		}
	}

}
