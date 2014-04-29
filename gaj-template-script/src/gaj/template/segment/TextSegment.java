/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;

import gaj.template.data.ScriptData;
import gaj.template.text.TextOutput;
import gaj.template.text.UncheckedIOException;


/**
 * Encapsulates a modifiable text segment.
 */
public class TextSegment extends Segment {

    protected final StringBuilder buf = new StringBuilder();

    /*package-private*/ TextSegment() {
        super(SegmentType.Text);
    }

    @Override
    public boolean isEmpty() {
        return buf.length() <= 0;
    }

    @Override
    public String toString() {
        return buf.toString();
    }

    @Override
    public boolean embed(ScriptData data, TextOutput output) throws UncheckedIOException {
        if (buf.length() > 0) {
            output.append(buf.toString());
            return true;
        }
        return false;
    }

    /**
     * Indicates whether or not the text segment ends with the given string.
     * 
     * @param s - The comparison string.
     * @return A value of true (or false) if the text segment does (or does not) end with the comparison string.
     */
    public boolean endsWith(String s) {
        int pos = buf.length() - s.length();
        if (pos < 0) return false;
        return s.equals(buf.substring(pos));
    }

    /**
     * Locates the first occurrence of the given string in the text segment.
     * 
     * @param s - The search string.
     * @return The position of (the first character of) the string, or a value of -1
     * if the string was not found.
     */
    public int indexOf(String s) {
        return buf.indexOf(s);
    }

    /**
     * Extracts the specified substring from the text segment.
     * 
     * @param start - The starting position of the substring.
     * @param end - The ending position of the substring.
     * @return The substring from the starting position up to but not including the ending position.
     */
    public String substring(int start, int end) {
        return buf.substring(start, end);
    }

    /**
     * Extracts the specified substring from the text segment.
     * 
     * @param start - The starting position of the substring.
     * @return The substring from the specified position to the end of the segment.
     */
    public String substring(int start) {
        return buf.substring(start);
    }

}