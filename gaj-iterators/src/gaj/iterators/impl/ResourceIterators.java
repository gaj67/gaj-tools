/*
 * (c) Geoff Jarrad, 2016.
 */
package gaj.iterators.impl;

import gaj.iterators.core.ResourceIterator;

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

}
