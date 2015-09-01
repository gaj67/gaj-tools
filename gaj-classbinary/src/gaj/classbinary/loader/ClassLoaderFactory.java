/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.loader;

import gaj.classbinary.descriptors.ClassDescriptor;
import gaj.classbinary.parser.ClassParser;
import gaj.classbinary.parser.ParseException;
import gaj.classbinary.parser.ParserFactory;
import gaj.classbinary.paths.ManagerFactory;
import gaj.classbinary.paths.ClassPathManager;
import gaj.iterators.core.Filter;
import gaj.iterators.utilities.IteratorFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Encapsulates the handling of class paths. A class path is essentially a path
 * to either a single .class file, a directory of .class files, or an archive
 * (.jar, .war or .zip) of .class files.
 * 
 * <p/><b>Note:</b> The current implementation cannot handle loading an archive from
 * within another archive!
 */
public abstract class ClassLoaderFactory {

    private static final ClassParser parser = ParserFactory.newParser();
    private static final Filter<InputStream, ClassDescriptor> INPUT_STREAM_PARSER = 
        new Filter<InputStream, ClassDescriptor>() {
            @Override
            public ClassDescriptor filter(InputStream stream) {
                try {
                    return parser.parse(stream);
                } catch (ParseException e) {
                    try {
                        stream.close();
                    } catch (IOException e1) {
                        e.addSuppressed(e1);
                    }
                    throw new UncheckedParseException(e.getMessage(), e);
                }
            }
        };

    private ClassLoaderFactory() {}

    /**
     * Creates an empty class-path manager.
     * 
     * @return An empty class-path manager instance.
     */
    public static ClassBinaryLoader newClassLoader() {
        return new ClassBinaryLoader() {
            private final ClassPathManager manager = ManagerFactory.newClassPathManager();

            @Override
            public void addClassPath(File classPath) {
                manager.addClassPath(classPath);
            }

            @Override
            public Iterable<File> getClassPaths() {
                return manager.getClassPaths();
            }

            @Override
            public Iterable<ClassDescriptor> getClassDescriptors() {
                return IteratorFactory.newIterator(manager.getClassStreams(), ClassLoaderFactory.INPUT_STREAM_PARSER);
            }
        };
    }

    /**
     * Creates a class-path manager bound to the given class-path(s).
     * 
     * @return An initialised class-path manager instance.
     */
    public static ClassBinaryLoader newClassLoader(File... classPaths) {
        ClassBinaryLoader loader = newClassLoader();
        for (File classPath : classPaths) loader.addClassPath(classPath);
        return loader;
    }
}
