package gaj.text.handler;

import java.util.Collection;

public abstract class StatefulSAXEventRuleHandler<S> extends StatefulSAXEventHandler<S> {

	/**
	 * Temporary place holder for the event that triggers a rule.
	 */
    private SAXEvent event;

	protected StatefulSAXEventRuleHandler() {}

    protected abstract Collection<? extends StateEventRule<S,SAXEventType,String>> getRules();

    @Override
    protected void handleEvent(SAXEvent event) {
        final Collection<? extends StateEventRule<S,SAXEventType,String>> rules = getRules();
        for (StateEventRule<S, SAXEventType, String> rule : rules) {
            if (rule.matches(this, event)) {
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
                break;
            }
        }
    }

	protected void setEvent(SAXEvent event) {
		this.event = event;
	}

	protected SAXEvent getEvent() {
		return this.event;
	}

}
