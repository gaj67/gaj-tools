/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.test;

import gaj.classbinary.flags.AccessFlags;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.components.ClassesComponent;
import gaj.dependency.manager.components.ComponentFactory;
import gaj.dependency.manager.groups.ComponentGroup;
import gaj.dependency.manager.groups.GroupFactory;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.packages.PackageFactory;
import java.io.File;
import java.io.IOException;

public class TestGroup {

    private TestGroup() {}

    public static void main(String[] args) throws IOException {
        final String componentName = "pkg";
        File root = new File("./bin/gaj/dependency/manager");
        ClassesComponent component = ComponentFactory.newComponent(componentName, new File(root, "packages"));
        ClassesComponent component2 = ComponentFactory.newComponent("cmp", new File(root, "components"));
        ComponentGroup group = GroupFactory.newGroup(component, component2).load();
        System.out.printf("group.numComponents()=%d%n", group.numComponents());
        System.out.printf("group.numPackages()=%d%n", group.numPackages());
        System.out.printf("group.numClasses()=%d%n", group.numClasses());

        System.out.printf("group.hasComponent(\"%s\")=%s%n", componentName, group.hasComponent(componentName));
        System.out.printf("group.getComponent(\"%s\")==component is %s%n",
                componentName, group.getComponent(componentName) == component);

        final Class<?> klass = PackageFactory.class;
        final String className = klass.getName();
        System.out.printf("group.hasClass(\"%s\")=%s%n", className, group.hasClass(className));
        ClassDescription desc = group.getClass(className);
        System.out.printf("class=group.getClass(\"%s\")=%s%n", className, desc);
        System.out.printf("group.hasClass(class)=%s%n", group.hasClass(desc));
        System.out.printf("Class=%s%n", desc.getClassName());
        AccessFlags flags = desc.getAccessFlags();
        System.out.printf("Modifiers=%s%n", flags.getModifiers());
        System.out.printf("Super-class=%s%n", desc.getSuperClassName());
        System.out.printf("Interfaces=%s%n", desc.getInterfaceClassNames());
        System.out.printf("Imported-classes=%s%n", desc.getImportedClassNames());

        final String packageName = klass.getPackage().getName();
        System.out.printf("group.hasPackage(\"%s\")=%s%n", packageName, group.hasPackage(packageName));
        ClassPackage apackage = group.getPackage(desc);
        System.out.printf("package=group.getPackage(class)=%s%n", apackage);
        System.out.printf("group.hasPackage(package)=%s%n", group.hasPackage(apackage));
        System.out.printf("package.hasClass(\"%s\")=%s%n", className, apackage.hasClass(className));
        System.out.printf("package.hasClass(class)=%s%n", apackage.hasClass(desc));
        System.out.printf("package.getClass(\"%s\")==class is %s%n", className, apackage.getClass(className) == desc);

        System.out.printf("group.getComponent(class)==component is %s%n",
                group.getComponent(desc) == component);
        System.out.printf("component.getPackage(class)==apackage is %s%n",
                component.getPackage(desc) == apackage);
    }

}
