/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.parser;

import gaj.template.data.ScriptData;
import gaj.template.script.ScriptTemplate;
import gaj.template.segment.Segment;
import gaj.template.segment.SegmentType;
import gaj.template.text.TextOutput;
import gaj.template.text.UncheckedIOException;

/*package-private*/ class ScriptTemplateImpl extends Segment implements ScriptTemplate {

    private final Segment segment;

    /*package-private*/ ScriptTemplateImpl(Segment segment) {
        super(SegmentType.Block);
        this.segment = segment;
    }

    @Override
    public boolean isEmpty() {
        return segment.isEmpty();
    }

    @Override
    public boolean embed(ScriptData data, TextOutput output) throws UncheckedIOException {
        return segment.embed(data, output);
    }

}
