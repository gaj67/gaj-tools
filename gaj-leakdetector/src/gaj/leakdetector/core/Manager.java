/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.core;





/**
 * Encapsulates a collection of leak detectors for a common type of resource.
 *
 * @param <T> - The type of resource being instrumented.
 */
public interface Manager<T> {

    /**
     * Adds a leak detector to the manager.
     * 
     * @param detector - The leak detector.
     */
    public void add(Detector<? extends T> detector);

    /**
     * Removes a leak detector from the manager.
     * 
     * @param detector - The leak detector.
     */
    public void remove(Detector<? extends T> detector);

    /**
     * Determines whether or not the manager has ever held any leak detectors for instrumented resources.
     * 
     * @return A value of true (or false) if resources are (or are not) being instrumented.
     */
    public boolean isInstrumented();

    /**
     * Forces the manager to update its statistics concerning the estimated numbers (extant and destroyed)
     * of detectors currently being managed.
     */
    public void update();

    /**
     * Determines the current number of instrumented resources being managed.
     * 
     * @return The number of instrumented resources/leak detectors that have been managed to date.
     */
    public long numManaged();

    /**
     * Estimates the number of instrumented resources 
     * that are no longer extant (i.e. have been garbage collected), 
     * depending upon when {@link #update}() was last called.
     * 
     * @return The number of instrumented resources/leak detectors that have been destroyed.
     */
    public long numDestroyed();

    /**
     * Estimates the number of instrumented resources 
     * that are still extant (i.e. have not been garbage collected),
     * depending upon when {@link #update}() was last called.
     * 
     * @return The number of instrumented resources/leak detectors that are still active.
     */
    public long numExtant();

    /**
     * Determines the currently managed detectors of instrumented resources.
     * <p/>Updates the statistics concerning the managed detectors. However,
     * each underlying resource might still need to be checked in case it has been garbage collected.
     *  
     * @return An iterable over the current resource leak detectors.
     */
    public Iterable<Detector<? extends T>> getDetectors();

}