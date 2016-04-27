/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.iterators.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Provides an input stream iterator over a sequence of file entries.
 * Each file entry is expected to refer to a single, physical, non-archived file.
 */
/*package-private*/ class FileInputStreamIterator extends BaseResourceIterator<InputStream> {

	private final Stream<? extends Path> fileStream;
	private final boolean autoClose;
	/** Keeps track of the currently open file.*/
    private InputStream inputStream = null;

    /**
     * Constructs an input stream iterator over all given file entries.
     * <p/><b>Note:</b> Each stream is automatically closed when the next stream is returned.
     * 
     * @param files - a stream of file entries.
     * @param autoClose - indicates whether (true) or not (false) to automatically close the given path stream.
     */
    /*package-private*/ FileInputStreamIterator(Stream<? extends Path> files, boolean autoClose) {
        this.fileStream = files;
		this.autoClose = autoClose;
    }

    @Override
	public Iterator<InputStream> openResource() {
        return fileStream.map(f -> getInputStream(f)).iterator();
    }

    private InputStream getInputStream(Path file) {
        try {
            closeStream(); // Close previous stream, if any.
			return inputStream = Files.newInputStream(file);
		} catch (IOException e) {
			throw failure(e);
		}
    }

    private void closeStream() throws IOException {
        if (inputStream != null) {
            inputStream.close();
            inputStream = null;
        }
    }

    @Override
	public void closeResource() throws IOException {
        // Handle any dangling, open stream.
        closeStream();
        if (autoClose) fileStream.close();
    }

}
