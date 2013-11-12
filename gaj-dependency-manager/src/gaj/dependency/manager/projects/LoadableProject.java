/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import java.io.IOException;

public interface LoadableProject extends GroupProject {

    /**
     * Indicates if the project's source and libraries components have been loaded.
     * 
     * @return A value of true (or false) if the components have (or have not) both been loaded.
     */
    public boolean isLoaded();

    /**
     * Causes the project's source and libraries components to be loaded. Idempotent.
     * 
     * @return The project instance.
     * @throws IOException If any component cannot be loaded.
     */
    public GroupProject load() throws IOException;

}
