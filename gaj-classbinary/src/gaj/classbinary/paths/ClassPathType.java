/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.paths;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Defines the basic types of class paths.
 */
/*package-private*/ enum ClassPathType {
    Directory, ClassFile, Archive, Unknown;

    /**
     * Determines the class-path type of the given directory or file path.
     * 
     * @param classPath - The path instance.
     * @return The corresponding class-path type, or a value of Unknown
     * if the type cannot be determined.
     */
    public static ClassPathType fromPath(Path classPath) {
        return Files.isDirectory(classPath) ? ClassPathType.Directory 
        		: Files.isRegularFile(classPath) ? fromPathString(classPath.toFile().getName()) : ClassPathType.Unknown;
    }

    private static final String[] VALID_ARCHIVE_ENDINGS = new String[] { "jar", "war", "zip" };

    /**
     * Determines the class-path type of the given file path.
     * 
     * @param fileName - The string-valued path to a file.
     * @return The corresponding class-path type, or a value of Unknown
     * if the type cannot be determined.
     */
    public static ClassPathType fromPathString(String fileName) {
        int idx = fileName.lastIndexOf('.');
        if (idx >= 0) {
            String ending = fileName.substring(idx + 1);
            if (ending.equalsIgnoreCase("class")) {
                return ClassPathType.ClassFile;
            }
            for (String arcEnding : VALID_ARCHIVE_ENDINGS) {
                if (ending.equalsIgnoreCase(arcEnding)) {
                    return ClassPathType.Archive;
                }
            }
        }
        return ClassPathType.Unknown;
    }

}