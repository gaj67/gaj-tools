/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;

import java.io.Closeable;

public interface TextOutput extends Closeable {

    public void append(char output) throws UncheckedIOException;

    public void append(String output) throws UncheckedIOException;

    /**
     * Specifies how many characters have been written the stream.
     * 
     * @return The current number of characters already written.
     */
    public int numWritten();

    /**
     * Indicates whether or not the stream has had any characters written to it.
     * 
     * @return A value of true (or false) if the stream has (or has not) received output.
     */
    public boolean isEmpty();

    @Override
    public void close() throws UncheckedIOException;

}
