/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;


public class ModifiableTextSegment extends TextSegment {

    /*package-private*/ ModifiableTextSegment() {
        super();
    }

    /**
     * Appends a character to the end of the text segment.
     * 
     * @param c - The character to be added.
     */
    public void append(char c) {
        buf.append(c);
    }

    /**
     * Appends a string to the end of the text segment.
     * 
     * @param s - The string to be added.
     */
    public void append(String s) {
        buf.append(s);
    }

    /**
     * Unwinds the specified number of characters from the end of the text segment.
     * If the text segment has fewer characters than are to be removed, then its resulting length will be 0.
     * 
     * @param length - The number of characters to unwind.
     */
    public void unwind(int length) {
        int newLen = buf.length() - length;
        buf.setLength((newLen < 0) ? 0 : newLen);
    }

}
