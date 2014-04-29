/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.parser;

import gaj.template.script.ScriptException;


@SuppressWarnings("serial")
public class ScriptParseException extends ScriptException {

    /*package-private*/ ScriptParseException(String message) {
        super(message);
    }

}
