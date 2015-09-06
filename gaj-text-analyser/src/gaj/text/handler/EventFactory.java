package gaj.text.handler;

import java.util.Map;

import org.xml.sax.Attributes;

public abstract class EventFactory {

	private EventFactory() {}
	
	public static <T,V> Event<T,V> newMappedEvent(/*@Nullable*/ T type, /*@Nullable*/ String label, /*@Nullable*/ Map<String,V> properties) {
		return new MappedEvent<T,V>(type, label, properties);
	}
	
	public static SAXEvent newSAXEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name, /*@Nullable*/ Attributes attrs) {
		return new SAXEvent(type, name, attrs);
    }

	public static SAXEvent newSAXEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name) {
		return new SAXEvent(type, name, null);
	}

	public static SAXEvent newSAXEvent(/*@Nullable*/ SAXEventType type) {
		return new SAXEvent(type, null, null);
	}

}
