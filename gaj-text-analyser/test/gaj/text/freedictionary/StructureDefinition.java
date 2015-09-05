package gaj.text.freedictionary;

import org.xml.sax.Attributes;

/*package-private*/ abstract class StructureDefinition {

	/*package-private*/ static final String SECTION_TAG = "section";
	/*package-private*/ static final String SECTION_TYPE = "data-src";
	/*package-private*/ static final String SECTION_TYPE_HM = "hm";
	/*package-private*/ static final String SECTION_TYPE_HC = "hc_dict";
	/*package-private*/ static final String SECTION_WORD_TAG = "h2";
	/*package-private*/ static final String SEGMENT_TAG = "div";
	/*package-private*/ static final String SEGMENT_TYPE = "class";
	/*package-private*/ static final String SEGMENT_TYPE_HM = "pseg";
	/*package-private*/ static final String SEGMENT_WORD_TAG = "b";
	/*package-private*/ static final String EXAMPLE_TAG = "span";
	/*package-private*/ static final String EXAMPLE_TYPE = "class";
	/*package-private*/ static final String EXAMPLE_TYPE_HM = "illustration";
	
	private StructureDefinition() {}
	
	/*package-private*/ static String getSectionType(Attributes attributes) {
		String type = attributes.getValue(SECTION_TYPE);
		return (type == null) ? "" : type;
	}

}
