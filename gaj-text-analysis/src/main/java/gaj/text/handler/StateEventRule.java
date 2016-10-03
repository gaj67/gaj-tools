package gaj.text.handler;


/**
 * A state transition rule for a stateful event handler.
 */
public interface StateEventRule<S, E extends Event<?, ?>> {

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
    /* @Nullable */E getEvent();

    /**
     * Indicates whether or not the rule matches the given state for the given event.
     *
     * @param stateGetter - The state to be matched.
     * @param event - The event to be matched.
     * @return A value of true (or false) if the rule matches.
     */
    boolean matches(StateGetter<S> stateGetter, E event);

    /**
     * If the rule matches, then this state transition will be performed.
     *
     * @return The state transition to perform.
     */
    StateTransition<S> getStateTransition();

}
