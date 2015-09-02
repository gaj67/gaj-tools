package gaj.text.freedictionary;

import org.xml.sax.Attributes;

/*package-private*/ abstract class StructureDefinition {

	/*package-private*/ static final String SECTION_TAG = "section";
	/*package-private*/ static final String SECTION_TYPE = "data-src";
	/*package-private*/ static final String SECTION_TYPE_HM = "hm";
	/*package-private*/ static final String SECTION_TYPE_HC = "hc_dict";

	private StructureDefinition() {}
	
	/*package-private*/ static String getSectionType(Attributes attributes) {
		String type = attributes.getValue(SECTION_TYPE);
		return (type == null) ? "" : type;
	}

}
