/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.classes;

import gaj.classbinary.descriptors.ClassDescriptor;


/**
 * Describes the various aspects of a class as determined from its .class file. 
 */
public interface ClassDescription extends ClassDescriptor {

    /**
     * Determines whether the descriptor represents an 'external' class.
     * 
     * @return A value of true (or false) if the class is (or is not) considered to be external.
     */
    public boolean isExternal();

    /**
     * 
     * @return The name of the component into which this class was loaded.
     */
    public String getComponentName();

    /**
     * Determines whether or not the class is visible outside of its defining package.
     * 
     * @return A value of true (or false) if the class is (or is not) considered to be 'visible'.
     */
    public boolean isVisible();

    /**
     * Determines whether or not the class is instantiable outside of its defining package.
     * 
     * @return A value of true (or false) if the class is (or is not) considered to be 'instantiable'.
     */
    public boolean isInstantiable();

    /**
     * Determines whether or not the class is 'concrete' by definition.
     * Essentially, a class X is considered to be 'concrete' if it could 
     * (under suitable conditions of visibility) be instantiated directly via <tt><em>X x = new X()</em></tt>, 
     * and is considered to be 'abstract' if it cannot.
     * Thus, actual abstract classes, interfaces, annotations and enumerated classes are all 'abstract'.
     * 
     * @return A value of true (or false) if the class is (or is not) considered to be 'concrete'.
     */
    public boolean isConcrete();

    /**
     * Provides a short summary of the class in terms of the dimensions of: visibility (V for visible or H for hidden);
     * concreteness (C for concrete or A for abstract); and instantiability (I for instantiable or N for
     * non-instantiable). However, note that an external class, about which little is known, should simply be designated
     * as "?".
     * 
     * @return The class designation string.
     */
    public String getClassDesignation();

}