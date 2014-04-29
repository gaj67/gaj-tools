/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;

import java.io.IOException;
import java.io.Reader;

/**
 * Manages the creation of text streams bound to various text sources.
 */
public class TextIOFactory {

    public static TextInput newInput(final Reader reader) {
        return new TextInputImpl(new TextReader() {
            @Override
            public int read() throws IOException {
                return reader.read();
            }

            @Override
            public void close() throws IOException {
                reader.close();
            }
        });
    }

    public static TextInput newInput(final String text) {
        return new TextInputImpl(new TextReader() {
            private final int len = text.length();
            private int pos = 0;

            @Override
            public int read() {
                return (pos >= len) ? -1 : text.charAt(pos++);
            }

            @Override
            public void close() {}
        });
    }

    public static TextOutput newOutput(final StringBuilder buf) {
        return new TextOutputImpl(new TextWriter() {
            @Override
            public void write(char c) {
                buf.append(c);
            }

            @Override
            public void write(String s)  {
                buf.append(s);
            }

            @Override
            public void close() {}
        });
    }

}
