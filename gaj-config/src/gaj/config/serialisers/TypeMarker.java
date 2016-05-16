package gaj.config.serialisers;

/**
 * Specifies the special serialisation markers used to denote type information.
 */
public enum TypeMarker {

	/**
	 * Indicates the start of a type marker.
	 */
	Prefix,
	/**
	 * Indicates the end of a type marker.
	 */
	Suffix,
	/**
	 * Indicates the marker for a null object.
	 */
	Null;

}
