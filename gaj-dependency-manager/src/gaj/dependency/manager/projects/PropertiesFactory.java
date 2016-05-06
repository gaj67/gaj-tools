/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import java.io.IOException;
import java.nio.file.Path;

public interface PropertiesFactory {

    /**
     * 
     * @param projectPath - The path to the project directory.
     * @return A representation of the properties of the given project, or a value of null if the
     * type of project is not recognised by the factory.
     * @throws IOException If the project type is recognised by the factory but required properties
     * are missing or cannot be loaded.
     */
    /*@Nullable*/ ProjectProperties getProperties(Path projectPath) throws IOException;

}
