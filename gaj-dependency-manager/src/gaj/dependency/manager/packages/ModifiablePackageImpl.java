/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.packages;

import gaj.classbinary.descriptors.ClassNameSpace;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.classes.ClassDescriptionFactory;
import gaj.dependency.manager.dependencies.PackageDependency;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/*package-private*/ class ModifiablePackageImpl implements ModifiablePackage {

    private final Map<String,ClassDescription> classes = new HashMap<>();
    private final Set<String> efferentPackageNames = new HashSet<>();
    private final Set<String> selfReferencingClassNames = new HashSet<>();
    private final String componentName;
    private final String packageName;
    private int numClasses = 0;

    /*package-private*/ ModifiablePackageImpl(String componentName, String packageName) {
        this.componentName = componentName;
        this.packageName = packageName;
    }

    @Override
    public String getComponentName() {
        return componentName;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public void addClass(ClassDescription desc) {
        if (classes.containsKey(desc.getClassName())) {
            throw new IllegalArgumentException("Duplicate class name: " + desc);
        }
        classes.put(desc.getClassName(), desc);
        numClasses++;
        if (desc.isVisible()) {
        }
        for (String className : desc.getImportedClassNames()) {
            String packageName = ClassNameSpace.getPackageName(className);
            if (packageName.equals(this.packageName)) {
                selfReferencingClassNames.add(className);
            } else {
                efferentPackageNames.add(packageName);
            }
        }
    }

    @Override
    public int numClasses() {
        return numClasses;
    }

    @Override
    public Iterable<ClassDescription> getClasses() {
        return Collections.unmodifiableCollection(classes.values());
    }

    @Override
    public boolean hasClass(ClassDescription desc) {
        return classes.containsValue(desc);
    }

    @Override
    public boolean hasClass(String className) {
        return classes.containsKey(className);
    }

    @Override
    public ClassDescription getClass(String className) {
        return classes.get(className);
    }

    @Override
    public Collection<String> getImportedPackageNames() {
        return Collections.unmodifiableCollection(efferentPackageNames);
    }

    @Override
    public boolean isExternal() {
        return false;
    }

    @Override
    public boolean hasSelfReference() {
        return !selfReferencingClassNames.isEmpty();
    }

    @Override
    public boolean hasInternalSelfReference() {
        for (String className : selfReferencingClassNames) {
            if (classes.containsKey(className)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasExternalSelfReference() {
        for (String className : selfReferencingClassNames) {
            if (!classes.containsKey(className)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", componentName, packageName);
    }

    @Override
    public Collection<ClassDescription> getEfferents(ClassDescription afferent, PackageDependency filter) {
        if (!hasClass(afferent)) {
            return null;
        }
        Collection<ClassDescription> efferents = new LinkedList<>();
        final boolean permitExternal = filter.supports(PackageDependency.ExtraPackage);
        final boolean permitInternal = filter.supports(PackageDependency.IntraPackage);
        for (String efferentName : afferent.getImportedClassNames()) {
            ClassDescription efferent = getClass(efferentName);
            if (efferent == null) {
                // External dependency.
                if (permitExternal) {
                    efferents.add(ClassDescriptionFactory.newExternalClass(componentName, efferentName));
                }
            } else {
                // Internal dependency.
                if (permitInternal) {
                    efferents.add(efferent);
                }
            }
        }
        return efferents;
    }

    @Override
    public Collection<ClassDescription> getAfferents(ClassDescription efferent, PackageDependency filter) {
        // Check whether an answer is feasible.
        final boolean isInternal = hasClass(efferent);
        if (isInternal && filter == PackageDependency.ExtraPackage
                || !isInternal && filter == PackageDependency.IntraPackage) {
            return Collections.emptyList();
        }
        Collection<ClassDescription> afferents = new LinkedList<>();
        final String className = efferent.getClassName();
        for (ClassDescription afferent : getClasses()) {
            if (afferent.getImportedClassNames().contains(className)) {
                afferents.add(afferent);
            }
        }
        return afferents;
    }

}