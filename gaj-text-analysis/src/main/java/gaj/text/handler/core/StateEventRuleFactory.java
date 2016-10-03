package gaj.text.handler.core;

import gaj.text.handler.Action;
import gaj.text.handler.Event;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.StateGetter;
import gaj.text.handler.StateTransition;
import java.util.Map;

public abstract class StateEventRuleFactory {

    private StateEventRuleFactory() {}

    public static <S,T,V> StateEventRule<S,Event<T,V>> newRule(
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
        Event<T,V> event = EventFactory.newEvent(eventType, eventLabel, eventProperties);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> StateEventRule<S,Event<T,V>> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ Map<String,V> eventProperties,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        Event<T,V> event = EventFactory.newEvent(eventType, eventLabel, eventProperties);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> StateEventRule<S,Event<T,V>> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        Event<T, V> event = EventFactory.newEvent(eventType, eventLabel);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,T,V> StateEventRule<S,Event<T,V>> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ T eventType,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        Event<T,V> event = EventFactory.newEvent(eventType);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S,E extends Event<?,?>> StateEventRule<S,E> newRule(
            /*@Nullable*/ StateGetter<S> stateGetter,
            /*@Nullable*/ E event,
            /*@Nullable*/ StateTransition<S> stateTransition)
    {
        return new StateEventRuleImpl<>(stateGetter, event, stateTransition);
    }

    public static <S, T, V> StateEventRule<S, Event<T,V>> newRule(T eventType, Action action) {
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(action);
        Event<T, V> event = EventFactory.newEvent(eventType);
        return new StateEventRuleImpl<>(null, event, stateTransition);
    }

    public static <S, T, V> StateEventRule<S, Event<T,V>> newRule(
            S state, T eventType, String eventLabel,
            Map<String, V> eventAttrs, S transitionState)
    {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        Event<T,V> event = EventFactory.newEvent(eventType, eventLabel, eventAttrs);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState);
        return new StateEventRuleImpl<>(stateGetter, event, stateTransition);
    }

}
