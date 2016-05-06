/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Encapsulates the handling of Netbeans projects.
 */
/*package-private*/ class NetbeansFactory implements PropertiesFactory {


    private static final String NETBEANS_PROPERTY_FILE = "nbproject/project.properties";
    private static final String NETBEANS_COMPILER_PATH_PROPERTY = "javac.classpath"; // Required.
    private static final String NETBEANS_BUILD_PATH_PROPERTY = "build.dir";          // Required.
    private static final String NETBEANS_REFERENCE_PROPERTY = "${reference.";	     // Indicates external project?

    @Override
    public /*@Nullable*/ ProjectProperties getProperties(Path projectPath) throws IOException {
        Path propertiesFile = projectPath.resolve(NETBEANS_PROPERTY_FILE);
        if (!Files.exists(propertiesFile)) {
            return null;
        }
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(propertiesFile.toFile())) {
            properties.load(is);
        }
        return new NetbeansProperties(projectPath, properties);
    }

    private static class NetbeansProperties implements ProjectProperties {
		private final Path projectPath;
        private final List<Path> srcPaths = new LinkedList<>();
        private final List<Path> prjPaths = new LinkedList<>();
        private final List<Path> libPaths = new LinkedList<>();
        private final Properties properties;

        @Override
        public List<Path> getSourcePaths() {
            return Collections.unmodifiableList(srcPaths);
        }

        @Override
        public List<Path> getExternalProjectPaths() {
            return Collections.unmodifiableList(prjPaths);
        }

        @Override
        public List<Path> getLibraryPaths() {
            return Collections.unmodifiableList(libPaths);
        }

        private NetbeansProperties(Path projectPath, Properties properties) throws IOException {
            this.projectPath = projectPath;
            this.properties = properties;
            String classPaths = getRequiredProperty(NETBEANS_COMPILER_PATH_PROPERTY);
            for (String classPath : classPaths.split(":")) {
            	// XXX: Weak test for project dependencies.
            	if (classPath.startsWith(NETBEANS_REFERENCE_PROPERTY))
            		prjPaths.add(resolvePath(resolveReference(classPath)));
            	else
            		libPaths.add(resolvePath(resolveReference(classPath)));
            }
            srcPaths.add(resolvePath(getRequiredProperty(NETBEANS_BUILD_PATH_PROPERTY)));
        }

        private String getRequiredProperty(String propertyName) throws IOException {
            String classPaths = properties.getProperty(propertyName);
            if (classPaths == null) {
                throw new IOException("Cannot locate property \"" + propertyName + "\" in Netbeans project: " + projectPath);
            }
            return classPaths;
        }

        private String resolveReference(String reference) throws IOException {
            StringBuilder buf = new StringBuilder(reference);
            while (true) {
                int start = buf.lastIndexOf("${");
                if (start < 0) {
                    return buf.toString();
                }
                int end = buf.indexOf("}", start);
                if (end < 0) {
                    throw new IOException("Badly formed reference \"" + reference + "\" in Netbeans project: " + projectPath);
                }
                String subReference = buf.substring(start+2, end);
                String subReferent = properties.getProperty(subReference);
                if (subReferent == null) {
                    throw new IOException("Unresolved reference \"" + subReference + "\" in Netbeans project: " + projectPath);
                }
                buf.replace(start, end+1, subReferent);
            }
        }

        private Path resolvePath(String localPath) {
            return projectPath.resolve(localPath);
        }

		@Override
		public Path getProjectPath() {
			return projectPath;
		}

    }

}
