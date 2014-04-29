/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.metrics;

/**
 * Encapsulates the dependency metrics for a family of afferents and efferents for a given node.
 */
public interface FamilyMetrics<T> {

    /**
     * Dependency Inversion Principle: Depend upon abstractions.
     * 
     * @return A value of true (or false) if the family of dependencies does (or does not) break the DIP.
     */
    public boolean breaksDependencyInversionPrinciple();

    /**
     * Stable Dependency Principle: Do not depend upon less stable classes.
     * 
     * @return A value of true (or false) if the family of dependencies does (or does not) break the SDP.
     */
    public boolean breaksStableDependencyPrinciple();

    /**
     * Abstract Dependency Principle: Do not depend upon less abstract classes.
     * 
     * @return A value of true (or false) if the family of dependencies does (or does not) break the ADP.
     */
    public boolean breaksAbstractDependencyPrinciple();

    /**
     * Stable Abstractions Principle: Stable classes should be abstract.
     * 
     * @return A value of true (or false) if the family of dependencies does (or does not) break the SAP.
     */
    public boolean breaksStableAbstractionsPrinciple();

    /**
     * 
     * @return The metrics of the class or collection of classes being analysed.
     */
    public NodeMetrics<T> getNodeMetrics();

    /**
     * 
     * @return The total number of afferent and efferent dependencies in the family.
     */
    public int numDependencies();

    /**
     * 
     * @return The proportion of afferent and efferent dependencies in the family
     * that break one or more of the design principles.
     */
    public double getFragility();

    /**
     * 
     * @return An iterable over the family of afferent and efferent dependency metrics.
     */
    public Iterable<DependencyMetrics<T>> getDependencyMetrics();

    /**
     * 
     * @return The unweighted average distance of the node and its family.
     */
    public double getAverageDistance();

    /**
     * 
     * @return The unweighted average abstraction of the node and its family.
     */
    public double getAverageAbstraction();

    /**
     * 
     * @return The unweighted average instability of the node and its family.
     */
    public double getAverageInstability();

    /**
     * 
     * @return The overall score of the class or collection of classes being analysed,
     * in the context of its family of afferent and efferent dependencies.
     */
    public double getScore();

}
