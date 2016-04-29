/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.paths;

import gaj.iterators.core.ResourceIterator;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Encapsulates the handling of one or more class-paths, including the ability to iterate over the
 * input streams of all .class files.
 */
public interface ClassPathManager {

    /**
     * Adds the given class-path to the manager.
     * 
     * @param classPath - The path to a .class file or archive, or a directory of .class files.
     */
    public void addClassPath(Path classPath);

    /**
     * 
     * @return An iterable over the class-paths being managed.
     */
    public Iterable<Path> getClassPaths();

    /**
     * Provides an iterator over the class file input streams. This iterator should be closed after use.
     * 
     * @return A iterator over the input streams of all .class files in all of the class paths
     * being managed.
     */
    public ResourceIterator<InputStream> getClassStreams();

}