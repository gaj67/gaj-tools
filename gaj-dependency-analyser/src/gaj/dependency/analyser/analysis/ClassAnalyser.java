/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.analysis;

import gaj.dependency.analyser.metrics.MetricsFactory;
import gaj.dependency.analyser.metrics.NodeMetrics;
import gaj.dependency.manager.classes.ClassDescription;
import java.util.Collection;

/**
 * Encapsulates the analysis of the dependencies between classes.
 * <p/>The scope of the dependencies, e.g. between classes in the same package,
 * or between classes in different packages, etc., is determined by the caller.
 * <p/>Note: Concrete classes that are only visible within a package are treated as being effectively 'abstract',
 * otherwise use of the Abstract Factory pattern will still be deemed to break the Dependency Inversion Principle!
 */
/*package-private*/ abstract class ClassAnalyser extends AbstractAnalyser<ClassDescription> {

    @Override
    public abstract Collection<ClassDescription> getAfferents(ClassDescription desc);

    @Override
    public abstract Collection<ClassDescription> getEfferents(ClassDescription desc);

    @Override
    public NodeMetrics<ClassDescription> getMetrics(ClassDescription desc) {
        ClassSummary summary = SummaryFactory.summariseClass(desc);
        return MetricsFactory.newNodeMetrics(
                desc, summary.isDirectlyInstantiable(), 
                summary.getEnergy(), summary.getVisibleAbstraction(),
                getAfferents(desc).size(), getEfferents(desc).size());
    }

}