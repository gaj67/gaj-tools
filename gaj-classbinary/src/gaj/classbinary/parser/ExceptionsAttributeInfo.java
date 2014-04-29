/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.parser;


/*package-private*/ interface ExceptionsAttributeInfo extends AttributeInfo {

    /**
     * 
     * @return The (possibly empty) array of the fully-qualified class names of all exceptions throwable by a method. 
     */
    String[] getExceptions();

}
