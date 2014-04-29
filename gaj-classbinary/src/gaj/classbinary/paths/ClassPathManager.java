/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.paths;

import java.io.File;
import java.io.InputStream;

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
    public void addClassPath(File classPath);

    /**
     * 
     * @return An iterable over the class-paths being managed.
     */
    public Iterable<File> getClassPaths();

    /**
     * 
     * @return A iterable over the input streams of all .class files in all of the class paths
     * being managed.
     */
    public Iterable<InputStream> getClassStreams();

}