package gaj.text.handler;

import java.util.Collection;

public abstract class ContextfStatefulSAXEventRuleHandler<S> extends ContextStatefulSAXEventHandler<S> {

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

    protected abstract Collection<? extends ContextStateEventRule<S,SAXEventType,String>> getRules();

    @Override
    protected void handleEvent(SAXEvent event) {
        final Collection<? extends ContextStateEventRule<S,SAXEventType,String>> rules = getRules();
        System.out.printf("Event: %s[%s]%s ", event.getType(), event.getLabel(), event.getProperties());
        System.out.printf("Before: %s->%s(%s) ", getParentState(), getState(), getPreviousState());
        for (ContextStateEventRule<S, SAXEventType, String> rule : rules) {
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
                System.out.printf("After: %s->%s(%s)%n", getParentState(), getState(), getPreviousState());
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
