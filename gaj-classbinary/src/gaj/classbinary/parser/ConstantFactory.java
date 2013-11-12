/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.parser;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/*package-private*/ abstract class ConstantFactory {

    public static final int CONSTANT_UTF8 = 1;
    public static final int CONSTANT_UNICODE = 2;
    public static final int CONSTANT_INTEGER = 3;
    public static final int CONSTANT_FLOAT = 4;
    public static final int CONSTANT_LONG = 5;
    public static final int CONSTANT_DOUBLE = 6;
    public static final int CONSTANT_CLASS = 7;
    public static final int CONSTANT_STRING = 8;
    public static final int CONSTANT_FIELD = 9;
    public static final int CONSTANT_METHOD = 10;
    public static final int CONSTANT_INTERFACEMETHOD = 11;
    public static final int CONSTANT_NAMEANDTYPE = 12;

    public static ClassConstant newClassConstant(final byte tag, final int nameIndex, final int typeIndex) {
        return new ClassConstant() {
            @Override
            public byte getTag() {
                return tag;
            }

            @Override
            public int getNameIndex() {
                return nameIndex;
            }

            @Override
            public int getTypeIndex() {
                return typeIndex;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public String getValuesAsString() {
                return null;
            }

            @Override
            public boolean isDoubleEntry() {
                return tag == CONSTANT_DOUBLE || tag == CONSTANT_LONG;
            }
        };
    }

    public static ClassConstant newClassConstant(final byte tag, final Object value) {
        return new ClassConstant() {
            @Override
            public byte getTag() {
                return tag;
            }

            @Override
            public int getNameIndex() {
                return -1;
            }

            @Override
            public int getTypeIndex() {
                return -1;
            }

            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public String getValuesAsString() {
                return (tag == CONSTANT_UTF8) ? (String)value : null;
            }

            @Override
            public boolean isDoubleEntry() {
                return tag == CONSTANT_DOUBLE || tag == CONSTANT_LONG;
            }
        };
    }

    //=======================================================================================

    public static ClassConstants parseConstants(DataInputStream in) throws IOException {
        final int tableSize = in.readUnsignedShort();
        final ClassConstant[] constants = new ClassConstant[tableSize];
        // XXX: Deliberately leave zeroth element as null, since all indexing starts from 1.
        for (int i = 1; i < tableSize; i++) {
            if ((constants[i] = parseConstant(in)).isDoubleEntry()) {
                i++; // 8-byte constants use two entries, so skip one.
            }
        }
        return new ClassConstants() {
            @Override
            public ClassConstant getEntry(int index) {
                return (index < 0 || index >= constants.length) ? null : constants[index];
            }

            @Override
            public Collection<String> getClassNames() {
                Collection<String> classNames = new LinkedList<>();
                for (ClassConstant constant : constants) {
                    if (constant != null && constant.getTag() == CONSTANT_CLASS) {
                        String className = getDirectName(constant.getNameIndex());
                        if (!className.startsWith("[")) {
                            classNames.add(className);
                        }
                    }
                }
                return classNames;
            }
        };
    }

    private static ClassConstant parseConstant(DataInputStream in) throws IOException {
        byte tag = in.readByte();
        switch (tag) {
            case (CONSTANT_CLASS):
            case (CONSTANT_STRING):
                return newClassConstant(tag, in.readUnsignedShort(), -1);
            case (CONSTANT_FIELD):
            case (CONSTANT_METHOD):
            case (CONSTANT_INTERFACEMETHOD):
            case (CONSTANT_NAMEANDTYPE):
                return newClassConstant(tag, in.readUnsignedShort(), in.readUnsignedShort());
            case (CONSTANT_INTEGER):
                return newClassConstant(tag, new Integer(in.readInt()));
            case (CONSTANT_FLOAT):
                return newClassConstant(tag, new Float(in.readFloat()));
            case (CONSTANT_LONG):
                return newClassConstant(tag, new Long(in.readLong()));
            case (CONSTANT_DOUBLE):
                return newClassConstant(tag, new Double(in.readDouble()));
            case (CONSTANT_UTF8):
                return newClassConstant(tag, in.readUTF());
            default:
                throw new IOException("Unknown constant: " + tag);
        }
    }

}
