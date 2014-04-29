/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.analysis;

import gaj.dependency.analyser.metrics.FamilyMetrics;
import gaj.dependency.analyser.metrics.NodeMetrics;

public interface Analyser<T> {

    public NodeMetrics<T> getMetrics(T node);

    public FamilyMetrics<T> getFamilyMetrics(T node);

}
