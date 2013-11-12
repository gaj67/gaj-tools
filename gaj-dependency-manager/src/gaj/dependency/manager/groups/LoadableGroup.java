/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.groups;

import gaj.dependency.manager.components.ClassesComponent;
import java.io.IOException;
import java.util.Collection;

/**
 * Encapsulates a group of components.
 * @see {@link ClassesComponent}.
 */
public interface LoadableGroup extends ComponentGroup {

    //*********************************************************************************
    // Group initialisation.
    /**
     * Adds a component to the group.
     * @param component - A uniquely-named component of classes.
     * @return The group instance.
     * @throws IllegalArgumentException If the component name is not unique.
     */
    public LoadableGroup addComponent(ClassesComponent component);

    /**
     * Adds one or more components to the group.
     * @param components - An array or comma-separated list of uniquely-named components.
     * @return The group instance.
     * @throws IllegalArgumentException If any component name is not unique.
     */
    public LoadableGroup addComponents(ClassesComponent... components);

    /**
     * Adds one or more components to the group.
     * @param components - A collection of uniquely-named components.
     * @return The group instance.
     * @throws IllegalArgumentException If any component name is not unique.
     */
    public LoadableGroup addComponents(Collection<ClassesComponent> components);

    /**
     * Indicates if the components have all been loaded.
     * 
     * @return A value of true (or false) if all of the components have (or have not) been loaded.
     */
    public boolean isLoaded();

    /**
     * Causes all of the components to be loaded. Idempotent.
     * 
     * @return The group instance.
     * @throws IOException If any component cannot be loaded.
     */
    public ComponentGroup load() throws IOException;

}
