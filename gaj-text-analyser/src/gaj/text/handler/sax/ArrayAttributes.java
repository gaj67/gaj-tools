package gaj.text.handler.sax;

import org.xml.sax.Attributes;

/**
 * Specifies a list of attributes in key/value pairs.
 * As a convenience, if the last item is a key (i.e. the array length is odd), then an implicit value of null is assumed.
 */
/*package-private*/ class ArrayAttributes implements Attributes {

    private final String[] attrs;
    private final int length;

    /*package-private*/ ArrayAttributes(String/*@Nullable*/[] attrs) {
        this.attrs = attrs;
        length = (attrs.length + 1) / 2;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public String getLocalName(int index) {
        return getLocalName(getQName(index));
    }

    @Override
    public String getURI(int index) {
        return getURI(getQName(index));
    }

    @Override
    public /*@Nullable*/ String getQName(int index) {
        return (index < 0 || index > length) ? null : attrs[2 * index]; 
    }

    @Override
    public /*@Nullable*/ String getValue(int index) {
        if (index < 0 || index > length) return null;
        int pos = 2 * index + 1;
        return (pos >= attrs.length) ? null : attrs[pos];
    }

    @Override
    public int getIndex(String qName) {
        for (int i = 0; i < attrs.length; i += 2) {
            if (attrs[i].equals(qName)) return i / 2;
        }
        return -1;
    }

    @Override
    public /*@Nullable*/ String getValue(String qName) {
        return getValue(getIndex(qName));
    }

    @Override
    public int getIndex(String uri, String localName) {
        return getIndex(toQName(uri, localName));
    }

    @Override
    public /*@Nullable*/ String getValue(String uri, String localName) {
        return getValue(toQName(uri, localName));
    }

    @Override
    public /*@Nullable*/ String getType(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public /*@Nullable*/ String getType(String qName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public /*@Nullable*/ String getType(String uri, String localName) {
        // TODO Auto-generated method stub
        return null;
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
