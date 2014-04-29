/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.test;

import gaj.classbinary.descriptors.MethodDescriptor;
import gaj.classbinary.flags.AccessFlags;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.components.ClassesComponent;
import gaj.dependency.manager.components.ComponentFactory;
import gaj.dependency.manager.dependencies.PackageDependency;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.packages.ModifiablePackage;
import java.io.File;
import java.io.IOException;

public class TestComponent {

    private TestComponent() {}

    public static void main(String[] args) throws IOException {
        final String componentPath = "./bin";
        System.out.printf("Testing component for path \"%s\"%n", componentPath);
        ClassesComponent component = ComponentFactory.newComponent("test", new File(componentPath)).load();
        System.out.printf("component.getComponentName()==componentName is %s%n",
                component.getComponentName() == "test");
        System.out.printf("component.numPackages()=%d%n", component.numPackages());
        System.out.printf("component.numClasses()=%d%n", component.numClasses());

        String className = ModifiablePackage.class.getName();
        System.out.printf("component.hasClass(\"%s\")=%s%n", className, component.hasClass(className));
        ClassDescription desc = component.getClass(className);
        System.out.printf("class=component.getClass(\"%s\")=%s%n", className, desc);
        System.out.printf("component.hasClass(class)=%s%n", component.hasClass(desc));
        describeClass(desc);

        ClassPackage apackage = component.getPackage(desc);
        System.out.printf("package=component.getPackage(class)=%s%n", apackage);
        System.out.printf("package.hasClass(\"%s\")=%s%n", className, apackage.hasClass(className));
        System.out.printf("package.hasClass(class)=%s%n", apackage.hasClass(desc));
        System.out.printf("package.getClass(\"%s\")==class is %s%n", className, apackage.getClass(className) == desc);
        System.out.printf("package.getAfferents(class)=%s%n", apackage.getAfferents(desc, PackageDependency.Universal));
        System.out.printf("package.getEfferents(class)=%s%n", apackage.getEfferents(desc, PackageDependency.Universal));

        String className2 = className + "Impl";
        ClassDescription desc2 = component.getClass(className2);
        describeClass(desc2);
    }

    private static void describeClass(ClassDescription desc) {
        System.out.println("-----------------------------");
        System.out.printf("Class=%s%n", desc);
        AccessFlags flags = desc.getAccessFlags();
        System.out.printf("Modifiers=%s%n", flags.getModifiers());
        System.out.printf("Super-class=%s%n", desc.getSuperClassName());
        System.out.printf("Interfaces=%s%n", desc.getInterfaceClassNames());
        System.out.printf("Imported-classes=%s%n", desc.getImportedClassNames());
        System.out.print("Methods=[");
        for (MethodDescriptor method : desc.getMethods()) {
            System.out.printf("%n* %s", method); 
        }
        System.out.println("]");
        System.out.println("-----------------------------");
    }

}
