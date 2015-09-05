package gaj.text.handler;


/**
 * A state transition rule for a contextual, stateful event handler.
 */
public interface ContextStateEventRule<S,T> {

	/**
	 * Indicates whether or not the rule matches the given state for the given event.
	 * 
	 * @param state - The state to be matched.
	 * @param event - The event to be matched.
	 * @return A value of true (or false) if the rule matches. 
	 */
	boolean matches(ContextStateGetter<S> state, Event<T> event);

	/**
	 * If the rule matches, then this state transition will be performed.
	 *  
	 * @return The state transition to perform.
	 */
	StateTransition<S> getStateTransition();
	
}
