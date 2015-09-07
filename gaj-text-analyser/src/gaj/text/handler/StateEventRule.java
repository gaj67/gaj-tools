package gaj.text.handler;


/**
 * A state transition rule for a stateful event handler.
 */
public interface StateEventRule<S,T,V> {

    /**
     * Obtains the getter describing the rule state, to match against the handler state.
     *
     * @return The state-getter, or a value of null if any handler state should match.
     */
    /*@Nullable*/ StateGetter<S> getStateGetter();

    /**
     * Obtains the rule event, to match against the handler event.
     *
     * @return The event, or a value of null if any handler event should match.
     */
    /*@Nullable*/ Event<T,V> getEvent();

    /**
     * Indicates whether or not the rule matches the given state for the given event.
     *
     * @param stateGetter - The state to be matched.
     * @param event - The event to be matched.
     * @return A value of true (or false) if the rule matches.
     */
    boolean matches(StateGetter<S> stateGetter, Event<T,V> event);

    /**
     * If the rule matches, then this state transition will be performed.
     *
     * @return The state transition to perform.
     */
    StateTransition<S, Event<T, V>> getStateTransition();

}
