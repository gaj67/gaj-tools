package gaj.text.handler.sax;

import java.util.Map;
import org.xml.sax.Attributes;

public abstract class SAXEventFactory {

    private SAXEventFactory() {}

    public static Attributes newAttributes(/*@Nullable*/ Map<String, String> attrs) {
        return new MappedAttributes(attrs);
    }

    public static SAXEvent newEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name, /*@Nullable*/ Attributes attrs) {
        return new SAXEventImpl(type, name, attrs);
    }

    public static SAXEvent newEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name, /*@Nullable*/ Map<String,String> attrs) {
        return new SAXEventImpl(type, name, newAttributes(attrs));
    }

    public static SAXEvent newEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name) {
        return new SAXEventImpl(type, name, null);
    }

    public static SAXEvent newEvent(/*@Nullable*/ SAXEventType type) {
        return new SAXEventImpl(type, null, null);
    }

}
