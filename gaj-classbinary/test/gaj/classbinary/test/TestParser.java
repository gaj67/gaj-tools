/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.test;

import gaj.classbinary.descriptors.ClassDescriptor;
import gaj.classbinary.descriptors.FieldDescriptor;
import gaj.classbinary.descriptors.MethodDescriptor;
import gaj.classbinary.flags.AccessFlags;
import gaj.classbinary.parser.ClassParser;
import gaj.classbinary.parser.ParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

public abstract class TestParser {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    private static @interface AnnotationForTesting {
        String value();
    }

    private static final String[] CLASSPATHS = new String[] {
    	"paths/ClassPath.class",
        "parser/ParserFactory.class",
        "descriptors/ClassDescriptor.class",
        "descriptors/ModifiableClassDescriptor.class",
        "descriptors/MethodType.class",
        "descriptors/ClassType.class",
        "flags/MethodAccessFlags.class",
        "$test/TestParser$AnnotationForTesting.class",
    };

    private TestParser() {}

    public static void main(String[] args) throws IOException {
        ClassParser parser = ParserFactory.newParser();
        String pkgName = parser.getClass().getPackage().getName();
        String pkgPath = pkgName.substring(0, pkgName.lastIndexOf('.')).replace('.', '/');
        File mainRoot = new File("./bin", pkgPath);
        File testRoot = new File("./test-bin", pkgPath);
        for (String classPath : CLASSPATHS) {
            System.out.println("-------------------------------------------");
            File classFile = classPath.startsWith("$") 
                    ? new File(testRoot, classPath.substring(1))
                    : new File(mainRoot, classPath);
            try (InputStream is = new FileInputStream(classFile)) {
                ClassDescriptor desc = parser.parse(is);
                System.out.printf("Class=%s%n", desc);
                AccessFlags flags = desc.getAccessFlags();
                System.out.printf("Modifiers=%s%n", flags.getModifiers());
                System.out.printf("Super-class=%s%n", desc.getSuperClassName());
                System.out.printf("Interfaces=%s%n", desc.getInterfaceClassNames());
                System.out.printf("Imported-classes=%s%n", desc.getImportedClassNames());
                summariseFields(desc.getFields());
                summariseMethods(desc.getMethods());
            }
        }
    }

    private static void summariseFields(Collection<FieldDescriptor> fields) {
        System.out.print("Fields: [");
        if (!fields.isEmpty()) {
            System.out.println();
            for (FieldDescriptor field : fields)
                System.out.printf(" + %s%n", field);
        }
        System.out.println("]");
    }

    private static void summariseMethods(Collection<MethodDescriptor> methods) {
        System.out.print("Methods: [");
        if (!methods.isEmpty()) {
            System.out.println();
            for (MethodDescriptor method : methods)
                System.out.printf(" + %s%n", method);
        }
        System.out.println("]");
    }

}