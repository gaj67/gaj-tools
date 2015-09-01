/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.io;

import gaj.iterators.core.Filter;
import gaj.iterators.core.ResourceIterator;
import gaj.iterators.utilities.IteratorFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;

/**
 * Defines a filtered iterator over input streams obtained from the archive entries.
 * <p/><b>Note:</b> These input streams <b>must not</b> be closed by the consumer!
 */
public abstract class ArchiveStreamIterator extends ResourceIterator<InputStream> {

    private final File path;
    private /*@Nullable*/ ArchiveEntryIterator subiterator = null;

    public ArchiveStreamIterator(File archivePath) {
        path = archivePath;
    }

    /**
     * 
     * @param entry - The current archive entry in the iteration.
     * @return A value of true (or false) if an input stream for the entry is (or is not) desired.
     */
    protected abstract boolean accept(ZipEntry entry);

    @Override
    protected Iterator<? extends InputStream> openResource() {
        subiterator = StreamIteratorFactory.newArchiveEntryIterator(path);
        return (Iterator<? extends InputStream>)IteratorFactory.newIterator(
                subiterator,
                new Filter<ZipEntry,InputStream>() {
                    @Override
                    public /*@Nullable*/ InputStream filter(ZipEntry entry) {
                        try {
                            return (subiterator != null && accept(entry)) ? subiterator.getInputStream() : null;
                        } catch (IOException e) {
                            throw failure(e);
                        }
                    }
                });
    }

    @Override
    protected void closeResource() {
        if (subiterator != null) {
            subiterator.close();
            subiterator = null;
        }
    }

}