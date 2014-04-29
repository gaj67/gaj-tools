/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.loader;


@SuppressWarnings("serial")
public class UncheckedParseException extends RuntimeException {

    /*package-private*/ UncheckedParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
