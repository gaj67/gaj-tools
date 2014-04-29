/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.dependencies;

/**
 * Defines the hierarchy for dependency queries to a component.
 * A dependency is of the form: afferent -> efferent; and it is assumed the afferent class is in the component.
 * Thus, the dependency type indicates broadly whether or not the efferent class is in the same component.
 * If it is, then the dependency type may also indicate more specifically whether or not the efferent class is
 * in the same package.
 */
public enum ComponentDependency implements DependencyType {
    /**
     * Dependency of any type.
     */
    Universal(UNIVERSAL_MASK),
        /**
         * Dependency between different components.
         */
        ExtraComponent(EXTRA_COMPONENT_MASK),
        /**
         * Dependency within the same component.
         */
        IntraComponent(INTRA_COMPONENT_MASK),
            /**
             * Dependency within the same package instance (in the same component).
             */
            IntraPackage(INTRA_PACKAGE_INTRA_COMPONENT_MASK),
            /**
             * Dependency between different package instances in the same component.
             */
            InterPackage(INTER_PACKAGE_INTRA_COMPONENT_MASK);

    private final int mask;

    private ComponentDependency(int mask) {
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
    public static ComponentDependency fromDependency(DependencyType type) {
        final int omask = type.getMask();
        for (ComponentDependency dependency : values()) {
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
