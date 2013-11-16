/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;




/**
 * Allows text segments to be created from a text stream using a segmenter.
 */
public class TextSegmenterFactory {

    private TextSegmenterFactory() {}

    /**
     * Creates a text segmenter bound to the given text stream.
     * 
     * @param stream - The text stream to be segmented.
     * @return A segmenter instance.
     */
    public static TextSegmenter newSegmenter(TextInput stream) {
        return new TextSegmenterImpl(stream);
    }
}
