/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.dependencies;

/**
 * Encapsulates the dependencies between nodes in a generic graph.
 * <p/>However, dependencies are not quite the same as arcs.
 * Although dependencies are directed from afferents to efferents,
 * a dependency here is to be taken between a primary node P and a secondary node S. Thus,
 * if P is an afferent and S is an efferent, then P -> S. Conversely, if P is an efferent and
 * S is an afferent, then P <- S.
 * Thus, the interpretation of the direction of a dependency will depend upon the context.
 * However, the primary and secondary nodes in a dependency will always have a fixed, explicit ordering.
 * <p/>As a further consequence of holding dependencies rather than arcs, there is no explicit notion
 * of an in-set or an out-set for a node. Instead, a primary node has a collection of dependencies with secondary nodes.
 * Thus, if there is a dependency between primary node P and secondary node S, then S will appear amongst the
 * dependencies of P, but P will <b>not</b> appear amongst the dependencies of S
 * (unless such a reverse dependency has been explicitly added).
 */
public interface ModifiableDependencies<T> extends Dependencies<T> {

    /**
     * Adds a node to the graph.
     * <p/>This has no effect if a dependency between the node and another node
     * has already been defined.
     * 
     * @param node - The node.
     */
    void addNode(T node);

    /**
     * Removes the node from the graph, along with any dependencies on other nodes.
     * <p/>The other nodes are not removed.
     * <p/>This has no effect if the node does not exist.
     * 
     * @param node - The node.
     */
    void removeNode(T node);

    /**
     * Adds a context-sensitive dependency between the primary node and the secondary node.
     * If either node does not already exist amongst the dependencies,
     * then it will be added.
     * 
     * 
     * @param primary - The primary node.
     * @param secondary - The secondary node.
     */
    void addDependency(T primary, T secondary);

    /**
     * Removes a context-sensitive dependency between the primary node and the secondary node.
     * <p/>This has no effect if there is no such dependency.
     * <p/><b>Note:</b>If a dependency is added and subsequently removed, then the nodes will still exist,
     * even if they no longer have any secondary dependencies.
     * 
     * @param primary - The primary node.
     * @param secondary - The secondary node.
     */
    void removeDependency(T primary, T secondary);

}
