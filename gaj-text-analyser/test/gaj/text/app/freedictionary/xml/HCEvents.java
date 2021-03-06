package gaj.text.app.freedictionary.xml;

import gaj.text.handler.sax.SAXEvent;
import gaj.text.handler.sax.SAXEventFactory;
import gaj.text.handler.sax.SAXEventType;

public abstract class HCEvents {

    /*
     *  <section data-src="hc_dict">
     *      <h2>word</h2>
     *      <div>*
     *          <i>tag</i>+  <b>word</b>*
     *          <div class="ds-list">*
     *              <span class="illustration">example</span>?
     *              <div class="sds-list">*
     *                  <span class="illustration">example</span>?
     *              </div>
     *          OR <div class="ds-single">
     *                  <span class="illustration">example</span>?
     *          </div>
     *          </div>
     *      </div>
     *  </section>
     */
    public static final SAXEvent START_SECTION = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "section", "data-src", "hc_dict");
    public static final SAXEvent START_SECTION_REP = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "hr", "class", "hmsep");
    public static final SAXEvent END_SECTION_REP = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "hr");
    public static final SAXEvent START_SECTION_WORD = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "h2");
    public static final SAXEvent END_SECTION_WORD = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "h2");
    public static final SAXEvent START_SEGMENT = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", null, "id", null);
    public static final SAXEvent START_SEGMENT_TAG = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "i");
    public static final SAXEvent END_SEGMENT_TAG = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "i");
    public static final SAXEvent START_SEGMENT_WORD = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "b");
    public static final SAXEvent END_SEGMENT_WORD = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "b");
    public static final SAXEvent START_SEGMENT_ITEM = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", "ds-list");
    public static final SAXEvent START_SEGMENT_ITEM2 = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", "ds-single");
    public static final SAXEvent START_SEGMENT_SUBITEM = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "div", "class", "sds-list");
    public static final SAXEvent START_SEGMENT_EXAMPLE = SAXEventFactory.newEvent(SAXEventType.BEGIN_ELEMENT, "span", "class", "illustration");
    public static final SAXEvent END_SEGMENT_EXAMPLE = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "span");
    public static final SAXEvent END_SEGMENT_SUBITEM = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");
    public static final SAXEvent END_SEGMENT_ITEM = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");
    public static final SAXEvent END_SEGMENT = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "div");
    public static final SAXEvent END_SECTION = SAXEventFactory.newEvent(SAXEventType.END_ELEMENT, "section");

    private HCEvents() {}

}
