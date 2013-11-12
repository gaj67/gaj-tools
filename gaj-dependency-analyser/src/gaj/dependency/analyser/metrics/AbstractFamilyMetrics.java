/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.metrics;

/*package-private*/ abstract class AbstractFamilyMetrics<T> implements FamilyMetrics<T> {

    final double abstraction, instability, distance, score;
    final int numDependencies, numBrokenDependencies;
    final boolean breaksDIP, breaksSAP, breaksSDP, breaksADP;

    /*package-private*/ AbstractFamilyMetrics() {
        boolean breaksDIP = false, breaksSAP = false, breaksSDP = false, breaksADP = false;
        int numDependencies = 0, numBrokenDependencies = 0;
        final NodeMetrics<T> metrics = getNodeMetrics();
        double distance = metrics.getDistance();
        double abstraction = metrics.getAbstraction();
        double instability = metrics.getInstability();
        for (DependencyMetrics<T> dmetrics : getDependencyMetrics()) {
            numDependencies++;
            if (dmetrics.hasSmell()) {
                numBrokenDependencies++;
                if (dmetrics.breaksDependencyInversionPrinciple()) {
                    breaksDIP = true;
                }
                if (dmetrics.breaksStableAbstractionsPrinciple()) {
                    breaksSAP = true;
                }
                if (dmetrics.breaksStableDependencyPrinciple()) {
                    breaksSDP = true;
                }
                if (dmetrics.breaksAbstractDependencyPrinciple()) {
                    breaksADP = true;
                }
            }
            NodeMetrics<T> ometrics = (dmetrics.getAfferentMetrics() != metrics) ? dmetrics.getAfferentMetrics() : dmetrics.getEfferentMetrics();
            distance += ometrics.getDistance();
            abstraction += ometrics.getAbstraction();
            instability += ometrics.getInstability();
        }
        this.numDependencies = numDependencies;
        this.numBrokenDependencies = numBrokenDependencies;
        final int Np1 = 1 + numDependencies;
        this.distance = distance / Np1;
        this.abstraction = abstraction / Np1;
        this.instability = instability / Np1;
        this.breaksDIP = breaksDIP;
        this.breaksSAP = breaksSAP;
        this.breaksSDP = breaksSDP;
        this.breaksADP = breaksADP;
        // Penalise "zone of pain" but not "zone of uselessness".
        score = 0.5 * (Math.max(metrics.getDistance(), 0) + getFragility());
    }


    @Override
    public int numDependencies() {
        return numDependencies;
    }

    @Override
    public double getFragility() {
        return (numBrokenDependencies == 0) ? 0 : 1.0 * numBrokenDependencies / numDependencies;
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
    public boolean breaksAbstractDependencyPrinciple() {
        return breaksADP;
    }

    @Override
    public boolean breaksStableDependencyPrinciple() {
        return breaksSDP;
    }

    @Override
    public boolean breaksStableAbstractionsPrinciple() {
        return breaksSAP;
    }

    @Override
    public boolean breaksDependencyInversionPrinciple() {
        return breaksDIP;
    }

    @Override
    public double getAverageDistance() {
        return distance;
    }

    @Override
    public double getAverageAbstraction() {
        return abstraction;
    }

    @Override
    public double getAverageInstability() {
        return instability;
    }

}
