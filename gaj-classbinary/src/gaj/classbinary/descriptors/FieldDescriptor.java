/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.descriptors;

import gaj.classbinary.flags.AccessFlags;

/**
 * Describes the various aspects of a class field as determined from the .class file. 
 */
public interface FieldDescriptor {

	/**
	 * 
	 * @return The name of the field.
	 */
	public String getFieldName();

    /**
     * @see {@link AbstractAccessFlags}.
     * @return The access flags denoting the modifiers on the field.
     */
	public AccessFlags getAccessFlags();

	/**
	 * Specifies the type of the field. 
	 * 
	 * @return The fully-qualified name of the field type.
	 */
	public String getFieldType();

}
