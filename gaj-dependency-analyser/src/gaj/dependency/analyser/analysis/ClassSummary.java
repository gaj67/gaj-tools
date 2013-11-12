/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.analysis;

public interface ClassSummary {

    public boolean isDirectlyInstantiable();

    public double getOverallAbstraction();

    public double getVisibleAbstraction();

    public double getEnergy();

}
