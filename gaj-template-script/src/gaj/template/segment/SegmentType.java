/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;

/**
 * Describes the types of segments into which a script may be parsed.
 */
public enum SegmentType {
    /**
     * An arbitrary amalgamation of script segments.
     */
    Block,
    /**
     * An ignorable comment segment, of the form:<p/>
     * {@link SymbolType#CommentOpen} {<em>segment</em>} {@link SymbolType#CommentClose}. 
     * <p/>Note: The commented <em>segment</em> may be of any type, including a nested comment.
     */
    Comment,
    /**
     * A variable-substitution segment, of the form:<p/>
     * {@link SymbolType#VariableOpen} <em>variable</em> [{@link SymbolType#VariableSet} <em>default</em>] 
     * {@link SymbolType#VariableClose}.
     * <p/>Note: The <em>variable</em> and the <em>default</em> contain text, and may optionally
     * contain nested variable substitutions.
     */
    Variable,
    /**
     * A command segment, of the form:<br/>
     * {@link SymbolType#CommandStart} <em>name</em> {@link SymbolType#ArgumentOpen} 
     * [ <em>argument</em> { {@link SymbolType#ArgumentSeparator} <em>argument</em> } ] {@link SymbolType#ArgumentClose}.
     * <p/>Note: The command <em>name</em> must match one of the textual forms of the {@link CommandType} elements.
     * <p/>Note: A command <em>argument</em> may generally contain a variable substitution or a variable assignment,
     * unless specified otherwise by specific commands.
     */
    Command,
    /**
     * A text segment, containing plain, non-symbolic text.
     */
    Text;
}
