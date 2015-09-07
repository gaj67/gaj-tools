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
            /*@Nullable*/ Action<Event<T,V>> postTransitionAction,
            /*@Nullable*/ Action<Event<T,V>> preTransitionAction) 
    {
        ContextStateGetter<S> stateGetter = StateFactory.newContextStateGetter(previousState, state, parentState);
        StateTransition<S,Event<T,V>> stateTransition = StateFactory.newStateTransition(transitionState, preTransitionAction, postTransitionAction);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, eventLabel, eventProperties);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ Map<String,V> eventProperties,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action<Event<T,V>> postTransitionAction)
    {
        ContextStateGetter<S> stateGetter = StateFactory.newContextStateGetter(null, state, null);
        StateTransition<S,Event<T,V>> stateTransition = StateFactory.newStateTransition(transitionState, null, postTransitionAction);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, eventLabel, eventProperties);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action<Event<T,V>> postTransitionAction)
    {
        ContextStateGetter<S> stateGetter = StateFactory.newContextStateGetter(null, state);
        StateTransition<S,Event<T,V>> stateTransition = StateFactory.newStateTransition(transitionState, null, postTransitionAction);
        Event<T, V> event = EventFactory.newMappedEvent(eventType, eventLabel);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action<Event<T,V>> postTransitionAction)
    {
        ContextStateGetter<S> stateGetter = StateFactory.newContextStateGetter(null, state, null);
        StateTransition<S,Event<T,V>> stateTransition = StateFactory.newStateTransition(transitionState, null, postTransitionAction);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, null, null);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> ContextStateEventRule<S,T,V> newRule(
            /*@Nullable*/ ContextStateGetter<S> stateGetter,
            /*@Nullable*/ Event<T,V> event,
            /*@Nullable*/ StateTransition<S,Event<T,V>> stateTransition)
    {
        return new ContextStateEventRuleImpl<S, T, V>(stateGetter, event, stateTransition);
    }

    public static <S, T, V> ContextStateEventRule<S, T, V> newRule(T eventType, Action<Event<T, V>> action) {
        StateTransition<S,Event<T,V>> stateTransition = StateFactory.newStateTransition(null, null, action);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, null, null);
        return new ContextStateEventRuleImpl<S, T, V>(null, event, stateTransition);
    }

}
