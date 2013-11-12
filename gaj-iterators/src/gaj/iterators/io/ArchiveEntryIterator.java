/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public abstract class ArchiveEntryIterator extends ResourceIterator<ZipEntry> {

    @Override
    protected abstract void closeResource();

    /**
     * Obtains the input stream for the current archive entry.
     *
     * @return An input stream for the entry. Do not close this stream!
     * @throws IOException - If the input stream cannot be obtained.
     */
    public abstract InputStream getInputStream() throws IOException;

}