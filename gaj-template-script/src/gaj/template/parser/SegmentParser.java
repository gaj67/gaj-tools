/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.parser;

import gaj.template.conditional.Evaluator;
import gaj.template.data.Embedder;
import gaj.template.data.KeyValue;
import gaj.template.segment.CommandSegment;
import gaj.template.segment.CommandType;
import gaj.template.segment.ConditionalSegment;
import gaj.template.segment.LoopSegment;
import gaj.template.segment.Segment;
import gaj.template.segment.SegmentFactory;
import gaj.template.segment.SegmentType;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates a process that parses a sequence of segments into a combined block segment.
 */
/*package-private*/ class SegmentParser {

    private final Iterator<Segment> iterator;

    /*package-private*/ SegmentParser(Iterator<Segment> iterator) {
        this.iterator = iterator;
    }

    /*package-private*/ Segment parse() {
        List<Segment> segments = new LinkedList<>();
        while (iterator.hasNext()) {
            Segment segment = parseSegment(iterator.next());
            if (!segment.isEmpty()) segments.add(segment);
        }
        return SegmentFactory.newBlockSegment(segments);
    }

    private Segment parseSegment(Segment segment) {
        switch (segment.getType()) {
            case Comment:
            case Block:
            case Text:
            case Variable:
                return segment;
            case Command:
                CommandSegment cmd = (CommandSegment)segment;
                CommandType type = cmd.getCommandType();
                if (!type.hasBlock()) return segment;
                if (!type.isBlockStart())
                    throw new ScriptParseException("Orphaned block segment: " + cmd);
                switch (type) {
                    case If:
                        return parseConditionalBlock((ConditionalSegment)cmd);
                    case StartLoop:
                        return parseLoopBlock((LoopSegment)cmd);
                    default:
                        throw new ScriptParseException("Unhandled block segment: " + cmd);
                }
            default:
                throw new ScriptParseException("Unhandled segment: " + segment);
        }
    }

    private Segment parseConditionalBlock(final ConditionalSegment cmd) {
        // Parse up to and including matching EndIf segment.
        Collection<KeyValue<? extends Evaluator,? extends Embedder>> branches = new LinkedList<>();
        ConditionalSegment cond = cmd;
        boolean hasElse = false;
        boolean foundEnd = false;
        List<Segment> segments = new LinkedList<>();
        while (iterator.hasNext()) {
            Segment segment = iterator.next();
            if (segment instanceof ConditionalSegment) {
                final ConditionalSegment newCond = (ConditionalSegment)segment;
                final CommandType type = newCond.getCommandType();
                if (type != CommandType.If) {
                    if (hasElse && type != CommandType.EndIf)
                        throw new ScriptParseException("Cannot specify " + type + " after " + CommandType.Else);
                    branches.add(new KeyValue<ConditionalSegment,Segment>(cond, SegmentFactory.newBlockSegment(segments)));
                    if (type == CommandType.EndIf) {
                        foundEnd = true;
                        break;
                    }
                    if (type == CommandType.Else) hasElse = true;
                    cond = newCond;
                    segments = new LinkedList<>();
                }
            }
            // All paths not related to ElseIf, Else or EndIf branches fall through to here!
            segment = parseSegment(segment);
            if (!segment.isEmpty()) segments.add(segment);
        }
        if (!foundEnd)
            throw new ScriptParseException("Missing " + CommandType.EndIf + " for block segment: " + cmd);
        return SegmentFactory.newConditionalBlockSegment(branches);
    }

    private Segment parseLoopBlock(final LoopSegment cmd) {
        // Parse up to and including matching EndLoop segment.
        boolean foundEnd = false;
        List<Segment> segments = new LinkedList<>();
        while (iterator.hasNext()) {
            Segment segment = iterator.next();
            if (segment.getType() == SegmentType.Command 
                    && ((CommandSegment)segment).getCommandType() == CommandType.EndLoop) {
                foundEnd = true;
                break;
            }
            segment = parseSegment(segment);
            if (!segment.isEmpty()) segments.add(segment);
        }
        if (!foundEnd)
            throw new ScriptParseException("Missing " + CommandType.EndLoop + " for block segment: " + cmd);
        return SegmentFactory.newLoopBlockSegment(cmd, SegmentFactory.newBlockSegment(segments));
    }

}
