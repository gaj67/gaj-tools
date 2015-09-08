package gaj.text.handler.sax;

import gaj.text.handler.Action;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.StateTransition;
import java.util.Collection;

public abstract class StatefulSAXEventRuleHandler<S> extends StatefulSAXEventHandler<S> {

    /**
     * Temporary place holder for the event that triggers a rule.
     */
    private SAXEvent event;

    /**
     * An indicator of whether or not to capture event text in the text buffer.
     */
    private boolean captureText = false;

    /**
     * The text buffer.
     */
    private final StringBuilder buf = new StringBuilder();

    protected StatefulSAXEventRuleHandler() {}

    protected abstract Collection<? extends StateEventRule<S,SAXEventType,String>> getRules();

    @Override
    public void handle(SAXEvent event) {
        System.out.printf("Event: %s[%s]%s ", event.getType(), event.getLabel(), event.getProperties());
        System.out.printf("Before: %s->%s ", getPreviousState(), getState());
        final Collection<? extends StateEventRule<S, SAXEventType, String>> rules = getRules();
        for (StateEventRule<S, SAXEventType, String> rule : rules) {
            if (rule.matches(this, event)) {
                System.out.printf("Have rule ");
                StateTransition<S> transition = rule.getStateTransition();
                if (transition != null) {
                    setEvent(event);
                    Action action = transition.getPreTransitionAction();
                    if (action != null) {
                        action.perform();
                    }
                    S newState = transition.getTransitionState();
                    if (newState != null) {
                        setState(newState);
                    }
                    action = transition.getPostTransitionAction();
                    if (action != null) {
                        action.perform();
                    }
                }
                System.out.printf("After: %s->%s%n", getPreviousState(), getState());
                return;
            }
        }
        System.out.println("No rule!");
    }

    protected void setEvent(SAXEvent event) {
        this.event = event;
    }

    protected SAXEvent getEvent() {
        return this.event;
    }

    protected void captureTextOn() {
        captureText = true;
    }

    protected void captureTextOff() {
        captureText = false;
    }

    protected void appendTextBuffer() {
        if (captureText) {
            buf.append(getEvent().getLabel());
        }
    }

    protected void clearTextBuffer() {
        if (captureText) {
            buf.append(getEvent().getLabel());
        }
    }

    protected String getTextBuffer() {
        return buf.toString();
    }

}
