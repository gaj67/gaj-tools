/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.flags;

import java.util.Collection;

public interface AccessFlags {

    public static final short ACC_PUBLIC = 0x0001;
    public static final short ACC_PRIVATE = 0x0002;
    public static final short ACC_PROTECTED = 0x0004;
    public static final short ACC_STATIC = 0x0008;
    public static final short ACC_FINAL = 0x0010;
    public static final short ACC_SYNCHRONIZED = 0x0020;
    public static final short ACC_SUPER = 0x0020;
    public static final short ACC_VOLATILE = 0x0040;
    public static final short ACC_TRANSIENT = 0x0080;
    public static final short ACC_NATIVE = 0x0100;
    public static final short ACC_INTERFACE = 0x0200;
    public static final short ACC_ABSTRACT = 0x0400;
    public static final short ACC_STRICT = 0x0800;
    public static final short ACC_SYNTHETIC = 0x1000;
    public static final short ACC_ANNOTATION = 0x2000;
    public static final short ACC_ENUM = 0x4000;

    public abstract boolean isPublic();

    public abstract boolean isPrivate();

    public abstract boolean isProtected();

    public abstract boolean isStatic();

    public abstract boolean isFinal();

    /**
     * For method access flags only. For classes, this bit corresponds to isSuper().
     */
    public abstract boolean isSynchronized();

    /**
     * For class access flags only. For methods, this bit corresponds to isSynchronized().
     */
    public abstract boolean isSuper();

    public abstract boolean isVolatile();

    public abstract boolean isTransient();

    public abstract boolean isNative();

    public abstract boolean isInterface();

    public abstract boolean isAbstract();

    public abstract boolean isStrict();

    public abstract boolean isSynthetic();

    public abstract boolean isAnnotation();

    public abstract boolean isEnum();

    public abstract Collection<String> getModifiers();

    public abstract int getBitMask();

    public abstract String toHexString();

}