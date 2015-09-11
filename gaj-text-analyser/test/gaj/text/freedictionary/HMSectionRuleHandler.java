package gaj.text.freedictionary;

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
                    StructureDefinition.START_HM_SECTION,
                    State.SECTION, this::clearSectionData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    StructureDefinition.START_HM_SECTION_WORD,
                    State.WORD, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.<State> newRule(
                    SAXEventType.CHARACTERS, this::appendTextBuffer));
            rules.add(StateSAXEventRuleFactory.newRule(
                    null, State.WORD, State.SECTION,
                    StructureDefinition.END_HM_SECTION_WORD,
                    State.REWIND, this::getSectionWord));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    StructureDefinition.START_HM_SEGMENT,
                    State.SEGMENT, this::clearSegmentData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    StructureDefinition.START_HM_SEGMENT_TAG,
                    State.TAG, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.TAG,
                    StructureDefinition.END_HM_SEGMENT_TAG,
                    State.REWIND, this::getSegmentTag));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.TAG, State.SEGMENT,
                    StructureDefinition.START_HM_SEGMENT_WORD,
                    State.WORD, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.newRule(
                    null, State.WORD, State.SEGMENT,
                    StructureDefinition.END_HM_SEGMENT_WORD,
                    State.REWIND, this::addSegmentWord));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.WORD, State.SEGMENT,
                    StructureDefinition.START_HM_SEGMENT_WORD,
                    State.WORD, this::captureTextOn));
            // TODO handle intra-word elements.
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    StructureDefinition.START_HM_SEGMENT_ITEM,
                    State.ITEM));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.ITEM,
                    StructureDefinition.START_HM_SEGMENT_SUBITEM,
                    State.SUBITEM));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SUBITEM,
                    StructureDefinition.END_HM_SEGMENT_SUBITEM,
                    State.REWIND));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.ITEM,
                    StructureDefinition.END_HM_SEGMENT_ITEM,
                    State.REWIND));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    StructureDefinition.END_HM_SEGMENT,
                    State.REWIND, this::addSegmentData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    StructureDefinition.END_HM_SECTION,
                    State.REWIND, this::sendSectionData));
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

    protected void addSegmentWord() {
        @SuppressWarnings("unchecked")
		List<String> words = (List<String>) segmentData.get(SEGMENT_WORDS_KEY);
        if (words == null) {
        	words = new ArrayList<>();
            segmentData.put(SEGMENT_WORDS_KEY, words);
        }
        words.add(getTextBuffer());
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

}
