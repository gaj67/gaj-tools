/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.components;

import gaj.classbinary.descriptors.ClassDescriptor;
import gaj.classbinary.loader.ClassBinaryLoader;
import gaj.classbinary.loader.ClassLoaderFactory;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.classes.ClassDescriptionFactory;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.packages.ModifiablePackage;
import gaj.dependency.manager.packages.PackageFactory;
import gaj.iterators.core.Iterative;
import gaj.iterators.impl.Iteratives;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*package-private*/ abstract class LoadableComponentImpl extends AbstractComponent implements LoadableComponent {
    private final ClassBinaryLoader loader = ClassLoaderFactory.newClassLoader();
    private final Map<String,ClassPackage> packages = new HashMap<>();
    private boolean loaded = false;
    private int numClasses = 0;

    //*********************************************************************************
    // Component construction.

    @Override
    public abstract void warnDuplicateClass(ClassDescription desc);

    /**
     * Defines a named component. If the given name is null, then an automatic name is assigned.
     * @param componentName - The (possibly null) name of the component.
     */
    /*package-private*/ LoadableComponentImpl(String componentName) {
        super(componentName);
    }

    //*********************************************************************************
    // Component initialisation.

    @Override
    public LoadableComponent addClasses(Iterable<ClassDescription> classes) {
        for (ClassDescription desc : classes) {
            addClass(desc);
        }
        loaded = false;
        return this;
    }

    private void addClass(ClassDescription desc) {
        final String packageName = desc.getPackageName();
        ModifiablePackage apackage = (ModifiablePackage) packages.get(packageName);
        if (apackage == null) {
            packages.put(packageName, apackage = PackageFactory.newModifiablePackage(getComponentName(), packageName));
        }
        try {
            apackage.addClass(desc);
        } catch (IllegalArgumentException e) {
            warnDuplicateClass(desc);
        }
        numClasses++;
    }

    @Override
    public LoadableComponent addClassPath(Path classPath) {
        loader.addClassPath(classPath);
        loaded = false;
        return this;
    }

    @Override
    public LoadableComponent addClassPaths(Path... classPaths) {
        for (Path path : classPaths) {
            addClassPath(path);
        }
        return this;
    }

    @Override
    public LoadableComponent addClassPaths(Iterable<Path> classPaths) {
        for (Path path : classPaths) {
            addClassPath(path);
        }
        return this;
    }

    @Override
    public Iterable<Path> getClassPaths() {
        return loader.getClassPaths();
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public ClassesComponent load() throws IOException {
        if (!loaded) {
            final String componentName = getComponentName();
            for (ClassDescriptor desc : loader.getClassDescriptors()) {
                addClass(ClassDescriptionFactory.newInternalClass(componentName, desc));
            }
            loaded = true;
        }
        return this;
    }

    //*********************************************************************************
    // Class-level methods.

    @Override
    public int numClasses() {
        return numClasses;
    }

    //*********************************************************************************
    // Package-level methods.

    @Override
    public int numPackages() {
        return packages.size();
    }

    @Override
    public Iterative<ClassPackage> getPackages() {
        return Iteratives.toIterative(Collections.unmodifiableCollection(packages.values()));
    }

    @Override
    public boolean hasPackage(String packageName) {
        return packages.containsKey(packageName);
    }

    @Override
    public ClassPackage getPackage(String packageName) {
        return packages.get(packageName);
    }

}