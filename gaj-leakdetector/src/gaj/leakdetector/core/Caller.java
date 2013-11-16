/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.core;

/**
 * Encapsulates a call in the sequence of calls that led to a 
 * leak detector being instantiated.  
 */
public interface Caller {

    /**
     * @return The fully-qualified name of the class that made this call.
     */
    String getClassName();

    /**
     * @return The name of the class method that made this call.
     */
    String getMethodName();

    /**
     * @return The line number at which this call was made.
     */
    int getLineNumber();

}