/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Encapsulates various collection-type operations on iterables and iterators
 */
public abstract class Iterables {

    private Iterables() {}

    /**
     * Adds all of the iterator elements to the collection.
     * 
     * @param collection - A collection of elements.
     * @param iterator - An iterator over another collection of elements.
     */
    public static <T> void addAll(Collection<T> collection, Iterator<? extends T> iterator) {
        while (iterator.hasNext()) {
            collection.add(iterator.next());
        }
    }

    /**
     * Adds all of the iterable elements to the collection.
     * 
     * @param collection - A collection of elements.
     * @param iterable - An instance that is iterable over another collection of elements.
     */
    public static <T> void addAll(Collection<T> collection, Iterable<? extends T> iterable) {
        for (T elem : iterable) {
            collection.add(elem);
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
			List<T> _list = (List<T>)iterable;
			list = _list;
    	} else if (iterable instanceof Collection) {
    		Collection<? extends T> collection = (Collection<? extends T>) iterable;
    		list = new ArrayList<T>(collection.size()); 
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
    	List<T> list = new ArrayList<T>();
    	addAll(list, iterator);
    	return list;
    }

    /**
     * Sorts the elements provided by the given iterable into a list, using the given comparator.
     * 
     * @param iterable - An iterable over elements.
     * @param comparator - A comparator of element pairs.
     * @return A sorted list of the iterated elements.
     */
    public <T> List<T> sort(Iterable<? extends T> iterable, Comparator<T> comparator) {
    	List<T> list = asList(iterable);
    	Collections.sort(list, comparator);
    	return list;
    }

    /**
     * Sorts the elements provided by the given iterable into a list.
     * 
     * @param iterable - An iterable over elements.
     * @return A sorted list of the iterated elements.
     */
    public <T extends Comparable<? super T>> List<T> sort(Iterable<? extends T> iterable) {
    	List<T> list = asList(iterable);
    	Collections.sort(list);
    	return list;
    }

}
