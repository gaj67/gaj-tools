package gaj.config.properties;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class PropertiesManagerImpl<T> implements PropertiesManager<T> {

	private final GroupedProperties<T> config = new GroupedPropertiesImpl<>();

	public void add(Configuration config) {
		this.config.addProperties(config);
	}

	public boolean exists(String key) {
		return this.config.existsGlobalProperties()
				&& this.config.getGlobalProperties().existsProperty(key);
	}

	public Object get(String key) {
		return this.config.existsGlobalProperties()
				? this.config.getGlobalProperties().getProperty(key)
						: null;
	}

	public void set(String key, Object value) {
		this.config.getGlobalProperties(true).setProperty(key, value);
	}

	public void remove(String key) {
		if (this.config.existsGlobalProperties())
			this.config.getGlobalProperties().removeProperty(key);
	}

	public boolean exists(final String[] groups, String key) {
		Configuration config = getNestedConfiguration(this.config, groups, false);
		if (config != null && config.isGrouped())
			config = ((GroupedProperties)config).getGlobalProperties();
		return (config != null)
				? ((Properties)config).existsProperty(key)
						: false;
	}

	public Object get(String[] groups, String key) {
		Configuration config = getNestedConfiguration(this.config, groups, false);
		if (config != null && config.isGrouped())
			config = ((GroupedProperties)config).getGlobalProperties();
		return (config != null)
				? ((Properties)config).getProperty(key)
						: null;
	}

	public void set(String[] groups, String key, Object value) {
		Configuration config = getNestedConfiguration(this.config, groups, true);
		if (config.isGrouped())
			config = ((GroupedProperties)config).getGlobalProperties(true);
		((Properties)config).setProperty(key, value);
	}

	public void remove(String[] groups, String key) {
		Configuration config = getNestedConfiguration(this.config, groups, false);
		if (config != null && config.isGrouped())
			config = ((GroupedProperties)config).getGlobalProperties();
		if (config != null)
			((Properties)config).removeProperty(key);
	}

	public Properties<T> accumulate(final String[] groups) {
		Properties<T> properties = new PropertiesImpl<>();
		GroupedProperties<T> parent = this.config;
		properties.addProperties(parent.getGlobalProperties());
		for (String groupName : groups) {
			Configuration group = parent.getGroupProperties(groupName);
			if (group == null) break;
			if (!group.isGrouped()) {
				properties.addProperties((Properties)group);
				break;
			}
			parent = (GroupedProperties)group;
			properties.addProperties(parent.getGlobalProperties());
		}
		return properties;
	}

	@Override
	public Collection<String> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean has(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void set(String key, T value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isGrouped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean has(String[] groups, String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void set(String[] groups, String key, T value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(Configuration<T> config) {
		// TODO Auto-generated method stub
		
	}

}
}