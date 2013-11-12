/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import gaj.dependency.manager.components.ClassesComponent;
import gaj.dependency.manager.groups.ComponentGroup;

/**
 * Encapsulates the idea that a project consists of source code and third-party libraries.
 *
 */
public interface GroupProject {

    /**
     * 
     * @return The name of the project.
     */
    String getName();

    /**
     * 
     * @return The component bound to the project's compiled source code.
     */
    ClassesComponent getSourceComponent();

    /**
     * 
     * @return The component bound to the project's required, external projects.
     */
    ClassesComponent getProjectsComponent();

    /**
     * 
     * @return The component bound to the project's required, external libraries.
     */
    ClassesComponent getLibrariesComponent();

    /**
     * 
     * @return A group instance bound to the project's source and libraries components.
     */
    ComponentGroup getGroup();

}
