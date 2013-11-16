/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;

import java.io.Closeable;
import java.io.IOException;

/*package-private*/ interface TextReader extends Closeable {

    /**
     * Reads the next character from the text stream.
     * 
     * @return The character as an integer value, or a value of -1 if the stream has ended.
     * @throws IOException If the next character fails to be read.
     */
    public int read() throws IOException;

}