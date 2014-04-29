/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.parser;

import static gaj.template.script.ScriptSymbol.ArgumentClose;
import static gaj.template.script.ScriptSymbol.ArgumentOpen;
import static gaj.template.script.ScriptSymbol.ArgumentSeparator;
import static gaj.template.script.ScriptSymbol.CommandStart;
import static gaj.template.script.ScriptSymbol.CommentClose;
import static gaj.template.script.ScriptSymbol.CommentOpen;
import static gaj.template.script.ScriptSymbol.VariableClose;
import static gaj.template.script.ScriptSymbol.VariableOpen;
import static gaj.template.script.ScriptSymbol.VariableSet;
import gaj.iterators.core.IterableIterator;
import gaj.template.conditional.ConditionalFactory;
import gaj.template.conditional.Evaluator;
import gaj.template.data.KeyValue;
import gaj.template.script.ScriptSymbol;
import gaj.template.segment.CommandType;
import gaj.template.segment.ModifiableTextSegment;
import gaj.template.segment.Segment;
import gaj.template.segment.SegmentFactory;
import gaj.template.segment.SegmentType;
import gaj.template.segment.TextSegment;
import gaj.template.text.DelimiterInfo;
import gaj.template.text.TextSegmenter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates a process for breaking an input script into a sequence of low-level segments.
 */
