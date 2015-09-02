package gaj.text.freedictionary;

import static gaj.text.freedictionary.StructureDefinition.getSectionType;
import static gaj.text.freedictionary.StructureDefinition.SECTION_TAG;
import static gaj.text.freedictionary.StructureDefinition.SECTION_TYPE_HC;
import static gaj.text.freedictionary.StructureDefinition.SECTION_TYPE_HM;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SectionsHandler extends DelegatingHandler {

	// TODO Pass in DefinitionConsumer to constructor and pass to delegate handlers.
	
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (localName) {
            case SECTION_TAG:
            	// Delegate to specialised section handler.
            	switch (getSectionType(attributes)) {
                	case SECTION_TYPE_HM:
                		setHandler(new HMSectionHandler());
                		break;
                	case SECTION_TYPE_HC:
                		setHandler(new HCSectionHandler());
                		break;
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
            case SECTION_TAG:
            	// Turn off delegation.
             	setHandler(null);
                break;
            default:
            	// Use existing handler.
            	break;
        }
    }

}
