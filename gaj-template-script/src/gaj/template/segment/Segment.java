/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;

import gaj.template.data.Embedder;
import gaj.template.data.ScriptData;
import gaj.template.text.TextOutput;
import gaj.template.text.UncheckedIOException;

public class Segment implements Embedder {

    private final SegmentType type;

    protected Segment(SegmentType type) {
        this.type = type;
    }
    
    /**
     * Specifies the type of the script segment.
     * 
     * @return The segment type.
     */
    public SegmentType getType() {
        return type;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean embed(ScriptData data, TextOutput output) throws UncheckedIOException {
        return false;
    }

}
