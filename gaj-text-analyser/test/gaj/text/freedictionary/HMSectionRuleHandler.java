package gaj.text.freedictionary;

import static gaj.text.freedictionary.StructureDefinition.HM_SECTION_ATTRS;
import static gaj.text.freedictionary.StructureDefinition.HM_SEGMENT_ATTRS;
import static gaj.text.freedictionary.StructureDefinition.SECTION_TAG;
import static gaj.text.freedictionary.StructureDefinition.SECTION_WORD_TAG;
import static gaj.text.freedictionary.StructureDefinition.SEGMENT_TAG;
import gaj.text.handler.Action;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.core.StateEventRuleFactory;
import gaj.text.handler.sax.ContextfStatefulSAXEventRuleHandler;
import gaj.text.handler.sax.SAXEventType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HMSectionRuleHandler extends ContextfStatefulSAXEventRuleHandler<State> {

    private static final String SECTION_WORD_KEY = "sectionWord";

    private List<StateEventRule<State, SAXEventType, String>> rules = null;

    private final Map<String, String> sectionData = new HashMap<>();

    @Override
    public State nullState() {
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
    protected Collection<? extends StateEventRule<State, SAXEventType, String>> getRules() {
        if (rules == null) {
            rules = new ArrayList<>();
            rules.add(StateEventRuleFactory.newRule(
                    State.INIT,
                    SAXEventType.BEGIN_ELEMENT, SECTION_TAG, HM_SECTION_ATTRS,
                    State.SECTION, clearSectionData));
            rules.add(StateEventRuleFactory.<State, SAXEventType, String> newRule(
                    State.SECTION,
                    SAXEventType.BEGIN_ELEMENT, SECTION_WORD_TAG,
                    State.WORD, captureTextOn));
            rules.add(StateEventRuleFactory.<State, SAXEventType, String> newRule(
                    SAXEventType.CHARACTERS, appendTextBuffer));
            rules.add(StateEventRuleFactory.<State, SAXEventType, String> newRule(
                    State.WORD,
                    SAXEventType.END_ELEMENT, SECTION_WORD_TAG,
                    State.REWIND, getSectionWord));
            rules.add(StateEventRuleFactory.newRule(
                    State.SECTION,
                    SAXEventType.BEGIN_ELEMENT, SEGMENT_TAG, HM_SEGMENT_ATTRS,
                    State.SEGMENT));
            rules.add(StateEventRuleFactory.<State, SAXEventType, String> newRule(
                    State.SEGMENT,
                    SAXEventType.END_ELEMENT, SEGMENT_TAG,
                    State.REWIND, addSegmentData));
            rules.add(StateEventRuleFactory.<State, SAXEventType, String> newRule(
                    State.SECTION,
                    SAXEventType.END_ELEMENT, SECTION_TAG,
                    State.INIT, sendSectionData));
        }
        return rules;
    }

    protected void clearSectionData() {
        sectionData.clear();
        clearTextBuffer();
        captureTextOff();
    }

    protected void getSectionWord() {
        sectionData.put(SECTION_WORD_KEY, getTextBuffer());
        clearTextBuffer();
        captureTextOff();
    }

    protected void addSegmentData() {
        System.out.printf("segmentData");
    }

    protected void sendSectionData() {
        System.out.printf("sectionData=%s%n", sectionData);
    }

    //*************************************************************************
    // Section for Java 7 (non-lambda) compatibility.

    private final Action captureTextOff = new Action() {
        @Override
        public void perform() {
            captureTextOff();
        }
    };

    private final Action captureTextOn = new Action() {
        @Override
        public void perform() {
            captureTextOn();
        }
    };

    private final Action clearSectionData = new Action() {
        @Override
        public void perform() {
            clearSectionData();
        }
    };

    private final Action getSectionWord = new Action() {
        @Override
        public void perform() {
            getSectionWord();
        }
    };

    private final Action sendSectionData = new Action() {
        @Override
        public void perform() {
            sendSectionData();
        }
    };

    private final Action appendTextBuffer = new Action() {
        @Override
        public void perform() {
            appendTextBuffer();
        }
    };

    private final Action addSegmentData = new Action() {
        @Override
        public void perform() {
            addSegmentData();
        }
    };

}
