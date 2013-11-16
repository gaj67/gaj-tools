/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.parser;

import gaj.template.script.ScriptSymbol;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Encapsulates the configuration of the symbols required to interpret scripting commands within a text-based template.
 * These symbols are assumed to uniquely determine:
 * <ul>
 * <li>The start and end of the span of a variable for later substitution. 
 * Variables are denoted by: <br/>&lt;<em>prefix-symbol</em>&gt;&lt;<em>variable</em>&gt;&lt;<em>suffix-symbol</em>&gt;.</li>
 * <li>The start and end of the span of a command for later interpretation.
 * Commands are denoted by:<br/>&lt;<em>prefix-symbol</em>&gt;&lt;<em>command-name</em>&gt;&lt;<em>open-symbol</em>&gt;
 *[&lt;<em>arguments</em>&gt;]&lt;<em>close-symbol</em>&gt;.
 * </li>
 * </ul>
 */
public class ScriptConfig implements Cloneable {

    // Supply default values for the scripting symbols.
    private final Map<ScriptSymbol, String> symbols = new HashMap<>();
    {
       for (ScriptSymbol symbol : ScriptSymbol.values()) {
           symbols.put(symbol, symbol.value());
       }
    };

    /*package-private*/ ScriptConfig() {}

    @Override
    public ScriptConfig clone() {
        try {
            return (ScriptConfig)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void set(Object key, Object value) {
        ScriptSymbol symbol = 
                (key instanceof ScriptSymbol) ? (ScriptSymbol)key 
                : (key instanceof String) ? ScriptSymbol.fromString((String)key) : null;
        if (symbol == null)
            throw new IllegalArgumentException("Invalid configuration key: " + key);
        if (!(value instanceof String))
            throw new IllegalArgumentException("Invalid configuration value: " + value + " for key: " + key);
        symbols.put(symbol, (String)value);
    }

    /**
     * Specifies some or all of the configuration scripting symbols to be used by a parser.
     * 
     * @param symbols - An array of key/value pairs, with key type {@link ScriptSymbol} and value type String.
     * @throws IllegalArgumentException If the supplied symbol or symbols are invalid. 
     */
    public void setSymbols(Object... symbols) {
        if (symbols.length % 2 != 0)
            throw new IllegalArgumentException("Invalid number of arguments: " + symbols.length);
        Object key = null;
        int i = 0;
        for (Object arg : symbols) {
            if (i++ % 2 == 0) { // Key.
                key = arg;
            } else {            // Value.
                set(key, arg);
            }
        }
    }
    
    /**
     * Specifies some or all of the configuration scripting symbols to be used by a parser.
     * 
     * @param symbols - A mapping of key/value pairs, with key type {@link ScriptSymbol} and value type String.
     * @throws IllegalArgumentException If the supplied symbol or symbols are invalid. 
     */
    public void setSymbols(Map<ScriptSymbol,String> symbols) {
        for (Entry<ScriptSymbol,String> entry : symbols.entrySet()) {
            final ScriptSymbol key = entry.getKey();
            if (key == null)
                throw new IllegalArgumentException("Null configuration key");
            final String value = entry.getValue();
            if (value == null)
                throw new IllegalArgumentException("Null configuration value for key: " + key);
            symbols.put(key, value);
        }
    }

    public String getSymbol(ScriptSymbol symbol) {
        return symbols.get(symbol);
    }

    public void setSymbol(ScriptSymbol symbol, String value) {
        set(symbol, value);
    }

}
