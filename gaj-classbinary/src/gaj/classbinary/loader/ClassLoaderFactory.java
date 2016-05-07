/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.loader;

import gaj.classbinary.descriptors.ClassDescriptor;
import gaj.classbinary.parser.ClassParser;
import gaj.classbinary.parser.ParseException;
import gaj.classbinary.parser.ParserFactory;
import gaj.classbinary.paths.ClassPathManager;
import gaj.classbinary.paths.ManagerFactory;
import gaj.iterators.core.Iterative;
import gaj.iterators.impl.Iteratives;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * Encapsulates the handling of class paths. A class path is essentially a path
 * to either a single .class file, a directory of .class files, or an archive
 * (.jar, .war or .zip) of .class files.
 * 
 * <p/><b>Note:</b> The current implementation cannot handle loading an archive from
 * within another archive!
 */
public abstract class ClassLoaderFactory {

	private static final ClassParser parser = ParserFactory.newParser();

	private ClassLoaderFactory() {}

	/**
	 * Creates an empty class-path manager.
	 * 
	 * @return An empty class-path manager instance.
	 */
	public static ClassBinaryLoader newClassLoader() {
		return new ClassBinaryLoader() {
			private final ClassPathManager manager = ManagerFactory.newClassPathManager();

			@Override
			public void addClassPath(Path classPath) {
				manager.addClassPath(classPath);
			}

			@Override
			public Iterative<Path> getClassPaths() {
				return manager.getClassPaths();
			}

			@Override
			public Iterative<ClassDescriptor> getClassDescriptors() {
				return Iteratives.newIterative(manager.getClassStreams().stream().map(is -> parse(is)));
			}
		};
	}

	private static ClassDescriptor parse(InputStream stream) {
		try {
			return parser.parse(stream);
		} catch (ParseException e) {
			throw new UncheckedParseException(e.getMessage(), e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}
	/**
	 * Creates a class-path manager bound to the given class-path(s).
	 * 
	 * @return An initialised class-path manager instance.
	 */
	public static ClassBinaryLoader newClassLoader(Path... classPaths) {
		ClassBinaryLoader loader = newClassLoader();
		for (Path classPath : classPaths) loader.addClassPath(classPath);
		return loader;
	}
}
