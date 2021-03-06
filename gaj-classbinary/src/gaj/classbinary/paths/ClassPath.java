/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.paths;

import gaj.iterators.core.ResourceIterative;
import gaj.iterators.core.ResourceIterator;
import gaj.iterators.impl.Iteratives;
import gaj.iterators.impl.PathIterators;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;

/**
 * Encapsulates a single path to one or more .class files. 
 */
/*package-private*/ class ClassPath {

	private static final Predicate<Path> IS_ARCHIVE = path -> ClassPathType.Archive == ClassPathType.fromPath(path);
	private static final Predicate<ZipEntry> IS_CLASSFILE_ENTRY = ze -> ClassPathType.ClassFile == ClassPathType.fromPathString(ze.getName());
	private static final Predicate<Path> IS_CLASSFILE = path -> ClassPathType.ClassFile == ClassPathType.fromPath(path);
	private static final Predicate<Path> IS_CLASSFILE_OR_ARCHIVE = IS_CLASSFILE.or(IS_ARCHIVE);

	private final Path classPath;
	private final ClassPathType pathType;

	/*package-private*/ ClassPath(Path classPath) {
		this.classPath = classPath;
		pathType = ClassPathType.fromPath(classPath);
		if (ClassPathType.Unknown == pathType) {
			throw new IllegalArgumentException("Invalid class path: " + classPath);
		}
	}

	public ResourceIterative<InputStream> getClassStreams() {
		return Iteratives.newResourceIterative(this::_getClassStreams);
	}
	
	private ResourceIterator<InputStream> _getClassStreams() {
		switch (pathType) {
			case Archive:
				return PathIterators.newArchiveInputStreamIterator(classPath, IS_CLASSFILE_ENTRY);
			case ClassFile:
				return PathIterators.newFileInputStreamIterator(classPath);
			case Directory:
				try {
					return PathIterators.newFileInputStreamIterator(Files.walk(classPath).filter(IS_CLASSFILE_OR_ARCHIVE), IS_ARCHIVE, IS_CLASSFILE_ENTRY);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			default:
				throw new IllegalStateException();
		}
	}

	@Override
	public String toString() {
		return classPath.toString();
	}
	
}