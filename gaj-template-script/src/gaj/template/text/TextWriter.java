/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;

import java.io.Closeable;
import java.io.IOException;

/*package-private*/ abstract class TextWriter implements Closeable {

    /**
     * Writes the specified character to the text stream.
     * 
     * @param c - The character to write.
     * @throws IOException If the character fails to be written.
     */
    public abstract void write(char c) throws IOException;

    /**
     * Writes the specified characters to the text stream.
     * 
     * @param buf - The array of characters from which to write.
     * @param start - The start of the character sequence to write.
     * @param len - The number of characters to write.
     * @throws IOException If any of the characters fail to be written.
     */
    public void write(final char[] buf, final int start, final int len) throws IOException {
        // Default, non-optimal implementation.
        final int end = start + len;
        for (int i = start; i < end; i++)
            write(buf[i]);
    }
    
    /**
     * Writes the specified string to the text stream.
     * 
     * @param s - The string to write.
     * @throws IOException If the string fails to be written.
     */
    public void write(String s) throws IOException {
        // Default, non-optimal implementation.
        final int len = s.length();
        for (int i = 0; i < len; i++)
            write(s.charAt(i));
    }

    @Override
    public abstract void close() throws IOException;

}