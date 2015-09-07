package gaj.text.handler;

import java.util.Collection;

public abstract class StatefulSAXEventRuleHandler<S> extends StatefulSAXEventHandler<S> {

    protected StatefulSAXEventRuleHandler() {}

    protected abstract Collection<? extends StateEventRule<S,SAXEventType,String>> getRules();

    @Override
    protected void handleEvent(SAXEvent event) {
        final Collection<? extends StateEventRule<S,SAXEventType,String>> rules = getRules();
        for (StateEventRule<S, SAXEventType, String> rule : rules) {
            if (rule.matches(this, event)) {
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
                break;
            }
        }
    }

}
