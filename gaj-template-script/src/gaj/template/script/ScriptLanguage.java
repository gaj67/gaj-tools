/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.script;


/**
 * <p/>Precedence: The first scripting segment to occur, e.g. either a comment, variable or command, takes
 * precedence over the other symbols.
 * <ol>
 * <li>Comments: A comment may not appear inside a script command or variable substitution.
 * Any text between the {@link ScriptSymbol#CommentOpen} and {@link ScriptSymbol#CommentClose} symbols, 
 * including script command or variable symbols, is part of an ignorable comment. Nested comments are acceptable.</li>
 * <li>Variables: A variable may not appear inside a script command. A variable inside a comment is ignored.
 * Nested variables are acceptable.</li>
 * <li>Commands:A command may not appear inside a script variable or another command. A command inside a comment is ignored.</li> 
 * </ol>
 * 
 * Template script language definitions: <ul>
 * <li><pre>VERSION := "@@version(" language version ")"</pre></li>
 * <li><pre>COMMENT := "@@comment(" comment ")"</pre></li>
 * <li><pre>INITIALISATION&lt;variable&gt; := "@@set(" variable [ "=" value | "<" variable2 ] ")" </pre></li>
 * <li><pre>SUBSTITUTION&lt;variable&gt; := "@@" variable [ "=" default ] ";"</pre></li>
 * <li><pre>INCLUSION&lt;variable&gt; := "@@include(" relative-path | absolute-path ")"</pre></li>
 * <li><pre>IF_BLOCK&lt;variable&gt; :=
 * "@@if(" CONDITIONAL&lt;variable&gt; ")" block
 * {"@@elseif(" CONDITIONAL&lt;variable&gt; ")" block }
 * ["@@else(" variable ")" block ]
 * "@@end(" variable ")"</pre></li>
 * <li><pre>FOR_LOOP&lt;variable&gt; := "@@for(" variable ")" block "@@next(" variable ")"</pre></li>
 * </ul>
 * <p/>Note: Nested conditionals on the same variable are not allowed due to implementation issues.
 * However, this technicality can be overcome by the syntactic trick of appending one or more asterisks to the
 * variable name of inner conditionals (see below for an example).
 * <p/>Note: There are two special multi-valued variants for an <em>IF_BLOCK</em>:
 * <ol>
 * <li><pre>@@if(X=A|B) <tt><em>block</em></tt> @@end(X)</pre> is equivalent to
 *<pre>@@if(X=A) <tt><em>block</em></tt> @@elseif(X=B) <tt><em>block</em></tt> @@end(X)</pre></li>
 *<li><pre>@@if(X!=A|B) <tt><em>block</em></tt> @@end(X)</pre> is equivalent to
 *<pre>@@if(X!=A) @@if(*X!=B) <tt><em>block</em></tt> @@end(*X) @@end(X)</pre></li>
 * </ol>
 *
 * @see Conditional {@link Conditional} &nbsp;- for more information about
 * conditional statements.
 */
public interface ScriptLanguage {

    /*
     * Scripting commands have two variations:
     * <ol>
     * <li>Parameterised commands, of the form: <pre>"&#x40;@" name "(" parameters ")"</pre></li>
     * <li>The variable substitution command, of the form:<pre>"&#x40;@" variable [ = default ] ";"</pre></li>
     * </ol>
     */
    public static final String START_COMMAND_STRING = "@@";
    public static final String START_PARAMETER_STRING = "(";
    public static final char START_PARAMETER_CHAR = START_PARAMETER_STRING.charAt(0);
    public static final String END_PARAMETER_STRING = ")";
    public static final char END_PARAMETER_CHAR = END_PARAMETER_STRING.charAt(0);
    public static final char END_VARIABLE_CHAR = ';';
    public static final String VERSION_LANGUAGE = "XTScript";
    public static final String VERSION_NUMBER = "1.0.0";

}