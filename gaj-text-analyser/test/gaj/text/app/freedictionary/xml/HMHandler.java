package gaj.text.app.freedictionary.xml;

import static gaj.text.handler.sax.StateSAXEventRuleFactory.newRule;
import gaj.text.handler.Action;
import gaj.text.handler.StateEventRule;
import gaj.text.handler.sax.ContextfStatefulSAXEventRuleHandler;
import gaj.text.handler.sax.SAXEvent;
import gaj.text.handler.sax.SAXEventType;
import gaj.text.handler.sax.StateSAXEventRuleFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HMHandler extends ContextfStatefulSAXEventRuleHandler<State> {

    private static final boolean IS_TRACE = Boolean.parseBoolean(System.getProperty("trace.handler.hm", "false"));

    private static final String SECTION_WORD_KEY = "word";
    private static final String SECTION_SEGMENTS_KEY = "segments";
    private static final String SEGMENT_WORDS_KEY = "words";
    private static final String SEGMENT_TAG_KEY = "tag";
	private static final String SEGMENT_ITEMS_KEY = "items";
	private static final String ITEM_SUBITEMS_KEY = "subitems";
	private static final String EXAMPLES_KEY = "examples";

	private final Consumer<UnstructuredData> consumer;

    private List<StateEventRule<State, SAXEvent>> rules = null;

    private Map<String, Object> sectionData = null;
    private Map<String, Object> segmentData = null;
    private Map<String, Object> itemData = null;
    private Map<String, Object> subitemData = null;

    {
        setTrace(IS_TRACE);
    }

    public HMHandler(Consumer<UnstructuredData> consumer) {
		this.consumer = consumer;
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
    protected Iterable<StateEventRule<State, SAXEvent>> getRules() {
        if (rules == null) {
            rules = new ArrayList<>();
            rules.add(newRule(
                    State.INIT,
                    HMEvents.START_SECTION,
                    State.SECTION, this::initSection));
            rules.add(newRule(
                    State.SECTION,
                    HMEvents.START_SECTION_WORD,
                    State.WORD, this::captureTextOn));
            rules.add(StateSAXEventRuleFactory.<State> newRule(
                    SAXEventType.CHARACTERS, this::appendTextBuffer));
            rules.add(newRule(
                    null, State.WORD, State.SECTION,
                    HMEvents.START_WORD_OTHER,
                    State.OTHER, this::captureTextOff));
            rules.add(newRule(
                    null, State.WORD, State.SECTION,
                    HMEvents.END_SECTION_WORD,
                    State.REWIND, this::getSectionWord));
            rules.add(newRule(
                    State.SECTION,
                    HMEvents.START_SEGMENT,
                    State.SEGMENT, this::initSegment));
            rules.add(newRule(
                    State.SECTION, State.SEGMENT,
                    HMEvents.START_SEGMENT_TAG,
                    State.TAG, this::captureTextOn));
            rules.add(newRule(
                    State.SEGMENT, State.TAG,
                    HMEvents.END_SEGMENT_TAG,
                    State.REWIND, this::getSegmentTag));
            rules.add(newRule(
                    State.TAG, State.SEGMENT,
                    HMEvents.START_SEGMENT_TAG,
                    State.TAG_FEATURE, this::captureTextOn, (Action) () -> appendTextBuffer("[")));
            rules.add(newRule(
                    State.TAG_FEATURE,
                    HMEvents.END_SEGMENT_TAG,
                    State.REWIND, this::getSegmentTag, (Action) () -> appendTextBuffer("]")));
            rules.add(newRule(
                    State.SEGMENT,
                    HMEvents.START_SEGMENT_WORD,
                    State.WORD, this::captureTextOn));
            rules.add(newRule(
                    null, State.WORD, State.SEGMENT,
                    HMEvents.END_SEGMENT_WORD,
                    State.REWIND, this::addSegmentWord));
            rules.add(newRule(
                    State.WORD, State.SEGMENT,
                    HMEvents.START_SEGMENT_WORD,
                    State.WORD, this::captureTextOn)); // Occluded by above?
            // TODO handle intra-word elements.
            rules.add(newRule(
                    State.SEGMENT,
                    HMEvents.START_SEGMENT_ITEM,
                    State.ITEM, this::initItem));
            rules.add(newRule(
                    State.SEGMENT,
                    HMEvents.START_SEGMENT_ITEM2,
                    State.ITEM, this::initItem));
            rules.add(newRule(
                    State.ITEM,
                    HMEvents.START_SEGMENT_EXAMPLE,
                    State.EXAMPLE, this::captureTextOn));
            rules.add(newRule(
                    null, State.EXAMPLE, State.ITEM,
                    HMEvents.END_SEGMENT_EXAMPLE,
                    State.REWIND, this::addItemExample));
            rules.add(newRule(
                    State.ITEM,
                    HMEvents.START_SEGMENT_SUBITEM,
                    State.SUBITEM, this::initSubItem));
            rules.add(newRule(
                    State.SUBITEM,
                    HMEvents.START_SEGMENT_EXAMPLE,
                    State.EXAMPLE, this::captureTextOn));
            rules.add(newRule(
                    null, State.EXAMPLE, State.SUBITEM,
                    HMEvents.END_SEGMENT_EXAMPLE,
                    State.REWIND, this::addSubItemExample));
            rules.add(newRule(
                    State.SUBITEM,
                    HMEvents.END_SEGMENT_SUBITEM,
                    State.REWIND, this::addSubItem));
            rules.add(newRule(
                    State.ITEM,
                    HMEvents.END_SEGMENT_ITEM,
                    State.REWIND, this::addItem));
            rules.add(newRule(
                    State.SEGMENT,
                    HMEvents.END_SEGMENT,
                    State.REWIND, this::addSegment));
            rules.add(newRule(
                    State.SECTION,
                    HMEvents.START_SECTION_REP,
                    null, this::addSection));
            rules.add(newRule(
                    State.SECTION,
                    HMEvents.END_SECTION_REP,
                    null, this::initSection));
            rules.add(newRule(
                    State.SECTION,
                    HMEvents.END_SECTION,
                    State.REWIND, this::addSection));
            // Expect the unexpected
            rules.add(newRule(
                    State.OTHER, SAXEventType.BEGIN_ELEMENT, State.OTHER));
            rules.add(newRule(
                    State.OTHER, SAXEventType.END_ELEMENT, State.REWIND));
            rules.add(newRule(
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
        if (IS_TRACE) {
        	System.out.printf("sectionData=%s%n", sectionData);
        }
        consumer.accept(new UnstructuredData() {
			@Override
			public Map<String, Object> getData() {
				return sectionData;
			}
		});
        sectionData = null;
    }

}
