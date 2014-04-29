/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;

import gaj.template.conditional.Evaluator;
import gaj.template.data.Embedder;
import gaj.template.data.KeyValue;
import gaj.template.data.ScriptData;
import gaj.template.text.TextOutput;
import gaj.template.text.UncheckedIOException;

import java.util.Collection;

/**
 * Encapsulates an entire conditional block of the form:
 * <p/>{@link CommandType#If} { {@link Segment} } 
 * { {@link CommandType#ElseIf}  { {@link Segment} } }
 * [ {@link CommandType#Else}  { {@link Segment} } ]
 * {@link CommandType#EndIf}.
 *
 */
/*package-private*/ class ConditionalBlockSegment extends Segment {

    private final Collection<KeyValue<? extends Evaluator,? extends Embedder>> branches;

    /*package-private*/ ConditionalBlockSegment(Collection<KeyValue<? extends Evaluator,? extends Embedder>> branches) {
        super(SegmentType.Block);
        this.branches = branches;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean embed(ScriptData data, TextOutput output) throws UncheckedIOException {
        for (KeyValue<? extends Evaluator,? extends Embedder> branch : branches) {
            if (branch.getKey().evaluate(data)) 
                return branch.getValue().embed(data, output);
        }
        return false;
    }

}
