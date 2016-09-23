package gaj.text.handler.sax;

import static gaj.text.handler.sax.SAXEventFactory.newEvent;
import static gaj.text.handler.core.StatefulFactory.newStateGetter;
import static gaj.text.handler.core.StatefulFactory.newStateTransition;
import gaj.text.handler.Action;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.StateGetter;
import gaj.text.handler.StateTransition;
import gaj.text.handler.core.StateEventRuleFactory;

import java.util.Map;

import org.xml.sax.Attributes;

public abstract class StateSAXEventRuleFactory {

    private StateSAXEventRuleFactory() {}

    public static <S> StateEventRule<S, SAXEvent> newRule(
            /*@Nullable*/ StateGetter<S> stateGetter,
            /*@Nullable*/ SAXEvent event,
            /*@Nullable*/ StateTransition<S> stateTransition)
    {
        return StateEventRuleFactory.newRule(stateGetter, event, stateTransition);
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(
            /*@Nullable*/ S previousState,
            /*@Nullable*/ S state,
            /*@Nullable*/ S parentState,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ Map<String,String> eventProperties,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction,
            /*@Nullable*/ Action preTransitionAction)
    {
        return newRule(
        	newStateGetter(previousState, state, parentState), 
        	newEvent(eventType, eventLabel, eventProperties), 
        	newStateTransition(transitionState, preTransitionAction, postTransitionAction)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ Map<String,String> eventProperties,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        return newRule(
        	newStateGetter(state), 
        	newEvent(eventType, eventLabel, eventProperties), 
        	newStateTransition(transitionState, postTransitionAction)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        return newRule(
        	newStateGetter(state), 
        	newEvent(eventType, eventLabel), 
        	newStateTransition(transitionState, postTransitionAction)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(
            /*@Nullable*/ S state,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
    {
        return newRule(
        	newStateGetter(state), 
        	newEvent(eventType), 
        	newStateTransition(transitionState, postTransitionAction)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(SAXEventType eventType, Action action) {
        return newRule(
        	null, 
        	newEvent(eventType), 
        	newStateTransition(action)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(
            S state, SAXEventType eventType, String eventLabel,
            Attributes eventAttrs, S transitionState)
    {
        return newRule(
        	newStateGetter(state), 
        	newEvent(eventType, eventLabel, eventAttrs), 
        	newStateTransition(transitionState)
        );
    }

	public static <S> StateEventRule<S, SAXEvent> newRule(
			/*@Nullable*/ S previousState,
            /*@Nullable*/ S state,
            /*@Nullable*/ SAXEventType eventType,
            /*@Nullable*/ String eventLabel,
            /*@Nullable*/ S transitionState,
            /*@Nullable*/ Action postTransitionAction)
	{
        return newRule(
        	newStateGetter(previousState, state), 
        	newEvent(eventType, eventLabel), 
        	newStateTransition(transitionState, postTransitionAction)
        );
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
        return newRule(
        	newStateGetter(previousState, state, parentState), 
        	newEvent(eventType, eventLabel), 
        	newStateTransition(transitionState, postTransitionAction)
        );
	}

    public static <S> StateEventRule<S, SAXEvent> newRule(S previousState, S state, S parentState, SAXEvent event, S transitionState, Action postTransitionAction) {
        return newRule(
        	newStateGetter(previousState, state, parentState), 
        	event, 
        	newStateTransition(transitionState, postTransitionAction)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(S previousState, S state, SAXEvent event, S transitionState, Action postTransitionAction) {
        return newRule(
        	newStateGetter(previousState, state), 
        	event, 
        	newStateTransition(transitionState, postTransitionAction)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(S state, SAXEvent event, S transitionState, Action postTransitionAction) {
        return newRule(
        	newStateGetter(state), 
        	event, 
        	newStateTransition(transitionState, postTransitionAction)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(S state, SAXEvent event, S transitionState) {
        return newRule(
        	newStateGetter(state), 
        	event, 
        	newStateTransition(transitionState)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(SAXEvent event, S transitionState) {
        return newRule(
        	null, 
        	event, 
        	newStateTransition(transitionState)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(S state, SAXEventType eventType, S transitionState) {
        return newRule(
        	newStateGetter(state), 
        	newEvent(eventType), 
        	newStateTransition(transitionState)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(SAXEventType eventType, S transitionState) {
        return newRule(
        	null, 
        	newEvent(eventType), 
        	newStateTransition(transitionState)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(S previousState, S state, SAXEvent event, S transitionState, Action postTransitionAction, Action preTransitionAction) {
        return newRule(
        	newStateGetter(previousState, state), 
        	event, 
        	newStateTransition(transitionState, preTransitionAction, postTransitionAction)
        );
    }

    public static <S> StateEventRule<S, SAXEvent> newRule(S state, SAXEvent event, S transitionState, Action postTransitionAction, Action preTransitionAction) {
        return newRule(
        	newStateGetter(state), 
        	event, 
        	newStateTransition(transitionState, preTransitionAction, postTransitionAction)
        );
    }

}
