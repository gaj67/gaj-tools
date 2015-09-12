package gaj.text.handler.sax;

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Optionally delegates all methods to another handler.
 */
public class DelegatingSAXEventHandler extends SAXEventHandler {

    private SAXEventHandler handler = null;

	/**
	 * Discards all data until {@link setHandler}() is called.
	 */
	public DelegatingSAXEventHandler() {}
	
	/**
	 * Delegates all data to the given handler.
	 * 
	 * @param handler - The delegated handler.
	 */
    public DelegatingSAXEventHandler(SAXEventHandler handler) {
		this.handler = handler;
	}

    public SAXEventHandler getHandler() {
		return handler;
	}

    public void setHandler(SAXEventHandler handler) {
		this.handler = handler;
	}

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
        return (handler == null) ? null : handler.resolveEntity(publicId, systemId);
    }

    @Override
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
        if (handler != null) {
            handler.notationDecl(name, publicId, systemId);
        }
    }

    @Override
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        if (handler != null) {
            handler.unparsedEntityDecl(name, publicId, systemId, notationName);
        }
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        if (handler != null) {
            handler.setDocumentLocator(locator);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        if (handler != null) {
            handler.startDocument();
        }
    }

    @Override
    public void endDocument() throws SAXException {
        if (handler != null) {
            handler.endDocument();
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (handler != null) {
            handler.startPrefixMapping(prefix, uri);
        }
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        if (handler != null) {
            handler.endPrefixMapping(prefix);
        }
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        if (handler != null) {
            handler.processingInstruction(target, data);
        }
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        if (handler != null) {
            handler.skippedEntity(name);
        }
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        if (handler != null) {
            handler.warning(e);
        }
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        if (handler != null) {
            handler.error(e);
        }
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        if (handler != null) {
            handler.fatalError(e);
        } else {
            throw e;
        }
    }

    @Override
    public void handle(SAXEvent event) {
        if (handler != null) {
            handler.handle(event);
        }
    }

}
