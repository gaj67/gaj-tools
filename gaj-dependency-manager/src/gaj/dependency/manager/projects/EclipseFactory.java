/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.projects;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Encapsulates the handling of Eclipse projects.
 */
/*package-private*/ class EclipseFactory implements PropertiesFactory {

    private static final String ELCIPSE_CLASSPATHS_FILE = ".classpath";
    private static final String ELCIPSE_CLASSPATH_TAG = "classpathentry";
    private static final String ECLIPSE_CLASSPATH_PATH_ATTR = "path";                   // Required.
    private static final String ECLIPSE_CLASSPATH_OUTPUT_ATTR = "output";               // Optional, indicates source.
    private static final String ECLIPSE_CLASSPATH_KIND_ATTR = "kind";                   // Required.
    private static final String ECLIPSE_CLASSPATH_SOURCE_KIND = "src";
    private static final String ECLIPSE_CLASSPATH_LIBRARY_KIND = "lib";
    private static final String ECLIPSE_CLASSPATH_OUTPUT_KIND = "output";

    @Override
    public ProjectProperties getProperties(File projectPath) throws IOException {
        File classpathFile = new File(projectPath, ELCIPSE_CLASSPATHS_FILE);
        if (!classpathFile.exists()) {
            return null;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return new EclipseProperties(projectPath, builder.parse(classpathFile));
        } catch (SAXException | ParserConfigurationException e) {
            throw new IOException("Bad class-path configuration for Eclipse project: " + projectPath, e);
        }
    }

    private static String getAttribute(NamedNodeMap attrs, String attrName) {
        Node attr = attrs.getNamedItem(attrName);
        return (attr == null) ? null : attr.getNodeValue();
    }

    private class EclipseProperties implements ProjectProperties {

        private final File projectPath;
        private final List<File> srcPaths, prjPaths, libPaths;

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

        private EclipseProperties(File projectPath, Document properties) throws IOException {
            this.projectPath = projectPath;
            srcPaths = new LinkedList<>();
            prjPaths = new LinkedList<>();
            libPaths = new LinkedList<>();
            Set<File> _srcPaths = new HashSet<>();
            File defSrcPath = null;
            NodeList classpaths = properties.getElementsByTagName(ELCIPSE_CLASSPATH_TAG);
            final int len = classpaths.getLength();
            for (int i = 0; i < len; i++) {
                Node classpath = classpaths.item(i);
                NamedNodeMap attrs = classpath.getAttributes();
                switch (getRequiredAttribute(attrs, ECLIPSE_CLASSPATH_KIND_ATTR)) {
                    case ECLIPSE_CLASSPATH_SOURCE_KIND:
                        String refPath = getRequiredAttribute(attrs, ECLIPSE_CLASSPATH_PATH_ATTR);
                        if (refPath.startsWith("/")) {
                            // Project path reference.
                            prjPaths.addAll(getProperties(resolvePath(refPath)).getSourcePaths());
                        } else {
                            // Source path/class path reference.
                            String outAttr = getAttribute(attrs, ECLIPSE_CLASSPATH_OUTPUT_ATTR);
                            File outPath = (outAttr == null) ? defSrcPath : resolvePath(outAttr);
                            if (_srcPaths.add(outPath)) {
                                srcPaths.add(outPath);
                            }
                        }
                        break;
                    case ECLIPSE_CLASSPATH_LIBRARY_KIND:
                        libPaths.add(resolvePath(getRequiredAttribute(attrs, ECLIPSE_CLASSPATH_PATH_ATTR)));
                        break;
                    case ECLIPSE_CLASSPATH_OUTPUT_KIND:
                        File prevDefSrcPath = defSrcPath;
                        defSrcPath = resolvePath(getRequiredAttribute(attrs, ECLIPSE_CLASSPATH_PATH_ATTR));
                        if (prevDefSrcPath == null && _srcPaths.contains(null)) {
                            // Correct any default source output reference.
                            _srcPaths.remove(null);
                            if (_srcPaths.add(defSrcPath)) {
                                srcPaths.set(srcPaths.indexOf(null), defSrcPath);
                            }
                        }
                        break;
                    default:
                        // Ignore unknown classpath kind.
                        break;
                }
            }
        }

        private File resolvePath(String localPath) {
            // XXX: Assume relative paths of the form "local-path" or "/project/local-path".
            return new File(projectPath, localPath.startsWith("/") ? (".." + localPath) : localPath);
        }

        private String getRequiredAttribute(NamedNodeMap attrs, String attrName) throws IOException {
            String attr = getAttribute(attrs, attrName);
            if (attr == null) {
                throw new IOException("Missing required attribute: \"" + attrName + "\" for Eclipse project: " + projectPath);
            }
            return attr;
        }

    }

}
