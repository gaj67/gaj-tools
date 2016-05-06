/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates the handling of unrecognised projects.
 */
/*package-private*/ class DefaultFactory implements PropertiesFactory {

    private static final String[] PROJECT_SOURCE_PATHS = new String[] { "bin", "build" };
    private static final String[] PROJECT_LIBRARY_PATHS = new String[] { "lib", "dist/lib" };

    @Override
    public ProjectProperties getProperties(Path projectPath) throws IOException {
        return new DefaultProperties(projectPath);
    }

    private static class DefaultProperties implements ProjectProperties {

        private final Path projectPath;
        private final List<Path> srcPaths;
        private final List<Path> libPaths;

        /**
         * Loads and interprets the properties of an Eclipse project.
         * 
         * @param projectPath - The directory path of the project.
         * @return The standardised project properties, or a value of null if the
         * project is not an Eclipse project.
         * @throws IOException If valid properties cannot be loaded.
         */
        private DefaultProperties(Path projectPath) throws IOException {
            this.projectPath = projectPath;
            srcPaths = new LinkedList<>();
            for (String srcDir : PROJECT_SOURCE_PATHS) {
                Path spath = projectPath.resolve(srcDir);
                if (Files.isDirectory(spath)) {
                    srcPaths.add(spath);
                    break;
                }
            }
            if (srcPaths.isEmpty()) {
                throw new IOException("Cannot locate class-build path for project: " + projectPath);
            }
            libPaths = new LinkedList<>();
            for (String libDir : PROJECT_LIBRARY_PATHS) {
                Path lpath = projectPath.resolve(libDir);
                if (Files.isDirectory(lpath)) {
                    libPaths.add(lpath);
                    break;
                }
            }
        }

        @Override
        public List<Path> getSourcePaths() {
            return Collections.unmodifiableList(srcPaths);
        }

		@Override
		public List<Path> getExternalProjectPaths() {
			return Collections.emptyList();
		}

        @Override
        public List<Path> getLibraryPaths() {
            return Collections.unmodifiableList(libPaths);
        }

		@Override
		public Path getProjectPath() {
			return projectPath;
		}

    }

}
