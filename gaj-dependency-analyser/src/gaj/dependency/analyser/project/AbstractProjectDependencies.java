/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.project;

import gaj.dependency.analyser.analysis.Analyser;
import gaj.dependency.analyser.analysis.AnalysisFactory;
import gaj.dependency.analyser.dependencies.Cycle;
import gaj.dependency.analyser.dependencies.Dependencies;
import gaj.dependency.analyser.dependencies.DependenciesFactory;
import gaj.dependency.analyser.dependencies.PackageDependenciesFactory;
import gaj.dependency.analyser.metrics.FamilyMetrics;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.projects.GroupProject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Encapsulates a given project, and uses it to analyse the 
 * dependencies between classes and packages in the source component.
 */
/*package-private*/ abstract class AbstractProjectDependencies implements ProjectDependencies {

    private Dependencies<ClassPackage> packageAfferents = null;
    private Dependencies<ClassPackage> packageEfferents = null;
    private Dependencies<ClassDescription> classEfferents = null;
    private ClassPackage classPackage = null;
    protected final GroupProject project;

    /*package-private*/ AbstractProjectDependencies(GroupProject project) {
        this.project = project;
    }
    
    /**
     * Obtains the efferent dependencies from each package in the project to the other packages.
     * This potentially expensive operation will only be performed at most once.
     * 
     * @return The inter-package efferent dependencies.
     */
    public abstract Dependencies<ClassPackage> _getInterPackageEfferentDependencies();

    @Override
    public Dependencies<ClassPackage> getInterPackageEfferentDependencies() {
        if (packageEfferents == null) {
            packageEfferents = _getInterPackageEfferentDependencies();
        }
        return packageEfferents;
    }


    @Override
    public Dependencies<ClassPackage> getInterPackageAfferentDependencies() {
        if (packageAfferents == null) {
            packageAfferents = DependenciesFactory.invertDependencies(getInterPackageEfferentDependencies());
        }
        return packageAfferents;
    }

    @Override
    public Collection<Cycle<ClassPackage>> getInterPackageCycles() {
        return DependenciesFactory.getCycles(getInterPackageEfferentDependencies());
    }

    @Override
    public List<FamilyMetrics<ClassPackage>> getInterPackageMetrics() {
        Analyser<ClassPackage> analyser = AnalysisFactory.newPackageAnalyser(
        		getInterPackageAfferentDependencies(),
        		getInterPackageEfferentDependencies());
        List<FamilyMetrics<ClassPackage>> metrics = new ArrayList<>();
        for (ClassPackage apackage : getPackages()) {
            metrics.add(analyser.getFamilyMetrics(apackage));
        }
        return metrics;
    }

    @Override
    public Dependencies<ClassDescription> getIntraPackageEfferentDependencies(ClassPackage apackage) {
        if (classEfferents == null || apackage != classPackage) {
            classEfferents = PackageDependenciesFactory.getIntraPackageClassDependencies(apackage);
            classPackage = apackage;
        }
        return classEfferents;
    }

    @Override
    public Dependencies<ClassDescription> getIntraPackageAfferentDependencies(ClassPackage apackage) {
        return DependenciesFactory.invertDependencies(getIntraPackageEfferentDependencies(apackage));
    }

    @Override
    public Collection<Cycle<ClassDescription>> getIntraPackageCycles(ClassPackage apackage) {
        return DependenciesFactory.getCycles(getIntraPackageEfferentDependencies(apackage));
    }

    @Override
    public List<FamilyMetrics<ClassDescription>> getIntraPackageMetrics(ClassPackage apackage) {
        Dependencies<ClassDescription> efferents = getIntraPackageEfferentDependencies(apackage);
        Dependencies<ClassDescription> afferents = DependenciesFactory.invertDependencies(efferents);
        Analyser<ClassDescription> analyser = AnalysisFactory.newClassAnalyser(afferents, efferents);
        List<FamilyMetrics<ClassDescription>> metrics = new ArrayList<>(efferents.numNodes());
        for (ClassDescription desc : efferents.getNodes()) {
            metrics.add(analyser.getFamilyMetrics(desc));
        }
        return metrics;
    }

}
