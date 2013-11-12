/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.descriptors;

import gaj.classbinary.flags.AccessFlags;
import java.util.Collection;

/**
 * Describes the various aspects of a class as determined from its .class file. 
 */
public interface ClassDescriptor {

    /**
     * 
     * @return The fully qualified name of the class.
     */
    public String getClassName();

    /**
     * 
     * @return The short name of the class, without the package name.
     */
    public String getSimpleName();

    /**
     * 
     * @return The fully qualified name of the package containing the class.
     */
    public String getPackageName();

    /**
     * Determines the type of class as described by the class modifiers.
     * 
     * @return The class type instance.
     */
    public ClassType getClassType();

    /**
     * Determines whether or not the descriptor represents an inner class.
     * 
     * @return A value of true (or false) if the class is (or is not) an inner class.
     */
    public boolean isInner();

    /**
     * Determines whether or not the descriptor represents an anonymous class.
     * 
     * @return A value of true (or false) if the class is (or is not) anonymous.
     */
    public boolean isAnonymous();

    /**
     * @see {@link AbstractAccessFlags}.
     * @return The access flags denoting the modifiers on this class.
     */
    public AccessFlags getAccessFlags();

    /**
     * 
     * @return The fully qualified name of the super class extended by this class, or a value of null if there
     * is no such super class.
     */
    public /*@Nullable*/ String getSuperClassName();

    /**
     * 
     * @return A non-null collection of the fully qualified names of all interfaces implemented by the class.
     */
    public Collection<String> getInterfaceClassNames();

    /**
     * 
     * @return A non-null collection of the fully qualified names of all classes imported into this class.
     */
    public Collection<String> getImportedClassNames();

    /**
     * Indicates the methods declared by this class, including any constructors or class initialiser.
     * 
     * @see {@link MethodDescriptor}
     * @return A non-null collection of method descriptors.
     */
    public Collection<MethodDescriptor> getMethods();

    /**
     * Indicates the fields declared by this class.
     * 
     * @see {@link FieldDescriptor}
     * @return A non-null collection of field descriptors.
     */
    public Collection<FieldDescriptor> getFields();

}