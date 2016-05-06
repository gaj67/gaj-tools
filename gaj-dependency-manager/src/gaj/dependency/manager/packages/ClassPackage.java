/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.packages;

import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.dependencies.PackageDependency;
import gaj.iterators.core.Iterative;

import java.util.Collection;

/**
 * Encapsulates a common set of classes all having the same package name.
 * <p/>Note that different component instances might have distinct package instances with the same name.
 * @see {@link ClassDescription}
 */
public interface ClassPackage {

    /**
     * 
     * @return The name of the package.
     */
    public String getPackageName();

    /**
     * 
     * @return The name of the component containing the package instance.
     */
    public String getComponentName();

    /**
     * Determines whether the instance represents an 'external' package that is outside of
     * the scope of the component or group of components.
     * @return A value of true (or false) if the package is (or is not) considered to be external.
     */
    public boolean isExternal();

    /**
     * @return The number of classes loaded into the package.
     */
    public int numClasses();

    /**
     * @return A non-null iterable over the set of all classes loaded into the package.
     */
    public Iterative<ClassDescription> getClasses();

    /**
     * Tests whether the class descriptor instance has been loaded into this package instance.
     * <br/>This is expected to be a shallow test.
     * 
     * @param desc - The descriptor of the class.
     * @return A value of true (or false) if the class is (or is not) located in the package.
     */
    public boolean hasClass(ClassDescription desc);

    /**
     * Tests whether the class has been loaded into this package.
     * <br/>This is expected to be a deep test.
     * <br/>Note that this is not the same as checking whether the class has the same package name,
     * since the class could have been loaded into a different package instance in a different component.
     * 
     * @param className - The fully qualified name of the class.
     * @return A value of true (or false) if the class is (or is not) located in the package.
     */
    public boolean hasClass(String className);

    /**
     * Looks up the named class in the package.
     * 
     * @param className - The fully qualified name of the class.
     * @return An object representing the named class, or a value of null if the class is not found.
     */
    public ClassDescription getClass(String className);

    /**
     * Determines the names of external packages that are depended upon by
     * classes within this package. Excludes the name of this package.
     * 
     * @return A collection of the fully qualified names of packages upon which this package is dependent.
     */
    public Collection<String> getImportedPackageNames();

    /**
     * Indicates whether any class in the package depends upon another class with
     * the same (fully qualified) package name.
     * 
     * <p/>This is expected to be a shallow test, since it is not feasible to (efficiently)
     * determine at the package level whether
     * a class depends upon another class in the same package in the same component,
     * or upon a class in an eponymous package in another component.
     * 
     * @return A value of true (or false) if the package does (or does not) reference itself.
     */
    public boolean hasSelfReference();

    /**
     * Indicates whether classes in the package instance depend upon each other.
     * <p/>This is expected to be a deep test.
     * @return A value of true (or false) if the package instance does (or does not) reference itself.
     */
    public boolean hasInternalSelfReference();

    /**
     * Indicates whether classes in the package instance depend upon any classes external to the instance
     * that have the same package name (e.g. are in a different but eponymous package instance in
     * a different component).
     * <p/>This is expected to be a deep test.
     * @return A value of true (or false) if the package instance does (or does not) reference a different
     * package instance of the same name.
     */
    public boolean hasExternalSelfReference();

    /**
     * Determines the class efferents, which are the classes upon which the given class depends.
     * 
     * @param afferent - The descriptor of the class for which efferents are to be found.
     * @param filter - The (non-null) type of dependency required as output.
     * Valid values are: {@link Universal}; or its sub-types {@link ExtraPackage} or
     * {@link IntraPackage}.
     * @return The set of all efferent classes having the desired dependency type,
     * or a value of null if the given class does not belong to the package.
     * @throws IllegalArgumentException If the specified dependency type is invalid.
     */
    public Collection<ClassDescription> getEfferents(ClassDescription afferent, PackageDependency filter);

    /**
     * Determines the class afferents, which are the classes that depend upon the given class.
     * Note: Only those afferents within the current package can be located.
     * 
     * @param efferent - The descriptor of the class for which afferents are to be found.
     * @param filter - The (non-null) type of dependency required as output.
     * Valid values are: {@link Universal}; or its sub-types {@link ExtraPackage} or
     * {@link IntraPackage}.
     * @return The (non-null) set of all intra-package afferent classes having
     * the desired dependency type.
     * @throws IllegalArgumentException If the specified dependency type is invalid.
     */
    public Collection<ClassDescription> getAfferents(ClassDescription efferent, PackageDependency filter);

}
