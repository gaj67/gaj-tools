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

public class HMHandler extends ContextfStatefulSAXEventRuleHandler<State> {

    private static final String SECTION_WORD_KEY = "word";
    private static final String SECTION_SEGMENTS_KEY = "segments";
    private static final String SEGMENT_WORDS_KEY = "words";
    private static final String SEGMENT_TAG_KEY = "tag";
	private static final String SEGMENT_ITEMS_KEY = "items";
	private static final String ITEM_SUBITEMS_KEY = "subitems";

    private List<StateEventRule<State, SAXEvent>> rules = null;

    private Map<String, Object> sectionData = null;
    private Map<String, Object> segmentData = null;
    private Map<String, Object> itemData = null;
    private Map<String, Object> subitemData = null;

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
                    HMEvents.START_SECTION,
                    State.SECTION, this::initSectionData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    HMEvents.START_SECTION_WORD,
                    State.WORD, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.<State> newRule(
                    SAXEventType.CHARACTERS, this::appendTextBuffer));
            rules.add(StateSAXEventRuleFactory.newRule(
                    null, State.WORD, State.SECTION,
                    HMEvents.END_SECTION_WORD,
                    State.REWIND, this::getSectionWord));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    HMEvents.START_SEGMENT,
                    State.SEGMENT, this::initSegmentData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    HMEvents.START_SEGMENT_TAG,
                    State.TAG, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.TAG,
                    HMEvents.END_SEGMENT_TAG,
                    State.REWIND, this::getSegmentTag));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.TAG, State.SEGMENT,
                    HMEvents.START_SEGMENT_WORD,
                    State.WORD, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.newRule(
                    null, State.WORD, State.SEGMENT,
                    HMEvents.END_SEGMENT_WORD,
                    State.REWIND, this::addSegmentWord));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.WORD, State.SEGMENT,
                    HMEvents.START_SEGMENT_WORD,
                    State.WORD, this::captureTextOn));
            // TODO handle intra-word elements.
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    HMEvents.START_SEGMENT_ITEM,
                    State.ITEM, this::initItemData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.ITEM,
                    HMEvents.START_SEGMENT_SUBITEM,
                    State.SUBITEM, this::initSubItemData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SUBITEM,
                    HMEvents.END_SEGMENT_SUBITEM,
                    State.REWIND, this::addSubItemData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.ITEM,
                    HMEvents.END_SEGMENT_ITEM,
                    State.REWIND, this::addItemData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    HMEvents.END_SEGMENT,
                    State.REWIND, this::addSegmentData));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    HMEvents.END_SECTION,
                    State.REWIND, this::sendSectionData));
        }
        return rules;
    }

    protected void initSectionData() {
        sectionData = new HashMap<>();
        clearTextBuffer();
        captureTextOff();
    }

    protected void getSectionWord() {
        sectionData.put(SECTION_WORD_KEY, getTextBuffer());
        clearTextBuffer();
        captureTextOff();
    }

    protected void initSegmentData() {
        segmentData = new HashMap<>();
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

    protected void initItemData() {
        itemData = new HashMap<>();
        clearTextBuffer();
        captureTextOff();
    }

    protected void initSubItemData() {
        subitemData = new HashMap<>();
        clearTextBuffer();
        captureTextOff();
    }

    protected void addSubItemData() {
        System.out.printf("subitemData=%s%n", subitemData);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> subitems = (List<Map<String, Object>>) itemData.get(ITEM_SUBITEMS_KEY);
        if (subitems == null) {
            subitems = new ArrayList<>();
            itemData.put(ITEM_SUBITEMS_KEY, subitems);
        }
        subitems.add(subitemData);
        subitemData = null;
    }

    protected void addItemData() {
        System.out.printf("itemData=%s%n", itemData);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) segmentData.get(SEGMENT_ITEMS_KEY);
        if (items == null) {
            items = new ArrayList<>();
            segmentData.put(SEGMENT_ITEMS_KEY, items);
        }
        items.add(itemData);
        itemData = null;
    }

    protected void addSegmentData() {
        System.out.printf("segmentData=%s%n", segmentData);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> segments = (List<Map<String, Object>>) sectionData.get(SECTION_SEGMENTS_KEY);
        if (segments == null) {
            segments = new ArrayList<>();
            sectionData.put(SECTION_SEGMENTS_KEY, segments);
        }
        segments.add(segmentData);
        segmentData = null;
    }

    protected void sendSectionData() {
        System.out.printf("sectionData=%s%n", sectionData);
        sectionData = null;
    }

}
