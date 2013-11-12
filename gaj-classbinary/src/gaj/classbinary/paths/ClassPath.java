/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.paths;

import gaj.iterators.io.ArchiveStreamIterator;
import gaj.iterators.io.DirectoryMultiStreamIterator;
import gaj.iterators.io.StreamIteratorFactory;
import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Encapsulates a single path to one or more .class files. 
 */
/*package-private*/ class ClassPath {
    private final File classPath;
    private final ClassPathType pathType;

    /*package-private*/ ClassPath(File classPath) {
        this.classPath = classPath;
        pathType = ClassPathType.fromPath(classPath);
        if (pathType == null) {
            throw new IllegalArgumentException("Invalid class path: " + classPath);
        }
    }

    public Iterable<InputStream> getClassStreams() {
        switch (pathType) {
            case Archive:
                return new ArchiveStreamIterator(classPath) {
                    @Override
                    protected boolean accept(ZipEntry entry) {
                        // XXX: Currently can't handle nested archive files.
                        return ClassPathType.ClassFile == ClassPathType.fromPathString(entry.getName());
                    }
                };
            case ClassFile:
                return StreamIteratorFactory.newFileStreamIterator(classPath);
            case Directory:
                return new DirectoryMultiStreamIterator(classPath) {
                    @Override
                    protected boolean acceptFile(File file) {
                        return ClassPathType.ClassFile == ClassPathType.fromPath(file);
                    }

                    @Override
                    protected boolean acceptArchive(File file) {
                        return ClassPathType.Archive == ClassPathType.fromPath(file);
                    }

                    @Override
                    protected boolean acceptEntry(ZipEntry entry) {
                        // XXX: Currently can't handle nested archive files.
                        return ClassPathType.ClassFile == ClassPathType.fromPathString(entry.getName());
                    }
                };
            default:
                throw new IllegalStateException();
        }
    }

}