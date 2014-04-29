/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.project;

import gaj.dependency.analyser.dependencies.Cycle;
import gaj.dependency.analyser.dependencies.Dependencies;
import gaj.dependency.analyser.metrics.FamilyMetrics;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.packages.ClassPackage;
import java.util.Collection;

/**
 * Encapsulates the dependencies between classes and packages in the project. 
 */
public interface ProjectDependencies {

    /**
     * Determines the relevant packages in the project suitable for analysis.
     * 
     * @return An iterable of class package instances.
     */
    Iterable<ClassPackage> getPackages();
    
    /**
     * Determines the efferent dependencies between packages in the project.
     * The efferent dependencies specify the packages upon which a given package depends.
     * 
     * @return The package-level efferent dependencies.
     */
    Dependencies<ClassPackage> getInterPackageEfferentDependencies();

    /**
     * Determines the afferent dependencies between packages in the project.
     * The afferent dependencies specify the packages that depend upon a given package.
     * 
     * @return The package-level afferent dependencies.
     */
    Dependencies<ClassPackage> getInterPackageAfferentDependencies();

    /**
     * Determines if the project contains any inter-package cycles.
     * <p/>A pacage cycle is described by a collection of package instances, such that the
     * first package depends upon the second, the second upon the third, and so on, with
     * the last package completing the cycle by depending upon the first package.
     * 
     * @return A non-null (but possibly empty) collection of inter-package cycles.
     */
    Collection<Cycle<ClassPackage>> getInterPackageCycles();

    /**
     * Determines the inter-package dependency metrics for each package in the source component of the project.
     * 
     * @return A collection of the summary metrics of each package.
     */
    public Collection<FamilyMetrics<ClassPackage>> getInterPackageMetrics();

    /**
     * Determines if the given package contains any intra-package class cycles.
     * <p/>A class cycle is described by a collection of class descriptor instances, such that the
     * first class depends upon the second, the second upon the third, and so on, with
     * the last class completing the cycle by depending upon the first class.
     * 
     * @param apackage - The given package of classes.
     * @return A non-null (but possibly empty) collection of intra-package class cycles.
     */
    Collection<Cycle<ClassDescription>> getIntraPackageCycles(ClassPackage apackage);

    /**
     * Determines the intra-package dependency metrics for the given package in the source component of the project.
     * 
     * @param apackage - The given package of classes.
     * @return A collection of the summary metrics of each class in the package.
     */
    public Collection<FamilyMetrics<ClassDescription>> getIntraPackageMetrics(ClassPackage apackage);

    /**
     * Determines the efferent dependencies between classes in the given package.
     * The efferent dependencies specify the classes upon which a given class depends.
     * 
     * @param apackage - The given package of classes.
     * @return The class-level efferent dependencies.
     */
    Dependencies<ClassDescription> getIntraPackageEfferentDependencies(ClassPackage apackage);

    /**
     * Determines the afferent dependencies between classes in the given package.
     * The afferent dependencies specify the classes that depend upon a given class.
     * 
     * @param apackage - The given package of classes.
     * @return The class-level afferent dependencies.
     */
    Dependencies<ClassDescription> getIntraPackageAfferentDependencies(ClassPackage apackage);

}
