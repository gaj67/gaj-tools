package gaj.config.configurators;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public abstract class Configurations {

	private Configurations() {}

	public static Configuration newConfiguration(Map<String, String> properties) {
		return new Configuration() {			
			@Override
			public int numProperties() {
				return properties.size();
			}
			
			@Override
			public /*@Nullable*/ String getValue(String key) {
				return properties.get(key);
			}
			
			@Override
			public Iterable<String> getKeys() {
				return Collections.unmodifiableCollection(properties.keySet());
			}
		};
	}
	
	public static Configuration newConfiguration(Properties properties) {
		return new Configuration() {			
			@Override
			public int numProperties() {
				return properties.size();
			}
			
			@Override
			public /*@Nullable*/ String getValue(String key) {
				return properties.getProperty(key);
			}
			
			@Override
			public Iterable<String> getKeys() {
				return properties.stringPropertyNames();
			}
		};
	}
	
}
