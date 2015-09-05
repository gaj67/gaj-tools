package gaj.text.handler;

import java.util.Map;

import org.xml.sax.Attributes;

public abstract class EventFactory {

	private static final String ELEMENT_NAME_PROPERTY = "_QNAME";
	
	private EventFactory() {}
	
	public static <T> Event<T> getMappedEvent(/*@Nullable*/ String label, /*@Nullable*/ Map<String,T> properties) {
		return new MappedEvent<T>(label, properties);
	}
	
	public static Event<String> getElementEvent(String qualifiedName, Attributes attrs) {
		return new ElementEvent(qualifiedName, attrs); 
    }
}
