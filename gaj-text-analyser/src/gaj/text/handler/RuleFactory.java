package gaj.text.handler;

import java.util.Map;

public abstract class RuleFactory {

	private RuleFactory() {}
	
	public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
			/*@Nullabl*/ S state,
			/*@Nullabl*/ S parentState,
			/*@Nullabl*/ S previousState,
			/*@Nullabl*/ T eventType,
			/*@Nullabl*/ String eventLabel,
			/*@Nullabl*/ Map<String,V> eventProperties,
			/*@Nullabl*/ Action preTransitionAction,
			/*@Nullabl*/ S transitionState,
			/*@Nullabl*/ Action postTransitionAction)
	{
		@SuppressWarnings("unchecked")
		ContextStateGetter<S> stateGetter = StateFactory.newContextStateGetter(previousState, state, parentState);
		StateTransition<S> stateTransition = StateFactory.newStateTransition(transitionState, preTransitionAction, postTransitionAction);
		Event<T,V> event = EventFactory.newMappedEvent(eventType, eventLabel, eventProperties);
		return newRule(stateGetter, event, stateTransition);
	}

	public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
			/*@Nullable*/ ContextStateGetter<S> stateGetter,
			/*@Nullable*/ Event<T,V> event,
			/*@Nullable*/ StateTransition<S> stateTransition)
	{
		return new ContextStateEventRuleImpl(stateGetter, event, stateTransition);
	}
			
}
