/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.dependencies;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class CycleFactory {

    private CycleFactory() { }

    /**
     * Creates a cycle object from a list of the nodes in the cycle, where it is assumed that the 
     * first node depends upon the second node, etc., and the last node depends upon the first node.
     * <p/>For example, the list [ X, Y ] represents (one ordering of) the cycle X -> Y -> X.
     * 
     * @param nodes - A list of the nodes in the cycle, in an arbitrary but valid dependency order.
     * @return A cycle instance, with the nodes in some standardised dependency order.
     */
    public static <T> Cycle<T> newCycle(List<T> nodes) {
        final int length = nodes.size(); 
        int firstNodePos = -1;
        {
            int minHash = Integer.MAX_VALUE;
            for (int i = 0; i < length; i++) {
                final int hash = nodes.get(i).hashCode();
                if (hash < minHash) {
                    minHash = hash;
                    firstNodePos = i;
                }
            }
        }
        int _cycleHash = length; 
        final LinkedList<T> cycle = new LinkedList<>();
        {
            int pos = 0;
            for (int i = firstNodePos; i < length; i++) {
                T node = nodes.get(i);
                cycle.add(node);
                _cycleHash ^= Integer.rotateRight(node.hashCode(), pos++);
            }
            for (int i = 0; i < firstNodePos; i++) {
                T node = nodes.get(i);
                cycle.add(node);
                _cycleHash ^= Integer.rotateRight(node.hashCode(), pos++);
            }
        }
        final int cycleHash = _cycleHash;
        return new Cycle<T>() {

            @Override
            public int length() {
                return length;
            }

            @Override
            public T getFirst() {
                return cycle.getFirst();
            }

            @Override
            public T getLast() {
                return cycle.getLast();
            }

            @Override
            public T get(int index) {
                int pos = index % length;
                if (pos < 0) pos += length;
                return cycle.get(pos);
            }

            @Override
            public Iterator<T> iterator() {
                return Collections.unmodifiableCollection(cycle).iterator();
            }

            @Override
            public int hashCode() {
                return cycleHash;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof Cycle) {
                    @SuppressWarnings("unchecked")
                    Cycle<T> _cycle = (Cycle<T>)obj;
                    if (_cycle == this) return true;
                    if (_cycle.length() != length || _cycle.hashCode() != cycleHash) return false;
                    for (int i = 0; i < length; i++) {
                        if (_cycle.get(i) != cycle.get(i)) return false;
                    }
                    return true;
                } else {
                    return false;
                }
            }
            
            @Override
            public String toString() {
                return cycle.toString();
            }
        };
    }
}
