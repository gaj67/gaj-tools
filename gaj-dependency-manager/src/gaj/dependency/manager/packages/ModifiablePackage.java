/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.packages;

import gaj.dependency.manager.classes.ClassDescription;

public interface ModifiablePackage extends ClassPackage {

    /**
     * Adds the given class to the package.
     * 
     * @param desc - The descriptor of a loaded class.
     * @throws IllegalArgumentException If a class of the same (fully qualified) name
     * already exists in the package.
     */
    public void addClass(ClassDescription desc);

}
