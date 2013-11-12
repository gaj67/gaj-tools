/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.dependencies;


/**
 * Encapsulates the notion of a cycle of dependencies between nodes.
 * <p/>In particular, each cycle has a standard, internally ordered form.
 * Thus, the cycles X -> Y -> Z -> X, Y -> Z -> X -> Y and Z -> X -> Y -> Z are all
 * considered to be equal. 
 */
public interface Cycle<T> extends Iterable<T> {

    /**
     * Specifies the length of the cycle. <br/>For example, the cycle X -> Y -> X has a length of 2.
     * 
     * @return The length of the cycle.
     */
    public int length();

    /**
     * Obtains the first node in the cycle, according to some internal ordering.
     * <br/>For example, the node X would be returned for the cycle X -> Y -> X.
     * 
     * @return The 'first' node in the cycle.
     */
    public T getFirst();

    /**
     * Obtains the last node in the cycle, according to some internal ordering.
     * <br/>For example, the node Y would be returned for the cycle X -> Y -> X.
     * 
     * @return The 'last' node in the cycle.
     */
    public T getLast();

    /**
     * Obtains the node in the given (internally-ordered) position in the cycle, starting from 0, with wrap-around.
     * 
     * @param index The position of the node.
     * @return The node at that position of the cycle.
     */
    public T get(int index);
    
}
