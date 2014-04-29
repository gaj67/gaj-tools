/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.script;

/**
 * Specifies the types of configurable symbols that delimit template scripting commands, along with their
 * default values.
 */
public enum ScriptSymbol {
    /**
     * The symbol specifying the start of a comment.
     */
    CommentOpen("cmtOpen", "/*"), 
    /**
     * The symbol specifying the end of a comment.
     */
    CommentClose("cmtClose", "*/"),
    /**
     * The symbol specifying the start of a variable.
     */
    VariableOpen("varOpen", "${"), 
    /**
     * The symbol specifying the assignment of a variable's default value.
     */
    VariableSet("varSet", "="),
    /**
     * The symbol specifying the end of a variable.
     */
    VariableClose("varClose", "}"),
    /**
     * The symbol specifying the start of a command (and its name).
     */
    CommandStart("cmdStart", "@@"),
    /**
     * The symbol specifying the end of the command name and the start of the optional command arguments.
     */
    ArgumentOpen("argOpen", "("),
    /**
     * The symbol specifying the end of one command argument and the start of another.
     */
    ArgumentSeparator("argSep", ","),
    /**
     * The symbol specifying the end of the command (and its arguments).
     */
    ArgumentClose("argClose", ")"),
    /**
     * The symbol specifying an escape from the usual rules of script parsing. Thus, 
     * {@link #Escape} <em>newline</em> removes the <em>newline</em> (and/or <em>carriage-return</em>) from the text,
     * whereas {@link #Escape} <em>symbol</em> treats the script <em>symbol</em> as plain text.
     */
    Escape("esc", "\\");

    private final String shortName;
    private final String value;

    /*package-private*/ private ScriptSymbol(String shortName, String value) {
        this.shortName = shortName;
        this.value = value;
    }

    /**
     * 
     * @return The textual name of the configurable script symbol.
     */
    public String shortName() {
        return shortName;
    }

    /**
     * 
     * @return The default textual value of the configurable script symbol.
     */
    public String value() {
        return value;
    }

    public static ScriptSymbol fromString(String name) {
        for (ScriptSymbol type : values()) {
            if (type.name().equals(name)) return type;
        }
        return null;
    }
}