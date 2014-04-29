/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.analysis;

public interface PackageSummary {

    public int numVisibleClasses();

    public double getOverallAbstraction();

    public double getVisibleAbstraction();

    public double getEnergy();

}
