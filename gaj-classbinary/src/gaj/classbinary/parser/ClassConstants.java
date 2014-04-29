/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.parser;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Encapsulates an indexed collection of ClassConstant instances.
 */
/*package-private*/ abstract class ClassConstants {

    public static final int JAVA_MAGIC = 0xCAFEBABE;

    /*package-private*/ ClassConstants() { }

    /**
     * 
     * @param index - The index of the desired class constant.
     * @return The indexed class constant instance.
     */
    public abstract ClassConstant getEntry(int index);

    public abstract Collection<String> getClassNames();

    public ClassConstant getNameEntry(int index) {
        ClassConstant entry = getEntry(index);
        return (entry == null) ? null : getEntry(entry.getNameIndex());
    }

    public ClassConstant getTypeEntry(int index) {
        ClassConstant entry = getEntry(index);
        return (entry == null) ? null : getEntry(entry.getTypeIndex());
    }

    public String getDirectName(int index) {
        ClassConstant entry = getEntry(index);
        return (entry == null) ? null : slashesToDots(entry.getValuesAsString());
    }

    public String getIndirectName(int index) {
        ClassConstant entry = getNameEntry(index);
        return (entry == null) ? null : slashesToDots(entry.getValuesAsString());
    }

    public Collection<String> getDirectTypes(int index) {
        ClassConstant entry = getEntry(index);
        return (entry == null) ? null : toTypeNames(entry.getValuesAsString());
    }

    private Collection<String> toTypeNames(String value) {
        Collection<String> typeNames = new LinkedList<>();
        for (String part : value.split("[;()]")) {
            if (part.startsWith("L")) {
                typeNames.add(slashesToDots(part.substring(1)));
            }
        }
        return typeNames;
    }

    private String slashesToDots(String qualifiedName) {
        return qualifiedName.replace('/', '.');
    }

}