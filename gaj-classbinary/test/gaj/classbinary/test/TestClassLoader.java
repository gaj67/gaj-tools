/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.test;

import gaj.classbinary.descriptors.ClassDescriptor;
import gaj.classbinary.loader.ClassBinaryLoader;
import gaj.classbinary.loader.ClassLoaderFactory;
import java.io.File;
import java.io.IOException;

public abstract class TestClassLoader {

    private TestClassLoader() {}

    public static void main(String[] args) throws IOException {
        ClassBinaryLoader manager = ClassLoaderFactory.newClassLoader(new File("./bin"));
        for (ClassDescriptor desc : manager.getClassDescriptors()) {
            System.out.printf("* %s %s: %s%n", desc.getAccessFlags().getModifiers(), desc, desc.getMethods());
        }
    }

}
