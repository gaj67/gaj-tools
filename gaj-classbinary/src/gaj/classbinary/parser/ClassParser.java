/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.parser;

import gaj.classbinary.descriptors.ClassDescriptor;
import java.io.InputStream;

public interface ClassParser {

    /**
     * Parses the input stream of a .class file into a class descriptor.
     * @param classStream - The input stream.
     * @return An object that describes the parsed class.
     * @throws ParseException If the class stream cannot be parsed.
     */
    public ClassDescriptor parse(InputStream classStream) throws ParseException;

}