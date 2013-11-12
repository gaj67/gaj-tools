/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.metrics;

/*package-private*/ abstract class AbstractNodeMetrics<T> implements NodeMetrics<T> {

    private final double instability, distance;

    /*package-private*/ AbstractNodeMetrics() {
        instability = (numAfferentCouplings() == 0) ? 1 : 1.0 * numEfferentCouplings() / (numAfferentCouplings() + numEfferentCouplings());
        distance = 1 - getAbstraction() - instability;
    }

    @Override
    public double getInstability() {
        return instability;
    }

    @Override
    public double getDistance() {
        return distance;
    }

}
