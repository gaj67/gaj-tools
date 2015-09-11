package gaj.text.freedictionary;

import gaj.text.handler.sax.SAXEvent;
import gaj.text.handler.sax.SAXEventFactory;
import gaj.text.handler.sax.SAXEventType;

public abstract class StructureDefinition {

    public static final String SECTION_TYPE_HC = "hc_dict";
    public static final String SECTION_WORD_TAG = "h2";
    public static final String SEGMENT_WORD_TAG = "b";
    public static final String SEGMENT_DEF_TAG = "i";
    public static final String EXAMPLE_TAG = "span";
    public static final String EXAMPLE_TYPE = "class";
    public static final String EXAMPLE_TYPE_HM = "illustration";

    /*
     *  <section data-src="hm">
     *      <h2>word</h2>
     *      <div class="pseg">*
     *          <i>tag</i>+  <b>word</b>*
     *          <div class="ds-list">*
     *              <div class="sds-list">*
     *              </div>
     *          </div>
     *      </div>
     *  </section>
     */
    public static final SAXEvent START_HM_SECTION = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "section", "data-src", "hm");
    public static final SAXEvent START_HM_SECTION_WORD = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "h2");
    public static final SAXEvent END_HM_SECTION_WORD = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "h2");
    public static final SAXEvent START_HM_SEGMENT = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", "pseg");
    public static final SAXEvent START_HM_SEGMENT_TAG = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "i");
    public static final SAXEvent END_HM_SEGMENT_TAG = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "i");
    public static final SAXEvent START_HM_SEGMENT_WORD = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "b");
    public static final SAXEvent END_HM_SEGMENT_WORD = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "b");
    public static final SAXEvent START_HM_SEGMENT_ITEM = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", "ds-list");
    public static final SAXEvent START_HM_SEGMENT_SUBITEM = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", "sds-list");
    public static final SAXEvent END_HM_SEGMENT_SUBITEM = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");
    public static final SAXEvent END_HM_SEGMENT_ITEM = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");
    public static final SAXEvent END_HM_SEGMENT = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");
    public static final SAXEvent END_HM_SECTION = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "section");

    private StructureDefinition() {}

}
