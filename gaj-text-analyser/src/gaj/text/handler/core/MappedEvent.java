package gaj.text.handler.core;

import gaj.text.handler.Event;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

/*package-private*/ class MappedEvent<T,V> implements Event<T,V> {

    private final T type;
    private final String label;
    private final Map<String,V> properties;

    /*package-private*/ MappedEvent(/*@Nullable*/ T type, /*@Nullable*/ String label, /*@Nullable*/ Map<String,V> properties) {
        this.type = type;
        this.label = label;
        this.properties = (properties == null) ? Collections.<String, V> emptyMap() : properties;
    }

    @Override
    public /*@Nullable*/ T getType() {
        return type;
    }

    @Override
    public /*@Nullable*/ String getLabel() {
        return label;
    }

    @Override
    public boolean hasProperty(String name, /*@Nullable*/ Object value) {
        V prop = properties.get(name);
        return value == null && prop == null || value != null && value.equals(prop);
    }

    @Override
    public boolean hasProperties(Map<String, ?> keyValues) {
        for (Entry<String, V> entry : properties.entrySet()) {
            if (!hasProperty(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public /*@Nullable*/ V getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public /*@Nullable*/ Map<String, V> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    @Override
    public boolean matches(Event<?, ?> event) {
        return matchesEventType(getType(), event.getType())
                && matchesEventLabel(getLabel(), event.getLabel())
                && matchesEventProperties(getProperties(), event);
    }

    private boolean matchesEventType(/*@Nullable*/ T myEventType, Object theirEventType) {
        return myEventType == null || myEventType.equals(theirEventType);
    }

    private boolean matchesEventLabel(/*@Nullable*/ String myEventLabel, String theirEventLabel) {
        return myEventLabel == null || myEventLabel.equals(theirEventLabel);
    }

    private boolean matchesEventProperties(/*@Nullable*/Map<String, V> myEventProperties, Event<?, ?> event) {
        return myEventProperties == null || event.hasProperties(myEventProperties);
    }

}
