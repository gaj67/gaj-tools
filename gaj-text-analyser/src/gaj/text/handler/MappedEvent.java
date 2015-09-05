package gaj.text.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*package-private*/ class MappedEvent<T> implements Event<T> {

	private final String label;
	private final Map<String,T> properties;
	
	/*package-private*/ MappedEvent(/*@Nullable*/ String label, /*@Nullable*/ Map<String,T> properties) {
		this.label = label;
		this.properties = (properties == null) ? Collections.emptyMap() : properties;
	}
	
	@Override
	public /*@Nullable*/ String getLabel() {
		return label;
	}

	@Override
	public boolean hasProperty(String name, /*@Nullable*/ T value) {
		T prop = properties.get(name);
		return value == null && prop == null || value != null && value.equals(prop);
	}

	@Override
	public boolean hasProperties(Map<String, T> keyValues) {
		for (Entry<String, T> entry : properties.entrySet()) {
			if (!hasProperty(entry.getKey(), entry.getValue()))
				return false;
		}
		return true;
	}

	@Override
	public /*@Nullable*/ T getProperty(String name) {
		return properties.get(name);
	}

	@Override
	public Map<String, T> getProperties() {
		return Collections.unmodifiableMap(properties);
	}

}
