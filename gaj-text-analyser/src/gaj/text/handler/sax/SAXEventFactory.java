package gaj.text.handler.sax;

import java.util.Map;
import java.util.Objects;
import org.xml.sax.Attributes;

public abstract class SAXEventFactory {

    private SAXEventFactory() {}

    public static Attributes newAttributes(Map<String, String> attrs) {
        Objects.requireNonNull(attrs);
        return new MappedAttributes(attrs);
    }

    public static Attributes newAttributes(String/*@Nullable*/... attrs) {
        return new ArrayAttributes(attrs);
    }

    public static SAXEvent newEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name, Attributes attrs) {
        Objects.requireNonNull(attrs);
        return new SAXEventImpl(type, name, attrs);
    }

    public static SAXEvent newEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name, Map<String,String> attrs) {
        Objects.requireNonNull(attrs);
        return new SAXEventImpl(type, name, newAttributes(attrs));
    }

    public static SAXEvent newEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name, String/*@Nullable*/... attrs) {
        Objects.requireNonNull(attrs);
        return new SAXEventImpl(type, name, newAttributes(attrs));
    }

    public static SAXEvent newEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name) {
        return new SAXEventImpl(type, name, null);
    }

    public static SAXEvent newEvent(/*@Nullable*/ SAXEventType type) {
        return new SAXEventImpl(type, null, null);
    }

}
