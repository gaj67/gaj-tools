/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.components;

import gaj.dependency.manager.classes.ClassDescription;

import java.nio.file.Path;


/**
 * Manages all the byte-compiled classes loaded from one or more class paths.
 */
public abstract class ComponentFactory {

    public static enum DuplicateClassWarning {
        ignore, warn, exception;
    }

    private static DuplicateClassWarning duplicationWarning = DuplicateClassWarning.exception;

    public static DuplicateClassWarning getDuplicateClassWarning() {
        return duplicationWarning;
    }

    public static void setDuplicateClassWarning(DuplicateClassWarning duplicationWarning) {
        ComponentFactory.duplicationWarning = duplicationWarning;
    }

    private ComponentFactory() {}

    /**
     * Creates a new component with an automatically assigned name.
     */
    public static LoadableComponent newComponent() {
        return newComponent(null);
    }

    /**
     * Creates a new named component. If the given name is null, then an automatic name is assigned.
     * 
     * @param componentName - The (possibly null) name of the component.
     */
    public static LoadableComponent newComponent(String componentName) {
        return new LoadableComponentImpl(componentName) {
            @Override
            public void warnDuplicateClass(ClassDescription desc) {
                switch (duplicationWarning) {
                    case exception:
                        throw new IllegalArgumentException("Duplicate loading of class: " + desc);
                    case warn:
                        System.out.printf("WARNING: Duplicate loading of class: %s%n", desc);
                        break;
                    default:
                        break; // Ignore error.
                }
            } };
    }

    /**
     * Creates a new named component bound to he given class paths.
     * If the given name is null, then an automatic name is assigned.
     * 
     * @param name - The (possibly null) name of the component.
     */
    public static LoadableComponent newComponent(String name, Path... classPaths) {
        LoadableComponent component = newComponent(name);
        for (Path path : classPaths) {
            component.addClassPath(path);
        }
        return component;
    }

    /**
     * Creates a new named component bound to he given class paths.
     * If the given name is null, then an automatic name is assigned.
     * 
     * @param name - The (possibly null) name of the component.
     */
    public static LoadableComponent newComponent(String name, Iterable<Path> classPaths) {
        LoadableComponent component = newComponent(name);
        if (classPaths instanceof Path) {
        	// XXX Why ever did they make Path implement Iterable<Path>?
        	component.addClassPath((Path) classPaths);
        } else {
        	for (Path path : classPaths) {
        		component.addClassPath(path);
        	}
        }
        return component;
    }

}