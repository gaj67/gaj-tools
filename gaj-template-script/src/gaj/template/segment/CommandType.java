/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;

/**
 * Specifies the valid types of script commands.
 */
public enum CommandType {

    /**
     * A variable initialisation command, which sets or clears a local data variable.
     * No output will appear in a {@link Segment#embed}() call.
     * <p/>Format:<p/> {@link SymbolType#CommandStart} set {@link SymbolType#ArgumentOpen}
     * <em>variable</em> [{@link SymbolType#VariableSet <em>value</em>] {@link SymbolType#ArgumentClose}.
     */
    VarInit("set"),
    /**
     * An imperative command to dynamically redefine the script language symbols used for parsing.
     * No output will appear in a {@link Segment#embed}() call.
     * <p/>Format:<p/> {@link SymbolType#CommandStart} script {@link SymbolType#ArgumentOpen}
     * [ <em>symbol</em> {@link SymbolType#VariableSet} <em>value</em>
     * { , <em>symbol</em> {@link SymbolType#VariableSet} <em>value</em> }
     * ] {@link SymbolType#ArgumentClose}.
     * <p/>Note: Symbol redefinitions only take effect after this command.
     */
    Config("script"),
    /**
     * A start-of-loop command for a data-driven loop, matched by a corresponding {@link #EndLoop} end-of-loop command.
     * <p/>Format:<p/> {@link SymbolType#CommandStart} for {@link SymbolType#ArgumentOpen} <em>loop-variable</em>
     * {@link SymbolType#ArgumentClose}.
     * <p/>An entire loop-block has the format: <em>start-loop</em> { <em>segment</em> } <em>end-loop</em>, where
     * each enclosed <em>segment</em> is repeatedly processed (in order) during a {@link Segment#embed}() call. 
     */
    StartLoop("for", true, false),
    /**
     * An end-of-loop command for a data-driven loop, matched by a corresponding {@link #StartLoop} start-of-loop command.
     * <p/>Format:<p/> {@link SymbolType#CommandStart} next {@link SymbolType#ArgumentOpen} {@link SymbolType#ArgumentClose}.
     * @see {@link #StartLoop} for a description of a loop-block.
     */
    EndLoop("next", false, true),
    /**
     * A start-of-conditional command for a data-driven conditional block, 
     * matched by a corresponding {@link #EndIf} end-of-conditional command.
     * <p/>Format:<p/> {@link SymbolType#CommandStart} if {@link SymbolType#ArgumentOpen} 
     * <em>conditional</em> {@link SymbolType#ArgumentClose}.
     * <p/>The start-of-conditional block may optionally be followed by zero, one or more {@link #ElseIf}  
     * conditional blocks, which may optionally be followed by a single {@link #Else} conditional block.
     * Thus, an entire conditional-block has the format: 
     * {@link #If} { <em>segment</em> } 
     * { {@link #ElseIf} { <em>segment</em> } }
     * [ {@link #Else} { <em>segment</em> } ]
     * {@link #EndIf}.
     * <p/>At most one block of segments will be processed
     * during a {@link Segment#embed}() call, corresponding to the first conditional (if any) that evaluates to true.
     * If no conditional evaluates to true, then the else-block (if any) will be processed.
     */
    If("if", true, false),
    /**
     * An optional, alternative-conditional command for a data-driven conditional block. 
     * <p/>Format:<p/> {@link SymbolType#CommandStart} elseif {@link SymbolType#ArgumentOpen} 
     * <em>conditional</em> {@link SymbolType#ArgumentClose}.
     * @see {@link #If} for a description of a conditional-block.
     */
    ElseIf("elseif", false, false),
    /**
     * An optional, non-conditional command for a data-driven conditional block.
     * <p/>Format:<p/> {@link SymbolType#CommandStart} else {@link SymbolType#ArgumentOpen} {@link SymbolType#ArgumentClose}.
     * @see {@link #If} for a description of a conditional-block.
     */
    Else("else", false, false),
    /**
     * An end-of-conditional command for a data-driven conditional block, matched by a corresponding {@link #If} 
     * start-of-conditional command.
     * <p/>Format:<p/> {@link SymbolType#CommandStart} end {@link SymbolType#ArgumentOpen} {@link SymbolType#ArgumentClose}.
     * @see {@link #If} for a description of a conditional-block.
     */
    EndIf("end", false, true);

    private final String symbol;
    private final boolean hasBlock; 
    private final boolean isStart;
    private final boolean isEnd;

    /*package-private*/ private CommandType(String symbol) {
        this.symbol = symbol;
        this.hasBlock = false;
        this.isStart = false;
        this.isEnd = false;
    }

    /*package-private*/ private CommandType(String symbol, boolean isStart, boolean isEnd) {
        this.symbol = symbol;
        this.hasBlock = true;
        this.isStart = isStart;
        this.isEnd = isEnd;
    }

    public static final /*@Nullable*/ CommandType fromString(String name) {
        for (CommandType cmd : values()) {
            if (cmd.symbol.equalsIgnoreCase(name)) return cmd;
        }
        return null;
    }
    
    public boolean hasBlock() {
        return hasBlock;
    }
    
    public boolean isBlockStart() {
        return isStart;
    }

    public boolean isBlockEnd() {
        return isEnd;
    }
}
