/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;

import gaj.iterators.impl.BaseIterator;
import gaj.iterators.impl.BaseResourceIterator;

import java.io.IOException;
import java.util.Iterator;

/*package-private*/ class TextInputImpl extends BaseResourceIterator<Character> implements TextInput {

    private final TextReader reader;
    private int numRead = 0;
    private boolean hasNext = true;
    private Character next = null;

    /*package-private*/ TextInputImpl(TextReader reader) {
        this.reader = reader;
    }

    @Override
    public int numRead() {
        return numRead;
    }

    @Override
    public boolean isEmpty() {
        return !hasNext;
    }

    @Override
	public Iterator<Character> openResource() {
        return new BaseIterator<Character>() {
            @Override
            public boolean hasNext() {
                if (hasNext && next == null) {
                    try {
                        int c = reader.read();
                        if (hasNext = (c >= 0)) {
                            numRead++;
                            next = (char)c;
                            return true;
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
                return hasNext;
            }

            @Override
            public Character next() {
                if (hasNext()) {
                    Character ret = next;
                    next = null;
                    return ret;
                } else {
                    return halt("End of text stream");
                }
            }

            @Override
            public void remove() {
                // Do nothing, as character is 'consumed' anyway.
            }
        };
    }

    @Override
	public void closeResource() throws IOException {
        reader.close();
    }

}
