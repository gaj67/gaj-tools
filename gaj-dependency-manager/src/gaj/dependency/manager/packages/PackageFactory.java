/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.packages;

import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.dependencies.PackageDependency;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public abstract class PackageFactory {

    private PackageFactory() {}

    /**
     * Determines if the afferent package contains classes that actually depend upon classes
     * in the efferent package.
     * 
     * @param afferent - The afferent package instance.
     * @param efferent - The efferent package instance.
     * @return A value of true (or false) if the afferent package does (or does not)
     * depend upon the efferent package <em>instance</em>.
     */
    public static boolean hasPackageReference(ClassPackage afferent, ClassPackage efferent) {
        for (ClassDescription desc : afferent.getClasses()) {
            for (String className : desc.getImportedClassNames()) {
                if (efferent.hasClass(className)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void filterByPackageName(Collection<ClassPackage> packages, String packageName, boolean keepSamePackage) {
        for (Iterator<ClassPackage> iter = packages.iterator(); iter.hasNext();) {
            if (packageName.equals(iter.next().getPackageName()) != keepSamePackage) {
                iter.remove();
            }
        }
    }

    public static void filterByComponentName(Collection<ClassPackage> packages, String componentName, boolean keepSameComponent) {
        for (Iterator<ClassPackage> iter = packages.iterator(); iter.hasNext();) {
            if (componentName.equals(iter.next().getComponentName()) != keepSameComponent) {
                iter.remove();
            }
        }
    }

    public static void filterByExternality(Collection<ClassPackage> packages, boolean keepExternal) {
        for (Iterator<ClassPackage> iter = packages.iterator(); iter.hasNext();) {
            if (iter.next().isExternal() != keepExternal) {
                iter.remove();
            }
        }
    }

    /**
     * Creates a dummy package container with the given name, bound to the named component.
     * 
     * @param componentName - The name of the component managing the external package.
     * @param packageName - The fully qualified name of the package.
     * @return An enclosing package instance that is marked as 'external'.
     */
    public static ClassPackage newExternalPackage(final String componentName, final String packageName) {
        return new ClassPackage() {
            @Override
            public String getComponentName() {
                return componentName;
            }

            @Override
            public String getPackageName() {
                return packageName;
            }

            @Override
            public int numClasses() {
                return 0; // XXX: Make guess with least consequences. 
            }

            @Override
            public Iterable<ClassDescription> getClasses() {
                return Collections.emptyList();
            }

            @Override
            public boolean hasClass(ClassDescription desc) {
                return false;
            }

            @Override
            public boolean hasClass(String className) {
                return false;
            }

            @Override
            public ClassDescription getClass(String className) {
                return null;
            }

            @Override
            public boolean isExternal() {
                return true;
            }

            @Override
            public Collection<String> getImportedPackageNames() {
                return Collections.emptyList();
            }

            @Override
            public boolean hasSelfReference() {
                return false;
            }

            @Override
            public boolean hasInternalSelfReference() {
                return false;
            }

            @Override
            public boolean hasExternalSelfReference() {
                return false;
            }

            @Override
            public Collection<ClassDescription> getEfferents(ClassDescription afferent,
                    PackageDependency filter) {
                return null;
            }

            @Override
            public Collection<ClassDescription> getAfferents(ClassDescription efferent, PackageDependency filter) {
                return Collections.emptyList();
            }

            @Override
            public String toString() {
                return String.format("EXTERNAL:%s", packageName);
            }
        };
    }

    /**
     * Creates a modifiable package container with the given name, bound to the named component.
     * 
     * @param componentName - The name of the component creating the external package.
     * @param packageName - The fully qualified name of the package.
     * @return An instance of a package container to which classes may be added.
     */
    public static ModifiablePackage newModifiablePackage(String componentName, String packageName) {
        return new ModifiablePackageImpl(componentName, packageName);
    }

}
