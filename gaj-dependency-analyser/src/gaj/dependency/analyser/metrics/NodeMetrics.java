/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.metrics;

/**
 * Reflects various dependency metrics for a node, based upon qualities of the node and its afferent
 * and efferent dependencies.
 * 
 * <p/>Note that a node could represent a collection of objects. In that case, its afferent and efferent nodes
 * are also collections of objects. However, the coupling and coverage metrics could measure either the number
 * of node -> node dependencies, or the number of object -> object dependencies, depending upon context.
 */
public interface NodeMetrics<T> {

    /**
     * 
     * @return The node from which the metrics were computed.
     */
    public T getReference();

    /**
     * Computes a measure of abstraction.
     * <br/>For a collection of objects, this is typically the proportion of objects in the collection that are 'abstract'.
     * 
     * @return The degree of abstraction, between 0 and 1 (inclusive).
     */
    public double getAbstraction();

    /**
     * Computes a measure of energy, which compares the overall degree of abstraction of the node 
     * to its visible degree of abstraction.
     * 
     * @return The degree of abstraction, between 0 and 1 (inclusive).
     */
    public double getEnergy();

    /**
     * Computes the Martin measure of instability.
     * <br/>This is the number of efferent couplings divided by the sum of the numbers of efferent and afferent couplings.
     * <br/>For a collection of objects, a coupling is typically an object -> object dependency.
     * 
     * @return The instability, between 0 and 1 (inclusive).
     */
    public double getInstability();

    /**
     * Computes a directed version of the normalised Martin distance.
     * <br/>This is 1 - (abstraction + instability). A positive value is deemed to be in the "zone of pain",
     * and a negative value is deemed to be in the "zone of uselessness".
     * 
     * @return The directed distance from the 'main sequence', between -1 and 1 (inclusive).
     */
    public double getDistance();

    /**
     * Computes the 'coverage' of the node by its afferent nodes.
     * <br/>For a collection of objects, this is typically the proportion of objects in the collection that are
     * depended upon by objects in the afferent collections.
     * 
     * @return The coverage of the node by its afferents.
     */
    public double getAfferentCoverage();

    /**
     * Computes the 'coverage' of the node by its efferent nodes.
     * <br/>For a collection of objects, this is typically the proportion of objects in the collection that
     * depend upon objects in the efferent collections.
     * 
     * @return The coverage of the node by its efferents.
     */
    public double getEfferentCoverage();

    /**
     * Computes the 'coverage' of the node by itself.
     * <br/>For a collection of objects, this is typically the proportion of objects in the collection that
     * depend upon, or are depended upon by, other objects in the collection.
     * 
     * @return The coverage of the node by itself.
     */
    public double getSelfCoverage();

    /**
     * Computes the coupling between the node and its afferent nodes.
     * <br/>For a collection of objects, this is typically the number of objects in the afferent collections that
     * depend upon objects in the node's collection.
     *
     * @return The number of couplings between the node and its afferents.
     */
    public int numAfferentCouplings();

    /**
     * Computes the coupling between the node and its efferent nodes.
     * <br/>For a collection of objects, this is typically the number of objects in the efferent collections that are
     * depended upon by objects in the node's collection.
     * 
     * @return The number of couplings between the node and its efferents.
     */
    public int numEfferentCouplings();

    /**
     * Determines if the node is 'instantiable' by another node.
     * <br/>For a collection of objects, this is true if any object in the collection is instantiable by another object
     * in another collection.
     * 
     * @return A value of true (or false) if the node is (or is not) instantiable.
     */
    public boolean isInstantiable();

}
