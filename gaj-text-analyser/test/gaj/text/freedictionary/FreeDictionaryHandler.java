package gaj.text.freedictionary;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class FreeDictionaryHandler extends DefaultHandler {

    private static final String SECTION_TAG = "section";
    private static final String SECTION_TYPE = "data-src";
    private static final String SECTION_TYPE_HM = "hm";
    private static final String SECTION_TYPE_HC = "hc_dict";

    private DefaultHandler handler = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (localName) {
            case SECTION_TAG:
                String type = attributes.getValue(SECTION_TYPE);
                if (SECTION_TYPE_HM.equals(type)) {
                    handler = new HMSectionHandler();
                } else if (SECTION_TYPE_HC.equals(type)) {
                    handler = new HCSectionHandler();
                }
                System.out.printf("Start of section %s%n", type);
                break;
            default:
                if (handler != null) {
                    handler.startElement(uri, localName, qName, attributes);
                }
                break;
        }
    }

}
