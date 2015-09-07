package gaj.text.handler;

import java.util.Collection;

public abstract class ContextfStatefulSAXEventRuleHandler<S> extends ContextStatefulSAXEventHandler<S> {

    protected ContextfStatefulSAXEventRuleHandler() {}

    protected abstract Collection<? extends ContextStateEventRule<S,SAXEventType,String>> getRules();

    @Override
    protected void handleEvent(SAXEvent event) {
        final Collection<? extends ContextStateEventRule<S,SAXEventType,String>> rules = getRules();
        System.out.printf("Event: %s[%s]%s ", event.getType(), event.getLabel(), event.getProperties());
        System.out.printf("Before: %s->%s(%s) ", getParentState(), getState(), getPreviousState());
        for (ContextStateEventRule<S, SAXEventType, String> rule : rules) {
            if (rule.matches(this, event)) {
                System.out.printf("Have rule ");
                StateTransition<S, Event<SAXEventType, String>> transition = rule.getStateTransition();
                if (transition != null) {
                    Action<Event<SAXEventType, String>> action = transition.getPreTransitionAction();
                    if (action != null) {
                        action.perform(event);
                    }
                    S newState = transition.getTransitionState();
                    if (newState != null) {
                        setState(newState);
                    }
                    action = transition.getPostTransitionAction();
                    if (action != null) {
                        action.perform(event);
                    }
                }
                System.out.printf("After: %s->%s(%s)%n", getParentState(), getState(), getPreviousState());
                return;
            }
        }
        System.out.println("No rule!");
    }

}
