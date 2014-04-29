/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.io;

import gaj.iterators.core.Filter;
import gaj.iterators.core.IteratorFactory;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;

/**
 * Constructs an iterable-iterator over the input streams of all files recursively
 * found in the directory, even those files contained in archive files.
 * <p/>The filtering of desirable or undesirable files occurs via the two abstract methods:
 * {@link #acceptArchive}(); and {@link #acceptFile}(). The filtering of archive entries
 * occurs via the abstract method {@link #acceptEntry}().
 * <p/><b>Note:</b> Each stream is automatically closed when the next stream is returned.
 * However, close() should be called explicitly if the iteration is halted abruptly.
 * <p/><b>Note:</b> Closing a stream explicitly with close() is also acceptable, but deprecated.
 */
public abstract class DirectoryMultiStreamIterator extends ResourceIterator<InputStream> {

    private final File path;
    private /*@Nullable*/ ResourceIterator<InputStream> subiterator = null; // Track the currently stream iterator.

    /**
     * Constructs an iterable-iterator over the input streams of all files in the directory.
     * 
     * @see {@link DirectoryMultiStreamIterator} for more details.
     * @param dirPath - The path of the directory.
     */
    public DirectoryMultiStreamIterator(File dirPath) {
        path = dirPath;
    }

    /**
     * Controls whether or not an input stream is generated for a non-archive file.
     * 
     * @param file - The current file path in the iteration.
     * @return A value of true (or false) if the file is (or is not) a desired non-archive file.
     */
    protected abstract boolean acceptFile(File file);

    /**
     * Controls whether or not an input stream is generated for an archive file.
     * 
     * @param file - The current file path in the iteration.
     * @return A value of true (or false) if the file is (or is not) a desired archive.
     */
    protected abstract boolean acceptArchive(File file);

    /**
     * Controls whether or not an input stream is generated for an archive file entry.
     * 
     * @param entry - The current archive entry in the iteration.
     * @return A value of true (or false) if an input stream for the entry is (or is not) desired.
     */
    protected abstract boolean acceptEntry(ZipEntry entry);

    @Override
    protected Iterator<? extends InputStream> openResource() {
        return (Iterator<? extends InputStream>)IteratorFactory.newMultiIterator(
                FileIteratorFactory.newDeepDirectoryIterator(path),
                new Filter<File,Iterable<? extends InputStream>>() {
                    @Override
                    public /*@Nullable*/ Iterable<InputStream> filter(File file) {
                        close(); // Handle any dangling, open stream.
                        if (acceptArchive(file)) {
                            subiterator = new ArchiveStreamIterator(file) {
                                @Override
                                protected boolean accept(ZipEntry entry) {
                                    return acceptEntry(entry);
                                }
                            };
                        } else if (acceptFile(file)) {
                            subiterator = StreamIteratorFactory.newFileStreamIterator(file);
                        }
                        return subiterator;
                    }
                });
    }

    @Override
    protected void closeResource() {
        // Handle any dangling, open stream.
        if (subiterator != null) {
            subiterator.close();
            subiterator = null;
        }
    }

}
