/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import java.io.File;
import java.util.List;

/**
 * Encapsulates the standardised properties of a project.
 */
public interface ProjectProperties {

    /**
     * 
     * @return A list of paths to project source directories.
     */
    public List<File> getSourcePaths();

    /**
     * 
     * @return A list of paths to external project dependencies.
     */
    public List<File> getProjectPaths();

    /**
     * 
     * @return A list of paths to external library dependencies.
     */
    public List<File> getLibraryPaths();

}
