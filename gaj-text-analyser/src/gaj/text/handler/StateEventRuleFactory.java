package gaj.text.handler;

import java.util.Map;

public abstract class StateEventRuleFactory {

    private StateEventRuleFactory() {}

    public static <S,T,V> StateEventRule<S,T,V> newRule(
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
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(previousState, state, parentState);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, preTransitionAction, postTransitionAction);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, eventLabel, eventProperties);
        return newRule(stateGetter, event, stateTransition);
            }

    public static <S,T,V> StateEventRule<S,T,V> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ Map<String,V> eventProperties,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
            {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, eventLabel, eventProperties);
        return newRule(stateGetter, event, stateTransition);
            }

    public static <S,T,V> StateEventRule<S,T,V> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
            {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        Event<T, V> event = EventFactory.newMappedEvent(eventType, eventLabel);
        return newRule(stateGetter, event, stateTransition);
            }

    public static <S,T,V> StateEventRule<S,T,V> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
            {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        Event<T,V> event = EventFactory.newMappedEvent(eventType);
        return newRule(stateGetter, event, stateTransition);
            }

    public static <S,T,V> StateEventRule<S,T,V> newRule(
            /*@Nullable*/ StateGetter<S> stateGetter,
            /*@Nullable*/ Event<T,V> event,
            /*@Nullable*/ StateTransition<S> stateTransition)
            {
        return new StateEventRuleImpl<S, T, V>(stateGetter, event, stateTransition);
            }

    public static <S, T, V> StateEventRule<S, T, V> newRule(T eventType, Action action) {
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(action);
        Event<T, V> event = EventFactory.newMappedEvent(eventType);
        return new StateEventRuleImpl<S, T, V>(null, event, stateTransition);
    }

    public static <S, T, V> StateEventRule<S, T, V> newRule(
            S state, T eventType, String eventLabel,
            Map<String, V> eventAttrs, S transitionState)
            {
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState);
        Event<T,V> event = EventFactory.newMappedEvent(eventType, eventLabel, eventAttrs);
        return new StateEventRuleImpl<S, T, V>(null, event, stateTransition);
            }

}
