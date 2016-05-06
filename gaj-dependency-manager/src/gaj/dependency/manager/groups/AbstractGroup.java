/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.groups;

import gaj.classbinary.descriptors.ClassNameSpace;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.classes.ClassDescriptionFactory;
import gaj.dependency.manager.components.ClassesComponent;
import gaj.dependency.manager.dependencies.ComponentDependency;
import gaj.dependency.manager.dependencies.GroupDependency;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.packages.PackageFactory;
import gaj.iterators.core.Iterative;
import gaj.iterators.impl.Iteratives;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/*package-private*/ abstract class AbstractGroup implements ComponentGroup {

    //*********************************************************************************
    // Component-level methods.

    @Override
    public abstract int numComponents();

    @Override
    public abstract Iterative<ClassesComponent> getComponents();

    @Override
    public abstract boolean hasComponent(String componentName);

    @Override
    public abstract boolean hasComponent(ClassesComponent component);

    @Override
    public abstract ClassesComponent getComponent(String componentName);

    @Override
    final public ClassesComponent getComponent(ClassDescription desc) {
        return desc.isExternal() ? null : getComponent(desc.getComponentName());
    }

    @Override
    public ClassesComponent getComponent(ClassPackage apackage) {
        return apackage.isExternal() ? null : getComponent(apackage.getComponentName());
    }

    //*********************************************************************************
    // Class-level methods.

    @Override
    public int numClasses() {
        int numClasses = 0;
        for (ClassesComponent component : getComponents()) {
            numClasses += component.numClasses();
        }
        return numClasses;
    }

    @Override
    public Iterative<ClassDescription> getClasses() {
        return Iteratives.newIterative(getComponents().stream().flatMap(component -> component.getClasses().stream()));
    }

    @Override
    public ClassDescription getClass(String className) {
        for (ClassesComponent component : getComponents()) {
            ClassDescription desc = component.getClass(className);
            if (desc != null) {
                return desc;
            }
        }
        return null;
    }

    protected ClassDescription getClassOther(String className, ClassesComponent component) {
        for (ClassesComponent other : getComponents()) {
            if (component != other) {
                ClassDescription desc = other.getClass(className);
                if (desc != null) {
                    return desc;
                }
            }
        }
        return null;
    }

    @Override
    public boolean hasClass(ClassDescription desc) {
        return !desc.isExternal() && hasComponent(desc.getComponentName());
    }

    @Override
    public boolean hasClass(String className) {
        for (ClassesComponent component : getComponents()) {
            if (component.hasClass(className)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<ClassDescription> getEfferents(ClassDescription afferent, GroupDependency filter) {
        final ClassesComponent component = getComponent(afferent);
        if (component == null) {
            return null; // External class - no idea about its efferents.
        }
        if (filter.isSupportedBy(GroupDependency.IntraComponent)) {
            return component.getEfferents(afferent, ComponentDependency.fromDependency(filter));
        }
        final boolean extraComponent =
                filter == GroupDependency.ExtraGroup ||
                filter.isSupportedBy(GroupDependency.InterComponent);
        Collection<ClassDescription> efferents = component.getEfferents(
                afferent,
                extraComponent ? ComponentDependency.ExtraComponent : ComponentDependency.Universal);
        mapExternalToGroupClasses(efferents, component);
        // Check whether filtering is needed.
        if (filter == GroupDependency.Universal) {
            return efferents;
        }
        // Keep only internal or external classes.
        ClassDescriptionFactory.filterByExternality(efferents, filter == GroupDependency.ExtraGroup);
        // Check whether further filtering is needed.
        if (filter.isSubTypeOf(GroupDependency.IntraGroup)) {
            if (filter.isSupportedBy(GroupDependency.InterComponent)) {
                // Filter by component.
                ClassDescriptionFactory.filterByComponentName(
                        efferents, component.getComponentName(), false);
            }
            if (filter != GroupDependency.InterComponent) {
                // Filter by package.
                ClassDescriptionFactory.filterByPackageName(
                        efferents, afferent.getPackageName(), filter.isSupportedBy(GroupDependency.IntraPackage));
            }
        }
        return efferents;
    }

    // Classes that match isExternal() might in fact be in the group, but just not in the
    // component that created the descriptors. Replace such dummy descriptors with real ones.
    protected void mapExternalToGroupClasses(Collection<ClassDescription> classes, ClassesComponent component) {
        Collection<ClassDescription> internalClasses = new LinkedList<>();
        for (Iterator<ClassDescription> iter = classes.iterator(); iter.hasNext();) {
            ClassDescription desc = iter.next();
            if (desc.isExternal()) {
                // Could be external to component but internal to group.
                ClassDescription jclass = getClassOther(desc.getClassName(), component);
                if (jclass != null) {
                    iter.remove();
                    internalClasses.add(jclass);
                }
            }
        }
        classes.addAll(internalClasses);
    }

    @Override
    public Collection<ClassDescription> getAfferents(ClassDescription efferent, GroupDependency filter) {
        // Check whether an answer is feasible.
        final ClassesComponent component = getComponent(efferent);
        final boolean isInternal = component != null;
        if (isInternal && filter == GroupDependency.ExtraGroup
                || !isInternal && filter.isSupportedBy(GroupDependency.IntraGroup)) {
            return Collections.emptyList();
        }
        if (filter.isSupportedBy(GroupDependency.IntraComponent)) {
            Collection<ClassDescription> afferents = component.getAfferents(efferent, ComponentDependency.fromDependency(filter));
            return afferents;
        }
        // Get all afferents in this group.
        final String className = efferent.getClassName();
        Collection<ClassDescription> afferents = new LinkedList<>();
        for (ClassDescription desc : getClasses()) {
            if (desc.getImportedClassNames().contains(className)) {
                afferents.add(desc);
            }
        }
        // Check whether filtering is needed.
        if (filter.isSubTypeOf(GroupDependency.IntraGroup)) {
            if (filter.isSupportedBy(GroupDependency.InterComponent)) {
                // Filter by component.
                ClassDescriptionFactory.filterByComponentName(
                        afferents, efferent.getComponentName(), false);
            }
            if (filter != GroupDependency.InterComponent) {
                // Filter by package.
                ClassDescriptionFactory.filterByPackageName(
                        afferents, efferent.getPackageName(), filter.isSupportedBy(GroupDependency.IntraPackage));
            }
        }
        return afferents;
    }

    //*********************************************************************************
    // Package-level methods.

    @Override
    public int numPackages() {
        int numPackages = 0;
        for (ClassesComponent component : getComponents()) {
            numPackages += component.numPackages();
        }
        return numPackages;
    }

    @Override
    public Iterative<ClassPackage> getPackages() {
        return Iteratives.newIterative(getComponents().stream().flatMap(component -> component.getPackages().stream()));
    }

    @Override
    public boolean hasPackage(ClassPackage apackage) {
        return !apackage.isExternal() && hasComponent(apackage.getComponentName());
    }

    @Override
    public boolean hasPackage(String packageName) {
        for (ClassesComponent component : getComponents()) {
            if (component.hasPackage(packageName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterative<ClassPackage> getPackages(final String packageName) {
        return Iteratives.newIterative(getComponents().stream().map(component -> component.getPackage(packageName)).filter(pkg -> pkg != null));
    }

    @Override
    public ClassPackage getPackage(ClassDescription desc) {
        ClassesComponent component = getComponent(desc);
        return (component == null) ? null : component.getPackage(desc.getPackageName());
    }

    @Override
    public Collection<ClassPackage> getAfferents(ClassPackage efferent, GroupDependency filter) {
        // Check whether an answer is feasible.
        if (efferent.isExternal()) {
            // Fake package, containing no supporting classes.
            return Collections.emptyList();
        }
        final ClassesComponent component = getComponent(efferent.getComponentName());
        final boolean isInternal = component != null;
        if (isInternal && filter == GroupDependency.ExtraGroup
                || !isInternal && filter.isSupportedBy(GroupDependency.IntraGroup)) {
            return Collections.emptyList();
        }
        if (filter.isSupportedBy(GroupDependency.IntraComponent)) {
            Collection<ClassPackage> afferents = component.getAfferents(efferent, ComponentDependency.fromDependency(filter));
            return afferents;
        }
        // Get all afferents in this group.
        final String packageName = efferent.getPackageName();
        Collection<ClassPackage> afferents = new LinkedList<>();
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
        // Check whether filtering is needed.
        if (filter.isSubTypeOf(GroupDependency.IntraGroup)) {
            if (filter.isSupportedBy(GroupDependency.InterComponent)) {
                // Filter by component.
                PackageFactory.filterByComponentName(
                        afferents, efferent.getComponentName(), false);
            }
            if (filter != GroupDependency.InterComponent) {
                // Filter by package.
                PackageFactory.filterByPackageName(
                        afferents, efferent.getPackageName(), filter.isSupportedBy(GroupDependency.IntraPackage));
            }
        }
        return afferents;
    }

    @Override
    public Collection<ClassPackage> getEfferents(ClassPackage afferent, GroupDependency filter) {
        // Check whether an answer is feasible.
        final ClassesComponent component = getComponent(afferent);
        if (component == null) {
            return null; // External package - no idea about its efferents.
        }
        if (filter.isSupportedBy(GroupDependency.IntraComponent)) {
            return component.getEfferents(afferent, ComponentDependency.fromDependency(filter));
        }
        final boolean permitExternal = filter.supports(GroupDependency.ExtraGroup);
        final boolean permitInternal = filter != GroupDependency.ExtraGroup;
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
            // Turn external package names into external package instances.
            final String componentName = component.getComponentName();
            for (String packageName : externalPackageNames) {
                efferents.add(PackageFactory.newExternalPackage(componentName, packageName));
            }
        } else if (filter.isSubTypeOf(GroupDependency.IntraGroup)) {
            // Filter out unwanted dependencies.
            if (filter.isSupportedBy(GroupDependency.InterComponent)) {
                // Filter by component.
                PackageFactory.filterByComponentName(
                        efferents, afferent.getComponentName(), false);
            }
            if (filter != GroupDependency.InterComponent) {
                // Filter by package.
                PackageFactory.filterByPackageName(
                        efferents, afferent.getPackageName(), filter.isSupportedBy(GroupDependency.IntraPackage));
            }
        }
        return efferents;
    }

    private /*@Nullable*/ ClassPackage _getPackage(String className) {
        for (ClassPackage apackage : getPackages(ClassNameSpace.getPackageName(className))) {
            if (apackage.hasClass(className)) {
                return apackage;
            }
        }
        return null;
    }

}
