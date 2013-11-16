/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.data;

import gaj.template.script.ScriptException;
import gaj.template.text.TextOutput;
import gaj.template.text.UncheckedIOException;

public interface Embedder {

    /**
     * Indicates whether or not the embedder contains usable information.
     * In other words, if there are no circumstances in which
     * a call to {@link #embed}() will ever result in any output, or any side effects, then
     * the embedder is considered o be empty.
     * 
     * @return A value of true (or false) if the embedder is (or is not) empty.
     */
    public boolean isEmpty();

    /**
     * Embeds the given data into the template according to its scripting commands and outputs the resulting text.
     * <br/>Note: An empty segment will not result in any output.
     * 
     * @param data - The data to be embedded.
     * @param output - The place to write the resulting text.
     * @return A value of true (or false) if any output was (or was not) written.
     * @throws ScriptException If the script data cannot be embedded.
     * @throws UncheckedIOException If any output failed to be written.
     */
    public boolean embed(ScriptData data, TextOutput output) throws ScriptException, UncheckedIOException;

}
