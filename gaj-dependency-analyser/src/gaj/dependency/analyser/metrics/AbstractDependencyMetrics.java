/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.metrics;


/*package-private*/ abstract class AbstractDependencyMetrics<T> implements DependencyMetrics<T> {

    /*package-private*/ static boolean useDIP = true, useSDP = true, useADP = true, useSAP = true;
    private final boolean breaksDIP, breaksSAP, breaksSDP, breaksADP;

    /*package-private*/ AbstractDependencyMetrics() {
        final NodeMetrics<T> efferent = getEfferentMetrics();
        final NodeMetrics<T> afferent = getAfferentMetrics();
        breaksDIP = useDIP && efferent.isInstantiable();
        // XXX: Don't penalise being in or entering the "zone of uselessness".
        // Do penalise entering or going further into the "zone of pain".
        breaksSAP = useSAP && ((afferent.getDistance() > 0) 
                ? (efferent.getDistance() > afferent.getDistance()) : (efferent.getDistance() > 0));
        breaksSDP = useSDP && efferent.getInstability() > afferent.getInstability();
        breaksADP = useADP && efferent.getAbstraction() < afferent.getAbstraction();
    }

    /**
     * Dependency Inversion Principle: Depend upon abstractions.
     */
    @Override
    public boolean breaksDependencyInversionPrinciple() {
        return breaksDIP;
    }

    /**
     * Abstract Dependency Principle: Do not depend upon less abstract classes.
     */
    @Override
    public boolean breaksAbstractDependencyPrinciple() {
        return breaksADP;
    }

    /**
     * Stable Dependency Principle: Do not depend upon less stable classes.
     */
    @Override
    public boolean breaksStableDependencyPrinciple() {
        return breaksSDP;
    }

    /**
     * Stable Abstractions Principle: Stable classes should be abstract.
     */
    @Override
    public boolean breaksStableAbstractionsPrinciple() {
        return breaksSAP;
    }

    @Override
    public boolean hasSmell() {
        return breaksDIP || breaksSAP || breaksSDP || breaksADP;
    }

}