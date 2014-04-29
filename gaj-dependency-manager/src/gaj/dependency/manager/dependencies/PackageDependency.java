/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.dependencies;

/**
 * Defines the hierarchy for dependency queries to a package.
 * A dependency is of the form: afferent -> efferent; and it is assumed the afferent class is in the package.
 * Thus, the dependency type indicates whether or not the efferent class is in the same package.
 */
public enum PackageDependency implements DependencyType {
    
    /**
     * Dependency of any type.
     */
    Universal(UNIVERSAL_MASK),
        /**
         * Dependency between different package instances.
         */
        ExtraPackage(EXTRA_PACKAGE_MASK),
        /**
         * Dependency within the same package instances.
         */
        IntraPackage(INTRA_PACKAGE_MASK);

    private final int mask;

    private PackageDependency(int mask) {
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
    public static PackageDependency fromDependency(DependencyType type) {
        final int omask = type.getMask();
        for (PackageDependency dependency : values()) {
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
