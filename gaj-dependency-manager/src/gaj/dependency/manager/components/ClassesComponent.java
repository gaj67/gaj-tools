/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.components;

import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.dependencies.ComponentDependency;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.iterators.core.Iterative;

import java.util.Collection;

/**
 * Encapsulates a common set of classes (or, rather, class descriptions) into a single, named component.
 * <br/>These classes can be accessed directly or via their packages. 
 * @see {@link ClassDescription}
 * @see {@link ClassPackage}
 */
public interface ClassesComponent {

    //*********************************************************************************
    // Component-level methods.
    /**
     * 
     * @return The name of the component.
     */
    public String getComponentName();

    //*********************************************************************************
    // Class-level methods.
    /**
     * @return The number of classes loaded into the component.
     */
    public int numClasses();

    /**
     * @return An iterable over the set of all classes in the component.
     */
    public Iterative<ClassDescription> getClasses();

    /**
     * Tests whether the class is located in the component.
     * <p/>This is expected to be a shallow test, based upon whether or not the component created
     * the class description. However, note that an external class is deemed to not be located
     * in the component that created it.
     * 
     * @param desc - The descriptor of the class.
     * @return A value of true (or false) if the class is (or is not) located in the component.
     */
    public boolean hasClass(ClassDescription desc);

    /**
     * Tests whether the named class is located in the component.
     * <br/>This is expected to be a deep test, since there is no additional information.
     * 
     * @param className - The fully qualified name of the class.
     * @return A value of true (or false) if the class is (or is not) located in the component.
     */
    public boolean hasClass(String className);

    /**
     * Looks up the named class in the component.
     * 
     * @param className - The fully qualified name of the class.
     * @return An object representing the named class, or a value of null if the class is not found.
     */
    public ClassDescription getClass(String className);

    /**
     * Determines the class efferents, which are the classes upon which the named class depends.
     * 
     * @param afferent - The descriptor of the class for which efferents are to be found.
     * @param filter - The (non-null) type of dependency required as output.
     * Valid values are: {@link Universal}; its sub-types {@link ExtraComponent} or
     * {@link IntraComponent}; or the sub-types {@link IntraPackageComponent} or
     * {@link InterPackageComponent} of {@link IntraComponent}.
     * @return The set of all efferent classes having the desired dependency type,
     * or a value of null if the class does not belong to the component.
     * @throws IllegalArgumentException If the specified dependency type is invalid.
     */
    public Collection<ClassDescription> getEfferents(ClassDescription afferent, ComponentDependency filter);

    /**
     * Determines the class afferents, which are the classes that depend upon the given class.
     * Note: Only those afferents within the current component can be located.
     * 
     * @param efferent - The descriptor of the class for which afferents are to be found.
     * @param filter - The (non-null) type of dependency required as output.
     * Valid values are: {@link Universal}; its sub-types {@link ExtraComponent} or
     * {@link IntraComponent}; or the sub-types {@link IntraPackageComponent} or
     * {@link InterPackageComponent} of {@link IntraComponent}.
     * @return The (non-null) set of all intra-component afferent classes having
     * the desired dependency type.
     * @throws IllegalArgumentException If the specified dependency type is invalid.
     */
    public Collection<ClassDescription> getAfferents(ClassDescription efferent, ComponentDependency filter);

    //*********************************************************************************
    /*package-level methods.

    /**
     * @return The number of packages loaded into the component.
     */
    public int numPackages();

    /**
     * @return An iterable over the packages in the component.
     */
    public Iterative<ClassPackage> getPackages();

    /**
     * Tests whether the named package is located in the component.
     * 
     * @param packageName - The fully qualified name of the package.
     * @return A value of true (or false) if the package is (or is not) located in the component.
     */
    public boolean hasPackage(String packageName);

    /**
     * Tests whether the given package instance is located in the component.
     * <p/>This is expected to be a shallow test, based upon whether or not the component created
     * the package instance. However, note that an external package is deemed to not be located
     * in the component that created it.
     * 
     * @param apackage - The package instance.
     * @return A value of true (or false) if the package is (or is not) located in the component.
     */
    public boolean hasPackage(ClassPackage apackage);

    /**
     * Obtains an instance of the named package.
     * 
     * @param packageName - The fully qualified name of the package.
     * @return A container for the named package,
     * or a value of null if the package is not located within the component.
     */
    public /*@Nullable*/ ClassPackage getPackage(String packageName);

    /**
     * Obtains the package instance containing the given class.
     * 
     * @param desc - The descriptor of the class.
     * @return The containing package instance,
     * or a value of null if the class is not located within the component.
     */
    public /*@Nullable*/ ClassPackage getPackage(ClassDescription desc);

    /**
     * Determines the package afferents, which are the packages that depend upon the given package.
     * <br/>Note: Only those afferents within the current component can be located.
     * 
     * @param efferent - The package instance for which afferents are to be found.
     * @param filter - The (non-null) type of dependency required as output.
     * Valid values are: {@link Universal}; its sub-types {@link ExtraComponent} or
     * {@link IntraComponent}; or the sub-types {@link IntraPackageComponent} or
     * {@link InterPackageComponent} of {@link IntraComponent}.
     * @return The (non-null) set of all intra-component afferent packages having
     * the desired dependency type.
     * @throws IllegalArgumentException If the specified dependency type is invalid.
     */
    public Collection<ClassPackage> getAfferents(ClassPackage efferent, ComponentDependency filter);

    /**
     * Determines the package efferents, which are the packages upon which the given package depends.
     * 
     * @param afferent - The package instance for which efferents are to be found.
     * @param filter - The (non-null) type of dependency required as output.
     * Valid values are: {@link Universal}; its sub-types {@link ExtraComponent} or
     * {@link IntraComponent}; or the sub-types {@link IntraPackageComponent} or
     * {@link InterPackageComponent} of {@link IntraComponent}.
     * @return The set of all efferent packages having the desired dependency type,
     * or a value of null if the package does not belong to the component.
     * @throws IllegalArgumentException If the specified dependency type is invalid.
     */
    public Collection<ClassPackage> getEfferents(ClassPackage afferent, ComponentDependency filter);

}