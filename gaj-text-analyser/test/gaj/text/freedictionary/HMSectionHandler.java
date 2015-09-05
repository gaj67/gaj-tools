package gaj.text.freedictionary;

import static gaj.text.freedictionary.StructureDefinition.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Provides a handler for the HM section of a FreeDictionary page.
 */
/*package-private*/ class HMSectionHandler extends HierarchicalStatefulHandler<State> {
	
	private final StringBuilder buf = new StringBuilder();

	private boolean captureText = false;

	// TODO Pass in DefinitionConsumer to constructor.
	
    @Override
	protected State getState() {
    	State state = super.getState();
    	return (state == null) ? State.INIT : state;
	}
	
	@Override
	protected void setState(State state) {
		super.setState(state);
		captureText = state.isTextual();
	}

    @Override
	protected State getParentState() {
    	State state = super.getParentState();
    	return (state == null) ? State.INIT : state;
	}
	
    @Override
	protected State getPreviousState() {
    	State state = super.getPreviousState();
    	return (state == null) ? State.INIT : state;
	}
	
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	State state = getState();
    	if (state.isTextual()) {
			// Some kind of text markup?
			captureText = false;
    	} else {
    		state = isStartOfState(state, localName, attributes);
    		if (state != null) setState(state);
    	}
    }

	@Override
    public void characters(char ch[], int start, int length) throws SAXException {
		if (captureText) {
			buf.append(ch, start, length);
		}
    }

    @Override
    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
		if (captureText) {
			buf.append(ch, start, length);
		}
    }

	@Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
		State state = getState();
    	if (state.isTextual()) {
			// End of text markup or end of text.
			captureText = true;
			if (isEndOfText(state, localName)) {
				restoreParentState();
			}
    	} else if (state.isStructural()) {
			if (isEndOfState(state, localName)) {
				restoreParentState();
			}
    	}
    }

	private /*@Nullable*/ State isStartOfState(State state, String localName, Attributes attributes) {
		switch (state) {
		case INIT:
			if (isStartSection(localName, attributes)) {
				clearSectionData();
				return State.SECTION;
			}
			return null;
		case SECTION:
			if (isStartSegment(localName, attributes)) {
				clearSegmentData();
				return State.SEGMENT;
			} else if (isStartSectionWord(localName, attributes)) {
				clearSectionWordData();
				return State.WORD;
			}
			return null;
		case SEGMENT:
		case ITEM:
		case SUBITEM:
		default:
			return null;
    	}
	}

	private void clearSectionWordData() {
		buf.setLength(0);
	}

	private boolean isStartSectionWord(String localName, Attributes attributes) {
		return SECTION_WORD_TAG.equals(localName);
	}

	private void clearSegmentData() {
		// TODO Auto-generated method stub
		
	}

	private boolean isStartSegment(String localName, Attributes attributes) {
		return SEGMENT_TAG.equals(localName) && SEGMENT_TYPE_HM.equals(attributes.getValue(SEGMENT_TYPE));
	}

	private boolean isStartSection(String localName, Attributes attributes) {
		return SECTION_TAG.equals(localName) && SECTION_TYPE_HM.equals(attributes.getValue(SECTION_TYPE));
	}

    private void clearSectionData() {
		// TODO Auto-generated method stub
		
	}

    private boolean isEndOfState(State state, String localName) {
		switch (state) {
		case ITEM:
			break;
		case SECTION:
			break;
		case SEGMENT:
			break;
		case SUBITEM:
			break;
		default:
			break;
		}
		return false;
	}

	private boolean isEndOfText(State state, String localName) {
		if (state == State.WORD) {
			if (isEndSectionWord(localName)) {
				processSectionWord(buf.toString());
			} else if (isEndSegmentWord(localName)) {
				processSegmentWord(buf.toString());
			}	
		} else if (state == State.TAG) {
			if (isEndTag(localName)) {
				processTag(buf.toString());
			}
		} else if (state == State.EXAMPLE) {
			if (isEndExample(localName)) {
				processExample(buf.toString());
			}	
		}
		return false;
	}

	private void processTag(String tag) {
		// TODO Auto-generated method stub
		
	}

	private boolean isEndTag(String localName) {
		// TODO Auto-generated method stub
		return false;
	}

	private void processSegmentWord(String word) {
		// TODO Auto-generated method stub
		
	}

	private void processSectionWord(String string) {
		// TODO Auto-generated method stub
		
	}

	private boolean isEndSectionWord(String localName) {
		return State.SECTION == getParentState() && localName.equals(SECTION_WORD_TAG);
	}

    private boolean isEndSegmentWord(String localName) {
		return State.SEGMENT == getParentState() && localName.equals(SEGMENT_WORD_TAG);
	}

	private void processExample(String string) {
		// TODO Auto-generated method stub
		
	}

	private boolean isEndExample(String localName) {
		return localName.equals(EXAMPLE_TAG);
	}

}
