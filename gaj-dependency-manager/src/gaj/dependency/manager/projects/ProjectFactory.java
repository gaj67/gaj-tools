/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import gaj.dependency.manager.components.ClassesComponent;
import gaj.dependency.manager.components.ComponentFactory;
import gaj.dependency.manager.components.LoadableComponent;
import gaj.dependency.manager.groups.ComponentGroup;
import gaj.dependency.manager.groups.GroupFactory;
import gaj.dependency.manager.groups.LoadableGroup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;


public abstract class ProjectFactory {

    private static final String SOURCE_COMPONENT_NAME = "src";
    private static final String PROJECTS_COMPONENT_NAME = "prj";
    private static final String LIBRARIES_COMPONENT_NAME = "lib";

    private ProjectFactory() {}

    /**
     * Creates a group project instance from which the classes and dependencies may be loaded.
     * 
     * @param name - The name of the project.
     * @param sourcePaths - The collection of paths to compiled classes that form part of the project.
     * @param projectPaths - The collection of paths to compiled classes that are part of external projects.
     * @param libraryPaths - The collection of paths to external classes and class libraries.
     * @return A loadable group project instance.
     */
    public static LoadableProject newProject(
            final String name,
            Collection<Path> sourcePaths,
            Collection<Path> projectPaths,
            Collection<Path> libraryPaths) {
        final LoadableComponent srcComponent = ComponentFactory.newComponent(SOURCE_COMPONENT_NAME, sourcePaths);
        final LoadableComponent prjComponent = ComponentFactory.newComponent(PROJECTS_COMPONENT_NAME, projectPaths);
        final LoadableComponent libComponent = ComponentFactory.newComponent(LIBRARIES_COMPONENT_NAME, libraryPaths);
        final LoadableGroup group = GroupFactory.newGroup(srcComponent, prjComponent, libComponent);
        return new LoadableProject() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public ClassesComponent getSourceComponent() {
                return srcComponent;
            }

			@Override
			public ClassesComponent getProjectsComponent() {
				return prjComponent;
			}

			@Override
            public ClassesComponent getLibrariesComponent() {
                return libComponent;
            }

            @Override
            public ComponentGroup getGroup() {
                return group;
            }

            @Override
            public GroupProject load() throws IOException {
                group.load();
                return this;
            }

            @Override
            public boolean isLoaded() {
                return group.isLoaded();
            }

        };
    }

    private static final PropertiesFactory[] PROJECT_FACTORIES = new PropertiesFactory[] {
        new NetbeansFactory(), new EclipseFactory(), new DefaultFactory()
    };

    /**
     * Creates a group project instance from which the classes and dependencies may be loaded.
     * 
     * @param projectPath - The path to the main project directory.
     * @param fromBuild - A flag indicating whether (true) or not (false) the project compilation
     * has been driven by a build script.
     * @return A loadable group project instance, or a value of null if the project type is unrecognised.
     * @throws IOException If any required project properties are missing or invalid.
     */
    public static LoadableProject newProject(Path projectPath, boolean fromBuild) throws IOException {
        Path path = projectPath.toRealPath();
        for (PropertiesFactory factory : PROJECT_FACTORIES) {
            ProjectProperties properties = factory.getProperties(path);
            if (properties == null) {
                continue; // Project is unrecognised by the factory.
            }
            try {
                return getProject(path, properties);
            } catch (MissingProjectDependencyException e) {
                if (fromBuild) {
                    throw new IOException(e.getMessage(), e);
                }
                // Fall through to next factory as a back-off.
            }
        }
        return null;
    }

    private static LoadableProject getProject(Path projectPath, ProjectProperties properties) {
        List<Path> srcPaths = properties.getSourcePaths();
		for (Path path : srcPaths) {
            if (!Files.exists(path)) {
                throw new MissingProjectDependencyException("Missing source: " + path.toString());
            }
        }
		List<Path> prjPaths = properties.getExternalProjectPaths();
        for (Path path : prjPaths) {
            if (!Files.exists(path)) {
                throw new MissingProjectDependencyException("Missing project: " + path.toString());
            }
        }
        List<Path> libPaths = properties.getLibraryPaths();
		for (Path path : libPaths) {
            if (!Files.exists(path)) {
                throw new MissingProjectDependencyException("Missing library: " + path.toString());
            }
        }
        return newProject(projectPath.toFile().getName(), srcPaths, prjPaths, libPaths);
    }

    @SuppressWarnings({ "serial" })
    private static class MissingProjectDependencyException extends RuntimeException {

        public MissingProjectDependencyException(String message) {
            super(message);
        }
        
    }
}