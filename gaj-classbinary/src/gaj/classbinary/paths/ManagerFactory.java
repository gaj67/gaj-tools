/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.paths;

import gaj.iterators.core.Iterative;
import gaj.iterators.core.ResourceIterative;
import gaj.iterators.impl.Iteratives;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Encapsulates the handling of class paths. A class path is essentially a path
 * to either a single .class file, a directory of .class files, or an archive
 * (.jar, .war or .zip) of .class files.
 * 
 * <p/><b>Note:</b> The current implementation cannot handle loading an archive from
 * within another archive!
 */
public abstract class ManagerFactory {

	private ManagerFactory() {}

	/**
	 * Creates an empty class-path manager.
	 * 
	 * @return An empty class-path manager instance.
	 */
	public static ClassPathManager newClassPathManager() {
		return new ClassPathManager() {
			private final Map<Path, ClassPath> classPaths = new HashMap<>();

			@Override
			public void addClassPath(Path classPath) {
				classPaths.put(classPath, new ClassPath(classPath)); // Ignore duplicates.
			}

			@Override
			public Iterative<Path> getClassPaths() {
				return Iteratives.toIterative(Collections.unmodifiableSet(classPaths.keySet()));
			}

			@Override
			public ResourceIterative<InputStream> getClassStreams() {
				return Iteratives.newStreamIterative(this::getInputStreams);
			}

			private Stream<InputStream> getInputStreams() {
				return classPaths.values().stream().flatMap(cp -> cp.getClassStreams().stream());
			}			
		};
	}

	/**
	 * Creates a class-path manager bound to the given class-path(s).
	 * 
	 * @return An initialised class-path manager instance.
	 */
	public static ClassPathManager newClassPathManager(Path... classPaths) {
		ClassPathManager manager = newClassPathManager();
		for (Path classPath : classPaths) manager.addClassPath(classPath);
		return manager;
	}

	/**
	 * Creates a class-path manager bound to the given class-path(s).
	 * 
	 * @return An initialised class-path manager instance.
	 */
	public static ClassPathManager newClassPathManager(Iterable<? extends Path> classPaths) {
		ClassPathManager manager = newClassPathManager();
		for (Path classPath : classPaths) manager.addClassPath(classPath);
		return manager;
	}

}
