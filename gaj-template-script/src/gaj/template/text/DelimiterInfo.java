/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;

/**
 * Information about the result of a segmentation search.
 */
/*@Immutable*/
public interface DelimiterInfo {

    /**
     * @return The index position of the found delimiter within the relevant search string array, or a value
     * of -1 if no delimiter was found.
     */
    public int getIndex();

    /**
     * @return The index position of the array containing the found delimiter within a group of arrays, 
     * or a value of -1 if no delimiter was found.
     */
    public int getGroup();

    /**
     * @return The delimiter string that terminated the search, or a value of null if
     * no delimiter was found.
     */
    public String getDelimiter();

}