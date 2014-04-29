/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;

import gaj.template.segment.ModifiableTextSegment;

/**
 * Enables the piecemeal segmentation of text into a sequence of segments. 
 * Each segment is formed during the detection of any one of a collection of supplied delimiter strings.
 */
public interface TextSegmenter {

    /**
     * Reads the text into the given text segment until any one of the given delimiters is found. 
     * 
     * @param textBlock - The text segment to contain the scanned text.
     * @param delimiters - An array of delimiter strings for which to search.
     * @return Information about the status of the search.
     * @throws UncheckedIOException If any text input fails to be processed.
     */
    public DelimiterInfo segment(ModifiableTextSegment textBlock, String... delimiters);

    /**
     * Reads the text into the given text segment until any one of the given delimiter strings is found.
     * 
     * @param textBlock - The text segment to contain the scanned text.
     * @param delimiters - An array of of arrays of delimiter strings for which to search.
     * @return Information about the status of the search.
     * @throws UncheckedIOException If any text input fails to be processed.
     */
    public DelimiterInfo segment(ModifiableTextSegment textBlock, String[]... delimiters);

    /**
     * Reads the text into the given text segment until any one of the given delimiters is found.
     * If a detected delimiter is immediately preceded in the text by the escape symbol, then it is treated as plain text. 
     * 
     * @param textBlock - The text segment to contain the scanned text.
     * @param escape - The symbol that negates detection of a delimiter string.
     * @param strip - A Boolean value indicating whether (true) or not (false) to strip off the escape symbol
     * when it escapes a delimiter.
     * @param delimiters - An array of delimiter strings for which to search.
     * @return Information about the status of the search.
     * @throws UncheckedIOException If any text input fails to be processed.
     */
    public DelimiterInfo segmentEscaped(ModifiableTextSegment textBlock, String escape, boolean strip, String... delimiters);

    /**
     * Reads the text into the given text segment until any one of the given delimiter strings is found.
     * If a detected delimiter is immediately preceded in the text by the escape symbol, then it is treated as plain text. 
     * 
     * @param textBlock - The text segment to contain the scanned text.
     * @param escape - The symbol that negates detection of a delimiter string.
     * @param strip - A Boolean value indicating whether (true) or not (false) to strip off the escape symbol
     * when it escapes a delimiter.
     * @param delimiters - An array of of arrays of delimiter strings for which to search.
     * @return Information about the status of the search.
     * @throws UncheckedIOException If any text input fails to be processed.
     */
    public DelimiterInfo segmentEscaped(ModifiableTextSegment textBlock, String escape, boolean strip, String[]... delimiters);

}