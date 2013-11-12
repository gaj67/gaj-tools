/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.parser;

/*package-private*/ interface ClassConstant {

    public byte getTag();

    public int getNameIndex();

    public int getTypeIndex();

    public Object getValue();

    /**
     * 
     * @return The string name of the constant if it is UTF8, otherwise a value of null.
     */
    public String getValuesAsString();

    public boolean isDoubleEntry();

}