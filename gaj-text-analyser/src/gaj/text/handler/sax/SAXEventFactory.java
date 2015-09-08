package gaj.text.handler.sax;

import org.xml.sax.Attributes;

public abstract class SAXEventFactory {

    private SAXEventFactory() {}

    public static SAXEvent newSAXEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name, /*@Nullable*/ Attributes attrs) {
        return new SAXEventImpl(type, name, attrs);
    }

    public static SAXEvent newSAXEvent(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name) {
        return new SAXEventImpl(type, name, null);
    }

    public static SAXEvent newSAXEvent(/*@Nullable*/ SAXEventType type) {
        return new SAXEventImpl(type, null, null);
    }

}
