/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.analysis;

import gaj.dependency.analyser.dependencies.Dependencies;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.packages.ClassPackage;
import java.util.Collection;

public abstract class AnalysisFactory {

    private AnalysisFactory() {}

    public static Analyser<ClassPackage> newPackageAnalyser(
            final Dependencies<ClassPackage> afferents,
            final Dependencies<ClassPackage> efferents) {
        return new PackageAnalyser() {
            @Override
            public Collection<ClassPackage> getEfferents(ClassPackage apackage) {
                return efferents.getDependencies(apackage);
            }

            @Override
            public Collection<ClassPackage> getAfferents(ClassPackage apackage) {
                return afferents.getDependencies(apackage);
            }
        };
    }

    public static Analyser<ClassDescription> newClassAnalyser(
            final Dependencies<ClassDescription> afferents,
            final Dependencies<ClassDescription> efferents) {
        return new ClassAnalyser() {
            @Override
            public Collection<ClassDescription> getEfferents(ClassDescription desc) {
                return efferents.getDependencies(desc);
            }

            @Override
            public Collection<ClassDescription> getAfferents(ClassDescription desc) {
                return afferents.getDependencies(desc);
            }
        };
    }

}
