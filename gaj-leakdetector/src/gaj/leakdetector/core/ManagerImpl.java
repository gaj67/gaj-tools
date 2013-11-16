/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.core;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/*package-private*/ class ManagerImpl<T> implements Manager<T> {

    private final Map<String,WeakReference<? extends Detector<? extends T>>> map = new ConcurrentHashMap<>();
    private final AtomicLong numManaged = new AtomicLong();
    private final AtomicLong numExtant = new AtomicLong();
    private final AtomicLong numDestroyed = new AtomicLong();

    @Override
    public void add(Detector<? extends T> detector) {
        map.put(detector.getIdentifier(), new WeakReference<>(detector));
        numManaged.incrementAndGet();
    }

    @Override
    public void remove(Detector<? extends T> detector) {
        map.remove(detector.getIdentifier());
    }

    @Override
    public boolean isInstrumented() {
        return numManaged.get() > 0;
    }

    @Override
    public long numManaged() {
        return numManaged.get();
    }
    
    @Override
    public long numDestroyed() {
        return numDestroyed.get();
    }

    @Override
    public long numExtant() {
        return numExtant.get();
    }

    @Override
    public Iterable<Detector<? extends T>> getDetectors() {
        return new Iterable<Detector<? extends T>>() {
            @Override
            public Iterator<Detector<? extends T>> iterator() {
                return new Iterator<Detector<? extends T>>() {
                    Iterator<WeakReference<? extends Detector<? extends T>>> _iter = null;
                    Detector<? extends T> _next = null;
                    
                    @Override
                    public boolean hasNext() {
                        if (_next != null) return true;
                        if (_iter == null) {
                            Collection<WeakReference<? extends Detector<? extends T>>> values = map.values();
                            numExtant.set(values.size());
                            numDestroyed.set(numManaged.get() - numExtant.get());
                            _iter = values.iterator();
                        }
                        while (_iter.hasNext()) {
                            _next = _iter.next().get();
                            if (_next != null && _next.isInstrumented()) return true;
                            _iter.remove();
                            numDestroyed.incrementAndGet();
                            numExtant.decrementAndGet();
                        }
                        _next = null;
                        return false;
                    }

                    @Override
                    public Detector<? extends T> next() {
                        if (hasNext()) {
                            Detector<? extends T> detector = _next;
                            _next = null;
                            return detector;
                        } else {
                            throw new NoSuchElementException("End of iteration");
                        }
                    }

                    @Override
                    public void remove() {
                        throw new IllegalStateException("Operation not permitted");
                    }
                };
            }
        };
    }

    @Override
    public void update() {
        Collection<WeakReference<? extends Detector<? extends T>>> values = map.values();
        numExtant.set(values.size());
        numDestroyed.set(numManaged.get() - numExtant.get());
        for (Iterator<WeakReference<? extends Detector<? extends T>>> iter = values.iterator(); iter.hasNext(); ) {
            Detector<? extends T> ref = iter.next().get();
            if (ref == null || !ref.isInstrumented()) {
                iter.remove();
                numDestroyed.incrementAndGet();
                numExtant.decrementAndGet();
            }
        }
    }

}
