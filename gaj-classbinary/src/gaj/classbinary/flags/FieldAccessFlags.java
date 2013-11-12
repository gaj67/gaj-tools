/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.flags;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Encapsulates the subset of access flags that are valid for fields.
 */
/*package-private*/ class FieldAccessFlags extends AbstractAccessFlags {

    private static final short[] FLAGS = new short[] {
        ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED,
        ACC_STATIC, ACC_FINAL, ACC_VOLATILE, ACC_TRANSIENT
    };

    private static final String[] MODIFIERS = new String[] {
        "public", "private", "protected",
        "static", "final", "volatile", "transient"
    };

    /*package-private*/ FieldAccessFlags(int bitMask) {
        super(bitMask);
    }

    @Override
    public Collection<String> getModifiers() {
        Collection<String> names = new ArrayList<String>();
        for (int b = 0; b < FLAGS.length; b++) {
            if ((accessFlags & FLAGS[b]) != 0) {
                names.add(MODIFIERS[b]);
            }
        }
        return names;
    }

}
