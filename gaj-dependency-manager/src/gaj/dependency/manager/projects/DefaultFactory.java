/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import java.io.File;
import java.io.IOException;
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
    public ProjectProperties getProperties(File projectPath) throws IOException {
        return new DefaultProperties(projectPath);
    }

    private static class DefaultProperties implements ProjectProperties {

        private final File projectPath;
        private final List<File> srcPaths, libPaths;

        /**
         * Loads and interprets the properties of an Eclipse project.
         * 
         * @param projectPath - The directory path of the project.
         * @return The standardised project properties, or a value of null if the
         * project is not an Eclipse project.
         * @throws IOException If valid properties cannot be loaded.
         */
        private DefaultProperties(File projectPath) throws IOException {
            this.projectPath = projectPath;
            srcPaths = new LinkedList<>();
            for (String srcDir : PROJECT_SOURCE_PATHS) {
                File spath = resolvePath(srcDir);
                if (spath.isDirectory()) {
                    srcPaths.add(spath);
                    break;
                }
            }
            if (srcPaths.isEmpty()) {
                throw new IOException("Cannot locate class-build path for project: " + projectPath);
            }
            libPaths = new LinkedList<>();
            for (String libDir : PROJECT_LIBRARY_PATHS) {
                File lpath = new File(projectPath, libDir);
                if (lpath.isDirectory()) {
                    libPaths.add(lpath);
                    break;
                }
            }
        }

        @Override
        public List<File> getSourcePaths() {
            return Collections.unmodifiableList(srcPaths);
        }

		@Override
		public List<File> getProjectPaths() {
			return Collections.emptyList();
		}

        @Override
        public List<File> getLibraryPaths() {
            return Collections.unmodifiableList(libPaths);
        }

        private File resolvePath(String localPath) {
            return new File(projectPath, localPath);
        }

    }

}
