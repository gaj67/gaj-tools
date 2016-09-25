package gaj.text.app.freedictionary.xml;

import java.util.function.Consumer;

import gaj.text.handler.sax.DelegatingSAXEventHandler;
import gaj.text.handler.sax.SAXEvent;

/*package-private*/ class SectionsHandler extends DelegatingSAXEventHandler {

	private final Consumer<UnstructuredData> consumer;

	/*package-private*/ SectionsHandler(Consumer<UnstructuredData> consumer) {
		this.consumer = consumer;
		
	}

    @Override
    public void handle(SAXEvent event) {
        if (HMEvents.START_SECTION.matches(event)) {
            setHandler(new HMHandler(consumer));
            super.handle(event);
        } else if (HCEvents.START_SECTION.matches(event)) {
            setHandler(new HCHandler(consumer));
            super.handle(event);
        } else if (HMEvents.END_SECTION.matches(event)) {
            super.handle(event);
            setHandler(null);
        } else {
            super.handle(event);
        }
    }

}
