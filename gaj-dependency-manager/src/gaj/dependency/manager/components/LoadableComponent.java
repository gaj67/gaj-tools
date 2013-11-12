/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.components;

import gaj.dependency.manager.classes.ClassDescription;
import java.io.File;
import java.io.IOException;

/**
 * Enables the component to have classes loaded from one or more class paths.
 */
public interface LoadableComponent extends ClassesComponent {

    //*********************************************************************************
    // Component initialisation.

    /**
     * Adds pre-loaded classes to the component.
     * 
     * @param classes - An iterable over pre-loaded classes.
     * @return The loadable component instance.
     */
    public LoadableComponent addClasses(Iterable<ClassDescription> classes);

    /**
     * Controls what response is generated when an attempt is made to
     * add a duplicate class to the component,
     * either directly via {@link #addClasses}() or indirectly via {@link #load}().
     * 
     * @param desc
     */
    public void warnDuplicateClass(ClassDescription desc);

    /**
     * Adds a class path to the component.
     * 
     * @param classPath - The path to a directory or .jar (or .zip or .war) file of byte-compiled classes.
     * @return The loadable component instance.
     */
    public LoadableComponent addClassPath(File classPath);

    /**
     * Adds one or more class paths to the component.
     * 
     * @param classPaths - An array or comma-separated list of class paths.
     * @return The loadable component instance.
     */
    public LoadableComponent addClassPaths(File... classPaths);

    /**
     * Adds one or more class paths to the component.
     * 
     * @param classPaths - An iterable over class paths.
     * @return The loadable component instance.
     */
    public LoadableComponent addClassPaths(Iterable<File> classPaths);

    /**
     * 
     * @return The collection of class paths managed by this component.
     */
    public Iterable<File> getClassPaths();

    /**
     * Causes all the class dependencies to be loaded from the component path(s). Idempotent.
     * 
     * @throws IOException If the classes cannot be loaded.
     * @return The loaded component instance.
     */
    public ClassesComponent load() throws IOException;

    /**
     * Indicates if the component classes have been loaded.
     * 
     * @return A value of true (or false) if the component classes have (or have not) been loaded.
     */
    public boolean isLoaded();

}