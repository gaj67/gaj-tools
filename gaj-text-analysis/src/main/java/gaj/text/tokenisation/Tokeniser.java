package gaj.text.tokenisation;

/**
 * Permits the tokenisation of text into spans.
 */
public interface Tokeniser {

    /**
     * Partitions the given text into a contiguous sequence of text spans.
     * 
     * @param text - The input text.
     * @return The (possibly empty) sequence of text spans.
     */
    SequenceTextSpan tokenise(String text);

}
