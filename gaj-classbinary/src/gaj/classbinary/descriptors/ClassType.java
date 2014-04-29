/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.descriptors;

import gaj.classbinary.flags.AccessFlags;

public enum ClassType {

    /**
     * Describes an ordinary concrete class.
     */
    Class("class"),
    /**
     * Describes an abstract class.
     */
    AbstractClass("abstract class"),
    /**
     * Describes an interface.
     */
    Interface("interface"),
    /**
     * Describes an annotation.
     */
    Annotation("@interface"),
    /**
     * Describes an enumerated type.
     */
    Enumeration("enum"),
    /**
     * Describes an external class with unknown type.
     */
    ExternalClass('X', "EXTERNAL");

    private final String strRep;
    private final char chrRep;

    private ClassType(String strRep) {
        this.strRep = strRep;
        chrRep = Character.toUpperCase(strRep.charAt(0));
    }

    private ClassType(char chrRep, String strRep) {
        this.chrRep = chrRep;
        this.strRep = strRep;
    }

    /**
     * 
     * @return A character that summarises the class type.
     */
    public char toChar() {
        return chrRep;
    }

    /**
     * @return A string that summarises the class type.
     */
    @Override
    public String toString() {
        return strRep;
    }

    public static ClassType fromAccessFlags(AccessFlags accessFlags) {
        if (accessFlags == null) return ExternalClass;
        if (accessFlags.isEnum()) return Enumeration;
        if (accessFlags.isAnnotation()) return Annotation;
        if (accessFlags.isInterface()) return Interface; // XXX: Must be tested after Annotation.
        if (accessFlags.isAbstract()) return AbstractClass; // XXX: Must be tested after Annotation and Interface.
        return Class;
    }

}
