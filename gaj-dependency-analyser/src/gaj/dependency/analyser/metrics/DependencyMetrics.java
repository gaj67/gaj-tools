/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.metrics;


/**
 * Compares the metrics of the afferent and efferent nodes in a dependency.
 */
public interface DependencyMetrics<T> {

    /**
     * 
     * @return The metrics of the afferent node in the dependency.
     */
    public NodeMetrics<T> getAfferentMetrics();

    /**
     * 
     * @return The metrics of the efferent node in the dependency.
     */
    public NodeMetrics<T> getEfferentMetrics();

    /**
     * Dependency Inversion Principle: Depend upon abstractions.
     */
    public boolean breaksDependencyInversionPrinciple();

    /**
     * Stable Dependency Principle: Do not depend upon less stable classes.
     */
    public boolean breaksStableDependencyPrinciple();

    /**
     * Abstract Dependency Principle: Do not depend upon less abstract classes.
     */
    public boolean breaksAbstractDependencyPrinciple();

    /**
     * Stable Abstractions Principle: Stable classes should be abstract.
     */
    public boolean breaksStableAbstractionsPrinciple();

    public boolean hasSmell();

}