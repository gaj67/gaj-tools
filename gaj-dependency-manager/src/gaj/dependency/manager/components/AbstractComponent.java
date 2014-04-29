/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.components;

import gaj.classbinary.descriptors.ClassNameSpace;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.classes.ClassDescriptionFactory;
import gaj.dependency.manager.dependencies.ComponentDependency;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.packages.PackageFactory;
import gaj.iterators.core.Filter;
import gaj.iterators.core.IterableIterator;
import gaj.iterators.core.IteratorFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

/*package-private*/ abstract class AbstractComponent implements ClassesComponent {
    private final String componentName;

    //*********************************************************************************
    // Component construction.

    /**
     * Defines a named component. If the given name is null, then an automatic name is assigned.
     * @param componentName - The (possibly null) name of the component.
     */
    /*package-private*/ AbstractComponent(/*@Nullable*/ String componentName) {
        this.componentName = (componentName == null) ? toString() : componentName;
    }

    @Override
    public String getComponentName() {
        return componentName;
    }

    //*********************************************************************************
    // Package-level methods.

    @Override
    public abstract int numPackages();

    @Override
    public abstract Iterable<ClassPackage> getPackages();

    @Override
    public abstract boolean hasPackage(String packageName);

    @Override
    public abstract ClassPackage getPackage(String packageName);

    @Override
    public boolean hasPackage(ClassPackage apackage) {
        return !apackage.isExternal() && getComponentName() == apackage.getComponentName();
    }

    @Override
    public ClassPackage getPackage(ClassDescription desc) {
        return hasClass(desc) ? getPackage(desc.getPackageName()) : null;
    }

    @Override
    public Collection<ClassPackage> getAfferents(ClassPackage efferent, ComponentDependency filter) {
        // Check whether an answer is feasible.
        if (efferent.isExternal()) {
            // Fake package, containing no supporting classes.
            return Collections.emptyList();
        }
        final boolean isInternal = hasPackage(efferent);
        if (isInternal && filter == ComponentDependency.ExtraComponent
                || !isInternal && filter.isSupportedBy(ComponentDependency.IntraComponent)) {
            return Collections.emptyList();
        }
        // Check for special singleton case.
        if (filter == ComponentDependency.IntraPackage) {
            return Collections.<ClassPackage>emptyList();
        }
        Collection<ClassPackage> afferents = new LinkedList<>();
        final String packageName = efferent.getPackageName();
        for (ClassPackage afferent : getPackages()) {
            // Check for self reference.
            if (afferent == efferent) continue; // Skip self.
            // Check for eponymous or other reference.
            if (!isInternal && afferent.getPackageName().equals(packageName)
                    || afferent.getImportedPackageNames().contains(packageName)) {
                if (PackageFactory.hasPackageReference(afferent, efferent)) {
                    afferents.add(afferent);
                }
            }
        }
        return afferents;
    }

    @Override
    public Collection<ClassPackage> getEfferents(ClassPackage afferent, ComponentDependency filter) {
        if (!hasPackage(afferent)) {
            return null; // External package - no idea about its efferents.
        }
        final boolean permitExternal = filter.supports(ComponentDependency.ExtraComponent);
        final boolean permitInternal = filter != ComponentDependency.ExtraComponent;
        Collection<ClassPackage> efferents = new HashSet<>();
        Collection<String> externalPackageNames = new HashSet<>();
        for (ClassDescription desc : afferent.getClasses()) {
            for (String efferentName : desc.getImportedClassNames()) {
                ClassPackage efferent = _getPackage(efferentName);
                if (efferent == null) {
                    // External package reference.
                    if (permitExternal) {
                        externalPackageNames.add(ClassNameSpace.getPackageName(efferentName));
                    }
                } else if (efferent != afferent) { // Exclude self-reference.
                    // Internal package reference.
                    if (permitInternal) {
                        efferents.add(efferent);
                    }
                }
            }
        }
        if (permitExternal) {
            final String componentName = getComponentName();
            for (String packageName : externalPackageNames) {
                efferents.add(PackageFactory.newExternalPackage(componentName, packageName));
            }
        } else if (filter.isSubTypeOf(ComponentDependency.IntraComponent)) {
            PackageFactory.filterByPackageName(
                    efferents, afferent.getPackageName(),
                    filter == ComponentDependency.IntraPackage);
        }
        return efferents;
    }

    private ClassPackage _getPackage(String className) {
        ClassPackage apackage = getPackage(ClassNameSpace.getPackageName(className));
        return (apackage != null && apackage.hasClass(className)) ? apackage : null;
    }

    //*********************************************************************************
    // Class-level methods.
    @Override
    public int numClasses() {
        int numClasses = 0;
        for (ClassPackage apackage : getPackages()) {
            numClasses += apackage.numClasses();
        }
        return numClasses;
    }

    protected static final Filter<ClassPackage,Iterable<? extends ClassDescription>>
    PACKAGE_CLASSES_FILTER = new Filter<ClassPackage,Iterable<? extends ClassDescription>>() {
        @Override
        public Iterable<ClassDescription> filter(ClassPackage apackage) {
            return apackage.getClasses();
        }
    };

    @Override
    public IterableIterator<ClassDescription> getClasses() {
        return IteratorFactory.newMultiIterator(getPackages(), PACKAGE_CLASSES_FILTER);
    }

    @Override
    public ClassDescription getClass(String className) {
        ClassPackage apackage = getPackage(ClassNameSpace.getPackageName(className));
        return (apackage == null) ? null : apackage.getClass(className);
    }

    @Override
    public boolean hasClass(ClassDescription desc) {
        return !desc.isExternal() && getComponentName() == desc.getComponentName();
    }

    @Override
    public boolean hasClass(String className) {
        for (ClassPackage apackage : getPackages()) {
            if (apackage.hasClass(className)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<ClassDescription> getEfferents(ClassDescription afferent, ComponentDependency filter) {
        if (!hasClass(afferent)) {
            return null; // External class - no idea about its efferents.
        }
        Collection<ClassDescription> efferents = new LinkedList<>();
        for (String efferentName : afferent.getImportedClassNames()) {
            efferents.add(makeClass(efferentName));
        }
        // Check whether filtering is needed.
        if (filter == ComponentDependency.Universal) {
            return efferents;
        }
        // Keep only internal or external classes.
        ClassDescriptionFactory.filterByExternality(efferents, filter == ComponentDependency.ExtraComponent);
        // Check whether further filtering is needed.
        if (filter.isSubTypeOf(ComponentDependency.IntraComponent)) {
            // Filter by package.
            ClassDescriptionFactory.filterByPackageName(
                    efferents, afferent.getPackageName(), filter == ComponentDependency.IntraPackage);
        }
        return efferents;
    }

    private ClassDescription makeClass(String className) {
        ClassDescription desc = getClass(className);
        return (desc != null) ? desc : ClassDescriptionFactory.newExternalClass(componentName, className);
    }

    @Override
    public Collection<ClassDescription> getAfferents(ClassDescription efferent, ComponentDependency filter) {
        // Check whether an answer is feasible.
        final boolean isInternal = hasClass(efferent);
        if (isInternal && filter == ComponentDependency.ExtraComponent
                || !isInternal && filter.isSupportedBy(ComponentDependency.IntraComponent)) {
            return Collections.emptyList();
        }
        // Get all afferents in this component.
        final String className = efferent.getClassName();
        Collection<ClassDescription> afferents = new LinkedList<>();
        for (ClassDescription afferent : getClasses()) {
            if (afferent.getImportedClassNames().contains(className)) {
                afferents.add(afferent);
            }
        }
        // Check whether filtering is needed.
        if (filter == ComponentDependency.Universal) {
            return afferents;
        }
        // Filter out sub-types, if requested.
        if (filter.isSubTypeOf(ComponentDependency.IntraComponent)) {
            // Filter by package.
            ClassDescriptionFactory.filterByPackageName(
                    afferents, efferent.getPackageName(), filter == ComponentDependency.IntraPackage);
        }
        return afferents;
    }

    @Override
    public String toString() {
        return componentName;
    }

}