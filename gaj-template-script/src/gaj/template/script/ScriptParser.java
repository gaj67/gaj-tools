/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.script;

import gaj.template.text.TextInput;
import gaj.template.text.UncheckedIOException;

/**
 * Specifies an interface for parsing a text stream of embedded scripting commands into a reusable template.
 * <br/>The parser itself is also resuable on multiple text streams.
 */
public interface ScriptParser {

    /**
     * Parses the given text-based script into a template.
     * 
     * @param input - The source of the script.
     * @return A reusable script template.
     * @throws ScriptException If the script is invalid.
     * @throws UncheckedIOException If the text stream fails to be read.
     */
    public ScriptTemplate parse(TextInput input) throws ScriptException, UncheckedIOException;

}
