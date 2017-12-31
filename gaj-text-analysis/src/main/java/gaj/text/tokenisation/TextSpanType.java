package gaj.text.tokenisation;

/**
 * Specifies the type of a contiguous span of text.
 */
public enum TextSpanType {

    // Basic types:
    /** The span is empty of any text. */
    EMPTY,
    /** The span consists entirely of one or more whitespace characters. */
    WHITESPACE,
    /** The span consists entirely of one or more alphabetic characters. */
    ALPHABETIC,
    /** The span consists entirely of one or more digit characters. */
    NUMERIC,
    /** The span consists entirely of one or more non-whitespace, non-alphabetic and non-numeric characters. */
    SYMBOLIC,
    /** The span consists entirely of one or more arbitrary characters. */
    ARBITRARY,
    
    // Nested type:
    /**
     * The span consists entirely of zero or more contiguous sub-spans. See
     * {@link SequenceTextSpan}.
     */
    SEQUENCE,
    ;
    
}
