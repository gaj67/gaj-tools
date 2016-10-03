package gaj.text.handler;

public interface EventHandler<E extends Event<?, ?>> {

    void handle(E event);

}
