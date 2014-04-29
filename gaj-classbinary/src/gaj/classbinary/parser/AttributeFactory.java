/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/*package-private*/ abstract class AttributeFactory {

    public static final String ATTRIBUTE_EXCEPTIONS = "Exceptions";

    private AttributeFactory() { }

    public static AttributeInfo parseAttribute(DataInputStream in, ClassConstants constants) throws IOException {
        final String attributeName = constants.getDirectName(in.readUnsignedShort());
        final int attributeLength = in.readInt();
        final byte[] data = new byte[attributeLength];
        for (int b = 0; b < attributeLength; b++) {
            data[b] = in.readByte();
        }
        if (attributeName.equals(ATTRIBUTE_EXCEPTIONS)) {
            try (DataInputStream _in = new DataInputStream(new ByteArrayInputStream(data))) {
                final int numExceptions = _in.readUnsignedShort();
                final String[] exceptionClassNames = new String[numExceptions];
                for (int i = 0; i < numExceptions; i++)
                    exceptionClassNames[i] = constants.getIndirectName(_in.readUnsignedShort());
                return new ExceptionsAttributeInfo() {
                    @Override
                    public String getAttributeType() {
                        return ATTRIBUTE_EXCEPTIONS;
                    }
                    
                    @Override
                    public byte[] getAttributeData() {
                        return data;
                    }
                    
                    @Override
                    public String[] getExceptions() {
                        return exceptionClassNames;
                    }
                };
            }
        } else {
            return new AttributeInfo() {
                @Override
                public String getAttributeType() {
                    return attributeName;
                }
                
                @Override
                public byte[] getAttributeData() {
                    return data;
                }
            };
        }
    }

    public static AttributeInfo[] parseAttributes(DataInputStream in, ClassConstants constants) throws IOException {
        final int numAttributes = in.readUnsignedShort();
        AttributeInfo[] attributes = new AttributeInfo[numAttributes];
        for (int i = 0; i < numAttributes; i++) {
            attributes[i] = parseAttribute(in, constants);
        }
        return attributes;
    }

}
