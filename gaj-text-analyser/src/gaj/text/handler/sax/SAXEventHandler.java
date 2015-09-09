package gaj.text.handler.sax;

import gaj.text.handler.EventHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class SAXEventHandler extends DefaultHandler implements EventHandler<SAXEvent> {
    // TODO Implement more events.

    protected SAXEventHandler() {}

    @Override
    public abstract void handle(SAXEvent event);

    @Override
    public void startDocument() throws SAXException {
        handle(SAXEventFactory.newEvent(SAXEventType.BEGIN_DOCUMENT));
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        handle(SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, qName, attributes));
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        handle(SAXEventFactory.newEvent(SAXEventType.CHARACTERS, new String(ch, start, length)));
    }

    @Override
    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        handle(SAXEventFactory.newEvent(SAXEventType.WHITESPACE, new String(ch, start, length)));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        handle(SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, qName));
    }

    @Override
    public void endDocument() throws SAXException {
        handle(SAXEventFactory.newEvent(SAXEventType.END_DOCUMENT));
    }

}
