package gaj.text.handler.sax;

import gaj.text.handler.Action;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.StateGetter;
import gaj.text.handler.StateTransition;
import gaj.text.handler.core.StateEventRuleFactory;
import gaj.text.handler.core.StatefulFactory;
import java.util.Map;
import org.xml.sax.Attributes;

public abstract class StateSAXEventRuleFactory {

    private StateSAXEventRuleFactory() {}

    public static <S> StateEventRule<S, SAXEvent> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ S parentState,
            /*@Nullable*/ S previousState,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ Map<String,String> eventProperties,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction,
            /*@Nullable*/ Action preTransitionAction)
    {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(previousState, state, parentState);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, preTransitionAction, postTransitionAction);
        SAXEvent event = SAXEventFactory.newEvent(eventType, eventLabel, eventProperties);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ Map<String,String> eventProperties,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        SAXEvent event = SAXEventFactory.newEvent(eventType, eventLabel, eventProperties);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        SAXEvent event = SAXEventFactory.newEvent(eventType, eventLabel);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        SAXEvent event = SAXEventFactory.newEvent(eventType);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(
            /*@Nullable*/ StateGetter<S> stateGetter,
            /*@Nullable*/ SAXEvent event,
            /*@Nullable*/ StateTransition<S> stateTransition)
    {
        return StateEventRuleFactory.newRule(stateGetter, event, stateTransition);
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(SAXEventType eventType, Action action) {
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(action);
        SAXEvent event = SAXEventFactory.newEvent(eventType);
        return StateEventRuleFactory.<S,SAXEvent>newRule(null, event, stateTransition);
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(
            S state, SAXEventType eventType, String eventLabel,
            Attributes eventAttrs, S transitionState)
    {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        SAXEvent event = SAXEventFactory.newEvent(eventType, eventLabel, eventAttrs);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState);
        return StateEventRuleFactory.newRule(stateGetter, event, stateTransition);
    }

	public static <S> StateEventRule<S, SAXEvent> newRule(
			/*@Nullable*/ S previousState,
            /*@Nullable*/ S state,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
	{
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(previousState, state);
        SAXEvent event = SAXEventFactory.newEvent(eventType, eventLabel);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        return newRule(stateGetter, event, stateTransition);
	}

	public static <S> StateEventRule<S, SAXEvent> newRule(
			/*@Nullable*/ S previousState,
            /*@Nullable*/ S state,
            /*@Nullable*/ S parentState,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
	{
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(previousState, state, parentState);
        SAXEvent event = SAXEventFactory.newEvent(eventType, eventLabel);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        return newRule(stateGetter, event, stateTransition);
	}

    public static <S> StateEventRule<S, SAXEvent> newRule(S previousState, S state, S parentState, SAXEvent event, S transitionState, Action postTransitionAction) {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(previousState, state, parentState);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(S previousState, S state, SAXEvent event, S transitionState, Action postTransitionAction) {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(previousState, state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(S state, SAXEvent event, S transitionState, Action postTransitionAction) {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState, postTransitionAction);
        return newRule(stateGetter, event, stateTransition);
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(S state, SAXEvent event, S transitionState) {
        StateGetter<S> stateGetter = StatefulFactory.newStateGetter(state);
        StateTransition<S> stateTransition = StatefulFactory.newStateTransition(transitionState);
        return newRule(stateGetter, event, stateTransition);
    }

}
