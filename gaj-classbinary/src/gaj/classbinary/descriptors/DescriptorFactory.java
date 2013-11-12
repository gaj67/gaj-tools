/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.descriptors;

import gaj.classbinary.flags.AccessFlags;
import gaj.classbinary.flags.AccessFlagsFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class DescriptorFactory {

    private DescriptorFactory() {}

    /**
     *  Creates an empty but modifiable class descriptor.
     * 
     * @param className - The fully qualified name of the class.
     * @return A modifiable class descriptor object.
     */
    public static ModifiableClassDescriptor newModifiableClass() {
        return new ModifiableClassDescriptor() {
            private final List<MethodDescriptor> methods = new LinkedList<>();
            private final List<FieldDescriptor> fields = new LinkedList<>();
            private final Set<String> importClassNames = new HashSet<>();
            private final List<String> interfaceNames = new LinkedList<>();
            private AccessFlags accessFlags = AccessFlagsFactory.newClassAccessFlags(0);
            private String superClassName = null;
            private String className = "", simpleName = "", packageName = "";
            private boolean isInnerClass = false, isAnonymousClass = false;

            @Override
            public String getClassName() {
                return className;
            }

            @Override
            public void setClassName(String className) {
                this.className = className;
                importClassNames.remove(className); // Prevent cycles.
                int index = className.lastIndexOf(".");
                simpleName = (index < 0) ? className : className.substring(index + 1);
                packageName = (index < 0) ? "" : className.substring(0, index);
                isInnerClass = ClassNameSpace.isInnerClass(simpleName);
                isAnonymousClass = ClassNameSpace.isAnonymousClass(simpleName);
            }

            @Override
            public String getSimpleName() {
                return simpleName;
            }

            @Override
            public String getPackageName() {
                return packageName;
            }

            @Override
            public String getSuperClassName() {
                return superClassName;
            }

            @Override
            public void setSuperClassName(String className) {
                superClassName = className;
            }

            @Override
            public Collection<String> getInterfaceClassNames() {
                return Collections.unmodifiableList((interfaceNames));
            }

            @Override
            public void setInterfaceClassNames(Collection<String> classNames) {
                interfaceNames.clear();
                interfaceNames.addAll(classNames);
                importClassNames.remove(className); // Prevent cycles.
            }

            @Override
            public Collection<String> getImportedClassNames() {
                return Collections.unmodifiableSet(importClassNames);
            }

            @Override
            public void setImportedClassNames(Collection<String> classNames) {
                importClassNames.clear();
                importClassNames.addAll(classNames);
                importClassNames.remove(className); // Prevent cycles.
            }

            @Override
            public void setAccessFlags(int accessFlags) {
                this.accessFlags = AccessFlagsFactory.newClassAccessFlags(accessFlags);
            }

            @Override
            public AccessFlags getAccessFlags() {
                return accessFlags;
            }

            @Override
            public void setMethods(Collection<MethodDescriptor> methods) {
                this.methods.clear();
                this.methods.addAll(methods);
            }

            @Override
            public Collection<MethodDescriptor> getMethods() {
                return Collections.unmodifiableCollection(methods);
            }

            @Override
            public boolean isInner() {
                return isInnerClass;
            }

            @Override
            public boolean isAnonymous() {
                return isAnonymousClass;
            }

            @Override
            public ClassType getClassType() {
                return ClassType.fromAccessFlags(accessFlags);
            }

            @Override
            public String toString() {
                return "{" + getClassType().toChar() + '}' + getClassName();
            }

            @Override
            public Collection<FieldDescriptor> getFields() {
                return Collections.unmodifiableCollection(fields);
            }

            @Override
            public void setFields(Collection<FieldDescriptor> fields) {
                this.fields.clear();
                this.fields.addAll(fields);
            }
        };
    }

    public static MethodDescriptor newMethodDescriptor(
    		final String methodName, final int accessFlags, final String returnType, 
    		final List<String> parameterTypes, final String[] exceptions) 
    {
        final AccessFlags _accessFlags = AccessFlagsFactory.newMethodAccessFlags(accessFlags);
        final MethodType type = MethodType.fromName(methodName);
        final Collection<String> _exceptions = Collections.unmodifiableCollection(
                (exceptions == null) ? new LinkedList<String>() : Arrays.<String>asList(exceptions)); 
    	return new MethodDescriptor() {
			@Override
			public MethodType getMethodType() {
				return type;
			}
			
			@Override
			public String getMethodName() {
				return methodName;
			}
			
			@Override
			public AccessFlags getAccessFlags() {
				return _accessFlags;
			}
			
			@Override
            public String toString() {
                return String.format("%s %s %s%s%s", 
                        getAccessFlags().getModifiers().toString().replace("[", "").replace("]", "").replace(",", ""),
                        getReturnType(),
                        getMethodName(),
                        getParameterTypes().toString().replace('[', '(').replace(']', ')'),
                        _exceptions.isEmpty() ? "" : " throws " + _exceptions.toString().replace("[", "").replace("]", ""));
			}

            @Override
            public String getReturnType() {
                return returnType;
            }

            @Override
            public List<String> getParameterTypes() {
                return Collections.unmodifiableList(parameterTypes);
            }

            @Override
            public Collection<String> getExceptionTypes() {
                return _exceptions;
            }
		};
    }

    public static FieldDescriptor newFieldDescriptor(
            final String fieldName, final int accessFlags, final String fieldType) 
    {
        final AccessFlags _accessFlags = AccessFlagsFactory.newMethodAccessFlags(accessFlags);
        return new FieldDescriptor() {
            @Override
            public String getFieldType() {
                return fieldType;
            }
            
            @Override
            public String getFieldName() {
                return fieldName;
            }
            
            @Override
            public AccessFlags getAccessFlags() {
                return _accessFlags;
            }

            @Override
            public String toString() {
                return String.format("%s %s %s", 
                        getAccessFlags().getModifiers().toString().replace("[", "").replace("]", "").replace(",", ""),
                        getFieldType(),
                        getFieldName());
            }
        };
    }
}
