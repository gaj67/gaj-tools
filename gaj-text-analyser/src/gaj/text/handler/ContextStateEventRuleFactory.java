package gaj.text.handler;

import java.util.Map;

public abstract class ContextStateEventRuleFactory {

    private ContextStateEventRuleFactory() {}

    public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ S parentState,
            /*@Nullable*/ S previousState,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ Map<String,V> eventProperties,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction,
            /*@Nullable*/ Action preTransitionAction) 
    {
        ContextStateGetter<S> stateGetter = StateFactory.newContextStateGetter(previousState, state, parentState);
        StateTransition<S> stateTransition = StateFactory.newStateTransition(transitionState, preTransitionAction, postTransitionAction);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, eventLabel, eventProperties);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ Map<String,V> eventProperties,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        ContextStateGetter<S> stateGetter = StateFactory.newContextStateGetter(null, state, null);
        StateTransition<S> stateTransition = StateFactory.newStateTransition(transitionState, null, postTransitionAction);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, eventLabel, eventProperties);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        ContextStateGetter<S> stateGetter = StateFactory.newContextStateGetter(null, state);
        StateTransition<S> stateTransition = StateFactory.newStateTransition(transitionState, null, postTransitionAction);
        Event<T, V> event = EventFactory.newMappedEvent(eventType, eventLabel);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        ContextStateGetter<S> stateGetter = StateFactory.newContextStateGetter(null, state, null);
        StateTransition<S> stateTransition = StateFactory.newStateTransition(transitionState, null, postTransitionAction);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, null, null);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
            /*@Nullable*/ ContextStateGetter<S> stateGetter,
            /*@Nullable*/ Event<T,V> event,
            /*@Nullable*/ StateTransition<S> stateTransition)
    {
        return new ContextStateEventRuleImpl<S, T, V>(stateGetter, event, stateTransition);
    }

    public static <S, T, V> ContextStateEventRule<S, T, V> newRule(T eventType, Action action) {
        StateTransition<S> stateTransition = StateFactory.newStateTransition(null, null, action);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, null, null);
        return new ContextStateEventRuleImpl<S, T, V>(null, event, stateTransition);
    }

	public static <S, T, V> ContextStateEventRule<S, T, V> newRule(
			S state, T eventType, String eventLabel,
			Map<String, V> eventAttrs, S transitionState) 
	{
        StateTransition<S> stateTransition = StateFactory.newStateTransition(transitionState, null, null);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, eventLabel, eventAttrs);
        return new ContextStateEventRuleImpl<S, T, V>(null, event, stateTransition);
	}

}
