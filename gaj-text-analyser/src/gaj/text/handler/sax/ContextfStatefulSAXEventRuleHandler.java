package gaj.text.handler.sax;

import gaj.text.handler.Action;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.StateTransition;
import java.util.Collection;

public abstract class ContextfStatefulSAXEventRuleHandler<S> extends ContextStatefulSAXEventHandler<S> {

    private boolean IS_TRACE = Boolean.parseBoolean(System.getProperty("trace.handler", "false"));

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


    protected ContextfStatefulSAXEventRuleHandler() {}

    protected abstract Collection<StateEventRule<S, SAXEvent>> getRules();

    protected void setTrace(boolean isTrace) {
        IS_TRACE = isTrace;
    }

    @Override
    public void handle(SAXEvent event) {
        final Collection<StateEventRule<S, SAXEvent>> rules = getRules();
        if (IS_TRACE) {
            System.out.printf("Event: %s[%s]%s ", event.getType(), event.getLabel(), event.getProperties());
            System.out.printf("Before: %s->%s(%s) ", getParentState(), getState(), getPreviousState());
        }
        int i = -1;
        for (StateEventRule<S, SAXEvent> rule : rules) {
        	i++;
            if (rule.matches(this, event)) {
                if (IS_TRACE)
                    System.out.printf("Have rule %d ", i);
                execute(event, rule);
                if (IS_TRACE)
                    System.out.printf("After: %s->%s(%s)%n", getParentState(), getState(), getPreviousState());
                return;
            }
        }
        if (IS_TRACE)
            System.out.println("No rule!");
        execute(event, null);
    }

	protected void execute(SAXEvent event, /*@Nullable*/ StateEventRule<S, SAXEvent> rule) {
		if (rule == null) return;
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

    protected void appendTextBuffer(String text) {
        buf.append(text);
    }

    protected void clearTextBuffer() {
        buf.setLength(0);
    }

    protected String getTextBuffer() {
        return buf.toString();
    }

}
