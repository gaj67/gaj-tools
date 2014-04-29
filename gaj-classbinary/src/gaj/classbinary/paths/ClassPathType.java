/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.paths;

import java.io.File;

/**
 * Defines the basic types of class paths.
 */
/*package-private*/ enum ClassPathType {
    Directory, ClassFile, Archive;

    /**
     * Determines the class-path type of the given directory or file path.
     * 
     * @param classPath - The path instance.
     * @return The corresponding class-path type, or a value of null
     * if the type cannot be determined.
     */
    public static ClassPathType fromPath(File classPath) {
        if (classPath.isDirectory()) {
            return ClassPathType.Directory;
        }
        if (classPath.isFile()) {
            return fromPathString(classPath.getName());
        }
        return null;
    }

    private static final String[] VALID_ARCHIVE_ENDINGS = new String[] { "jar", "war", "zip" };

    /**
     * Determines the class-path type of the given file path.
     * 
     * @param classPath - The string-valued path to a file.
     * @return The corresponding class-path type, or a value of null
     * if the type cannot be determined.
     */
    public static ClassPathType fromPathString(String classPath) {
        int idx = classPath.lastIndexOf('.');
        if (idx >= 0) {
            String ending = classPath.substring(idx + 1);
            if (ending.equalsIgnoreCase("class")) {
                return ClassPathType.ClassFile;
            }
            for (String arcEnding : VALID_ARCHIVE_ENDINGS) {
                if (ending.equalsIgnoreCase(arcEnding)) {
                    return ClassPathType.Archive;
                }
            }
        }
        return null;
    }

}