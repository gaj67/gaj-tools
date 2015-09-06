package gaj.text.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class SAXEventHandler extends DefaultHandler {
	// TODO Implement more events.
	
	protected SAXEventHandler() {}
	
	protected abstract void handleEvent(SAXEvent event);
	
    @Override
    public void startDocument() throws SAXException {
    	handleEvent(EventFactory.newSAXEvent(SAXEventType.BEGIN_DOCUMENT));
	}
	
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	handleEvent(EventFactory.newSAXEvent(SAXEventType.BEGIN_ELEMENT, qName, attributes));
    }

	@Override
    public void characters(char ch[], int start, int length) throws SAXException {
    	handleEvent(EventFactory.newSAXEvent(SAXEventType.CHARACTERS, new String(ch, start, length)));
    }

    @Override
    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
    	handleEvent(EventFactory.newSAXEvent(SAXEventType.WHITESPACE, new String(ch, start, length)));
    }

	@Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	handleEvent(EventFactory.newSAXEvent(SAXEventType.END_ELEMENT, qName));
    }

    @Override
    public void endDocument() throws SAXException {
    	handleEvent(EventFactory.newSAXEvent(SAXEventType.END_DOCUMENT));
	}
	
}
