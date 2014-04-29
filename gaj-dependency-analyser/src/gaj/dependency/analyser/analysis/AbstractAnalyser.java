/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.analysis;

import gaj.dependency.analyser.metrics.DependencyMetrics;
import gaj.dependency.analyser.metrics.FamilyMetrics;
import gaj.dependency.analyser.metrics.MetricsFactory;
import gaj.dependency.analyser.metrics.NodeMetrics;
import java.util.Collection;
import java.util.LinkedList;

public abstract class AbstractAnalyser<T> implements Analyser<T> {

    public abstract Collection<T> getAfferents(T node);

    public abstract Collection<T> getEfferents(T node);

    @Override
    public abstract NodeMetrics<T> getMetrics(T node);

    @Override
    public FamilyMetrics<T> getFamilyMetrics(T node) {
        final NodeMetrics<T> metrics = getMetrics(node);
        Collection<DependencyMetrics<T>> dependencies = new LinkedList<>();
        for (T afferent : getAfferents(node)) {
            dependencies.add(MetricsFactory.newDependencyMetrics(getMetrics(afferent), metrics));
        }
        for (T efferent : getEfferents(node)) {
            dependencies.add(MetricsFactory.newDependencyMetrics(metrics, getMetrics(efferent)));
        }
        return MetricsFactory.newFamilyMetrics(metrics, dependencies);
    }

}
