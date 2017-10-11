package gaj.text.tokenisation;


/**
 * Describes a single span of text, comprising a contiguous (sub-)sequence of
 * characters.
 */
public interface TextSpan {

    /**
     * Obtains the character position at which the span starts (counting from
     * zero). Thus, 'dog' in 'big dog ran' starts at position 4.
     * 
     * @return The starting position.
     */
    int getStart();

    /**
     * Obtains the character position after the end of the span (counting from
     * zero). Thus, 'dog' in 'big dog ran' ends at (i.e. immediately before)
     * position 7.
     * 
     * @return The ending position.
     */
    int getEnd();

    /**
     * Obtains the text of the span.
     * 
     * @return The span of text.
     */
    String getText();

    /**
     * Obtains the type of the text span.
     * 
     * @return The span type.
     */
    TextSpanType getType();

    /**
     * Indicates whether or not the text span has valid starting and ending
     * positions.
     * 
     * @return A value of null if the span positions are valid, or an error
     *         message if they are not.
     */
    default /*@Nullable*/ String validate() {
        if (getStart() < 0 || getEnd() < 0 || getStart() > getEnd())
            return "Invalid span position(s)";
        if (getText() == null || getText().length() != getEnd() - getStart())
            return "Invalid text length";
        if (getStart() == getEnd() && getType() != TextSpanType.EMPTY && getType() != TextSpanType.SEQUENCE)
            return "Invalid span type for an empty span";
        return null;
    }

}
