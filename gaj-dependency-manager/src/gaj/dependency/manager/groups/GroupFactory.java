/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.groups;

import gaj.dependency.manager.components.ClassesComponent;
import gaj.dependency.manager.components.LoadableComponent;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class GroupFactory {

    private GroupFactory() {}

    /**
     * Creates a new, loadable group.
     */
    public static LoadableGroup newGroup() {
        return new LoadableGroupImpl();
    }

    /**
     * Creates a new, loadable group bound to the given components.
     * @param components - An array or comma-separated list of uniquely-named components.
     */
    public static LoadableGroup newGroup(ClassesComponent... components) {
        LoadableGroup group = newGroup();
        group.addComponents(components);
        return group;
    }

    //********************************************************************************************

    private static class LoadableGroupImpl extends AbstractGroup implements LoadableGroup {

        private final Map<String,ClassesComponent> components = new HashMap<>();

        //*********************************************************************************
        // Group initialisation.

        @Override
        public LoadableGroup addComponent(ClassesComponent component) {
            if (components.containsKey(component.getComponentName())) {
                throw new IllegalArgumentException("Duplicate component name: " + component.getComponentName());
            }
            components.put(component.getComponentName(), component);
            return this;
        }

        @Override
        public LoadableGroup addComponents(ClassesComponent... components) {
            for (ClassesComponent component : components) {
                addComponent(component);
            }
            return this;
        }

        @Override
        public LoadableGroup addComponents(Collection<ClassesComponent> components) {
            for (ClassesComponent component : components) {
                addComponent(component);
            }
            return this;
        }

        @Override
        public boolean isLoaded() {
            for (ClassesComponent component : components.values()) {
                if (component instanceof LoadableComponent && !((LoadableComponent)component).isLoaded()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public ComponentGroup load() throws IOException {
            for (ClassesComponent component : components.values()) {
                if (component instanceof LoadableComponent) {
                    ((LoadableComponent)component).load();
                }
            }
            return this;
        }

        //*********************************************************************************
        // Component-level methods.

        @Override
        public int numComponents() {
            return components.size();
        }

        @Override
        public Iterable<ClassesComponent> getComponents() {
            return components.values();
        }

        @Override
        public boolean hasComponent(String componentName) {
            return components.containsKey(componentName);
        }

        @Override
        public boolean hasComponent(ClassesComponent component) {
            return components.containsValue(component);
        }

        @Override
        public ClassesComponent getComponent(String componentName) {
            return components.get(componentName);
        }

    }

}