package gaj.text.freedictionary;

import static gaj.text.freedictionary.StructureDefinition.HM_SECTION_ATTRS;
import static gaj.text.freedictionary.StructureDefinition.HM_SEGMENT_ATTRS;
import static gaj.text.freedictionary.StructureDefinition.SECTION_TAG;
import static gaj.text.freedictionary.StructureDefinition.SECTION_WORD_TAG;
import static gaj.text.freedictionary.StructureDefinition.SEGMENT_DEF_TAG;
import static gaj.text.freedictionary.StructureDefinition.SEGMENT_TAG;
import gaj.text.handler.Action;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.sax.ContextfStatefulSAXEventRuleHandler;
import gaj.text.handler.sax.SAXEvent;
import gaj.text.handler.sax.SAXEventType;
import gaj.text.handler.sax.StateSAXEventRuleFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HMSectionRuleHandler extends ContextfStatefulSAXEventRuleHandler<State> {

    private static final String SECTION_WORD_KEY = "word";
    private static final String SECTION_SEGMENTS_KEY = "segments";
    private static final String SEGMENT_WORDS_KEY = "words";
    private static final String SEGMENT_TAG_KEY = "tag";

    private List<StateEventRule<State, SAXEvent>> rules = null;

    private final Map<String, Object> sectionData = new HashMap<>();
    private final Map<String, Object> segmentData = new HashMap<>();

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
    protected Collection<StateEventRule<State, SAXEvent>> getRules() {
        if (rules == null) {
            rules = new ArrayList<>();
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.INIT,
                    SAXEventType.BEGIN_ELEMENT, SECTION_TAG, HM_SECTION_ATTRS,
                    State.SECTION, clearSectionData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    SAXEventType.BEGIN_ELEMENT, SECTION_WORD_TAG,
                    State.WORD, captureTextOn));
            rules.add(StateSAXEventRuleFactory.<State> newRule(
                    SAXEventType.CHARACTERS, appendTextBuffer));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.WORD,
                    SAXEventType.END_ELEMENT, SECTION_WORD_TAG,
                    State.REWIND, getSectionWord));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    SAXEventType.BEGIN_ELEMENT, SEGMENT_TAG, HM_SEGMENT_ATTRS,
                    State.SEGMENT, clearSegmentData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    SAXEventType.BEGIN_ELEMENT, SEGMENT_DEF_TAG,
                    State.TAG, captureTextOn));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.TAG,
                    SAXEventType.END_ELEMENT, SEGMENT_DEF_TAG,
                    State.REWIND, getSegmentTag));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    SAXEventType.END_ELEMENT, SEGMENT_TAG,
                    State.REWIND, addSegmentData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    SAXEventType.END_ELEMENT, SECTION_TAG,
                    State.REWIND, sendSectionData));
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

    protected void clearSegmentData() {
        segmentData.clear();
        clearTextBuffer();
        captureTextOff();
    }

    protected void getSegmentTag() {
        String tag = (String) segmentData.get(SEGMENT_TAG_KEY);
        if (tag == null) {
            segmentData.put(SEGMENT_TAG_KEY, getTextBuffer());
        } else {
            segmentData.put(SEGMENT_TAG_KEY, tag + getTextBuffer());
        }
        clearTextBuffer();
        captureTextOff();
    }

    protected void addSegmentData() {
        System.out.printf("segmentData=%s%n", segmentData);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> segments = (List<Map<String, Object>>) sectionData.get(SECTION_SEGMENTS_KEY);
        if (segments == null) {
            segments = new ArrayList<>();
            sectionData.put(SECTION_SEGMENTS_KEY, segments);
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> segment = (Map<String, Object>) ((HashMap<String, Object>) segmentData).clone();
        segments.add(segment);
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

    private final Action clearSegmentData = new Action() {
        @Override
        public void perform() {
            clearSegmentData();
        }
    };

    private final Action getSegmentTag = new Action() {
        @Override
        public void perform() {
            getSegmentTag();
        }
    };

    private final Action addSegmentData = new Action() {
        @Override
        public void perform() {
            addSegmentData();
        }
    };

}
