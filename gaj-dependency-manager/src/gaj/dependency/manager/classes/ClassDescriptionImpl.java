/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.classes;

import gaj.classbinary.descriptors.ClassDescriptor;
import gaj.classbinary.descriptors.ClassType;
import gaj.classbinary.descriptors.FieldDescriptor;
import gaj.classbinary.descriptors.MethodDescriptor;
import gaj.classbinary.descriptors.MethodType;
import gaj.classbinary.flags.AccessFlags;
import java.util.Collection;

/**
 * Encapsulates some standardised behaviour expected of the descriptor of a class parsed from a .class file.
 */
/*package-level */ class ClassDescriptionImpl implements ClassDescription {

    private final String componentName;
    private final ClassDescriptor desc;
    private final AccessFlags accessFlags;
    private final boolean isInstantiable;

    /**
     * Binds the class descriptor to a particular component.
     * 
     * @param componentName - The name of the component responsible for the class.
     * @param desc - The class descriptor instance.
     */
    /*package-level */ ClassDescriptionImpl(String componentName, ClassDescriptor desc) {
        this.componentName = componentName;
        this.desc = desc;
        accessFlags = desc.getAccessFlags();
        boolean _isInstantiable = false;
        for (MethodDescriptor method : desc.getMethods()) {
            if (method.getMethodType() == MethodType.Constructor && method.getAccessFlags().isPublic()) {
                _isInstantiable = true;
                break;
            }
        }
        isInstantiable = _isInstantiable;
    }

    @Override
    public String getComponentName() {
        return componentName;
    }

    @Override
    public AccessFlags getAccessFlags() {
        return accessFlags;
    }

    @Override
    public Collection<MethodDescriptor> getMethods() {
        return desc.getMethods();
    }

    @Override
    public boolean isVisible() {
        return accessFlags.isPublic();
    }

    @Override
    public boolean isConcrete() {
        return !accessFlags.isAbstract();
    }

    @Override
    public boolean isInstantiable() {
        return isInstantiable;
    }

    @Override
    public boolean isInner() {
        return desc.isInner();
    }

    @Override
    public boolean isAnonymous() {
        return desc.isAnonymous();
    }

    @Override
    public String getClassDesignation() {
        return isExternal() 
                ? "?" 
                : (isVisible() ? "V" : "H")  + (isConcrete() ? "C" : "A") + (isInstantiable() ? "I" : "N"); 
    }

    @Override
    public ClassType getClassType() {
        return isExternal() ? ClassType.ExternalClass : ClassType.fromAccessFlags(accessFlags);
    }

    @Override
    public String toString() {
        return "{" + getClassType().toChar() + ':' + getClassDesignation() + '}' + getClassName();
    }

    @Override
    public String getClassName() {
        return desc.getClassName();
    }

    @Override
    public String getSimpleName() {
        return desc.getSimpleName();
    }

    @Override
    public String getPackageName() {
        return desc.getPackageName();
    }

    @Override
    public String getSuperClassName() {
        return desc.getSuperClassName();
    }

    @Override
    public Collection<String> getInterfaceClassNames() {
        return desc.getInterfaceClassNames();
    }

    @Override
    public Collection<String> getImportedClassNames() {
        return desc.getImportedClassNames();
    }

    @Override
    public boolean isExternal() {
        return false;
    }

    @Override
    public Collection<FieldDescriptor> getFields() {
        return desc.getFields();
    }

}
