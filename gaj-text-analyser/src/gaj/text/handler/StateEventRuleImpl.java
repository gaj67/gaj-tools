package gaj.text.handler;

import java.util.Map;

/*package-private*/ class StateEventRuleImpl<S, T, V> implements StateEventRule<S, T, V> {

	private final StateGetter<S> stateGetter;
	private final Event<T, V> event;
	private final StateTransition<S,Event<T,V>> stateTransition;

	/*package-private*/ StateEventRuleImpl(
			/*@Nullable*/ StateGetter<S> stateGetter,
			/*@Nullable*/ Event<T, V> event, 
			/*@Nullable*/ StateTransition<S,Event<T,V>> stateTransition) 
	{
		this.stateGetter = stateGetter;
		this.event = event;
		this.stateTransition = stateTransition;
	}

	@Override
	public boolean matches(StateGetter<S> stateGetter, Event<T, V> event) {
		return matchesState(stateGetter) && matchesEvent(event);
	}

	protected boolean matchesState(StateGetter<S> stateGetter) {
		return this.stateGetter == null ||
				   matchesState(this.stateGetter.getState(), stateGetter.getState())
				   && matchesState(this.stateGetter.getPreviousState(), stateGetter.getPreviousState());
	}

	protected boolean matchesState(/*@Nullable*/ S ruleState, S handlerState) {
		return ruleState == null || ruleState.equals(handlerState);
	}

	protected boolean matchesEvent(Event<T, V> event) {
		return this.event == null || 
				   matchesEventType(this.event.getType(), event.getType())
				   && matchesEventLabel(this.event.getLabel(), event.getLabel())
				   && matchesEventProperties(this.event.getProperties(), event);
	}
	
	protected boolean matchesEventType(/*@Nullable*/ T ruleEventType, T handlerEventType) {
		return ruleEventType == null || ruleEventType.equals(handlerEventType);
	}

	protected boolean matchesEventLabel(/*@Nullable*/ String ruleEventLabel, String handlerEventLabel) {
		return ruleEventLabel == null || ruleEventLabel.equals(handlerEventLabel);
	}

	protected boolean matchesEventProperties(/*@Nullable*/ Map<String, V> ruleEventProperties, Event<T, V> event) 
	{
		return ruleEventProperties == null || event.hasProperties(ruleEventProperties);
	}

	@Override
	public /*@Nullable*/ StateTransition<S,Event<T,V>> getStateTransition() {
		return stateTransition;
	}

	@Override
	public /*@Nullable*/ StateGetter<S> getStateGetter() {
		return stateGetter;
	}

	@Override
	public /*@Nullable*/ Event<T, V> getEvent() {
		return event;
	}

}
