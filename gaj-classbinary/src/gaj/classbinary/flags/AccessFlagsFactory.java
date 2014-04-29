/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.flags;

public abstract class AccessFlagsFactory {

    private AccessFlagsFactory() {}

    public static AccessFlags newClassAccessFlags(int bitMask) {
        return new ClassAccessFlags(bitMask);
    }

    public static AccessFlags newMethodAccessFlags(int bitMask) {
        return new MethodAccessFlags(bitMask);
    }

    public static AccessFlags newFieldAccessFlags(int bitMask) {
        return new FieldAccessFlags(bitMask);
    }

}