/*package-private*/ class ScriptSegmenter extends IterableIterator<Segment> {

    //************************************************************************************
    // Segmenter functionality.
    
    private final ScriptConfig config;
    private final TextSegmenter segmenter;
    // XXX: See extractSymbols() about the following search blocks:
    private final String[] notAny = new String[8];
    private final String[] startAny = new String[3];
    private final String[] startArgument = new String[1];
    private final String[] endArgument = new String[3];
    private final String[] endComment = new String[2];
    private final String[] endVariable = new String[3];

    // XXX: Assumes the configuration has already been validated.
    /*package-private*/ ScriptSegmenter(ScriptConfig config, TextSegmenter segmenter) {
        this.config = config;
        this.segmenter = segmenter;
        extractSymbols();
    }

    /*package-private*/ static String validate(ScriptConfig config) {
        for (ScriptSymbol sym1 : ScriptSymbol.values()) {
            final String val1 = config.getSymbol(sym1);
            for (ScriptSymbol sym2 : ScriptSymbol.values()) {
                if (sym2 == sym1) continue;
                if (config.getSymbol(sym2).startsWith(val1))
                    return "Ambiguity between symbols " + sym1 + " and " + sym2;
            }
        }
        return null;
    }

    private void extractSymbols() {
        notAny[0] = config.getSymbol(CommandStart); 
        notAny[1] = config.getSymbol(ArgumentOpen); 
        notAny[2] = config.getSymbol(ArgumentClose); 
        notAny[3] = config.getSymbol(VariableOpen);
        notAny[4] = config.getSymbol(VariableSet);
        notAny[5] = config.getSymbol(VariableClose);
        notAny[6] = config.getSymbol(CommentOpen);
        notAny[7] = config.getSymbol(CommentClose);
        // XXX: The ordering of elements below is critical to the search! 
        // See parseSegment():
        startAny[0] = config.getSymbol(CommandStart); 
        startAny[1] = config.getSymbol(VariableOpen);
        startAny[2] = config.getSymbol(CommentOpen);
        // See parseCommandSegment():
        startArgument[0] = config.getSymbol(ArgumentOpen);
        endArgument[0] = config.getSymbol(ArgumentClose);
        endArgument[1] = config.getSymbol(ArgumentSeparator);
        endArgument[2] = config.getSymbol(VariableSet);
        // See parseCommentSegment().
        endComment[0] = config.getSymbol(CommentClose);
        endComment[1] = config.getSymbol(CommentOpen);
        // See parseVariableSegment().
        endVariable[0] = config.getSymbol(VariableClose);
        endVariable[1] = config.getSymbol(VariableSet);
        endVariable[2] = config.getSymbol(VariableOpen);
    }

    private /*@Nullable*/ Segment parseSegment(ModifiableTextSegment textBlock) {
        DelimiterInfo info = segmenter.segment(textBlock, startAny);
        switch (info.getIndex()) {
            case 0: 
                return parseCommandSegment();
            case 1: 
                return parseVariableSegment();
            case 2: 
                return parseCommentSegment();
            default: 
                return null; // No more segments.
        }
    }

    private Segment parseCommandSegment() {
        ModifiableTextSegment name = SegmentFactory.newTextSegment();
        {
            DelimiterInfo info = segmenter.segment(name, startArgument, notAny);
            final String delimiter = info.getDelimiter();
            if (delimiter == null)
                throw new ScriptParseException("Missing start of script command arguments");
            if (info.getGroup() > 0)
                throw new ScriptParseException("Script command contains unexpected symbol: " + delimiter);
        }
        CommandType command = CommandType.fromString(name.toString());
        if (command == null)
            throw new ScriptParseException("Unknown script command: " + name.toString());
        List<TextSegment> args = new LinkedList<>();
        ModifiableTextSegment arg = SegmentFactory.newTextSegment();
        while (true) {
            DelimiterInfo info = segmenter.segment(arg, endArgument, notAny);
            final String delimiter = info.getDelimiter();
            if (delimiter == null)
                throw new ScriptParseException("Missing end of script command arguments");
            if (info.getGroup() > 0)
                throw new ScriptParseException("Script command contains unexpected symbol: " + delimiter);
            if (info.getIndex() == 0) {
                if (!arg.isEmpty()) args.add(arg);
                break;
            }
            if (info.getIndex() == 1 && !arg.isEmpty()) {
                args.add(arg);
                arg = SegmentFactory.newTextSegment();
                continue;
            }
            arg.append(delimiter); // Append allowed symbol.
        }
        return parseCommand(command, args);
    }

    private static final Segment ELSE_SEGMENT = SegmentFactory.newConditionalSegment(CommandType.Else, true);
    private static final Segment END_IF_SEGMENT = SegmentFactory.newConditionalSegment(CommandType.EndIf, false);
    private static final Segment END_LOOP_SEGMENT = SegmentFactory.newCommandSegment(CommandType.EndLoop);

    private Segment parseCommand(CommandType command, List<TextSegment> args) {
        switch (command) {
            case Else:
                return ELSE_SEGMENT;
            case If: // Fall through...
            case ElseIf:
                return parseConditionalCommand(command, args);
            case EndIf:
                return END_IF_SEGMENT;
            case EndLoop:
                return END_LOOP_SEGMENT;
            case VarInit:
                return parseVarInitCommand(args);
            case Config:
                return parseConfigCommand(args);
            case StartLoop:
                return parseLoopCommand(args);
            default:
                throw new ScriptParseException("Unhandled script command: " + command);
        }
    }

    private Segment parseConditionalCommand(CommandType type, List<TextSegment> args) {
        Evaluator[] conditionals = new Evaluator[args.size()];
        int i = 0;
        for (TextSegment condition : args) {
            conditionals[i++] = ConditionalFactory.parseConditional(condition.toString());
        }
        // TODO Add isConj configuration, controllable by script command.
        return SegmentFactory.newConditionalSegment(type, ConditionalFactory.newMultiConditional(true/*config.isConjunctive()*/, conditionals));
    }

    private Segment parseVarInitCommand(List<TextSegment> args) {
        List<KeyValue<String,String>> vars = new LinkedList<>();
        final String varSet = config.getSymbol(VariableSet);
        for (TextSegment var : args) {
            int idx = var.indexOf(varSet);
            if (idx < 0) {
                // Unset command.
                vars.add(new KeyValue<String,String>(var.toString(), null));
            } else {
                // Set command.
                vars.add(new KeyValue<String,String>(var.substring(0, idx), var.substring(idx+varSet.length())));
            }
        }
        return SegmentFactory.newVariableSetSegment(vars);
    }

    // TODO: Have a real config. type when script text can be reconstructed.
    private static final Segment CONFIG_SEGMENT = SegmentFactory.newCommandSegment(CommandType.Config);

    private Segment parseConfigCommand(List<TextSegment> args) {
        Map<ScriptSymbol,String> vars = new HashMap<>();
        final String varSet = config.getSymbol(VariableSet);
        for (TextSegment symbolConfig : args) {
            int idx = symbolConfig.indexOf(varSet);
            if (idx < 0)
                throw new ScriptParseException("Missing key/value separator symbol: \"" + varSet + "\"");
            ScriptSymbol type = ScriptSymbol.fromString(symbolConfig.substring(0, idx));
            if (type == null)
                throw new ScriptParseException("Unknown symbol type: " + symbolConfig.substring(0, idx));
            String value = symbolConfig.substring(idx+varSet.length());
            if (value.isEmpty())
                throw new ScriptParseException("Symbol " + type + " has invalid value: \"" + value + "\"");
            vars.put(type, value);
        }
        // Perform directive.
        config.setSymbols(vars);
        extractSymbols();
        return CONFIG_SEGMENT; // TODO: Have a true ConfigSegment, if we want to reconstruct a script.
    }

    private Segment parseLoopCommand(List<TextSegment> args) {
        if (args.size() != 1)
            throw new ScriptParseException("Expected 1 loop argument, got " + args.size());
        return SegmentFactory.newLoopSegment(args.get(0).toString());
    }

    private Segment parseVariableSegment() {
        // Parse all of the variable into a text-segment, then reparse!
        ModifiableTextSegment name = SegmentFactory.newTextSegment();
        ModifiableTextSegment value = SegmentFactory.newTextSegment();
        ModifiableTextSegment segment = name;
        int depth = 1, maxDepth = depth;
        while (depth > 0) {
            DelimiterInfo info = segmenter.segment(segment, endVariable, notAny);
            final String delimiter = info.getDelimiter();
            if (delimiter == null)
                throw new ScriptParseException("Missing end of script variable");
            if (info.getGroup() > 0)
                throw new ScriptParseException("Script variable contains unexpected symbol: " + delimiter);
            final int idx = info.getIndex();
            if (idx == 0) { // Found (a) close of variable.
                if (--depth <= 0) break;
            } else if (idx == 1) { // Found a value delimiter.
                if (depth == 1) {
                    segment = value;
                    continue;
                }
            } else if (idx == 2) { // Found a nested variable.
                depth++;
                maxDepth++;
            }
            segment.append(delimiter); // Add nested variable delimiter.
        }
        // TODO: Allow nested variable substitution.
        if (maxDepth > 1)
            throw new ScriptParseException("Nested variables are not supported in this implementation");
        return SegmentFactory.newVariableGetSegment(name.toString(), value.toString()); 
    }

    private static final Segment COMMENT_SEGMENT = SegmentFactory.newSegment(SegmentType.Comment);

    private Segment parseCommentSegment() {
        ModifiableTextSegment comment = SegmentFactory.newTextSegment();
        int depth = 1;
        while (depth > 0) {
            DelimiterInfo info = segmenter.segment(comment, endComment);
            final int idx = info.getIndex();
            if (idx == 0) {
                depth--;
            } else if (idx == 1) {
                depth++;
            } else {
                throw new ScriptParseException("Missing end of script comment");
            }
        }
        return COMMENT_SEGMENT;
    }

    //************************************************************************************
    // Iterator functionality.
    
    private boolean hasNext = true;
    private ModifiableTextSegment textBlock = SegmentFactory.newTextSegment();
    private Segment segment = null;

    @Override
    public boolean hasNext() {
        if (hasNext) {
            if (!textBlock.isEmpty() || segment != null)
                return true;
            segment = parseSegment(textBlock);
            hasNext = (!textBlock.isEmpty() || segment != null);
        }
        return hasNext;
    }

    @Override
    public Segment next() {
        if (!hasNext())
            return halt("End of segment parsing");
        if (!textBlock.isEmpty()) {
            TextSegment retVal = textBlock;
            textBlock = SegmentFactory.newTextSegment();
            return retVal;
        }
        Segment retVal = segment;
        segment = null;
        return retVal;
    }

}
