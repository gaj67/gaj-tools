/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.classbinary.descriptors;

/**
 * Describes the type of a class method.
 */
public enum MethodType {

	/**
	 * Indicates a class initialiser.
	 */
	Initialiser,
	/**
	 * Indicates a class constructor.
	 */
	Constructor,
	/**
	 * Indicates a plain class method.
	 */
	Method;

	public static MethodType fromName(String methodName) {
		return methodName.equals("<clinit>") ? Initialiser
				: methodName.equals("<init>") ? Constructor : Method;
	}
}
