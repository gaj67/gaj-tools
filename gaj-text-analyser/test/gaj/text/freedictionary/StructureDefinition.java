package gaj.text.freedictionary;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

public abstract class StructureDefinition {

	public static final String SECTION_TAG = "section";
	public static final String SECTION_TYPE = "data-src";
	public static final String SECTION_TYPE_HM = "hm";
	public static final String SECTION_TYPE_HC = "hc_dict";
	public static final String SECTION_WORD_TAG = "h2";
	public static final String SEGMENT_TAG = "div";
	public static final String SEGMENT_TYPE = "class";
	public static final String SEGMENT_TYPE_HM = "pseg";
	public static final String SEGMENT_WORD_TAG = "b";
	public static final String EXAMPLE_TAG = "span";
	public static final String EXAMPLE_TYPE = "class";
	public static final String EXAMPLE_TYPE_HM = "illustration";
	
	@SuppressWarnings("serial")
	public static final Map<String,String> HM_SECTION_ATTRS = new HashMap<String,String>() {{
        put(SECTION_TYPE, SECTION_TYPE_HM);
    }};

	@SuppressWarnings("serial")
	public static final Map<String,String> HM_SEGMENT_ATTRS = new HashMap<String,String>() {{
        put(SEGMENT_TYPE, SEGMENT_TYPE_HM);
    }};

	private StructureDefinition() {}
	
	public static String getSectionType(Attributes attributes) {
		String type = attributes.getValue(SECTION_TYPE);
		return (type == null) ? "" : type;
	}

}
