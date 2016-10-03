package gaj.text.handler.sax;

import java.util.Collections;
import java.util.Map;
import org.xml.sax.Attributes;

/*package-private*/ class MappedAttributes implements Attributes {

    private final Map<String, String> attrs;

    /*package-private*/ MappedAttributes(/*@Nullable*/ Map<String, String> attrs) {
        this.attrs = (attrs == null) ? Collections.<String, String> emptyMap() : attrs;
    }

    @Override
    public int getIndex(String qName) {
        int i = -1;
        for (String key : attrs.keySet()) {
            i++;
            if (key.equals(qName)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getIndex(String uri, String localName) {
        return getIndex(toQName(uri, localName));
    }

    @Override
    public int getLength() {
        return attrs.size();
    }

    @Override
    public String getLocalName(int index) {
        return getLocalName(getQName(index));
    }

    @Override
    public /*@Nullable*/ String getQName(int index) {
        if (index < 0 || index > attrs.size()) {
            return null;
        }
        int i = -1;
        for (String key : attrs.keySet()) {
            if (index == ++i) {
                return key;
            }
        }
        return "";
    }

    @Override
    public String getType(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getType(String qName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getType(String uri, String localName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getURI(int index) {
        return getURI(getQName(index));
    }

    @Override
    public /*@Nullable*/ String getValue(int index) {
        if (index < 0 || index > attrs.size()) {
            return null;
        }
        int i = -1;
        for (String value : attrs.values()) {
            if (index == ++i) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String getValue(String qName) {
        return attrs.get(qName);
    }

    @Override
    public String getValue(String uri, String localName) {
        return getValue(toQName(uri, localName));
    }

    protected String toQName(/*@Nullable*/ String uri, String localName) {
        return (uri == null || uri.isEmpty()) ? localName : (uri + ":" + localName);
    }

    protected String getURI(String qName) {
        int idx = qName.indexOf(':');
        return (idx < 0) ? "" : qName.substring(0, idx);
    }

    protected String getLocalName(String qName) {
        int idx = qName.indexOf(':');
        return (idx < 0) ? qName : qName.substring(idx + 1);
    }

}
