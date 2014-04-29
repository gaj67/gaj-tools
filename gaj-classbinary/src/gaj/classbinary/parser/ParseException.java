/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.parser;

import java.io.IOException;

@SuppressWarnings("serial")
public class ParseException extends IOException {

    /*package-private*/ public ParseException(String message) {
        super(message);
    }

    /*package-private*/ public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
