/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.flags;

import java.util.Collection;

/**
 * Encapsulates the set of access flags applicable to classes, methods and fields.
 */
/*package-private*/abstract class AbstractAccessFlags implements AccessFlags {

    protected final int accessFlags;

    /*package-private*/ AbstractAccessFlags(int accessFlags) {
        this.accessFlags = accessFlags;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isPublic()
     */
    @Override
    public boolean isPublic() {
        return (accessFlags & ACC_PUBLIC) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isPrivate()
     */
    @Override
    public boolean isPrivate() {
        return (accessFlags & ACC_PRIVATE) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isProtected()
     */
    @Override
    public boolean isProtected() {
        return (accessFlags & ACC_PROTECTED) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isStatic()
     */
    @Override
    public boolean isStatic() {
        return (accessFlags & ACC_STATIC) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isFinal()
     */
    @Override
    public boolean isFinal() {
        return (accessFlags & ACC_FINAL) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isSynchronized()
     */
    @Override
    public boolean isSynchronized() {
        return (accessFlags & ACC_SYNCHRONIZED) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isSuper()
     */
    @Override
    public boolean isSuper() {
        return (accessFlags & ACC_SUPER) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isVolatile()
     */
    @Override
    public boolean isVolatile() {
        return (accessFlags & ACC_VOLATILE) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isTransient()
     */
    @Override
    public boolean isTransient() {
        return (accessFlags & ACC_TRANSIENT) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isNative()
     */
    @Override
    public boolean isNative() {
        return (accessFlags & ACC_NATIVE) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isInterface()
     */
    @Override
    public boolean isInterface() {
        return (accessFlags & ACC_INTERFACE) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isAbstract()
     */
    @Override
    public boolean isAbstract() {
        return (accessFlags & ACC_ABSTRACT) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isStrict()
     */
    @Override
    public boolean isStrict() {
        return (accessFlags & ACC_STRICT) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isSynthetic()
     */
    @Override
    public boolean isSynthetic() {
        return (accessFlags & ACC_SYNTHETIC) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isAnnotation()
     */
    @Override
    public boolean isAnnotation() {
        return (accessFlags & ACC_ANNOTATION) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#isEnum()
     */
    @Override
    public boolean isEnum() {
        return (accessFlags & ACC_ENUM) != 0;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#getModifiers()
     */
    @Override
    public abstract Collection<String> getModifiers();

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#getBitMask()
     */
    @Override
    public int getBitMask() {
        return accessFlags;
    }

    /* (non-Javadoc)
     * @see gaj.classbinary.parser.flags.IAccessFlags#toHexString()
     */
    @Override
    public String toHexString() {
        return String.format("%x", accessFlags).toUpperCase();
    }

    @Override
    public String toString() {
        return String.format("[0x%s, 0d%s]", toHexString(), accessFlags);
    }

}
