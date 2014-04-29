/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.groups;

import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.components.ClassesComponent;
import gaj.dependency.manager.dependencies.GroupDependency;
import gaj.dependency.manager.packages.ClassPackage;
import java.util.Collection;

/**
 * Encapsulates a group of components.
 * @see {@link ClassesComponent}.
 */
public interface ComponentGroup {

    //*********************************************************************************
    // Component-level methods.
    /**
     * @return The number of components in the group.
     */
    public int numComponents();

    /**
     * @return An iterable over the set of components in the group.
     */
    public Iterable<ClassesComponent> getComponents();

    /**
     * Tests whether the named component is located in the group.
     * 
     * @param componentName - The name of the component.
     * @return A value of true (or false) if the component is (or is not) located in the group.
     */
    public boolean hasComponent(String componentName);

    /**
     * Tests whether the component is located in the group.
     * 
     * @param component - The component instance.
     * @return A value of true (or false) if the component is (or is not) located in the group.
     */
    public boolean hasComponent(ClassesComponent component);

    /**
     * @param componentName - The name of the component.
     * @return The named component, or a value of null if the component is not in the group.
     */
    public ClassesComponent getComponent(String componentName);

    /**
     * Locates the component that contains the given class.
     * 
     * @param desc - The descriptor of the class.
     * @return The component containing the given class, or a value of null if no such component is found.
     */
    public ClassesComponent getComponent(ClassDescription desc);

    /**
     * Locates the component that contains the given package.
     * 
     * @param apackage - The package instance.
     * @return The component containing the given package, or a value of null if no such
     * component is found.
     */
    public ClassesComponent getComponent(ClassPackage apackage);

    //*********************************************************************************
    // Class-level methods.
    /**
     * @return The number of classes loaded into the group.
     */
    public int numClasses();

    /**
     * @return An iterable over the set of all classes in the group.
     */
    public Iterable<ClassDescription> getClasses();

    /**
     * Looks up the named class in the group.
     * <br/>In the event that the same class name is defined in multiple components, the class
     * from the first matching component will be used.
     * 
     * @param className - The fully qualified name of the class.
     * @return The descriptor of the named class, or a value of null if the class is not found.
     */
    public ClassDescription getClass(String className);

    /**
     * Tests whether the named class is located in the group.
     * 
     * @param className - The fully qualified name of the class.
     * @return A value of true (or false) if the class is (or is not) located in the group.
     */
    public boolean hasClass(String className);

    /**
     * Tests whether the given class is located in the group.
     * <p/>This is expected to be a shallow test, based upon whether or not the component that created
     * the class description is located in the group.
     * However, note that an external class is deemed to not be located
     * in the component that created it.
     * 
     * @param desc - The descriptor of the class.
     * @return A value of true (or false) if the class is (or is not) located in the group.
     */
    public boolean hasClass(ClassDescription desc);

    /**
     * Determines the class efferents, which are the classes upon which the named class depends.
     * 
     * @param desc - The descriptor of the class for which efferents are to be found.
     * @param filter - The (non-null) type of dependency required as output.
     * Valid values are: {@link Universal}; its sub-types {@link ExtraGroup} or
     * {@link IntraGroup};
     * the sub-types {@link IntraComponent} or
     * {@link InterComponent} of {@link IntraGroup};
     * the sub-types {@link IntraPackage} or
     * {@link InterPackage} of {@link IntraGroup}; or
     * any of the joint component-package types: {@link IntraPackageComponent};
     * {@link InterPackageComponent}; {@link IntraPackageOther}; or {@link InterPackageOther};
     * 
     * @return The set of all efferent classes having the desired dependency type,
     * or a value of null if the class does not belong to the group.
     * @throws IllegalArgumentException If the specified dependency type is invalid.
     */
    public Collection<ClassDescription> getEfferents(ClassDescription desc, GroupDependency filter);

