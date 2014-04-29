/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.analysis;

import static gaj.dependency.analyser.analysis.SummaryFactory.ratio;
import gaj.dependency.analyser.dependencies.Dependencies;
import gaj.dependency.analyser.dependencies.PackageDependenciesFactory;
import gaj.dependency.analyser.metrics.MetricsFactory;
import gaj.dependency.analyser.metrics.NodeMetrics;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.packages.ClassPackage;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates the analysis of the dependencies between packages of classes.
 * <p/><b>Note:</b> The measure of coupling has been chosen to use the finest level of granularity, namely the
 * total number of dependencies between classes in a given package and all classes in all other packages.
 * <br/>This appears to be finer than the granularity specified by Robert C. Martin, which measures the
 * number of classes in other packages that have a dependency with any class in the given package.
 * <br/>In contrast, the JDepend library uses the most coarse measure of coupling, which just counts the number of
 * packages having a dependency with the given package.
 * <p/><b>Note:</b> Various metrics for coverage have been added, which measure the proportion of classes in a package that are
 * used outside of the package. Only classes that are visible outside of their defining package are counted in these coverage metrics.
 */
/*package-private*/ abstract class PackageAnalyser extends AbstractAnalyser<ClassPackage> {

    @Override
    public abstract Collection<ClassPackage> getAfferents(ClassPackage apackage);

    @Override
    public abstract Collection<ClassPackage> getEfferents(ClassPackage apackage);

    @Override
    public NodeMetrics<ClassPackage> getMetrics(ClassPackage apackage) {
        Dependencies<ClassDescription> dependencies = PackageDependenciesFactory.getIntraPackageClassDependencies(apackage);
        int selfCoverage = dependencies.numNodes();
        int afferentCoupling = 0, efferentCoupling = 0;
        Set<ClassDescription> afferentCoverage = new HashSet<>();
        for (ClassPackage packageAfferent : getAfferents(apackage)) {
            dependencies = PackageDependenciesFactory.getInterPackageClassDependencies(packageAfferent, apackage);
            for (ClassDescription classAfferent : dependencies.getNodes()) {
                Collection<ClassDescription> coverage = dependencies.getDependencies(classAfferent);
                if (!coverage.isEmpty()) {
                    afferentCoupling += coverage.size();
                    afferentCoverage.addAll(coverage);
                }
            }
        }
        boolean isInstantiable = false;
        for (ClassDescription desc : afferentCoverage) {
            if (SummaryFactory.isDirectlyInstantiable(desc)) {
                isInstantiable = true;
                break;
            }
        }
        Set<ClassDescription> efferentCoverage = new HashSet<>();
        for (ClassPackage packageEfferent : getEfferents(apackage)) {
            dependencies = PackageDependenciesFactory.getInterPackageClassDependencies(apackage, packageEfferent);
            for (ClassDescription classAfferent : dependencies.getNodes()) {
                Collection<ClassDescription> coverage = dependencies.getDependencies(classAfferent);
                if (!coverage.isEmpty()) {
                    efferentCoupling += coverage.size();
                    efferentCoverage.add(classAfferent);
                }
            }
        }
        PackageSummary summary = SummaryFactory.summarisePackage(apackage);
        double _afferentCoverage = ratio(afferentCoverage.size(), summary.numVisibleClasses(), 0);
        double _efferentCoverage = ratio(efferentCoverage.size(), apackage.numClasses(), 0);
        double _selfCoverage = ratio(selfCoverage, apackage.numClasses(), 0); // A package cohesion measure.
        return MetricsFactory.newNodeCollectionMetrics(
                apackage, isInstantiable, 
                summary.getEnergy(), summary.getVisibleAbstraction(), 
                afferentCoupling, efferentCoupling,
                _afferentCoverage, _efferentCoverage, _selfCoverage);
    }

}