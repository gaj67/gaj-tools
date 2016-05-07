/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.iterators.impl;

import gaj.iterators.core.ResourceIterator;
import gaj.iterators.core.StreamableIterator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class ResourceIterators {

	public static final boolean AUTO_CLOSE = true;

	/**
     * Provides an iterator over a stream. The stream will automatically be closed when the iterator is closed.
     *
	 * @param stream - A stream of items.
	 * 
     * @return An iterator bound to the given stream.
     */
    public static <T> ResourceIterator<T> newIterator(final Stream<? extends T> stream) {
        return new ResourceIterator<T>() {
        	@SuppressWarnings("unchecked")
			private Stream<T> _stream = (Stream<T>) stream;
        	private StreamableIterator<T> iter = null;

			private StreamableIterator<T> iterator() {
				if (iter != null) return iter;
				if (_stream != null) {
					iter = Iterators.toStreamableIterator(_stream.iterator());
					// Stream instance is now terminal and cannot be re-used.
					_stream = null;
					return iter;
				}
				throw new IllegalStateException("Have neither Stream nor StreambleIterator");
			}

			@Override
			public boolean hasNext() {
				return iterator().hasNext();
			}

			@Override
			public T next() {
				return iterator().next();
			}

			@Override
			public Stream<T> stream() {
				if (_stream != null) return _stream;
				_stream = iterator().stream();
				return _stream;
            }

			@Override
			public void close() {
				if (_stream != null && _stream != stream) {
					_stream.close();
				}
				_stream = null;
				iter = null;
				stream.close();
			}
        };
    }

	//=================================================================================================================================
	// Path-based iterators.
	
	@SafeVarargs
	public static ResourceIterator<InputStream> newArchiveInputStreamIterator(final Path archive, Predicate<? super ZipEntry>... filters) {
		return new BaseResourceIterator<InputStream>() {
			private ZipFile zipFile;

			@Override
			public Iterator<InputStream> openResource() throws IOException {
				zipFile = new ZipFile(archive.toFile());
				Stream<? extends ZipEntry> stream = zipFile.stream();
				if (filters.length > 0) {
					stream = stream.filter(StreamOps.newAndPredicate(filters));
				}
				return stream.filter(ze -> !ze.isDirectory()).map(ze -> getInputStream(ze)).iterator();
			}

			private InputStream getInputStream(ZipEntry ze) {
				try {
					return zipFile.getInputStream(ze);
				} catch (IOException e) {
					throw failure(e);
				}
			}

			@Override
			public void closeResource() throws IOException {
				zipFile.close();
				zipFile = null;
			}
		};
	}

	/**
	 * Constructs a resource-iterator to return the singleton input stream of a single file.
	 * 
	 * @param file - The path of the file.
	 */
	public static ResourceIterator<InputStream> newFileInputStreamIterator(final Path file) {
		return new BaseResourceIterator<InputStream>() {
			private InputStream stream;

			@Override
			public Iterator<InputStream> openResource()	throws IOException {
				stream = Files.newInputStream(file);
				return Iterators.newIterator(stream);
			}

			@Override
			public void closeResource() throws IOException {
				stream.close();
			}
		};
	}

	public static ResourceIterator<InputStream> newFileInputStreamIterator(Iterator<? extends Path> files) {
		return new FileInputStreamIterator(Iterators.toStreamableIterator(files).stream(), true);
	}

	public static ResourceIterator<InputStream> newFileInputStreamIterator(Path... files) {
		return new FileInputStreamIterator(Iterators.newIterator(files).stream(), true);
	}

	public static ResourceIterator<InputStream> newFileInputStreamIterator(Iterable<? extends Path> files) {
		return new FileInputStreamIterator(Iteratives.toIterative(files).stream(), true);
	}

	public static ResourceIterator<InputStream> newFileInputStreamIterator(Stream<? extends Path> files) {
		return new FileInputStreamIterator(files, false);
	}

	public static ResourceIterator<InputStream> newFileInputStreamIterator(Stream<? extends Path> files, boolean autoClose) {
		return new FileInputStreamIterator(files, autoClose);
	}

	public static ResourceIterator<InputStream> newFileInputStreamIterator(Iterator<? extends Path> files, Predicate<? super Path> isArchive, Predicate<? super ZipEntry> useEntry) {
		return new ResourceMultiIterator<InputStream>() {
			@Override
			protected /*@Nullable*/ Iterator<? extends InputStream> nextIterator() {
				if (!files.hasNext()) return null;
				Path file = files.next();
				return isArchive.test(file) ? newArchiveInputStreamIterator(file, useEntry) : newFileInputStreamIterator(file);
			}
		};
	}

	public static ResourceIterator<InputStream> newFileInputStreamIterator(Stream<? extends Path> files, boolean autoClose, Predicate<? super Path> isArchive, Predicate<? super ZipEntry> useEntry) {
		return new ResourceMultiIterator<InputStream>() {
			private Iterator<? extends Path> iterator = null;
			private boolean isClosed = false;

			@Override
			protected /*@Nullable*/ Iterator<? extends InputStream> nextIterator() {
				if (iterator == null) iterator = files.iterator();
				if (!iterator.hasNext()) {
					close();
					return null;
				}
				Path file = iterator.next();
				return isArchive.test(file) ? newArchiveInputStreamIterator(file, useEntry) : newFileInputStreamIterator(file);
			}

			@Override
			public void close() {
				if (!isClosed) {
					super.close();
					iterator = null;
					isClosed = true;
					if (autoClose) files.close();
				}
			}
		};
	}

}
