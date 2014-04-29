/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.dependencies;

import java.util.Collection;

/**
 * Encapsulates the dependencies between nodes in a generic graph.
 * <p/>However, dependencies are not quite the same as arcs.
 * Although dependencies are directed from afferents to efferents,
 * a dependency here is to be taken between a primary node P and a secondary node S. Thus,
 * if P is an afferent and S is an efferent, then P -> S. Conversely, if P is an efferent and
 * S is an afferent, then P <- S.
 * Thus, the interpretation of the direction of a dependency will depend upon the context.
 * However, the primary and secondary nodes in a dependency will always have a fixed, explicit ordering.
 * 
 * <p/>As a further consequence of holding dependencies rather than arcs, there is no explicit notion
 * of an in-set or an out-set for a node. Instead, a primary node has a collection of dependencies with secondary nodes.
 * Thus, if there is a dependency between primary node P and secondary node S, then S will appear amongst the
 * dependencies of P, but P will <b>not</b> appear amongst the dependencies of S
 * (unless such a reverse dependency has been explicitly added).
 */
public interface Dependencies<T> {

    /**
     * Determines if the graph contains any nodes.
     * 
     * @return A value of false (or true) if there are (or are not) any nodes.
     */
    boolean isEmpty();

    /**
     * Determines if a node exists in the graph.
     * 
     * @param node - The node.
     * @return A value of true (or false) if the node does (or does not) exist.
     */
    boolean hasNode(T node);

    /**
     * 
     * @return The number of nodes in the graph.
     */
    int numNodes();

    /**
     * Obtains the collection of nodes in the graph.
     * 
     * @return A collection of nodes.
     */
    Collection<T> getNodes();

    /**
     * Determines if a primary node has any secondary dependencies.
     * 
     * @param node - The primary node.
     * @return A value of true (or false) if the primary node does (or does not) have
     * any secondary dependencies.
     */
    boolean hasDependencies(T node);

    /**
     * Obtains the (possibly empty) collection of secondary nodes for which there is a
     * dependency with the primary node.
     * 
     * @param node - The primary node.
     * @return A collection of secondary nodes, or a value of null if the primary node
     * is not in the graph.
     */
    Collection<T> getDependencies(T node);

    /**
     * Obtains an arbitrary secondary node for which there is a dependency with the
     * primary node.
     * 
     * @param node - The primary node.
     * @return An arbitrary secondary node, or a value of null if the primary node
     * has no dependencies.
     */
    T getDependency(T node);

}
