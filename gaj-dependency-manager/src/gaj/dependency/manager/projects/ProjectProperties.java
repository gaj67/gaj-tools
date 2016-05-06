/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import java.nio.file.Path;
import java.util.List;

/**
 * Encapsulates the standardised properties of a project.
 */
public interface ProjectProperties {

	/**
	 * 
	 * @return The path to the project to which these properties apply.
	 */
	public Path getProjectPath();

    /**
     * 
     * @return A list of paths to project source directories.
     */
    public List<Path> getSourcePaths();

    /**
     * 
     * @return A list of paths to external project dependencies.
     */
    public List<Path> getExternalProjectPaths();

    /**
     * 
     * @return A list of paths to external library dependencies.
     */
    public List<Path> getLibraryPaths();

}
