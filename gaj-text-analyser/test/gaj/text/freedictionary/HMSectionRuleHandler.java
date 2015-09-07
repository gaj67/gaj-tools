package gaj.text.freedictionary;

import gaj.text.handler.Action;
import gaj.text.handler.ContextStateEventRule;
import gaj.text.handler.ContextStateEventRuleFactory;
import gaj.text.handler.ContextfStatefulSAXEventRuleHandler;
import gaj.text.handler.Event;
import gaj.text.handler.SAXEventType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HMSectionRuleHandler extends ContextfStatefulSAXEventRuleHandler<State> {

    @SuppressWarnings("serial")
    private static Map<String,String> HM_SECTION_ATTRS = new HashMap<String,String>() {{
        put("data-src", "hm");
    }};

    private List<ContextStateEventRule<State, SAXEventType, String>> rules = null;

    private boolean captureText = false;

    private final StringBuilder buf = new StringBuilder();

    @Override
    protected State nullState() {
        return State.INIT;
    }

    @Override
    public void setState(State state) {
        if (State.REWIND == state) {
            rewindState();
        } else {
            super.setState(state);
        }
    }

    @Override
    protected Collection<? extends ContextStateEventRule<State, SAXEventType, String>> getRules() {
        if (rules == null) {
            rules = new ArrayList<>();
            Action<Event<SAXEventType, String>> postStateAction = new Action<Event<SAXEventType, String>>() {
                @Override
                public void perform(Event<SAXEventType, String> event) {
                    System.out.printf("Action! ");
                }
            };
            Action<Event<SAXEventType, String>> captureTextOn = new Action<Event<SAXEventType, String>>() {
                @Override
                public void perform(Event<SAXEventType, String> event) {
                    captureText = true;
                }
            };
            Action<Event<SAXEventType, String>> captureTextOff = new Action<Event<SAXEventType, String>>() {
                @Override
                public void perform(Event<SAXEventType, String> event) {
                    captureText = false;
                }
            };
            Action<Event<SAXEventType, String>> bufferText = new Action<Event<SAXEventType, String>>() {
                @Override
                public void perform(Event<SAXEventType, String> event) {
                    if (captureText) {
                        buf.append(event.getLabel());
                    }
                }
            };
            rules.add(ContextStateEventRuleFactory.newRule(
                    State.INIT,
                    SAXEventType.BEGIN_ELEMENT, "section", HM_SECTION_ATTRS,
                    State.SECTION, postStateAction));
            rules.add(ContextStateEventRuleFactory.newRule(
                    State.SECTION,
                    SAXEventType.BEGIN_ELEMENT, "h2",
                    State.WORD, captureTextOn));
            rules.add(ContextStateEventRuleFactory.<State, SAXEventType, String> newRule(
                    SAXEventType.CHARACTERS, bufferText));
            rules.add(ContextStateEventRuleFactory.newRule(
                    State.WORD,
                    SAXEventType.END_ELEMENT, "h2",
                    State.REWIND, captureTextOff));
            rules.add(ContextStateEventRuleFactory.newRule(
                    State.SECTION,
                    SAXEventType.END_ELEMENT, "section",
                    State.INIT, postStateAction));
        }
        return rules;
    }

}
