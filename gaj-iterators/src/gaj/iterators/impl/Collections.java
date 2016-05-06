/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * Encapsulates various collection-type operations on sequences.
 */
public abstract class Collections {

    private Collections() {}

    /**
     * Adds all of the iterator elements to the collection.
     *
     * @param collection - A collection of elements.
     * @param iterator - An iterator over a sequence.
     */
    public static <T> void addAll(Collection<? super T> collection, Iterator<? extends T> iterator) {
        while (iterator.hasNext()) {
            collection.add(iterator.next());
        }
    }

    /**
     * Adds all of the iterable elements to the collection.
     *
     * @param collection - A collection of elements.
     * @param iterable - An iterable over a sequence.
     */
    public static <T> void addAll(Collection<? super T> collection, Iterable<? extends T> iterable) {
        for (T elem : iterable) {
            collection.add(elem);
        }
    }

    /**
     * Adds all of the enumeration elements to the collection.
     *
     * @param collection - A collection of elements.
     * @param enumerator - An enumerator over a sequence.
     */
    public static <T> void addAll(Collection<? super T> collection, Enumeration<? extends T> enumerator) {
        while (enumerator.hasMoreElements()) {
            collection.add(enumerator.nextElement());
        }
    }

    /**
     * Adds all of an array of elements to the collection.
     *
     * @param collection - A collection of elements.
     * @param array - An array of items.
     */
    public static <T> void addAll(Collection<? super T> collection, T[] array) {
        for (T element : array) {
            collection.add(element);
        }
    }

    /**
     * Adds all of the producer items to the collection.
     *
     * @param collection - A collection of elements.
     * @param producer - A supplier of items that throws NoSuchElementException at the end of the sequence.
     */
    public static <T> void addAll(Collection<? super T> collection, Supplier<? extends T> producer) {
        while (true) {
        	try {
        		collection.add(producer.get());
        	} catch (NoSuchElementException e) {
        		break;
        	}
        }
    }

    /**
     * Constructs a list of the elements, in order, provided by the given iterable instance.
     *
     * @param iterable - An iterable over elements.
     * @return A list of the iterated elements.
     */
    public static <T> List<T> asList(Iterable<? extends T> iterable) {
        List<T> list;
        if (iterable instanceof List) {
            @SuppressWarnings("unchecked")
            List<T> _list = (List<T>) iterable;
            list = _list;
        } else if (iterable instanceof Collection) {
            Collection<? extends T> collection = (Collection<? extends T>) iterable;
            list = new ArrayList<>(collection.size());
            list.addAll(collection);
        } else {
            list = new ArrayList<>();
            addAll(list, iterable);
        }
        return list;
    }

    /**
     * Constructs a list of the elements, in order, provided by the given iterator instance.
     *
     * @param iterator - An iterator over elements.
     * @return A list of the iterated elements.
     */
    public static <T> List<T> asList(Iterator<? extends T> iterator) {
        List<T> list = new ArrayList<>();
        addAll(list, iterator);
        return list;
    }

    /**
     * Constructs a list of the elements, in order, provided by the given enumerator instance.
     *
     * @param enumerator - An enumerator over elements.
     * @return A list of the iterated elements.
     */
    public static <T> List<T> asList(Enumeration<? extends T> enumerator) {
        List<T> list = new ArrayList<>();
        addAll(list, enumerator);
        return list;
    }

    /**
     * Constructs a list of the elements, in order, provided by the given producer instance.
     *
     * @param producer - A supplier of items that throws NoSuchElementException at the end of the sequence.
     * @return A list of the iterated elements.
     */
    public static <T> List<T> asList(Supplier<? extends T> producer) {
        List<T> list = new ArrayList<>();
        addAll(list, producer);
        return list;
    }

    /**
     * Sorts the elements provided by the given iterable into a list, using the given comparator.
     *
     * @param iterable - An iterable over elements.
     * @param comparator - A comparator of element pairs.
     * @return A sorted list of the iterated elements.
     */
    public static <T> List<T> sort(Iterable<? extends T> iterable, Comparator<T> comparator) {
        List<T> list = asList(iterable);
        java.util.Collections.sort(list, comparator);
        return list;
    }

    /**
     * Sorts the elements provided by the given iterable into a list.
     *
     * @param iterable - An iterable over elements.
     * @return A sorted list of the iterated elements.
     */
    public static <T extends Comparable<? super T>> List<T> sort(Iterable<? extends T> iterable) {
        List<T> list = asList(iterable);
        java.util.Collections.sort(list);
        return list;
    }

}
