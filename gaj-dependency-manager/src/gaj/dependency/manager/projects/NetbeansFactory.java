/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public ProjectProperties getProperties(File projectPath) throws IOException {
        File propertiesFile = new File(projectPath, NETBEANS_PROPERTY_FILE);
        if (!propertiesFile.exists()) {
            return null;
        }
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(propertiesFile)) {
            properties.load(is);
        }
        return new NetbeansProperties(projectPath, properties);
    }

    private static class NetbeansProperties implements ProjectProperties {
		private final File projectPath;
        private final List<File> srcPaths, prjPaths, libPaths;
        private final Properties properties;

        @Override
        public List<File> getSourcePaths() {
            return Collections.unmodifiableList(srcPaths);
        }

        @Override
        public List<File> getProjectPaths() {
            return Collections.unmodifiableList(prjPaths);
        }

        @Override
        public List<File> getLibraryPaths() {
            return Collections.unmodifiableList(libPaths);
        }

        private NetbeansProperties(File projectPath, Properties properties) throws IOException {
            this.projectPath = projectPath;
            this.properties = properties;
            prjPaths = new LinkedList<>();
            libPaths = new LinkedList<>();
            String classPaths = getRequiredProperty(NETBEANS_COMPILER_PATH_PROPERTY);
            for (String classPath : classPaths.split(":")) {
            	// XXX: Weak test for project dependencies.
            	if (classPath.startsWith(NETBEANS_REFERENCE_PROPERTY))
            		prjPaths.add(resolvePath(resolveReference(classPath)));
            	else
            		libPaths.add(resolvePath(resolveReference(classPath)));
            }
            srcPaths = new LinkedList<>();
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

        private File resolvePath(String localPath) {
            return new File(projectPath, localPath);
        }

    }

}
