package gaj.text.freedictionary;

import gaj.text.handler.sax.DelegatingSAXEventHandler;
import gaj.text.handler.sax.SAXEvent;

/*package-private*/ class SectionsHandler extends DelegatingSAXEventHandler {

	// TODO Pass in DefinitionConsumer to constructor and pass to delegate handlers.

    @Override
    public void handle(SAXEvent event) {
        if (HMEvents.START_SECTION.matches(event)) {
            setHandler(new HMHandler());
            super.handle(event);
        } else if (HCEvents.START_SECTION.matches(event)) {
            setHandler(new HCHandler());
            super.handle(event);
        } else if (HMEvents.END_SECTION.matches(event)) {
            super.handle(event);
            setHandler(null);
        } else {
            super.handle(event);
        }
    }

}
