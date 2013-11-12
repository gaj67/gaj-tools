/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.descriptors;

import java.util.Collection;

public interface ModifiableClassDescriptor extends ClassDescriptor {

    /**
     * Sets the name of the class, along with its short name and package name.
     * 
     * @param className - The fully qualified name of the class.
     */
    public void setClassName(String className);

    /**
     * Sets the names of the interfaces implemented by the class.
     * 
     * @param interfaceNames - A collection of the fully qualified names of all interfaces implemented by the class.
     */
    public void setInterfaceClassNames(Collection<String> classNames);

    /**
     * Sets the name of the super class extended by this class.
     * 
     * @param The (possibly null) fully qualified name of the super class extended by this class.
     */
    public void setSuperClassName(String className);

    /**
     * Sets the names of the classes imported by this class.
     * 
     * @param classNames - A collection of the fully qualified names of all classes imported by this class.
     */
    public void setImportedClassNames(Collection<String> classNames);

    /**
     * Sets the access flags denoting the modifiers present on the class.
     * 
     * @see {@link AbstractAccessFlags.AccessFlags}.
     * @param accessFlags - The bit mask of access flags denoting the modifiers on this class.
     */
    public void setAccessFlags(int accessFlags);

    /**
     * Sets the methods declared on the class.
     * 
     * @param methods - A collection of method descriptors.
     */
    public void setMethods(Collection<MethodDescriptor> methods);

    /**
     * Sets the fields declared on the class.
     * 
     * @param fields - A collection of field descriptors.
     */
    public void setFields(Collection<FieldDescriptor> fields);

}
