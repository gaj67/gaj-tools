/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.descriptors;

public abstract class ClassNameSpace {

    private ClassNameSpace() {}

    /**
     * 
     * @param className - The fully-qualified name of the class.
     * @return The fully-qualified name of the package to which the class belongs.
     */
    public static String getPackageName(String className) {
        int index = className.lastIndexOf(".");
        return (index > 0) ? className.substring(0, index) : "";
    }

    /**
     * 
     * @param className - The fully-qualified name of the class.
     * @return The short name of the class.
     */
    public static String getSimpleName(String className) {
        int index = className.lastIndexOf(".");
        return (index < 0) ? className : className.substring(index + 1);
    }

    /**
     * 
     * @param className - The internal name of the class, corresponding to the name of its .class file.
     * @return A value of true (or false) if the class is (or is not) an inner class.
     */
    public static boolean isInnerClass(String className) {
        return className.contains("$");
    }

    /**
     * 
     * @param className - The internal name of the class, corresponding to the name of its .class file.
     * @return A value of true (or false) if the class is (or is not) an anonymous class.
     */
    public static boolean isAnonymousClass(final String className) {
        int idx = className.lastIndexOf('$') + 1;
        if (idx <= 0 || idx >= className.length()) {
            return false;
        }
        for (; idx < className.length(); idx++) {
            if (!Character.isDigit(className.charAt(idx))) {
                return false;
            }
        }
        return true;
    }

}