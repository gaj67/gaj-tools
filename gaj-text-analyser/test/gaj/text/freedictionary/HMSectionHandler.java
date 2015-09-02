package gaj.text.freedictionary;

import static gaj.text.freedictionary.StructureDefinition.getSectionType;
import static gaj.text.freedictionary.StructureDefinition.SECTION_TAG;
import static gaj.text.freedictionary.StructureDefinition.SECTION_TYPE;
import static gaj.text.freedictionary.StructureDefinition.SECTION_TYPE_HC;
import static gaj.text.freedictionary.StructureDefinition.SECTION_TYPE_HM;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Provides a handler for the HM section of a FreeDictionary page.
 */
public class HMSectionHandler extends DefaultHandler {

	private boolean isActive = false;
	
	// TODO Pass in DefinitionConsumer to constructor.
	
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (SECTION_TAG.equals(localName)) {
        	if (isActive) {
        		throw new IllegalStateException("Unexpected nested section");
        	}
        	if (!SECTION_TYPE_HM.equals(getSectionType(attributes))) {
           		throw new IllegalStateException("Processing wrong section");
            }
    		isActive = true;
        } else {
        	if (!isActive) {
        		throw new IllegalStateException("This handler is not active");
        	}
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	if (!isActive) {
    		throw new IllegalStateException("This handler is not active");
    	}
        if (SECTION_TAG.equals(localName)) {
        	isActive = false;
        } else {
        	
        }
    }

}
