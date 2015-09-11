package gaj.text.freedictionary;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/*package-private*/ class SectionsHandler extends DelegatingHandler {

	// TODO Pass in DefinitionConsumer to constructor and pass to delegate handlers.
	
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (localName) {
            case SECTION_TAG:
            	// Delegate to specialised section handler.
            	switch (getSectionType(attributes)) {
                	case SECTION_TYPE_HM:
                		setHandler(new HMSectionRuleHandler());
                		break;
                	case SECTION_TYPE_HC:
                		//setHandler(new HCSectionHandler());
                		//break;
                	default:
                		setHandler(null);
                		break;
                }
                break;
            default:
            	// Use existing handler.
            	break;
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        switch (localName) {
            case "section":
            	// Turn off delegation.
             	setHandler(null);
                break;
            default:
            	// Use existing handler.
            	break;
        }
    }

}
