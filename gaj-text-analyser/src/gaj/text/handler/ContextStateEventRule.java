package gaj.text.handler;


/**
 * A state transition rule for a contextual, stateful event handler.
 */
public interface ContextStateEventRule<S,T,V> extends StateEventRule<S,T,V> {

	/**
	 * Obtains the getter describing the rule state, to match against the handler state.
	 * 
	 * @return The state-getter, or a value of null if any handler state should match.
	 */
	@Override
	/*@Nullable*/ ContextStateGetter<S> getStateGetter();
	
	/**
	 * Indicates whether or not the rule matches the given state for the given event.
	 * 
	 * @param state - The state to be matched.
	 * @param event - The event to be matched.
	 * @return A value of true (or false) if the rule matches. 
	 */
	boolean matches(ContextStateGetter<S> state, Event<T,V> event);

}
