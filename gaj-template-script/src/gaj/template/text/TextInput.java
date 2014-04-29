/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;

import java.io.Closeable;
import java.util.Iterator;

/**
 * Encapsulates a consumable stream of text characters.
 * <p/>Note: An UncheckedIOException may occur if the next character in the stream cannot be read.
 */
public interface TextInput extends Iterator<Character>, Iterable<Character>, Closeable {

    /**
     * Specifies how many characters have been read from the stream.
     * 
     * @return The current number of characters already consumed.
     */
    public int numRead();

    /**
     * Indicates whether or not the stream potentially has unread characters.
     * 
     * @return A value of true (or false) if the stream has (or has not) been completely consumed.
     */
    public boolean isEmpty();

}
