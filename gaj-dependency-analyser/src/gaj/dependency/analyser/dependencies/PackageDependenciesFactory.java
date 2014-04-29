/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.dependencies;

import gaj.classbinary.descriptors.ClassNameSpace;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.classes.ClassDescriptionFactory;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.iterators.utilities.Iterables;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public abstract class PackageDependenciesFactory {

    private PackageDependenciesFactory() {}

    /**
     * Given a chain of packages, forms the class dependencies between consecutive packages by
     * computing which classes in the i-th package depend upon which classes in the (i+1)-th package.
     * 
     * @param packages - An iterable over a chain of packages.
     * @return The class dependencies between all consecutive pairs of packages.
     */
    public static ModifiableDependencies<ClassDescription> getInterPackageClassDependencies(final Iterable<ClassPackage> packages) {
        ModifiableDependencies<ClassDescription> dependencies = DependenciesFactory.newDependencies();
        ClassPackage afferentPackage = null;
        for (ClassPackage efferentPackage : packages) {
            if (afferentPackage != null) {
                if (efferentPackage.isExternal()) {
                    // Fake package - check imported classes against package name.
                    final String packageName = efferentPackage.getPackageName();
                    for (ClassDescription afferent : afferentPackage.getClasses()) {
                        for (String className : afferent.getImportedClassNames()) {
                            if (ClassNameSpace.getPackageName(className).equals(packageName)) {
                                ClassDescription efferent = ClassDescriptionFactory.newExternalClass(
                                        efferentPackage.getComponentName(), className);
                                dependencies.addDependency(afferent, efferent);
                            }
                        }
                    }
                } else {
                    // Real package - check imported classes against contained classes.
                    for (ClassDescription afferent : afferentPackage.getClasses()) {
                        for (String className : afferent.getImportedClassNames()) {
                            ClassDescription efferent = efferentPackage.getClass(className);
                            if (efferent != null) {
                                dependencies.addDependency(afferent, efferent);
                            }
                        }
                    }
                }
            }
            afferentPackage = efferentPackage;
        }
        return dependencies;
    }

    public static ModifiableDependencies<ClassDescription> getInterPackageClassDependencies(ClassPackage... packages) {
        return getInterPackageClassDependencies(Arrays.asList(packages));
    }

    public static ModifiableDependencies<ClassDescription> getIntraPackageClassDependencies(ClassPackage apackage) {
        ModifiableDependencies<ClassDescription> dependencies = getInterPackageClassDependencies(apackage, apackage);
        /*
         * XXX: An inner class has a cyclic dependency with its outer class(es).
         * Break these cycles by removing the inner-class -> outer-class dependencies.
         */
        for (ClassDescription afferent : dependencies.getNodes()) {
            if (afferent.isInner()) {
                final String afferentName = afferent.getClassName();
                for (ClassDescription efferent : dependencies.getDependencies(afferent)) {
                    final String efferentName = efferent.getClassName();
                    if (afferentName.startsWith(efferentName) && afferentName.charAt(efferentName.length()) == '$') {
                        dependencies.removeDependency(afferent, efferent);
                    }
                }
            }
        }
        return dependencies;
    }

    /**
     * Given a list of packages that form a dependency cycle
     * (i.e. the first package depends upon the second, the second upon the third, and so on,
     * and finally the last package depends upon the first),
     * computes the (possibly empty) collection of class-dependency cycles.
     * 
     * @param packageCycle - The list of packages forming a dependency cycle.
     * @return
     */
    public static Collection<Cycle<ClassDescription>> getClassCycles(Cycle<ClassPackage> packageCycle) {
        Collection<ClassPackage> fullCycle = new LinkedList<>();
        Iterables.addAll(fullCycle, packageCycle);
        fullCycle.add(packageCycle.get(0)); // Add first package in cycle to make loop explicit.
        Dependencies<ClassDescription> dependencies = getInterPackageClassDependencies(fullCycle);
        return DependenciesFactory.getCycles(dependencies);
    }

}
