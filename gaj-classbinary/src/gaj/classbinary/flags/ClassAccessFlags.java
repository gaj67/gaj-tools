/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.flags;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Encapsulates the subset of access flags that are valid for classes.
 */
/*package-private*/ class ClassAccessFlags extends AbstractAccessFlags {

    private static final short[] FLAGS = new short[] {
        ACC_PUBLIC,
        ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, // XXX: For inner classes only.
        ACC_FINAL, ACC_SUPER,
        ACC_INTERFACE, ACC_ABSTRACT,
        ACC_SYNTHETIC, ACC_ANNOTATION, ACC_ENUM
    };

    private static final String[] MODIFIERS = new String[] {
        "public", "private", "protected", "static", "final",
        "super", "interface", "abstract",
        "synthetic", "@interface", "enum"
    };

    /*package-private*/ ClassAccessFlags(int bitMask) {
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
