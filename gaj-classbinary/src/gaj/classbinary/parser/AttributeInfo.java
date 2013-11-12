/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.parser;


/*package-private*/ interface AttributeInfo {

    public String getAttributeType();

    /**
     * 
     * @return A byte-array of the raw attribute data.
     */
    public byte[] getAttributeData();

}
