/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.descriptors;

import gaj.classbinary.flags.AccessFlags;
import java.util.Collection;
import java.util.List;

/**
 * Describes the various aspects of a class method as determined from the .class file. 
 */
public interface MethodDescriptor {

	/**
	 * 
	 * @return The name of the method.
	 */
	public String getMethodName();

    /**
     * @see {@link AbstractAccessFlags}.
     * @return The access flags denoting the modifiers on the method.
     */
	public AccessFlags getAccessFlags();

	/**
	 * Specifies the type of the method. 
	 * 
	 * @return The class method type.
	 */
	public MethodType getMethodType();

    /**
     * 
     * @return The string representation of the method return type.
     */
    public String getReturnType();
    
    /**
     * 
     * @return A (possibly empty) list of the string representations of the method parameter types.
     */
    public List<String> getParameterTypes();

    /**
     * 
     * @return A (possibly empty) collection of the fully-qualified class names of all exceptions throwable by the method. 
     */
    public Collection<String> getExceptionTypes();

}
