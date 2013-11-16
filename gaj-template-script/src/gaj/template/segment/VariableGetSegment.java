/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;

import gaj.template.data.ScriptData;
import gaj.template.text.TextOutput;
import gaj.template.text.UncheckedIOException;

/*package-private*/ class VariableGetSegment extends Segment {

    private final String name;
    private final Object value;

    /*package-private*/ VariableGetSegment(String varName, Object defaultValue) {
        super(SegmentType.Variable);
        this.name = varName;
        this.value = defaultValue;
    }

    @Override
    public boolean isEmpty() {
        return false; // Cannot tell without embedding data.
    }

    @Override
    public boolean embed(ScriptData data, TextOutput output) throws UncheckedIOException {
        Object property = data.getProperty(decodeToString(name, data));
        if (property == null)
            property = decodeToObject(value, data);
        if (property != null) {
            String text = property.toString();
            if (!text.isEmpty()) {
                output.append(text);
                return true;
            } // else fall through...
        }
        return false;
    }

    /*package-private*/ static String decodeToString(String var, ScriptData data) {
        return var; // TODO: Allow nested variable substitutions.
    }

    /*package-private*/ static Object decodeToObject(Object value, ScriptData data) {
        return value; // TODO: Allow nested variable substitutions.
    }

}
