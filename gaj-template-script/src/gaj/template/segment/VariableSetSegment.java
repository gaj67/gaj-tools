/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;

import gaj.template.data.KeyValue;
import gaj.template.data.ScriptData;
import gaj.template.data.ScriptDataFactory;
import gaj.template.text.TextOutput;
import gaj.template.text.UncheckedIOException;

import java.util.List;

public class VariableSetSegment extends CommandSegment {

    private final List<KeyValue<String,String>> vars;

    /*package-private*/ VariableSetSegment(List<KeyValue<String,String>> vars) {
        super(CommandType.VarInit);
        this.vars = vars;
    }
    
    @Override
    public boolean embed(ScriptData data, TextOutput output) throws UncheckedIOException {
        for (KeyValue<String,String> var : vars) {
            String key = VariableGetSegment.decodeToString(var.getKey(), data);
            if (key == null || key.isEmpty())
                throw ScriptDataFactory.newException("Invalid variable: \"" + var.getKey() + "\"");
            Object value = var.getValue();
            if (value != null) 
                value = VariableGetSegment.decodeToObject(value, data);
            data.setProperty(key, value);
        }
        return false;
    }

}
