package gaj.text.freedictionary;

import gaj.text.handler.sax.SAXEvent;
import gaj.text.handler.sax.SAXEventFactory;
import gaj.text.handler.sax.SAXEventType;

public abstract class HMEvents {

    public static final String SECTION_TYPE_HC = "hc_dict";

    /*
     *  <section data-src="hm">
     *      <h2>word</h2>
     *      <div class="pseg">*
     *          <i>tag</i>+  <b>word</b>*
     *          <div class="ds-list">*
     *              <span class="illustration">example</span>?
     *              <div class="sds-list">*
     *                  <span class="illustration">example</span>?
     *              </div>
     *              <div class="notx">ignore this</div>?
     *          </div>
     *      </div>
     *  </section>
     */
    public static final SAXEvent START_SECTION = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "section", "data-src", "hm");
    public static final SAXEvent START_SECTION_WORD = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "h2");
    public static final SAXEvent END_SECTION_WORD = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "h2");
    public static final SAXEvent START_SEGMENT = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", "pseg");
    public static final SAXEvent START_SEGMENT_TAG = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "i");
    public static final SAXEvent END_SEGMENT_TAG = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "i");
    public static final SAXEvent START_SEGMENT_WORD = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "b");
    public static final SAXEvent END_SEGMENT_WORD = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "b");
    public static final SAXEvent START_SEGMENT_ITEM = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", "ds-list");
    public static final SAXEvent START_SEGMENT_SUBITEM = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", "sds-list");
    public static final SAXEvent START_SEGMENT_EXAMPLE = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "span", "class", "illustration");
    public static final SAXEvent END_SEGMENT_EXAMPLE = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "span");
    public static final SAXEvent END_SEGMENT_SUBITEM = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");
    public static final SAXEvent END_SEGMENT_ITEM = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");
    public static final SAXEvent END_SEGMENT = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");
    public static final SAXEvent END_SECTION = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "section");
    public static final SAXEvent START_OTHER = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div");
    public static final SAXEvent END_OTHER = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");

    private HMEvents() {}

}
