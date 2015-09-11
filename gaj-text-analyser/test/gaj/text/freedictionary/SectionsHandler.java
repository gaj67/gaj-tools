package gaj.text.freedictionary;

import gaj.text.handler.sax.SAXEvent;

/*package-private*/ class SectionsHandler extends DelegatingSAXEventHandler {

	// TODO Pass in DefinitionConsumer to constructor and pass to delegate handlers.

    @Override
    public void handle(SAXEvent event) {
        if (HMSectionEvents.START_SECTION.matches(event)) {
            setHandler(new HMSectionRuleHandler());
            super.handle(event);
        } else if (HMSectionEvents.END_SECTION.matches(event)) {
            super.handle(event);
            setHandler(null);
        } else {
            super.handle(event);
        }
    }

}
