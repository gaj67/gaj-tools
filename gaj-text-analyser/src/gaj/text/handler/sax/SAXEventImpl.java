package gaj.text.handler.sax;

import gaj.text.handler.Event;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.xml.sax.Attributes;

/*package-private*/ class SAXEventImpl implements SAXEvent {

    private final SAXEventType type;
    private final String name;
    private final Attributes attrs;

    private Map<String, String> mappedAttrs = null;

    protected SAXEventImpl(/*@Nullable*/ SAXEventType type, /*@Nullable*/ String name, /*@Nullable*/ Attributes attrs) {
        this.type = type;
        this.name = name;
        this.attrs = attrs;
    }

    @Override
    public /*@Nullable*/ SAXEventType getType() {
        return type;
    }

    @Override
    public /*@Nullable*/ String getLabel() {
        return name;
    }

    @Override
    public boolean hasProperty(String name, /*@Nullable*/ Object value) {
        if (attrs == null) {
            return value == null;
        }
        String attr = attrs.getValue(name);
        return value == null && attr == null || value != null && value.equals(attr);
    }

    @Override
    public boolean hasProperties(Map<String, ?> keyValues) {
        for (Entry<String, ?> entry : keyValues.entrySet()) {
            if (!hasProperty(entry.getKey(), entry.getValue())) {
                return false;
            }
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

    @Override
    public boolean matches(Event<?, ?> event) {
        return matchesEventType(getType(), event.getType())
                && matchesEventLabel(getLabel(), event.getLabel())
                && matchesEventProperties(getProperties(), event);
    }

    private boolean matchesEventType(/*@Nullable*/ SAXEventType myEventType, Object theirEventType) {
        return myEventType == null || myEventType.equals(theirEventType);
    }

    private boolean matchesEventLabel(/*@Nullable*/ String myEventLabel, String theirEventLabel) {
        return myEventLabel == null || myEventLabel.equals(theirEventLabel);
    }

    private boolean matchesEventProperties(/*@Nullable*/ Map<String, String> myEventProperties, Event<?, ?> event) {
        return myEventProperties == null || event.hasProperties(myEventProperties);
    }

}
