/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.dependencies;

/**
 * A 'component' is a container for the classes located in one or more class paths.
 * Suppose we locate a class within the container, and then find any other class upon which it depends
 * (its so-called efferent). Then we have two distinct cases:
 * <ol>
 * <li>Intra-component: the efferent class is in the same component.</li>
 * <li>Extra-component: the efferent class is in a different component.</li>
 * </ol>
 * 
 * Now, since every class must be in some package (including the default package), the component
 * may be exhaustively partitioned into mutually exclusive packages (of mutually exclusive classes).
 * Hence, the intra-component dependency may be sub-divided into two further, mutually exclusive cases:
 * <ol>
 * <li>Intra-package: the efferent class is in the same package in the same component.</li>
 * <li>Inter-package: the efferent class is in a different package within the same component.</li>
 * </ol>
 * Combinations of the three fundamental (and mutually exclusive) cases might also be useful when
 * summarising a collection of dependencies, including:
 * <ul>
 * <li>Extra-package: the efferent class is in either a different package or a different component.</li>
 * <li>Universal: the efferent class is either in the same component or a different component.</li>
 * </ul>
 * 
 * <p/>
 * In additional, a 'component group' (or just 'group') is a container for a collection of components.
 * Hence, if we locate a class (within a component) within a group, then its dependency
 * upon any one of its efferent classes can be divided into two distinct cases:
 * <ol>
 * <li>Intra-group: the efferent class is in the same group.</li>
 * <li>Extra-group: the efferent class is in a different group.</li>
 * </ol>
 * Furthermore, since the group may be exhaustively partitioned into mutually exclusive components, then
 * the intra-group dependency may be sub-divided into two further, mutually exclusive cases:
 * <ol>
 * <li>Intra-component: the efferent class is in the same component in the same group.</li>
 * <li>Inter-component: the efferent class is in a different component in the same group.</li>
 * </ol>
 * However, when it comes to packages within a group, the issue is not quite as clear cut
 * as it is for components. For example, it is entirely feasible for
 * two different components to contain (hopefully different) classes from the same package.
 * Hence, we must distinguish between intra- and inter-package dependencies within the same component,
 * and intra- and inter-package dependencies between components, thus:
 * <ol>
 * <li>Intra-package-component: the efferent class is in the same package in the same component.</li>
 * <li>Inter-package-component: the efferent class is in a different package within the same component.</li>
 * <li>Intra-package-other: the efferent class is in the same package in a different component in the same group.</li>
 * <li>Inter-package-other: the efferent class is in a different package in a different component in the same group component.</li>
 * </ol>
 * Thus, there are 5 fundamental types of dependencies from a group perspective, which may be collapsed
 * to various partitions of types from the perspective of components or packages.
 */
public interface DependencyType {
    // The five fundamental, mutually exclusive dependency types for a group.
    public static final int INTRA_PACKAGE_INTRA_COMPONENT_MASK = 0b00001;
    public static final int INTER_PACKAGE_INTRA_COMPONENT_MASK = 0b00010;
    public static final int INTRA_PACKAGE_INTER_COMPONENT_MASK = 0b00100;
    public static final int INTER_PACKAGE_INTER_COMPONENT_MASK = 0b01000;
    public static final int EXTRA_GROUP_MASK                   = 0b10000;
    // The unions of various fundamental dependency types.
    public static final int INTRA_COMPONENT_MASK = INTRA_PACKAGE_INTRA_COMPONENT_MASK | INTER_PACKAGE_INTRA_COMPONENT_MASK;
    public static final int INTER_COMPONENT_MASK = INTRA_PACKAGE_INTER_COMPONENT_MASK | INTER_PACKAGE_INTER_COMPONENT_MASK;
    public static final int EXTRA_COMPONENT_MASK = INTER_COMPONENT_MASK | EXTRA_GROUP_MASK;
    public static final int INTRA_PACKAGE_MASK   = INTRA_PACKAGE_INTRA_COMPONENT_MASK | INTRA_PACKAGE_INTER_COMPONENT_MASK;
    public static final int INTER_PACKAGE_MASK   = INTER_PACKAGE_INTRA_COMPONENT_MASK | INTER_PACKAGE_INTER_COMPONENT_MASK;
    public static final int EXTRA_PACKAGE_MASK   = INTER_PACKAGE_MASK | EXTRA_GROUP_MASK;
    public static final int INTRA_GROUP_MASK     = INTRA_COMPONENT_MASK | INTER_COMPONENT_MASK;
    public static final int UNIVERSAL_MASK       = INTRA_GROUP_MASK | EXTRA_GROUP_MASK;

    /**
     * 
     * @return The bit mask of fundamental dependency types.
     */
    public int getMask();

    /**
     * Determines if there is a path in the dependency hierarchy from the given base type
     * to the current type. Essentially, this means the current type is either equal to the given type
     * or is a sub-type of the given type.
     * 
     * @param type - The ancestral type in the hierarchy.
     * @return A value of true (or false) if the current type is (or is not) on the same branch
     * as the given type.
     */
    public boolean isSupportedBy(DependencyType type);

    /**
     * Determines if there is a path in the dependency hierarchy from the current base type
     * to the given type. Essentially, this means the current type is either equal to the given type
     * or is a super-type of the given type.
     * 
     * @param type - The descendant type in the hierarchy.
     * @return A value of true (or false) if the given type is (or is not) on the same branch
     * as the current type.
     */
    public boolean supports(DependencyType type);

    /**
     * Determines if the current dependency type is a sub-type of, and not equal to, the given super-type.
     * 
     * @param type - The potential super-type.
     * @return A value of true (or false) if the current type is (or is not) a sub-type
     * of the given type.
     */
    public boolean isSubTypeOf(DependencyType type);

    /**
     * Determines if the current dependency type is a super-type of, and not equal to, the given sub-type.
     * 
     * @param type - The potential sub-type.
     * @return A value of true (or false) if the current type is (or is not) a super-type
     * of the given type.
     */
    public boolean isSuperTypeOf(DependencyType type);

}