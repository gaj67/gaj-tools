/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.core;

import java.util.Date;
import java.util.List;

/**
 * Encapsulates a leak detector instrumenting a given type of resource.
 *
 * @param <T> - The type of resource being instrumented.
 */
public interface Detector<T> {

    /**
     * Indicates whether or not any extant (non-garbage collected) resource 
     * is being instrumented by this leak detector. 
     * 
     * @return A value of true (or false) if a resource is instrumented.
     */
    public boolean isInstrumented();

    /**
     * Provides a unique identifier for the detector to help identify the underlying instrumented resource.
     * 
     * @return A unique identifier string.
     * @throws IllegalStateException If a unique identifier cannot be determined. 
     */
    public String getIdentifier();

    /**
     * Determines the fully-qualified name of the resource class actually being instrumented by this leak detector.
     * <p/>Note: The instrumented class might possibly be a super-class of the actual resource class.
     * 
     * @return The instrumented class name.
     */
    public String getInstrumentedClassName();

    /**
     * Determines the fully-qualified class name of the resource instance being instrumented by this leak detector.
     * 
     * @return The resource class name.
     */
    public String getResourceClassName();

    /**
     * Determines the number of milliseconds from when this leak detector was created to the given date.
     *  
     * @param date - The comparison date, usually the current date.
     * @return The age of the detector.
     */
    public long getDuration(Date date);

    /**
     * Determines the sequence of calls that led to this 
     * leak detector being instantiated, ordered from outside to inside.  
     * 
     * @return The caller sequence.
     */
    public List<Caller> getCallers();

    /**
     * Obtains a strong reference to the instrumented resource.
     * 
     * @return The instrumented resource instance, or a value of null if the resource is no longer extant.
     */
    public T getResource();

}