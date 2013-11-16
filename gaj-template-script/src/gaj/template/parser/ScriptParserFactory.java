/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.parser;

import gaj.template.script.ScriptParser;
import gaj.template.script.ScriptTemplate;
import gaj.template.text.TextInput;
import gaj.template.text.TextSegmenterFactory;
import gaj.template.text.UncheckedIOException;

public class ScriptParserFactory {

    private ScriptParserFactory() {}
    
    /**
     * Constructs a default parser configuration.
     * 
     * @return The configuration instance.
     */
    public static ScriptConfig newConfig() {
        return new ScriptConfig();
    }

    /**
     * Constructs a parser configuration with the specified scripting symbols.
     * 
     * @param symbols - An array of key/value pairs, with key type ConfigType and value type String.
     * @return The configuration instance.
     * @throws IllegalArgumentException If the supplied symbol or symbols are invalid. 
     */
    public static ScriptConfig newConfig(Object... symbols) {
        ScriptConfig config = new ScriptConfig();
        config.setSymbols(symbols);
        return config;
    }

    /**
     * Constructs a parser with the default configuration.
     * 
     * @return The parser instance.
     */
    public static ScriptParser newParser() {
        return newParser(newConfig());
    }

    /**
     * Constructs a parser with the given configuration.
     * 
     * @param config - The parser configuration.
     * @return The parser instance.
     */
    public static ScriptParser newParser(final ScriptConfig config) {
        String err = ScriptSegmenter.validate(config);
        if (err != null)
            throw new IllegalArgumentException("Invalid configuration: " + err);
        return new ScriptParser() {
            @Override
            public ScriptTemplate parse(TextInput input) throws ScriptParseException, UncheckedIOException {
                return new ScriptTemplateImpl(
                        new SegmentParser(
                                new ScriptSegmenter(config.clone(), TextSegmenterFactory.newSegmenter(input))
                        ).parse());
            }
        };
    }

}
