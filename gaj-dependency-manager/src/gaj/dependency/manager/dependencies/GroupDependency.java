/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.dependencies;

public enum GroupDependency implements DependencyType {
    // Defines the hierarchy for dependency queries to a component.
    /**
     * Dependency of any type.
     */
    Universal(UNIVERSAL_MASK),
        /**
         * Dependency between different groups.
         */
        ExtraGroup(EXTRA_GROUP_MASK),
        /**
         * Dependency within the same group.
         */
        IntraGroup(INTRA_GROUP_MASK),
        // EITHER:
            // Component-level view
            /**
             * Dependency between different components in the same group.
             */
            InterComponent(INTER_COMPONENT_MASK),
                /**
                 * Dependency between equivalently named package instances in different components (in the same group).
                 */
                IntraPackageInterComponent(INTRA_PACKAGE_INTER_COMPONENT_MASK),
                /**
                 * Dependency between differently named package instances in different components (in the same group).
                 */
                InterPackageInterComponent(INTER_PACKAGE_INTER_COMPONENT_MASK),
            /**
             * Dependency within the same component (in the same group).
             */
            IntraComponent(INTRA_COMPONENT_MASK),
                /**
                 * Dependency within the same package instance (in the same component, in the same group).
                 */
                IntraPackageIntraComponent(INTRA_PACKAGE_INTRA_COMPONENT_MASK),
                /**
                 * Dependency between different package instances (in the same component, in the same group).
                 */
                InterPackageIntraComponent(INTER_PACKAGE_INTRA_COMPONENT_MASK),
        // OR:
            /*package-level view
            /**
             * Dependency between differently named package instances in the same group
             * (either within the same component or between different components).
             */
            InterPackage(INTER_PACKAGE_MASK),
                //InterPackageIntraComponent(INTER_PACKAGE_INTRA_COMPONENT_MASK) - see above.
                //InterPackageInterComponent(INTER_PACKAGE_INTER_COMPONENT_MASK) - see above.
            /**
             * Dependency within the same package instance (in the same component) 
             * or between equivalently named package instances (in different components) 
             * in the same group.
             */
            IntraPackage(INTRA_PACKAGE_MASK);
                //IntraPackageIntraComponent(INTRA_PACKAGE_INTRA_COMPONENT_MASK) - see above.
                //IntraPackageInterComponent(INTRA_PACKAGE_INTER_COMPONENT_MASK) - see above.

    private final int mask;

    private GroupDependency(int mask) {
        this.mask = mask;
    }

    @Override
    public int getMask() {
        return mask;
    }

    /**
     * Locates the internal dependency type that corresponds to the given external type.
     * 
     * @param type - The external dependency type.
     * @return The corresponding internal dependency type, or a value of null if no internal type is consistent with 
     * the given external type.
     */
    public static GroupDependency fromDependency(DependencyType type) {
        final int omask = type.getMask();
        for (GroupDependency dependency : values()) {
            if (dependency.getMask() == omask) return dependency;
        }
        return null;
    }

    @Override
    public boolean isSupportedBy(DependencyType type) {
        return (mask & type.getMask()) == mask;
    }

    @Override
    public boolean supports(DependencyType type) {
        final int omask = type.getMask();
        return (mask & omask) == omask;
    }

    @Override
    public boolean isSubTypeOf(DependencyType type) {
        final int omask = type.getMask();
        return mask != omask && (mask & omask) == mask;
    }

    @Override
    public boolean isSuperTypeOf(DependencyType type) {
        final int omask = type.getMask();
        return mask != omask && (mask & omask) == omask;
    }

}