    /**
     * Determines the class afferents, which are the classes that depend upon the named class.
     * Note: Only those afferents within the current group can be located.
     * 
     * @param desc - The descriptor of the class for which afferents are to be found.
     * @param filter - The (non-null) type of dependency required as output.
     * Valid values are: {@link Universal}; its sub-types {@link ExtraGroup} or
     * {@link IntraGroup};
     * the sub-types {@link IntraComponent} or {@link InterComponent} of {@link IntraGroup}; or
     * the sub-types {@link IntraPackage} or {@link InterPackage} of {@link IntraGroup}; or
     * any of the joint component-package types: {@link IntraPackageComponent};
     * {@link InterPackageComponent}; {@link IntraPackageOther}; or {@link InterPackageOther};
     * 
     * @return The (non-null) set of all intra-group afferent classes having
     * the desired dependency type.
     * @throws IllegalArgumentException If the specified dependency type is invalid.
     */
    public Collection<ClassDescription> getAfferents(ClassDescription desc, GroupDependency filter);

    //*********************************************************************************
    /*package-level methods.

    /**
     * Determines the total number of packages loaded into all of the components in the group.
     * <br/>Note that multiple components may collectively have distinct package instances
     * with the same name. Such multiple instances will each be counted.
     * 
     * @return The total number of packages loaded into the group.
     */
    public int numPackages();

    /**
     * 
     * @return An iterable over all package instances from all components.
     */
    public Iterable<ClassPackage> getPackages();

    /**
     * Tests whether the given package instance is located within the group.
     * <p/>This is expected to be a shallow test, based upon knowledge of the component that
     * manages the package instance.
     * 
     * @param apackage - The package instance.
     * @return A value of true (or false) if the package is (or is not) located in the group.
     */
    public boolean hasPackage(ClassPackage apackage);

    /**
     * Tests whether there is any package instance with the given name located within
     * any component in the group.
     * <p/>This is expected to be a deep test, since there is no additional information.
     * 
     * @param packageName - The fully qualified name of the package.
     * @return A value of true (or false) if the package is (or is not) located in the group.
     */
    public boolean hasPackage(String packageName);

    /**
     * Obtains all instances of the named package, from all components in the group.
     * 
     * @param packageName - The fully qualified name of the package.
     * @return A non-null iterable over any package instances with the given name that are
     * located within the group.
     */
    public Iterable<ClassPackage> getPackages(String packageName);

    /**
     * Obtains the package instance containing the given class.
     * 
     * @param desc - The descriptor of the class.
     * @return The containing package instance,
     * or a value of null if the class is not located within the group.
     */
    public ClassPackage getPackage(ClassDescription desc);

    /**
     * Determines the package efferents, which are the packages upon which classes in
     * the given package depend.
     * 
     * @param afferent - The package instance for which efferents are to be found.
     * @param filter - The (non-null) type of dependency required as output.
     * Valid values are: {@link Universal}; its sub-types {@link ExtraGroup} or
     * {@link IntraGroup};
     * the sub-types {@link IntraComponent} or {@link InterComponent} of {@link IntraGroup}; or
     * the sub-types {@link IntraPackage} or {@link InterPackage} of {@link IntraGroup}; or
     * any of the joint component-package types: {@link IntraPackageComponent};
     * {@link InterPackageComponent}; {@link IntraPackageOther}; or {@link InterPackageOther};
     *
     * @return The set of all efferent packages having the desired dependency type,
     * or a value of null if the given package does not belong to the group.
     * @throws IllegalArgumentException If the specified dependency type is invalid.
     */
    public Collection<ClassPackage> getEfferents(ClassPackage afferent, GroupDependency filter);

    /**
     * Determines the package afferents, which are the packages containing classes that
     * depend upon the given package.
     * <br/>Note: Only those afferents within the current group can be located.
     * 
     * @param afferent - The package instance for which afferents are to be found.
     * @param filter - The (non-null) type of dependency required as output.
     * Valid values are: {@link Universal}; its sub-types {@link ExtraGroup} or
     * {@link IntraGroup};
     * the sub-types {@link IntraComponent} or {@link InterComponent} of {@link IntraGroup}; or
     * the sub-types {@link IntraPackage} or {@link InterPackage} of {@link IntraGroup}; or
     * any of the joint component-package types: {@link IntraPackageComponent};
     * {@link InterPackageComponent}; {@link IntraPackageOther}; or {@link InterPackageOther};
     *
     * @return The (non-null) set of all intra-group afferent packages having
     * the desired dependency type.
     * @throws IllegalArgumentException If the specified dependency type is invalid.
     */
    public Collection<ClassPackage> getAfferents(ClassPackage efferent, GroupDependency filter);

}
