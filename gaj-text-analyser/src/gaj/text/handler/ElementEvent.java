package gaj.text.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.Attributes;

/*package-private*/ class ElementEvent implements Event<String> {

	private final String name;
	private final Attributes attrs;
	
	private Map<String, String> mappedAttrs = null;

	/*package-private*/ ElementEvent(/*@Nullable*/ String name, /*@Nullable*/ Attributes attrs) {
		this.name = name;
		this.attrs = attrs;
	}

	@Override
	public /*@Nullable*/ String getLabel() {
		return name;
	}

	@Override
	public boolean hasProperty(String name, /*@Nullable*/ String value) {
		if (attrs == null) return value == null;
		String attr = attrs.getValue(name);
		return value == null && attr == null || value != null && value.equals(attr);
	}

	@Override
	public boolean hasProperties(Map<String, String> keyValues) {
		for (Entry<String, String> entry : keyValues.entrySet()) {
			if (!hasProperty(entry.getKey(), entry.getValue()))
				return false;
		}
		return true;
	}

	@Override
	public String getProperty(String name) {
		return (attrs == null) ? null : attrs.getValue(name);
	}

	@Override
	public Map<String, String> getProperties() {
		if (mappedAttrs == null) {
			mappedAttrs = new HashMap<>();
			if (attrs != null) {
				final int len = attrs.getLength();
				for (int i = 0; i < len; i++) {
					mappedAttrs.put(attrs.getQName(i), attrs.getValue(i));
				}
			}
		}
		return mappedAttrs ;
	}

}
