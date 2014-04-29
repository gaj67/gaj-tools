/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.parser;

import gaj.classbinary.descriptors.ClassDescriptor;
import gaj.classbinary.descriptors.DescriptorFactory;
import gaj.classbinary.descriptors.FieldDescriptor;
import gaj.classbinary.descriptors.MethodDescriptor;
import gaj.classbinary.descriptors.ModifiableClassDescriptor;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ParserFactory {

    private ParserFactory() {}

    /**
     * Creates a .class file parser bound to the named component.
     * 
     * @return A class parser instance.
     */
    public static ClassParser newParser() {
        return new ClassParser() {

            @Override
            public ClassDescriptor parse(InputStream classStream) throws ParseException {
                ModifiableClassDescriptor desc = DescriptorFactory.newModifiableClass();
                DataInputStream in = new DataInputStream(classStream);
                Set<String> importedClassNames = new HashSet<>();

                try {
                    if (in.readInt() != ClassConstants.JAVA_MAGIC) {
                        throw new ParseException("Invalid magic marker");
                    }
                    @SuppressWarnings("unused") int minorVersion = in.readUnsignedShort();
                    @SuppressWarnings("unused") int majorVersion = in.readUnsignedShort();
                    final ClassConstants constants = ConstantFactory.parseConstants(in);
                    importedClassNames.addAll(constants.getClassNames());
                    desc.setAccessFlags(in.readUnsignedShort());
                    desc.setClassName(parseClassName(in, constants));
                    String superClassName = parseClassName(in, constants);
                    desc.setSuperClassName(superClassName);
                    importedClassNames.add(superClassName);
                    Collection<String> interfaceClassNames = parseInterfaces(in, constants);
                    desc.setInterfaceClassNames(interfaceClassNames);
                    importedClassNames.addAll(interfaceClassNames);
                    Collection<FieldDescriptor> fields = parseFields(in, constants);
                    desc.setFields(fields);
                    for (FieldDescriptor field : fields) {
                        addClassName(importedClassNames, field.getFieldType());
                    }
                    Collection<MethodDescriptor> methods = parseMethods(in, constants);
                    desc.setMethods(methods);
                    for (MethodDescriptor method : methods) {
                        addClassName(importedClassNames, method.getReturnType());
                        for (String paramType : method.getParameterTypes())
                            addClassName(importedClassNames, paramType);
                        importedClassNames.addAll(method.getExceptionTypes());
                    }
                    @SuppressWarnings("unused")
                    AttributeInfo[] classAttributes = AttributeFactory.parseAttributes(in, constants);
                } catch (ParseException e) {
                    throw e;
                } catch (IOException e) {
                    throw new ParseException(e.getMessage(), e);
                }

                desc.setImportedClassNames(importedClassNames);
                return desc;
            }

        };
    }

    //================================================================================
    // Private parsing methods.

    private static String parseClassName(DataInputStream in, ClassConstants constants) throws IOException {
        return constants.getIndirectName(in.readUnsignedShort());
    }

    private static Collection<String> parseInterfaces(DataInputStream in, ClassConstants constants) throws IOException {
        Collection<String> interfaceClassNames = new LinkedList<>();
        int numInterfaces = in.readUnsignedShort();
        for (int i = 0; i < numInterfaces; i++) {
            interfaceClassNames.add(parseClassName(in, constants));
        }
        return interfaceClassNames;
    }

    private static Collection<FieldDescriptor> parseFields(DataInputStream in, ClassConstants constants) throws IOException {
        List<FieldDescriptor> fields = new LinkedList<>();
        final int numFields = in.readUnsignedShort();
        for (int i = 0; i < numFields; i++) {
            int accessFlags = in.readUnsignedShort();
            String name = constants.getDirectName(in.readUnsignedShort());
            String descriptor = constants.getDirectName(in.readUnsignedShort());
            final int numAttributes = in.readUnsignedShort();
            for (int a = 0; a < numAttributes; a++) {
                @SuppressWarnings("unused")
                AttributeInfo attribute = AttributeFactory.parseAttribute(in, constants);
            }
            LinkedList<String> types = parseDescriptorTypes(descriptor);
            String fieldType = types.removeLast();
            fields.add(DescriptorFactory.newFieldDescriptor(name, accessFlags, fieldType));
        }
        return fields;
    }

    private static Collection<MethodDescriptor> parseMethods(DataInputStream in, ClassConstants constants) throws IOException {
        List<MethodDescriptor> methods = new LinkedList<>();
        final int numEntries = in.readUnsignedShort();
        for (int i = 0; i < numEntries; i++) {
            int accessFlags = in.readUnsignedShort();
            String name = constants.getDirectName(in.readUnsignedShort());
            String descriptor = constants.getDirectName(in.readUnsignedShort());
            final int numAttributes = in.readUnsignedShort();
            String[] exceptions = null;
            for (int a = 0; a < numAttributes; a++) {
                AttributeInfo attribute = AttributeFactory.parseAttribute(in, constants);
                if (attribute instanceof ExceptionsAttributeInfo)
                    exceptions = ((ExceptionsAttributeInfo)attribute).getExceptions();
            }
            LinkedList<String> types = parseDescriptorTypes(descriptor);
            String returnType = types.removeLast();
            methods.add(DescriptorFactory.newMethodDescriptor(name, accessFlags, returnType, types, exceptions));
        }
        return methods;
    }

    private static LinkedList<String> parseDescriptorTypes(String descriptor) throws ParseException {
        LinkedList<String> types = new LinkedList<>();
        final int len = descriptor.length();
        int i = 0;
        while (i < len) {
            char c = descriptor.charAt(i++);
            String type = getPrimitiveType(c);
            if (type != null) { // Primitive.
                types.add(type);
            } else {
                switch (c) {
                    case 'L': // Object.
                        type = getObjectType(descriptor, i);
                        types.add(type);
                        i += type.length() + 1;
                        break;
                    case '[': // Array.
                        int count = 1;
                        while (i < len && (c = descriptor.charAt(i++)) == '[') {
                            count++;
                        }
                        if (c == '[') throw new ParseException("Missing end to array type");
                        if (c == 'L') { // Object array.
                            type = getObjectType(descriptor, i);
                            i += type.length() + 1;
                        } else {
                            type = getPrimitiveType(c);
                            if (type == null) throw new ParseException("Unknown descriptor type: " + c);
                        }
                        StringBuilder buf = new StringBuilder(type);
                        while (count-- > 0) buf.append("[]");
                        types.add(buf.toString());
                        break;
                    case '(': // Start of parameters.
                    case ')': // End of parameters.
                        break; // Ignore.
                    default:
                        throw new ParseException("Unknown descriptor type: " + c);
                }
            }
        }
        return types;
    }

    private static String getPrimitiveType(char c) {
        switch (c) {
            case 'B': return "byte";
            case 'C': return "char";
            case 'D': return "double";
            case 'F': return "float";
            case 'I': return "int";
            case 'J': return "long";
            case 'S': return "short";
            case 'Z': return "boolean";
            case 'V': return "void";
            default: return null;
        }        
    }

    private static String getObjectType(String descriptor, int pos) throws ParseException {
        int idx = descriptor.indexOf(';', pos);
        if (idx < 0) throw new ParseException("Missing end to object type");
        return descriptor.substring(pos, idx);
    }

    // Filters out primitive types [with hack for default package classes, assuming class names are initial capitalised].
    private static void addClassName(Set<String> importedClassNames, String type) {
        if (Character.isUpperCase(type.charAt(0)) || type.indexOf('.') > 0) { // Object or object array.
            int idx = type.indexOf('[');
            importedClassNames.add((idx < 0) ? type : type.substring(0, idx));
        }
    }

}