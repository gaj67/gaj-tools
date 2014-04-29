/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.io;

import gaj.iterators.core.Filter;
import gaj.iterators.core.IteratorFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Constructs an iterable-iterator over the input streams of all files recursively found in the directory.
 * The filtering of desirable or undesirable files occurs via the abstract {@link #accept}() method.
 * <p/><b>Note:</b> Each stream is automatically closed when the next stream is returned.
 * However, close() should be called explicitly if the iteration is halted abruptly.
 * <p/><b>Note:</b> Closing a stream explicitly with close() is also acceptable, but deprecated.
 */
public abstract class DirectoryStreamIterator extends ResourceIterator<InputStream> {

    private final File path;
    private /*@Nullable*/ InputStream stream = null; // Keeps track of the currently open stream.

    /**
     * Constructs an iterable-iterator over the input streams of all files in the directory.
     * 
     * @see {@link DirectoryStreamIterator} for more details.
     * @param dirPath - The path of the directory.
     */
    public DirectoryStreamIterator(File dirPath) {
        path = dirPath;
    }

    /**
     * Controls whether or not an input stream is generated for a file.
     * 
     * @param file - The current file path in the iteration.
     * @return A value of true (or false) if an input stream for the file is (or is not) desired.
     */
    protected abstract boolean accept(File file);

    @Override
    protected Iterator<? extends InputStream> openResource() {
        return (Iterator<? extends InputStream>)IteratorFactory.newIterator(
                FileIteratorFactory.newDeepDirectoryIterator(path),
                new Filter<File,InputStream>() {
                    @Override
                    public /*@Nullable*/ InputStream filter(File file) {
                        try {
                            return accept(file) ? getInputStream(file) : null;
                        } catch (IOException e) {
                            throw failure(e);
                        }
                    }
                });
    }

    private InputStream getInputStream(File file) throws IOException {
        closeStream(); // Close previous stream, if any.
        return stream = new FileInputStream(file);
    }

    private void closeStream() throws IOException {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    @Override
    protected void closeResource() {
        // Handle any dangling, open stream.
        try {
            closeStream();
        } catch (IOException e) {
            throw failure(e);
        }
    }

}
