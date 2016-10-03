package gaj.text.handler.core;

import gaj.text.handler.Event;
import java.util.Map;

public abstract class EventFactory {

    private EventFactory() {}

    public static <T,V> Event<T,V> newEvent(/*@Nullable*/ T type, /*@Nullable*/ String label, /*@Nullable*/ Map<String,V> properties) {
        return new MappedEvent<>(type, label, properties);
    }

    public static <T,V> Event<T,V> newEvent(/*@Nullable*/ T type, /*@Nullable*/ String label) {
        return new MappedEvent<>(type, label, null);
    }

    public static <T, V> Event<T, V> newEvent(/* @Nullable */T type) {
        return new MappedEvent<>(type, null, null);
    }

}
