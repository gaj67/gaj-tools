/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.classes;

import gaj.classbinary.descriptors.ClassDescriptor;
import gaj.classbinary.descriptors.DescriptorFactory;
import gaj.classbinary.descriptors.ModifiableClassDescriptor;
import gaj.classbinary.flags.AccessFlags;
import java.util.Collection;
import java.util.Iterator;

public abstract class ClassDescriptionFactory {

    private ClassDescriptionFactory() {}

    public static void filterByPackageName(Collection<ClassDescription> classes, String packageName, boolean keepSamePackage) {
        for (Iterator<ClassDescription> iter = classes.iterator(); iter.hasNext();) {
            if (packageName.equals(iter.next().getPackageName()) != keepSamePackage) {
                iter.remove();
            }
        }
    }

    public static void filterByComponentName(Collection<ClassDescription> classes, String componentName, boolean keepSameComponent) {
        for (Iterator<ClassDescription> iter = classes.iterator(); iter.hasNext();) {
            if (componentName.equals(iter.next().getComponentName()) != keepSameComponent) {
                iter.remove();
            }
        }
    }

    public static void filterByExternality(Collection<ClassDescription> classes, boolean keepExternal) {
        for (Iterator<ClassDescription> iter = classes.iterator(); iter.hasNext();) {
            if (iter.next().isExternal() != keepExternal) {
                iter.remove();
            }
        }
    }

    /**
     *  Creates a dummy class description for an 'unknown' class, bound to the named component.
     *  <br/>Note: Since it is not known if an unknown class is abstract or concrete,
     *  all such classes may be treated with least consequence as being abstract.
     * 
     * @param componentName - The name of the component managing the descriptor.
     * @param className - The fully qualified name of the class.
     * @return A minimal class description object marked as being 'external'.
     */
    public static ClassDescription newExternalClass(final String componentName, final String className) {
        ModifiableClassDescriptor desc = DescriptorFactory.newModifiableClass();
        desc.setClassName(className);
        desc.setAccessFlags(AccessFlags.ACC_PUBLIC); // XXX: Guess least harmful option.
        return new ClassDescriptionImpl(componentName, desc) {
            @Override
            public boolean isExternal() {
                return true;
            }
        };
    }

    /**
     *  Creates a class description, bound to the named component.
     * 
     * @param componentName - The name of the component managing the descriptor.
     * @param desc - The class descriptor instance.
     * @return A class description object marked as being 'internal'.
     */
    public static ClassDescription newInternalClass(final String componentName, ClassDescriptor desc) {
        return new ClassDescriptionImpl(componentName, desc);
    }

}
