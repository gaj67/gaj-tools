/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.io;

import gaj.iterators.core.IterableIterator;
import gaj.iterators.core.IteratorFactory;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class FileIteratorFactory {

    /**
     * Creates a non-recursive iterator over the entries in a directory, where each returned entry is
     * either a file or a sub-directory instance.
     * 
     * @param dirPath - The path to a directory.
     */
    public static IterableIterator<File> newShallowDirectoryIterator(final File dirPath) {
        return new ResourceIterator<File>() {
            @Override
            protected Iterator<? extends File> openResource() {
                File[] listFiles = dirPath.listFiles();
                if (listFiles == null)
                    throw new UncheckedIOException("Non-directory path: " + dirPath);
                return IteratorFactory.newIterator(listFiles);
            }

            @Override
            protected void closeResource() {}
        };
    }

    /**
     * Creates a depth-first, recursive iterator over the files in a directory and its sub-directories.
     * Note that reordering might occur, such that any files in the directory will be processed before
     * any sub-directories.
     * 
     * @param dirPath - The path to a directory.
     */
    public static IterableIterator<File> newDeepDirectoryIterator(final File dirPath) {
        return new ResourceIterator<File>() {
            @Override
            protected Iterator<? extends File> openResource() {
                // Construct list of iterables from the directory entries.
                LinkedList<Iterable<? extends File>> iterables = new LinkedList<>();
                Collection<File> files = new LinkedList<>();
                File[] listFiles = dirPath.listFiles();
                if (listFiles == null)
                    throw failure("Non-directory path: " + dirPath);
                for (File elem : listFiles) {
                    if (elem.isDirectory()) {
                        iterables.add(newDeepDirectoryIterator(elem));
                    } else {
                        files.add(elem);
                    }
                }
                if (!files.isEmpty()) {
                    iterables.addFirst(files);
                }
                return IteratorFactory.newMultiIterator(iterables);
            }

            @Override
            protected void closeResource() { }
        };
    }

}
