/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;

import gaj.template.conditional.ConditionalFactory;
import gaj.template.conditional.Evaluator;
import gaj.template.data.Embedder;
import gaj.template.data.KeyValue;
import gaj.template.data.ScriptData;
import gaj.template.text.TextOutput;
import gaj.template.text.UncheckedIOException;

import java.util.Collection;
import java.util.List;




/**
 * Allows the creation of various types of script segments.
 */
public class SegmentFactory {

    private SegmentFactory() {}

    /**
     * Creates a generic, empty segment of the specified type.
     * 
     * @param type - The type of segment.
     * @return A segment instance.
     */
    public static Segment newSegment(final SegmentType type) {
        return new Segment(type);
    }
    
    /**
     * Creates a generic command segment of the specified type.
     * The segment is considered non-empty because, although it never produces output,
     * it is considered to have side-effects (even by its mere existence).
     * 
     * @param type - The type of command segment.
     * @return A command segment instance.
     */
    public static CommandSegment newCommandSegment(final CommandType type) {
        return new CommandSegment(type);
    }
    
    public static Segment newBlockSegment(final Collection<? extends Embedder> sequence) {
        return new Segment(SegmentType.Block) {
            @Override
            public boolean isEmpty() {
                for (Embedder segment : sequence) {
                    if (!segment.isEmpty()) return false;
                }
                return true;
            }

            @Override
            public boolean embed(ScriptData data, TextOutput output) throws UncheckedIOException {
                boolean isOutput = false;
                for (Embedder segment : sequence) {
                    if (!segment.isEmpty())
                        isOutput |= segment.embed(data, output);
                }
                return isOutput;
            }
        };
    }

    /**
     * Creates an initially empty text segment.
     * 
     * @return A modifiable text segment.
     */
    public static ModifiableTextSegment newTextSegment() {
        return new ModifiableTextSegment();
    }

    public static ConditionalSegment newConditionalSegment(CommandType type, boolean isTrue) {
        return new ConditionalSegment(type, ConditionalFactory.newFixedConditional(isTrue));
    }

    public static ConditionalSegment newConditionalSegment(CommandType type, Evaluator conditional) {
        return new ConditionalSegment(type, conditional);
    }

    public static Segment newConditionalBlockSegment(Collection<KeyValue<? extends Evaluator,? extends Embedder>> branches) {
        return new ConditionalBlockSegment(branches);
    }

    public static CommandSegment newVariableSetSegment(List<KeyValue<String,String>> vars) {
        return new VariableSetSegment(vars);
    }

    public static Segment newVariableGetSegment(String variable, String defaultValue) {
        return new VariableGetSegment(variable, defaultValue);
    }

    public static CommandSegment newLoopSegment(String loopVar) {
        return new LoopSegment(loopVar);
    }

    public static Segment newLoopBlockSegment(LoopSegment cmd, Segment block) {
        return new LoopBlockSegment(cmd, block);
    }

}
