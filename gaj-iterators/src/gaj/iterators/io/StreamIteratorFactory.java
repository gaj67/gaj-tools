/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.io;

import gaj.common.io.UncheckedIOException;
import gaj.iterators.core.IteratorFactory;
import gaj.iterators.core.ResourceIterator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class StreamIteratorFactory {

    /**
     * Constructs a resource-iterator to return the input stream of a single file.
     * <p/><b>Note:</b> If the iteration is run to completion, then the stream is automatically closed.
     * Otherwise, close() should be called explicitly if the iteration is halted abruptly.
     * <p/><b>Note:</b> Closing the stream explicitly with close() is also acceptable, but deprecated.
     * 
     * @param filePath - The path of the file.
     */
    public static ResourceIterator<InputStream> newFileStreamIterator(final File filePath) {
        return new ResourceIterator<InputStream>() {
            private /*@Nullable*/ InputStream stream = null;

            @Override
            protected Iterator<? extends InputStream> openResource() {
                try {
                    stream = new FileInputStream(filePath);
                    return IteratorFactory.newIterator(stream);
                } catch (FileNotFoundException e) {
                    throw UncheckedIOException.create(e);
                }
            }

            @Override
            protected void closeResource() {
                if (stream != null) {
                    try {
                        stream.close();
                        stream = null;
                    } catch (IOException e) {
                        throw UncheckedIOException.create(e);
                    }
                }
            }

        };
    }

    public static ArchiveEntryIterator newArchiveEntryIterator(final File archivePath) {
        return new ArchiveEntryIterator() {
            private /*@Nullable*/ ZipFile archive = null;
            private /*@Nullable*/ ZipEntry entry = null;

            @Override
            protected Iterator<? extends ZipEntry> openResource() {
                try {
                    archive = new ZipFile(archivePath);
                    return IteratorFactory.newIterator(archive.entries());
                } catch (IOException e) {
                    throw UncheckedIOException.create(e);
                }
            }

            @Override
            protected void closeResource() {
                if (archive != null) {
                    try {
                        entry = null;
                        archive.close();
                        archive = null;
                    } catch (IOException e) {
                        throw UncheckedIOException.create(e);
                    }
                }
            }

            @Override
            public ZipEntry next() {
                return entry = super.next();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                if (archive == null) {
                    throw new IOException("Archive is not open");
                }
                if (entry == null) {
                    throw new IOException("No current entry");
                }
                return archive.getInputStream(entry);
            }
        };
    }

}
