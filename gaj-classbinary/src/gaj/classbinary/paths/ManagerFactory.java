/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.paths;

import gaj.iterators.core.Filter;
import gaj.iterators.core.IteratorFactory;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates the handling of class paths. A class path is essentially a path
 * to either a single .class file, a directory of .class files, or an archive
 * (.jar, .war or .zip) of .class files.
 * 
 * <p/><b>Note:</b> The current implementation cannot handle loading an archive from
 * within another archive!
 */
public abstract class ManagerFactory {

    private static final Filter<ClassPath, Iterable<? extends InputStream>> INPUT_STREAM_FILTER = 
            new Filter<ClassPath, Iterable<? extends InputStream>>() {
        @Override
        public Iterable<InputStream> filter(ClassPath classPath) {
            return classPath.getClassStreams();
        }
    };

    private ManagerFactory() {}

    /**
     * Creates an empty class-path manager.
     * 
     * @return An empty class-path manager instance.
     */
    public static ClassPathManager newClassPathManager() {
        return new ClassPathManager() {
            private final Map<File, ClassPath> classPaths = new HashMap<>();

            @Override
            public void addClassPath(File classPath) {
                classPaths.put(classPath, new ClassPath(classPath)); // Ignore duplicates.
            }

            @Override
            public Iterable<File> getClassPaths() {
                return Collections.unmodifiableSet(classPaths.keySet());
            }

            @Override
            public Iterable<InputStream> getClassStreams() {
                return IteratorFactory.newMultiIterator(classPaths.values(), ManagerFactory.INPUT_STREAM_FILTER);
            }
        };
    }

    /**
     * Creates a class-path manager bound to the given class-path(s).
     * 
     * @return An initialised class-path manager instance.
     */
    public static ClassPathManager newClassPathManager(File... classPaths) {
        ClassPathManager manager = newClassPathManager();
        for (File classPath : classPaths) manager.addClassPath(classPath);
        return manager;
    }
}
