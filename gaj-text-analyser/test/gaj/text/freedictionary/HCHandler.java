package gaj.text.freedictionary;

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

public class HCHandler extends ContextfStatefulSAXEventRuleHandler<State> {

    private static final boolean IS_TRACE = Boolean.parseBoolean(System.getProperty("trace.handler.hc", "false"));

    private static final String SECTION_WORD_KEY = "word";
    private static final String SECTION_SEGMENTS_KEY = "segments";
    private static final String SEGMENT_WORDS_KEY = "words";
    private static final String SEGMENT_TAG_KEY = "tag";
	private static final String SEGMENT_ITEMS_KEY = "items";
	private static final String ITEM_SUBITEMS_KEY = "subitems";
	private static final String EXAMPLES_KEY = "examples";

    private List<StateEventRule<State, SAXEvent>> rules = null;

    private Map<String, Object> sectionData = null;
    private Map<String, Object> segmentData = null;
    private Map<String, Object> itemData = null;
    private Map<String, Object> subitemData = null;

    {
        super.setTrace(IS_TRACE);
    }

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
                    HCEvents.START_SECTION,
                    State.SECTION, this::initSection));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    HCEvents.START_SECTION_WORD,
                    State.WORD, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.<State> newRule(
                    SAXEventType.CHARACTERS, this::appendTextBuffer));
            rules.add(StateSAXEventRuleFactory.newRule(
                    null, State.WORD, State.SECTION,
                    HCEvents.END_SECTION_WORD,
                    State.REWIND, this::getSectionWord));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    HCEvents.START_SEGMENT,
                    State.SEGMENT, this::initSegment));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION, State.SEGMENT,
                    HCEvents.START_SEGMENT_TAG,
                    State.TAG, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT, State.TAG,
                    HCEvents.END_SEGMENT_TAG,
                    State.REWIND, this::getSegmentTag));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.TAG, State.SEGMENT,
                    HCEvents.START_SEGMENT_TAG,
                    State.TAG_FEATURE, this::captureTextOn, (Action) () -> appendTextBuffer("[")));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.TAG_FEATURE,
                    HCEvents.END_SEGMENT_TAG,
                    State.REWIND, this::getSegmentTag, (Action) () -> appendTextBuffer("]")));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    HCEvents.START_SEGMENT_WORD,
                    State.WORD, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.newRule(
                    null, State.WORD, State.SEGMENT,
                    HCEvents.END_SEGMENT_WORD,
                    State.REWIND, this::addSegmentWord));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.WORD, State.SEGMENT,
                    HCEvents.START_SEGMENT_WORD,
                    State.WORD, this::captureTextOn));
            // TODO handle intra-word elements.
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    HCEvents.START_SEGMENT_ITEM,
                    State.ITEM, this::initItem));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    HMEvents.START_SEGMENT_ITEM2,
                    State.ITEM, this::initItem));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.ITEM,
                    HCEvents.START_SEGMENT_EXAMPLE,
                    State.EXAMPLE, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.newRule(
                    null, State.EXAMPLE, State.ITEM,
                    HCEvents.END_SEGMENT_EXAMPLE,
                    State.REWIND, this::addItemExample));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.ITEM,
                    HCEvents.START_SEGMENT_SUBITEM,
                    State.SUBITEM, this::initSubItem));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SUBITEM,
                    HCEvents.START_SEGMENT_EXAMPLE,
                    State.EXAMPLE, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.newRule(
                    null, State.EXAMPLE, State.SUBITEM,
                    HCEvents.END_SEGMENT_EXAMPLE,
                    State.REWIND, this::addSubItemExample));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SUBITEM,
                    HCEvents.END_SEGMENT_SUBITEM,
                    State.REWIND, this::addSubItem));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.ITEM,
                    HCEvents.END_SEGMENT_ITEM,
                    State.REWIND, this::addItem));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SEGMENT,
                    HCEvents.END_SEGMENT,
                    State.REWIND, this::addSegment));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    HCEvents.START_SECTION_REP,
                    null, this::addSection));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    HCEvents.END_SECTION_REP,
                    null, this::initSection));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.SECTION,
                    HCEvents.END_SECTION,
                    State.REWIND, this::addSection));
            // Expect the unexpected
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.OTHER, SAXEventType.BEGIN_ELEMENT, State.OTHER));
            rules.add(StateSAXEventRuleFactory.newRule(
                    State.OTHER, SAXEventType.END_ELEMENT, State.REWIND));
            rules.add(StateSAXEventRuleFactory.newRule(
                    SAXEventType.BEGIN_ELEMENT, State.OTHER));
        }
        return rules;
    }

    protected void initSection() {
        sectionData = new HashMap<>();
        clearTextBuffer();
        captureTextOff();
    }

    protected void getSectionWord() {
        sectionData.put(SECTION_WORD_KEY, getTextBuffer());
        clearTextBuffer();
        captureTextOff();
    }

    protected void initSegment() {
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

    protected void initItem() {
        itemData = new HashMap<>();
        clearTextBuffer();
        captureTextOff();
    }

    protected void addItemExample() {
        String example = getTextBuffer();
        clearTextBuffer();
        if (IS_TRACE)
            System.out.printf("example=%s%n", example);
        @SuppressWarnings("unchecked")
        List<String> examples = (List<String>) itemData.get(EXAMPLES_KEY);
        if (examples == null) {
            examples = new ArrayList<>();
            itemData.put(EXAMPLES_KEY, examples);
        }
        examples.add(example);
    }

    protected void initSubItem() {
        subitemData = new HashMap<>();
        clearTextBuffer();
        captureTextOff();
    }

    protected void addSubItemExample() {
    	String example = getTextBuffer();
    	clearTextBuffer();
        if (IS_TRACE)
            System.out.printf("example=%s%n", example);
        @SuppressWarnings("unchecked")
        List<String> examples = (List<String>) subitemData.get(EXAMPLES_KEY);
        if (examples == null) {
            examples = new ArrayList<>();
            subitemData.put(EXAMPLES_KEY, examples);
        }
        examples.add(example);
    }

    protected void addSubItem() {
        if (IS_TRACE)
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

    protected void addItem() {
        if (IS_TRACE)
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

    protected void addSegment() {
        if (IS_TRACE)
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

    protected void addSection() {
        // if (IS_TRACE)
        System.out.printf("sectionData=%s%n", sectionData);
        sectionData = null;
    }

}
